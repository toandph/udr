package architectgroup.udr.webserver.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import architectgroup.fact.access.LicenseAccess;
import architectgroup.fact.access.license.KeyStatus;
import architectgroup.fact.access.license.LicenseObject;
import architectgroup.fact.access.util.CommonFunction;
import architectgroup.udr.webserver.model.SessionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LicenseInterceptor implements HandlerInterceptor  {
    @Autowired
    MessageSource messageSource;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LicenseAccess licenseAccess = new LicenseAccess();
        HttpSession session = request.getSession();
        SessionModel model = (SessionModel) session.getAttribute("sessionModel");
        String data = licenseAccess.getData(getClass().getResourceAsStream("/license/udr-license"));
        KeyStatus status = KeyStatus.KEY_INVALID;
        if (model.getLic() == null) {
            try {
                status = licenseAccess.verify(getClass().getResourceAsStream("/license/udr-license"));
            } catch (Exception err) {
                err.printStackTrace();
            }

            // Init the license //
            LicenseObject lic = new LicenseObject(status, "--", "--", new Date(), "--");
            model.setLic(lic);

            if (status == KeyStatus.KEY_VALID) {
                String[] p = data.split("#");
                String date = p[3];
                String version = p[2];
                String host = p[1];
                Date licDate = dateFormat.parse(date);
                if (!p[0].equalsIgnoreCase("TRIAL") || !p[1].equalsIgnoreCase("TRIAL")) {
                    if (host.equalsIgnoreCase(CommonFunction.getHostAddress())) {
                        Date current = new Date();
                        if (current.before(licDate)) {
                            String[] ver = version.split("\\.");
                            String currentVersion = messageSource.getMessage("version", null, Locale.ENGLISH);
                            String[] curver = currentVersion.split("\\.");
                            if (ver[0].equalsIgnoreCase(curver[0]) && ver[1].equalsIgnoreCase(curver[1])) {
                                lic = new LicenseObject(status, p[0], host, licDate, version);
                                model.setLic(lic);
                            } else {
                                lic = new LicenseObject(KeyStatus.KEY_INVALID, p[0], host, licDate, "Incorrect version");
                                model.setLic(lic);
                            }
                        } else {
                            lic = new LicenseObject(KeyStatus.KEY_EXPIRED, p[0], host, licDate, version);
                            model.setLic(lic);
                        }
                    } else {
                        lic = new LicenseObject(KeyStatus.KEY_INVALID, "--", "--", new Date(), "--");
                        model.setLic(lic);
                    }
                } else {
                    lic = new LicenseObject(KeyStatus.KEY_VALID, "TRIAL", "TRIAL", licDate, p[2]);
                    model.setLic(lic);
                }
            }
        }

        if (model.getLic().getStatus() != KeyStatus.KEY_VALID) {
            response.sendRedirect("/UDR/license/detail");
        }
        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
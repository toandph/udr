package architectgroup.udr.webserver.controller;

import architectgroup.fact.access.LicenseAccess;
import architectgroup.fact.access.ProjectAccess;
import architectgroup.fact.access.license.KeyStatus;
import architectgroup.fact.access.license.LicenseObject;
import architectgroup.fact.access.util.CommonFunction;
import architectgroup.fact.access.util.FactAccessFactory;
import architectgroup.fact.dto.ProjectDto;
import architectgroup.udr.webserver.model.LicenseModel;
import architectgroup.udr.webserver.model.SessionModel;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 5/2/13
 * Time: 10:28 AM
 */
@Controller
@Scope("request")
public class LicenseController {
    private static Logger k9logger = Logger.getLogger(ProjectController.class);
    @Autowired
    private SessionModel session;
    @Autowired
    private FactAccessFactory factAccess;

    @Autowired
    private MessageSource messageSource;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    @RequestMapping(value = "/license/detail")
    public ModelAndView detail() {
        ModelAndView modelAndView = new ModelAndView();
        LicenseAccess licenseAccess = new LicenseAccess();
        String data = licenseAccess.getData(getClass().getResourceAsStream("/license/udr-license"));
        KeyStatus status = KeyStatus.KEY_INVALID;

        if (session.getLic() == null) {
            try {
                status = licenseAccess.verify(getClass().getResourceAsStream("/license/udr-license"));
            } catch (Exception err) {
                System.out.println("Can not verify the license.");
            }

            // Init the license //
            LicenseObject lic = new LicenseObject(status, "--", "--", new Date(), "--");
            session.setLic(lic);

            if (status == KeyStatus.KEY_VALID) {
                String[] p = data.split("#");
                String date = p[3];
                String version = p[2];
                String host = p[1];
                Date licDate = new Date();
                try {
                    licDate = dateFormat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (!p[0].equalsIgnoreCase("TRIAL") || !p[1].equalsIgnoreCase("TRIAL")) {
                    if (host.equalsIgnoreCase(CommonFunction.getHostAddress())) {
                        Date current = new Date();
                        if (current.before(licDate)) {
                            String[] ver = version.split(".");
                            String currentVersion = messageSource.getMessage("version", null, Locale.ENGLISH);
                            String[] curver = currentVersion.split(".");
                            if (ver[0].equalsIgnoreCase(curver[0]) && ver[1].equalsIgnoreCase(curver[1])) {
                                lic = new LicenseObject(status, p[0], host, licDate, version);
                                session.setLic(lic);
                            } else {
                                lic = new LicenseObject(KeyStatus.KEY_INVALID, p[0], host, licDate, "Incorrect version");
                                session.setLic(lic);
                            }
                        } else {
                            lic = new LicenseObject(KeyStatus.KEY_EXPIRED, p[0], host, licDate, version);
                            session.setLic(lic);
                        }
                    } else {
                        lic = new LicenseObject(KeyStatus.KEY_INVALID, "--", "--", new Date(), "--");
                        session.setLic(lic);
                    }
                } else {
                    lic = new LicenseObject(KeyStatus.KEY_VALID, "TRIAL", "TRIAL", licDate, p[2]);
                    session.setLic(lic);
                }
            }
        }

        modelAndView.addObject("key", session.getLic().getStatus());
        modelAndView.addObject("name", session.getLic().getName());
        modelAndView.addObject("host", session.getLic().getHostId());
        modelAndView.addObject("version", session.getLic().getVersion());
        modelAndView.addObject("date", dateFormat.format(session.getLic().getExpiration()));
        modelAndView.addObject("currenthost", CommonFunction.getHostAddress());

        return modelAndView;
    }

    /*
    @RequestMapping(value = "/license/renew", method = RequestMethod.POST)
    public ModelAndView detail(HttpServletRequest request, @ModelAttribute("buildUploadModel") LicenseModel licenseModel) {
        ModelAndView modelAndView = new ModelAndView("redirect:/license/detail");

        // Upload to folder
        try {
            System.out.println(request.getServletPath() + "/" + request.getContextPath());
            File file = new File("E:/udr-license");
            FileUtils.copyInputStreamToFile(licenseModel.getLic().getInputStream(), file);
        } catch (Exception err) {
            err.printStackTrace();
        }

        return modelAndView;
    }
    */
}

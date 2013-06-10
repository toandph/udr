package architectgroup.udr.webserver.security;

import architectgroup.udr.webserver.model.SessionModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/17/13
 * Time: 7:37 PM
 */
public class UDRAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String referrer = request.getHeader("referer");
        HttpSession sessionOverall = request.getSession();
        SessionModel session = (SessionModel) sessionOverall.getAttribute("sessionModel");
        String lastURL = session.getLastURL();

        if (lastURL != null) {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
            try {
                response.sendRedirect(lastURL);
            } catch (IOException err) {
                err.printStackTrace();
            }
            session.setLastURL(null);
        } else {
            try {
                super.onAuthenticationSuccess(request, response, authentication);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }
}

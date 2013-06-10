package architectgroup.udr.webserver.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import architectgroup.udr.webserver.validator.CheckExtension;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 2/27/13
 * Time: 10:49 AM
 */
public class LoginModel {
    @NotEmpty(message = "{login.username-can-not-be-blank}")
    private String username;

    @NotEmpty(message = "{login.password-can-not-be-blank}")
    private String password;

    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

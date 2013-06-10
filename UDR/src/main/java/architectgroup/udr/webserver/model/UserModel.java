package architectgroup.udr.webserver.model;

import architectgroup.fact.util.EnumUtil;
import architectgroup.fact.util.Role;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/14/13
 * Time: 2:38 PM
 */
public class UserModel {
    private int id;

    @NotEmpty(message = "{login.username-can-not-be-blank}")
    private String username;

    @NotEmpty(message = "{login.password-can-not-be-blank}")
    private String password;

    @NotEmpty(message = "{login.confirm-password-can-not-be-blank}")
    private String confirmPassword;

    @NotEmpty(message = "{login.email-can-not-be-blank}")
    @Email(message = "{login.email-is-not-valid}")
    private String email;

    private int role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @NotNull
    public String getRoleInString() {
        return EnumUtil.roleFromInt(role).getDescription();
    }
}

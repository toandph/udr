package architectgroup.udr.webserver.model;

import architectgroup.fact.access.license.LicenseObject;
import architectgroup.fact.dto.BuildDto;
import architectgroup.fact.dto.ProjectDto;
import architectgroup.fact.dto.UserDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/5/13
 * Time: 2:54 PM
 */
@Component
@Scope("session")
public class SessionModel implements Serializable {
    private UserDto user;
    private ProjectDto project;
    private BuildDto build;
    private String lastURL;
    private LicenseObject lic;

    public String getLastURL() {
        return lastURL;
    }

    public void setLastURL(String lastURL) {
        this.lastURL = lastURL;
    }

    /**
     * Clear all the attribute of the session
     */
    public void clearSession() {
        setUser(null);
        setBuild(null);
        setProject(null);
        setLastURL(null);

    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public BuildDto getBuild() {
        return build;
    }

    public void setBuild(BuildDto build) {
        this.build = build;
    }

    public ProjectDto getProject() {
        return project;
    }

    public void setProject(ProjectDto project) {
        this.project = project;
    }

    public LicenseObject getLic() {
        return lic;
    }

    public void setLic(LicenseObject lic) {
        this.lic = lic;
    }
}

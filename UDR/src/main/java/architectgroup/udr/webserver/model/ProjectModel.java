package architectgroup.udr.webserver.model;

import architectgroup.fact.dto.UserDto;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/7/13
 * Time: 11:26 AM
 */
public class ProjectModel {
    private int id;

    @NotEmpty(message = "{project.add.project-name-can-not-be-blank}")
    @Size(min = 5, max = 64, message = "{project.add.project-name-must-be-from-5-to-64-characters}")
    private String name;

    @NotEmpty(message = "{project.add.description-can-not-be-blank}")
    private String description;

    private UserDto user;

    private String createdDate;
    private String createdBy;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}



package architectgroup.udr.webserver.model;

import javax.validation.constraints.Size;
import architectgroup.parser.ParserType;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 2/27/13
 * Time: 10:49 AM
 */
public class BuildDetailModel {
    private int id;

    @NotEmpty(message = "{build.add.build-name-can-not-be-blank}")
    @Size(min = 5, max = 16, message = "{build.add.build-name-must-be-from-5-to-16-characters}")
    private String buildName;

    @NotEmpty(message = "{build.add.description-can-not-be-blank}")
    private String description;
    private String createdBy;
    private String createdDate;
    private String sourceType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}

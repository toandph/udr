package architectgroup.udr.webserver.model;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import architectgroup.parser.ParserType;
import architectgroup.udr.webserver.validator.CheckExtension;
import architectgroup.udr.webserver.validator.CheckExtensionNullable;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 2/27/13
 * Time: 10:49 AM
 */
public class BuildUploadModel {
    @NotEmpty(message = "{build.add.build-name-can-not-be-blank}")
    @Size(min = 5, max = 16, message = "{build.add.build-name-must-be-from-5-to-16-characters}")
    private String buildName;
    private int projectId;

    @CheckExtension(extension = ".xml", message = "{build.add.please-upload-only-xml-file}")
    @NotNull(message = "Not null")
    private CommonsMultipartFile xmlFile;

    @CheckExtension(extension = ".zip", message = "{build.add.please-upload-only-zip-file}")
    private CommonsMultipartFile sourceFile;

    @CheckExtensionNullable(extension = ".xml", message = "{build.add.please-upload-only-xml-file}")
    private CommonsMultipartFile imagixFile;

    private ParserType sourceType;

    public ParserType getSourceType() {
        return sourceType;
    }

    public void setSourceType(ParserType sourceType) {
        this.sourceType = sourceType;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public CommonsMultipartFile getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(CommonsMultipartFile xmlFile) {
        this.xmlFile = xmlFile;
    }

    public CommonsMultipartFile getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(CommonsMultipartFile sourceFile) {
        this.sourceFile = sourceFile;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public CommonsMultipartFile getImagixFile() {
        return imagixFile;
    }

    public void setImagixFile(CommonsMultipartFile imagixFile) {
        this.imagixFile = imagixFile;
    }
}

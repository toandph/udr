package architectgroup.fact;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/5/12
 * Time: 2:30 PM
 */
public class Context {
    private int dbType;
    private String url;
    private String username;
    private String password;
    private String logFile;
    private String buildUpload;

    public String getBuildUpload() {
        return buildUpload;
    }

    public void setBuildUpload(String buildUpload) {
        this.buildUpload = buildUpload;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public int getDbType() {
        return dbType;
    }

    public void setDbType(int dbType) {
        this.dbType = dbType;
    }
}

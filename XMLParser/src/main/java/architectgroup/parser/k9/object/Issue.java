package architectgroup.parser.k9.object;
import java.util.ArrayList;
import java.lang.reflect.*;

import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Toan Dang, Architect Group INC
 * User: Administrator
 * Date: 3/23/12
 * Time: 10:56 AM
 */

public class Issue {
    private String problemID;
    private String file;
    private String method;
    private String line;
    private String column;
    private String code;
    private String message;
    private String anchor;
    private String prefix;
    private String postfix;
    private String citingStatus;
    private String state;
    private String owner;
    private String severity;
    private String severityLevel;
    private String displayAs;
    private String dateOriginated;
    private String url;

    private ArrayList<Trace> traceList;

    private static final String METHOD_NAME_GET = "get";
    private static final String METHOD_NAME_SET = "set";

    public Issue() {

    }

    public Issue(String problemID, String file, String method, String line, String column, String code, String message, String anchor, String prefix, String postfix, ArrayList<Trace> traceList, String citingStatus, String state, String owner, String severity, String severityLevel, String displayAs, String dateOriginated, String url) {
        this.problemID = problemID;
        this.file = file;
        this.method = method;
        this.line = line;
        this.column = column;
        this.code = code;
        this.message = message;
        this.anchor = anchor;
        this.prefix = prefix;
        this.postfix = postfix;
        this.traceList = traceList;
        this.citingStatus = citingStatus;
        this.state = state;
        this.owner = owner;
        this.severity = severity;
        this.severityLevel = severityLevel;
        this.displayAs = displayAs;
        this.dateOriginated = dateOriginated;
        this.url = url;
    }

    public <T> void setAttribute(String name, @NotNull T value) throws SecurityException,NoSuchMethodException,IllegalArgumentException,IllegalAccessException,InvocationTargetException {
        String methodName = METHOD_NAME_SET + WordUtils.capitalize(name);
        Method method = this.getClass().getMethod(methodName,value.getClass());
        method.invoke(this, value);
    }

    public Object getAttribute(String name) throws SecurityException,NoSuchMethodException,IllegalArgumentException,IllegalAccessException,InvocationTargetException {
        String methodName = METHOD_NAME_GET + WordUtils.capitalize(name);
        Method method = this.getClass().getMethod(methodName);
        return method.invoke(this);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPostfix() {
        return postfix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    public String getCitingStatus() {
        return citingStatus;
    }

    public void setCitingStatus(String citingStatus) {
        this.citingStatus = citingStatus;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSeveritylevel() {
        return severityLevel;
    }

    public void setSeveritylevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public String getDisplayAs() {
        return displayAs;
    }

    public void setDisplayAs(String displayAs) {
        this.displayAs = displayAs;
    }

    public String getDateOriginated() {
        return dateOriginated;
    }

    public void setDateOriginated(String dateOriginated) {
        this.dateOriginated = dateOriginated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProblemID() {
        return problemID;
    }

    public void setProblemID(String problemID) {
        this.problemID = problemID;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public ArrayList<Trace> getTraceList() {
        return traceList;
    }

    public void setTraceList(ArrayList<Trace> traceList) {
        this.traceList = traceList;
    }
}

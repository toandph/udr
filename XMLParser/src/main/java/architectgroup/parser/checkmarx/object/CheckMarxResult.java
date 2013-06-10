package architectgroup.parser.checkmarx.object;

import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CheckMarxResult {
    private String nodeId;          // 190619
    private String fileName;        // src\main\java\org\owasp\webgoat\lessons\Category.java
    private String line;            // 39
    private String column;          // 14
    private String falsePositive;   // False
    private String severity;        // Low
    private String assignToUser;    //
    private String state;           // 0
    private String remark;          //
    private String deepLink;        // http://AG-SVR202/CxWebClient/ViewerMain.aspx?scanid=19&amp;projectid=16&amp;pathid=619
    private List<CheckMarxPath> checkMarxPathList;

    private static final String METHOD_NAME_GET = "get";
    private static final String METHOD_NAME_SET = "set";

    public CheckMarxResult() {

    }

    public CheckMarxResult(String NodeId, String fileName, String column, String falsePositive, String severity, String line,
                           String assignToUser, String state, String remark, List<CheckMarxPath> checkMarxPathList, String deepLink) {
        this.nodeId = NodeId;
        this.fileName = fileName;
        this.column = column;
        this.falsePositive = falsePositive;
        this.severity = severity;
        this.line = line;
        this.assignToUser = assignToUser;
        this.state = state;
        this.remark = remark;
        this.deepLink = deepLink;
        this.checkMarxPathList = checkMarxPathList;
    }

    public <T> void setAttribute(String name, @NotNull T value) throws SecurityException,NoSuchMethodException,IllegalArgumentException,IllegalAccessException,InvocationTargetException {
        name = name.contains("class")?"ClassName":name;
        String methodName = METHOD_NAME_SET + WordUtils.capitalize(name);
        Method method = this.getClass().getMethod(methodName,value.getClass());
        method.invoke(this, value);
    }

    public Object getAttribute(String name) throws SecurityException,NoSuchMethodException,IllegalArgumentException,IllegalAccessException,InvocationTargetException {
        String methodName = METHOD_NAME_GET + WordUtils.capitalize(name);
        Method method = this.getClass().getMethod(methodName);
        return method.invoke(this);
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getFalsePositive() {
        return falsePositive;
    }

    public void setFalsePositive(String falsePositive) {
        this.falsePositive = falsePositive;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getAssignToUser() {
        return assignToUser;
    }

    public void setAssignToUser(String assignToUser) {
        this.assignToUser = assignToUser;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<CheckMarxPath> getCheckMarxPathList() {
        return checkMarxPathList;
    }

    public void setCheckMarxPathList(List<CheckMarxPath> checkMarxPathList) {
        this.checkMarxPathList = checkMarxPathList;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

}

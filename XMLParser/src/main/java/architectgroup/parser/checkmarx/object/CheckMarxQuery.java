package architectgroup.parser.checkmarx.object;

import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CheckMarxQuery {
    private String id;
    private String cweId;
    private String name;
    private String group;
    private String severity;
    private List<CheckMarxResult> checkMarxResults;

    private static final String METHOD_NAME_GET = "get";
    private static final String METHOD_NAME_SET = "set";

    public CheckMarxQuery() {

    }

    public CheckMarxQuery(String id, String cweId, String name, String group, String severity,
                         List<CheckMarxResult> checkMarxResults) {
        this.id = id;
        this.cweId = cweId;
        this.name = name;
        this.group = group;
        this.severity = severity;
        this.checkMarxResults = checkMarxResults;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCweId() {
        return cweId;
    }

    public void setCweId(String cweId) {
        this.cweId = cweId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public List<CheckMarxResult> getCheckMarxResults() {
        return checkMarxResults;
    }

    public void setCheckMarxResults(List<CheckMarxResult> checkMarxResults) {
        this.checkMarxResults = checkMarxResults;
    }
}

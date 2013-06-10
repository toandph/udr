package architectgroup.parser.infer.object;

import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Bug {
    private String bugID;
    private String className;
    private String kind;
    private String type;
    private String qualifier;
    private String severity;
    private String line;
    private String procedure;
    private String procedure_id;
    private String file;
    private ArrayList<Trace> inferTraceList;
    private String key;
    private QualifierTag qualifierTag;

    private static final String METHOD_NAME_GET = "get";
    private static final String METHOD_NAME_SET = "set";

    public Bug() {

    }

    public Bug(String bugID, String className, String kind, String type, String qualifier, String severity, String line,
               String procedure, String procedure_id, String file, ArrayList<Trace> inferTraceList, String key, QualifierTag qualifierTag) {
        this.bugID = bugID;
        this.className = className;
        this.kind = kind;
        this.type = type;
        this.qualifier = qualifier;
        this.severity = severity;
        this.line = line;
        this.procedure = procedure;
        this.procedure_id = procedure_id;
        this.file = file;
        this.inferTraceList = inferTraceList;
        this.key = key;
        this.qualifierTag = qualifierTag;
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

    public String getBugID() {
        return bugID;
    }

    public void setBugID(String bugID) {
        this.bugID = bugID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
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

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getProcedure_id() {
        return procedure_id;
    }

    public void setProcedure_id(String procedure_id) {
        this.procedure_id = procedure_id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public ArrayList<Trace> getInferTraceList() {
        return inferTraceList;
    }

    public void setInferTraceList(ArrayList<Trace> inferTraceList) {
        this.inferTraceList = inferTraceList;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public QualifierTag getQualifierTag() {
        return qualifierTag;
    }

    public void setQualifierTag(QualifierTag qualifierTag) {
        this.qualifierTag = qualifierTag;
    }
}

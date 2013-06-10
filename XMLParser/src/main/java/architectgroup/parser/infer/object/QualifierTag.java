package architectgroup.parser.infer.object;
/**
 * Created by Toan Dang, Architect Group INC
 * User: Administrator
 * Date: 3/23/12
 * Time: 10:56 AM
 */

public class QualifierTag {
    private int line;
    private int assigned_line;
    private String value;
    private int call_line;
    private String call_procedure;

    public QualifierTag(int line, int assigned_line, String value, int call_line, String call_procedure) {
        this.line = line;
        this.assigned_line = assigned_line;
        this.value = value;
        this.call_line = call_line;
        this.call_procedure = call_procedure;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getAssigned_line() {
        return assigned_line;
    }

    public void setAssigned_line(int assigned_line) {
        this.assigned_line = assigned_line;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCall_line() {
        return call_line;
    }

    public void setCall_line(int call_line) {
        this.call_line = call_line;
    }

    public String getCall_procedure() {
        return call_procedure;
    }

    public void setCall_procedure(String call_procedure) {
        this.call_procedure = call_procedure;
    }
}

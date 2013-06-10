package architectgroup.parser.goanna.object;
/**
 * Created by Toan Dang, Architect Group INC
 * User: Administrator
 * Date: 3/23/12
 * Time: 10:56 AM
 */

public class TraceLine {
    private String line;
    private String text;
    private String type;
    private String refID;

    public TraceLine() {

    }

    public TraceLine(String line, String text, String type, String refID) {
        this.line = line;
        this.text = text;
        this.type = type;
        this.refID = refID;
    }

    public String getLine() {
        return line;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefID() {
        return refID;
    }

    public void setRefID(String refID) {
        this.refID = refID;
    }

    public void setLine(String line) {

        this.line = line;
    }
}

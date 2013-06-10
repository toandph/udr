package architectgroup.parser.k9.object;

import java.util.ArrayList;
/**
 * Created by Toan Dang, Architect Group INC
 * User: Administrator
 * Date: 3/23/12
 * Time: 10:56 AM
 */


public class Trace {
    private String file;
    private String method;
    private String id;
    private ArrayList<TraceLine> traceLineList;

    public Trace() {

    }

    public Trace(String file, String method, String id, ArrayList<TraceLine> traceLineList) {
        this.file = file;
        this.method = method;
        this.id = id;
        this.traceLineList = traceLineList;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<TraceLine> getTraceLineList() {
        return traceLineList;
    }

    public void setTraceLineList(ArrayList<TraceLine> trace) {
        this.traceLineList = trace;
    }
}

package architectgroup.parser.infer.object;

import java.util.ArrayList;

/**
 * Created by Toan Dang, Architect Group INC
 * User: Administrator
 * Date: 3/23/12
 * Time: 10:56 AM
 */


public class Trace {
    private int num;
    private int level;
    private String file;
    private int line;
    private String code;
    private String description;
    private String node_kind;
    private String node_name;
    private String node_nameID;
    private String node_branch;
    private ArrayList<Trace> inferTraceList;

    public Trace(int num, int level, String file, int line, String code, String description, String node_kind,
                 String node_name, String node_nameID, String node_branch, ArrayList<Trace> inferTraceList) {
        this.num = num;
        this.level = level;
        this.file = file;
        this.line = line;
        this.code = code;
        this.description = description;
        this.node_kind = node_kind;
        this.node_name = node_name;
        this.node_nameID = node_nameID;
        this.node_branch = node_branch;
        this.inferTraceList = inferTraceList;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNode_kind() {
        return node_kind;
    }

    public void setNode_kind(String node_kind) {
        this.node_kind = node_kind;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public String getNode_nameID() {
        return node_nameID;
    }

    public void setNode_nameID(String node_nameID) {
        this.node_nameID = node_nameID;
    }

    public String getNode_branch() {
        return node_branch;
    }

    public void setNode_branch(String node_branch) {
        this.node_branch = node_branch;
    }

    public ArrayList<Trace> getInferTraceList() {
        return inferTraceList;
    }

    public void setInferTraceList(ArrayList<Trace> inferTraceList) {
        this.inferTraceList = inferTraceList;
    }
}

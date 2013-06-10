package architectgroup.parser.checkmarx.object;

import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Toan Dang, Architect Group INC
 * User: Administrator
 * Date: 3/23/12
 * Time: 10:56 AM
 */


public class CheckMarxPath {
    private int resultId;
    private int pathId;
    private String similarityId;
    private String fileName;
    private int line;
    private int column;
    private int nodeId;
    private String name;
    private String type;
    private String length;

    private static final String METHOD_NAME_GET = "get";
    private static final String METHOD_NAME_SET = "set";

    public CheckMarxPath() {

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

    public CheckMarxPath(int resultId, int pathId, String similarityId, int line, int column, int nodeId, String fileName,
                         String name, String type, String length) {
        this.resultId = resultId;
        this.pathId = pathId;
        this.similarityId = similarityId;
        this.line = line;
        this.column = column;
        this.nodeId = nodeId;
        this.fileName = fileName;
        this.name = name;
        this.type = type;
        this.length = length;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public void setResultId(String resultId) {
        try {
            int result = Integer.parseInt(resultId);
            this.resultId = result;
        } catch (Exception err) {
            this.resultId = 0;
        }
    }

    public int getPathId() {
        return pathId;
    }

    public void setPathId(int pathId) {
        this.pathId = pathId;
    }

    public void setPathId(String pathId) {
        try {
            int path = Integer.parseInt(pathId);
            this.pathId = path;
        } catch (Exception err) {
            this.pathId = 0;
        }
    }

    public String getSimilarityId() {
        return similarityId;
    }

    public void setSimilarityId(String similarityId) {
        this.similarityId = similarityId;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setLine(String line) {
        try {
            int lines = Integer.parseInt(line);
            this.line = lines;
        } catch (Exception err) {
            this.line = 0;
        }
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setColumn(String column) {
        try {
            int cols = Integer.parseInt(column);
            this.column = cols;
        } catch (Exception err) {
            this.column = 0;
        }
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public void setNodeId(String nodeId) {
        try {
            int id = Integer.parseInt(nodeId);
            this.nodeId = id;
        } catch (Exception err) {
            this.nodeId = 0;
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}

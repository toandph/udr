package architectgroup.fact.dto;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 5/27/12
 * Time: 6:27 PM
 */
public class IssueSignatureDto {
    protected Integer id;
    protected String method;
    protected Integer severity;
    protected String code;
    protected String traceSignature;
    protected Integer firstOccur;
    protected String trend;
    protected Integer flag;

    public IssueSignatureDto() {

    }

    public IssueSignatureDto(Integer id, String traceSignature, String trend) {
        setId(id);
        setTraceSignature(traceSignature);
        setTrend(trend);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTraceSignature() {
        return traceSignature;
    }

    public void setTraceSignature(String traceSignature) {
        this.traceSignature = traceSignature;
    }

    public Integer getFirstOccur() {
        return this.firstOccur;
    }

    public void setFirstOccur(Integer firstOccur) {
        this.firstOccur = firstOccur;
    }

    public String getTrend() {
        return this.trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public Integer getFlag() {
        return this.flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}

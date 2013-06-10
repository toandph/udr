package architectgroup.parser.goanna;

import architectgroup.fact.dto.IssueDto;
import architectgroup.fact.dto.TraceDto;
import architectgroup.fact.util.Result;
import architectgroup.fact.util.Status;
import architectgroup.parser.xmlreport.ReportHelper;
import architectgroup.parser.xmlreport.ReportParser;
import architectgroup.parser.goanna.object.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.util.*;

/**
 * Created by Toan Dang, Architect Group INC
 * User: Administrator
 * Date: 3/23/12
 * Time: 10:56 AM
 */

public class GoannaReportSAXParser extends ReportParser  {
    private static String ISSUE_ROOT_TAG = "results";
    private static String ISSUE_TAG = "warning";
    private static String TRACE_TAG = "trace";
    private static String[] PROBLEM_TAG_NAMES = {"warningID","absFile","method","lineNo","column","checkName","message", "severity", "rules", TRACE_TAG};
    private static String[] PROBLEM_METHOD = {"problemID","file","method","line","column","code","message","severitylevel", "displayAs"};
    private static String[] TRACE_TAG_NAMES = {"file","method","id","traceLine","line","text","type","refId"};
    private static String TRACE_BLOCK_TAG = "traceblock";
    private static String TRACE_LINE_TAG = "traceline";

    private Issue goannaIssue;                             // Raw issue (just temporary, this will store the issue that is in the process.
    private Trace goannaTrace;                             // Raw trace
    private ArrayList<TraceLine> goannaTraceLineList;      // Raw Trace Line
    private ArrayList<Trace> goannaTraceList;              // Raw Trace List
    private TraceLine goannaTraceLine;
    private String tagName = "";
    private String tagCharacter = "";
    private String value = "";
    private int blockId = 0;
    private HashMap<String, Integer> blockHash = new HashMap<String, Integer>();

    public GoannaReportSAXParser(InputStream is) {
        super("", is, ReportHelper.STREAM_TYPE);
    }

    private short severityCode(String severity) {
        if (severity != null) {
            if (severity.equalsIgnoreCase("Low")) {
                return 10;
            } else if (severity.equalsIgnoreCase("High")) {
                return 3;
            } else if (severity.equalsIgnoreCase("Medium")) {
                return 6;
            } else if (severity.equalsIgnoreCase("Info")) {
                return 10;
            }
        }
        return 10;
    }

    @Override
    public void startElement (String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase(ISSUE_ROOT_TAG)) {
            // Initialize
            _process.initialize();
        } else if (qName.equalsIgnoreCase(ISSUE_TAG)) {
            goannaIssue = new Issue();
            goannaTraceList = new ArrayList<Trace>();
            blockId = 0;
            tagName = "";
        }
        else if (qName.equalsIgnoreCase(TRACE_BLOCK_TAG)) {
            goannaTrace = new Trace();
            goannaTraceLineList = new ArrayList<TraceLine>();
            goannaTrace.setFile(attributes.getValue(TRACE_TAG_NAMES[0]));
            goannaTrace.setMethod(attributes.getValue(TRACE_TAG_NAMES[1]));

            if (!blockHash.containsKey(goannaTrace.getFile()+goannaTrace.getMethod())) {
                blockHash.put(goannaTrace.getFile()+goannaTrace.getMethod(), blockId);
                blockId++;
            } else {
                blockId = blockHash.get(goannaTrace.getFile()+goannaTrace.getMethod());
            }
            goannaTrace.setId(Integer.toString(blockId));
            tagName = "";
        } else if (qName.equalsIgnoreCase(TRACE_LINE_TAG)) {
            goannaTraceLine = new TraceLine();
            goannaTraceLine.setLine(attributes.getValue(TRACE_TAG_NAMES[4]));
            goannaTraceLine.setText(attributes.getValue(TRACE_TAG_NAMES[5]));
            goannaTraceLine.setType(attributes.getValue(TRACE_TAG_NAMES[6]));
            goannaTraceLine.setRefID(Integer.toString(-1));
            tagName = "";
        } else {
            for (String PROBLEM_TAG_NAME : PROBLEM_TAG_NAMES) {
                if (PROBLEM_TAG_NAME.equalsIgnoreCase(qName)) {
                    tagCharacter = "start";
                    value = "";
                }
            }
            tagName = qName;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase(ISSUE_TAG)) {
            // Finish get the 1 Issue
            if (goannaIssue != null) {
                IssueDto issueDto = convertToIssueDto(goannaIssue);
                errorOccur = Result.SUCCESS;        // Only come to here it is success
                _process.processInsertIssue(issueDto);
            } else {
                System.out.println("Error in Goanna Issue Syntax.");
            }

        } else if (qName.equalsIgnoreCase(TRACE_BLOCK_TAG)) {
            if (goannaTrace != null) {
                goannaTrace.setTraceLineList(goannaTraceLineList);
                if (goannaTraceList != null) {
                    goannaTraceList.add(goannaTrace);
                } else {
                    System.out.println("Error in Goanna Trace List Syntax");
                }
            }
        } else if (qName.equalsIgnoreCase(TRACE_TAG)) {
            if (goannaIssue != null) {
                goannaIssue.setTraceList(goannaTraceList);
            }
        } else if (qName.equalsIgnoreCase(TRACE_LINE_TAG)) {
            if (goannaTraceLineList != null) {
                goannaTraceLineList.add(goannaTraceLine);
            } else {
                System.out.println("Error in Goanna Trace Line List Syntax");
            }
        } else if (qName.equalsIgnoreCase(ISSUE_ROOT_TAG)) {
            // Finish ending inserting issue //
            _process.ending();
        } else {
            // Set Value When ending tag //
            for (int i = 0; i < PROBLEM_METHOD.length; i++) {
                if (tagName != null) {
                    if (tagName.equalsIgnoreCase(PROBLEM_TAG_NAMES[i])) {
                        try {
                            if (goannaIssue != null) {
                                goannaIssue.setAttribute(PROBLEM_METHOD[i], value);
                                tagCharacter = "close";
                                value = "";
                                break;
                            }
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }
                }
            }
        }
        tagName = "";
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (tagCharacter.equalsIgnoreCase("start")) {
            value += new String(ch, start, length);
        } else {
            value = new String(ch, start, length);
        }
    }

    /**
     * This function will used for all of the parser.
     * @return the list of the issues.
     */
    public IssueDto convertToIssueDto(Issue raw) {
        IssueDto issue = new IssueDto();
        if (raw != null) {
            issue.setCitingstatus(Status.ANALYZE.toString());
            issue.setCode(raw.getCode());
            issue.setColumn(Integer.parseInt(raw.getColumn()));
            issue.setDisplay("Not support");
            issue.setFile(raw.getFile());
            issue.setLine(Integer.parseInt(raw.getLine()));
            issue.setMessage(raw.getMessage());
            issue.setMethod("Not support");
            issue.setPostfix("Not support");
            issue.setPrefix("Not support");
            issue.setSeveritylevel(severityCode(raw.getSeveritylevel()));
            issue.setState(raw.getState());

            // Convert the raw trace list to TraceList DTO Object
            List<Trace> rawTraceList = raw.getTraceList();
            List<TraceDto> traceDto = getTraceIssueDtoList(rawTraceList, issue);
            issue.setTraceList(traceDto);
        }
        return issue;
    }

    /**
     * function to convert the raw trace to trace dto
     * @param rawTraceList the raw trace list
     * @param issueDto the issue that we want to get the trace
     * @return list of trace dto that can be insert to database
     */
    @NotNull
    private List<TraceDto> getTraceIssueDtoList(@Nullable List<Trace> rawTraceList, IssueDto issueDto) {
        List<TraceDto> traceDtoList = new ArrayList<TraceDto>();
        if (rawTraceList != null && rawTraceList.size() > 0) {
            Trace gTraceData;
            String file, method;
            int id;
            ArrayList<TraceLine> gTraceLineList;
            for (Trace aRawTraceList : rawTraceList) {
                gTraceData = aRawTraceList;
                file = gTraceData.getFile();
                method = gTraceData.getMethod();
                id = Integer.parseInt(gTraceData.getId());     // Current Goanna not have the other id than 0
                gTraceLineList = gTraceData.getTraceLineList();
                issueDto.setMethod(method);
                for (TraceLine gTraceLine : gTraceLineList) {
                    TraceDto traceDto = new TraceDto();
                    traceDto.setBlockid(id);
                    traceDto.setFile(file);
                    traceDto.setLine(Integer.parseInt(gTraceLine.getLine()));
                    traceDto.setMethod(method);

                    if (gTraceLine.getRefID() != null && !gTraceLine.getRefID().equals(""))
                        traceDto.setRefid(Integer.parseInt(gTraceLine.getRefID()));
                    else {
                        traceDto.setRefid(-1);
                    }

                    traceDto.setText(gTraceLine.getText());
                    traceDto.setType(gTraceLine.getType());
                    traceDto.setIssue(issueDto);

                    traceDtoList.add(traceDto);
                }
            }

            // Redefine the refId for Goanna trace
            if (traceDtoList.size() >= 2) {
                for (int j = 1; j < traceDtoList.size(); j++) {
                    if (traceDtoList.get(j).getBlockid() > traceDtoList.get(j-1).getBlockid()) {
                        traceDtoList.get(j-1).setRefid(traceDtoList.get(j).getBlockid());
                    }
                }
            }

        }
        return traceDtoList;
    }
}

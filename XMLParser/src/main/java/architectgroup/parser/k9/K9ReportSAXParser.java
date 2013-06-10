package architectgroup.parser.k9;

import architectgroup.fact.dto.IssueDto;
import architectgroup.fact.dto.TraceDto;
import architectgroup.fact.util.Result;
import architectgroup.parser.process.ParserProcess;
import architectgroup.parser.xmlreport.ReportHelper;
import architectgroup.parser.xmlreport.ReportParser;
import architectgroup.parser.k9.object.*;
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

public class K9ReportSAXParser extends ReportParser  {
    public static String ISSUE_ROOT_TAG = "errorList";
    private static String ISSUE_TAG = "problem";
    private static String TRACE_TAG = "trace";
    private static String TRACE_BLOCK_TAG = "traceblock";
    private static String TRACE_LINE_TAG = "traceline";

    private static String[] PROBLEM_TAG_NAMES = {"problemID","file","method","line","column","code","message","anchor","prefix",
            "postfix","citingStatus","state","owner","severity","severitylevel","displayAs",
            "dateOriginated","url",TRACE_TAG};

    private static String[] PROBLEM_METHOD = {"problemID","file","method","line","column","code","message","anchor","prefix","postfix","citingStatus","state","owner","severity","severitylevel","displayAs"};
    private static String[] TRACE_TAG_NAMES = {"file","method","id","traceLine","line","text","type","refId"};

    private Issue k9Issue;                             // Raw issue (just temporary, this will store the issue that is in the process.
    private Trace k9Trace;                             // Raw trace
    private ArrayList<TraceLine> k9TraceLineList;      // Raw Trace Line
    private ArrayList<Trace> k9TraceList;              // Raw Trace List
    private TraceLine k9TraceLine;
    private String tagName = "";
    private String tagCharacter = "";
    private String value = "";

    public K9ReportSAXParser(InputStream is) {
        super("", is, ReportHelper.STREAM_TYPE);
    }

    public K9ReportSAXParser(String sResourceXml, InputStream is, int type) {
        super(sResourceXml, is, type);
    }

    @Override
    public void startElement (String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase(ISSUE_ROOT_TAG)) {
            // Initialize
            _process.initialize();

        } else if (qName.equalsIgnoreCase(ISSUE_TAG)) {
            k9Issue = new Issue();
            k9TraceList = new ArrayList<Trace>();
            tagName = "";
        }
        else if (qName.equalsIgnoreCase(TRACE_BLOCK_TAG)) {
            k9Trace = new Trace();
            k9TraceLineList = new ArrayList<TraceLine>();
            k9Trace.setFile(attributes.getValue(TRACE_TAG_NAMES[0]));
            k9Trace.setMethod(attributes.getValue(TRACE_TAG_NAMES[1]));
            k9Trace.setId(attributes.getValue(TRACE_TAG_NAMES[2]));
            tagName = "";
        } else if (qName.equalsIgnoreCase(TRACE_LINE_TAG)) {
            k9TraceLine = new TraceLine();
            k9TraceLine.setLine(attributes.getValue(TRACE_TAG_NAMES[4]));
            k9TraceLine.setText(attributes.getValue(TRACE_TAG_NAMES[5]));
            k9TraceLine.setType(attributes.getValue(TRACE_TAG_NAMES[6]));
            k9TraceLine.setRefID(attributes.getValue(TRACE_TAG_NAMES[7]));
            tagName = "";
        } else {
            for (int i = 0; i < PROBLEM_TAG_NAMES.length; i++) {
                if (PROBLEM_TAG_NAMES[i].equalsIgnoreCase(qName)) {
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
            if (k9Issue != null) {
                IssueDto issueDto = convertToIssueDto(k9Issue);
                errorOccur = Result.SUCCESS;        // Only come to here it is success
                _process.processInsertIssue(issueDto);
            }

        } else if (qName.equalsIgnoreCase(TRACE_BLOCK_TAG)) {
            if (k9Trace != null) {
                k9Trace.setTraceLineList(k9TraceLineList);
                if (k9TraceList != null) {
                    k9TraceList.add(k9Trace);
                }
            }
        } else if (qName.equalsIgnoreCase(TRACE_TAG)) {
            if (k9Issue != null) {
                k9Issue.setTraceList(k9TraceList);
            }
        } else if (qName.equalsIgnoreCase(TRACE_LINE_TAG)) {
            if (k9TraceLineList != null) {
                k9TraceLineList.add(k9TraceLine);
            }
        } else if (qName.equalsIgnoreCase(ISSUE_ROOT_TAG)) {
            _process.ending();
        } else {
            // Set Value When ending tag //
            for (int i = 0; i < PROBLEM_METHOD.length; i++) {
                if (tagName != null) {
                    if (tagName.equalsIgnoreCase(PROBLEM_TAG_NAMES[i])) {
                        try {
                            if (k9Issue == null) {
                                //_process.error(new NullPointerException());
                            } else {
                                k9Issue.setAttribute(PROBLEM_METHOD[i], value);
                            }

                            tagCharacter = "close";
                            value = "";
                            break;
                        } catch (Exception err) {
                            err.printStackTrace();
                            _process.error(err);
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
    @NotNull
    public IssueDto convertToIssueDto(Issue raw) {
        IssueDto issue = new IssueDto();
        issue.setCitingstatus(raw.getCitingStatus());
        issue.setCode(raw.getCode());
        issue.setColumn(Integer.parseInt(raw.getColumn()));
        issue.setDisplay(raw.getDisplayAs());
        issue.setFile(raw.getFile());
        issue.setLine(Integer.parseInt(raw.getLine()));
        issue.setMessage(raw.getMessage());
        issue.setMethod(raw.getMethod());
        issue.setPostfix(raw.getPostfix());
        issue.setPrefix(raw.getPrefix());
        issue.setSeveritylevel(Short.parseShort(raw.getSeveritylevel()));
        issue.setState(raw.getState());

        // Convert the raw trace list to TraceList DTO Object
        List<Trace> rawTraceList = raw.getTraceList();
        List<TraceDto> traceDto = getTraceIssueDtoList(rawTraceList, issue);
        issue.setTraceList(traceDto);
        return issue;
    }

    /**
     * function to convert the raw trace to trace dto
     * @param rawTraceList
     * @param issueDto
     * @return
     */
    @NotNull
    private List<TraceDto> getTraceIssueDtoList(@Nullable List<Trace> rawTraceList, IssueDto issueDto) {
        List<TraceDto> traceDtoList = new ArrayList<TraceDto>();
        if (rawTraceList != null && rawTraceList.size() > 0) {
            Trace k9TraceData;
            String file, method;
            int id;
            TraceLine k9TraceLine;
            ArrayList<TraceLine> k9traceLineList;
            for (int i=0; i< rawTraceList.size(); i++) {
                k9TraceData = rawTraceList.get(i);
                file = k9TraceData.getFile();
                method = k9TraceData.getMethod();
                id = Integer.parseInt(k9TraceData.getId());
                k9traceLineList = k9TraceData.getTraceLineList();

                for (int j = 0; j < k9traceLineList.size(); j++){
                    TraceDto traceDto = new TraceDto();
                    k9TraceLine = k9traceLineList.get(j);

                    traceDto.setBlockid(id);
                    traceDto.setFile(file);
                    traceDto.setIssue(issueDto);
                    traceDto.setLine(Integer.parseInt(k9TraceLine.getLine()));
                    traceDto.setMethod(method);

                    if (k9TraceLine.getRefID() != null && !k9TraceLine.getRefID().equals(""))
                        traceDto.setRefid(Integer.parseInt(k9TraceLine.getRefID()));
                    else {
                        traceDto.setRefid(-1);
                    }

                    traceDto.setText(k9TraceLine.getText());
                    traceDto.setType(k9TraceLine.getType());
                    traceDto.setIssue(issueDto);

                    traceDtoList.add(traceDto);
                }
            }
        }
        return traceDtoList;
    }

}

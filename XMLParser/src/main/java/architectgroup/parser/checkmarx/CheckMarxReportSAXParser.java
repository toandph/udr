package architectgroup.parser.checkmarx;

import architectgroup.fact.dao.IssueDao;
import architectgroup.fact.dao.IssueSignatureDao;
import architectgroup.fact.dao.TraceDao;
import architectgroup.fact.dto.IssueDto;
import architectgroup.fact.dto.TraceDto;
import architectgroup.fact.util.Result;
import architectgroup.fact.util.State;
import architectgroup.fact.util.Status;
import architectgroup.parser.xmlreport.ReportHelper;
import architectgroup.parser.xmlreport.ReportParser;
import architectgroup.parser.checkmarx.object.*;
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

public class CheckMarxReportSAXParser extends ReportParser  {
    public static String ISSUE_ROOT_TAG = "CxXMLResults";
    private static String ISSUE_TAG_QUERY = "query";
    private static String ISSUE_TAG_RESULT = "result";
    private static String TRACE_TAG = "trace";
    private static String[] PROBLEM_TAG_NAMES = {"NodeId","FileName","Line","Column","FalsePositive","Severity","message", "severity", "rules", TRACE_TAG};
    private static String[] TRACE_METHOD = {"fileName","line","column","nodeId","name","type","length"};
    private static String[] TRACE_TAG_NAMES = {"FileName","Line","Column","NodeId","Name","Type","Length"};
    private static String TRACE_BLOCK_TAG = "path";
    private static String TRACE_LINE_TAG = "pathnode";

    private CheckMarxQuery cmQuery;                   // Raw issue (just temporary, this will store the issue that is in the process.
    private CheckMarxResult cmResult;                 // Raw trace
    private ArrayList<CheckMarxPath> cmPathList;      // Raw Trace Line
    private CheckMarxPath cmPath;
    private String tagName = "";
    private String tagCharacter = "";
    private String value = "";

    private List<String> tmpSignature = new ArrayList<String>();
    private IssueSignatureDao issueSignatureDao;
    private IssueDao issueDao;
    private TraceDao traceDao;
    private int sizeSignature;

    private int newId = 1;
    private List<IssueDto> buffIssue = new ArrayList<IssueDto>();
    private List<TraceDto> buffTrace = new ArrayList<TraceDto>();

    public CheckMarxReportSAXParser(InputStream is) {
        super("", is, ReportHelper.STREAM_TYPE);
    }

    public CheckMarxReportSAXParser(String sResourceXml, InputStream is, int type) {
        super(sResourceXml, is, type);
    }

    @Override
    public void startElement (String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase(ISSUE_ROOT_TAG)) {
            // Initialize
            _process.initialize();
        } else if (qName.equalsIgnoreCase(ISSUE_TAG_QUERY)) {
            cmQuery = new CheckMarxQuery();
            cmQuery.setCweId(attributes.getValue("cweId"));
            cmQuery.setName(attributes.getValue("name"));
            cmQuery.setGroup(attributes.getValue("group"));
            cmQuery.setSeverity(attributes.getValue("Severity"));
            tagName = "";
        } else if (qName.equalsIgnoreCase(ISSUE_TAG_RESULT)) {
            cmResult = new CheckMarxResult();
            cmResult.setAssignToUser(attributes.getValue("AssignToUser"));
            cmResult.setColumn(attributes.getValue("Column"));
            cmResult.setDeepLink(attributes.getValue("DeepLink"));
            cmResult.setFalsePositive(attributes.getValue("FalsePositive"));
            cmResult.setFileName(attributes.getValue("FileName"));
            cmResult.setLine(attributes.getValue("Line"));
            cmResult.setNodeId(attributes.getValue("NodeId"));
            cmResult.setRemark(attributes.getValue("Remark"));
            cmResult.setSeverity(attributes.getValue("Severity"));
            cmResult.setState(attributes.getValue("State"));

            cmPathList = new ArrayList<CheckMarxPath>();
            tagName = "";
        }
        else if (qName.equalsIgnoreCase(TRACE_BLOCK_TAG)) {
            cmPathList = new ArrayList<CheckMarxPath>();
            tagName = "";
        } else if (qName.equalsIgnoreCase(TRACE_LINE_TAG)) {
            cmPath = new CheckMarxPath();
            tagName = "";
        } else {
            for (int i = 0; i < TRACE_TAG_NAMES.length; i++) {
                if (TRACE_TAG_NAMES[i].equalsIgnoreCase(qName)) {
                    tagCharacter = "start";
                    value = "";
                }
            }
            tagName = qName;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase(ISSUE_TAG_RESULT)) {
            // Finish get the 1 Issue
            if (cmQuery != null && cmResult != null) {
                IssueDto issueDto = convertToIssueDto(cmQuery, cmResult);
                errorOccur = Result.SUCCESS;        // Only come to here it is success
                _process.processInsertIssue(issueDto);
            } else {
                System.out.println("Error in Checkmarx Query syntax");
            }
        } else if (qName.equalsIgnoreCase(TRACE_BLOCK_TAG)) {
            if (cmResult != null) {
                cmResult.setCheckMarxPathList(cmPathList);
            } else {
                System.out.println("Error in Checkmarx result syntax");
            }
        } else if (qName.equalsIgnoreCase(TRACE_LINE_TAG)) {
            if (cmPathList != null) {
                cmPathList.add(cmPath);
            } else {
                System.out.println("Error in Checkmarx Path syntax");
            }
        } else if (qName.equalsIgnoreCase(ISSUE_ROOT_TAG)) {
            // Finish ending inserting issue //
            _process.ending();
        } else {
            // Set Value When ending tag //
            for (int i = 0; i < TRACE_TAG_NAMES.length; i++) {
                if (tagName != null) {
                    if (tagName.equalsIgnoreCase(TRACE_TAG_NAMES[i])) {
                        try {
                            if (cmPath != null) {
                                cmPath.setAttribute(TRACE_METHOD[i], value);
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

    private short severityCode(String severity) {
        if (severity != null) {
            if (severity.equalsIgnoreCase("Low")) {
                return 7;
            } else if (severity.equalsIgnoreCase("High")) {
                return 1;
            } else if (severity.equalsIgnoreCase("Medium")) {
                return 4;
            } else if (severity.equalsIgnoreCase("Info")) {
                return 10;
            }
        }
        return 10;
    }

    /**
     * This function will used for all of the parser.
     * @return the list of the issues.
     */
    @NotNull
    public IssueDto convertToIssueDto(CheckMarxQuery rawQuery, CheckMarxResult rawResult) {
        IssueDto issue = new IssueDto();
        issue.setCitingstatus(Status.ANALYZE.toString());
        issue.setCode(rawQuery.getGroup());
        issue.setColumn(Integer.parseInt(rawResult.getColumn()));
        issue.setDisplay(cmResult.getRemark());
        issue.setFile(cmResult.getFileName());
        issue.setLine(Integer.parseInt(rawResult.getLine()));
        issue.setMessage(rawQuery.getName());
        if (cmResult.getCheckMarxPathList() != null) {
            if (cmResult.getCheckMarxPathList().get(0) != null) {
                issue.setMethod(cmResult.getCheckMarxPathList().get(0).getName());
            } else {
                issue.setMethod("Not support");
            }
        } else {
            issue.setMethod("Not support");
        }
        issue.setPostfix("Not support");
        issue.setPrefix("Not support");
        issue.setSeveritylevel(severityCode(rawResult.getSeverity()));
        issue.setState(State.NEW.toString());

        // Convert the raw trace list to TraceList DTO Object
        List<CheckMarxPath> rawTraceList = rawResult.getCheckMarxPathList();
        List<TraceDto> traceDto = getTraceIssueDtoList(rawTraceList, issue);
        issue.setTraceList(traceDto);
        return issue;
    }

    /**
     * function to convert the raw trace to trace dto
     * @param rawPathList
     * @param issueDto
     * @return
     */
    @NotNull
    private List<TraceDto> getTraceIssueDtoList(@Nullable List<CheckMarxPath> rawPathList, IssueDto issueDto) {
        List<TraceDto> traceDtoList = new ArrayList<TraceDto>();
        if (rawPathList != null && rawPathList.size() > 0) {
            String file, method;
            int id;
            for (int i = 0; i < rawPathList.size(); i++) {
                CheckMarxPath cmPath = rawPathList.get(i);
                file = cmPath.getFileName();
                method = cmPath.getName();
                TraceDto traceDto = new TraceDto();
                traceDto.setBlockid(0);
                traceDto.setFile(file);
                traceDto.setIssue(issueDto);
                traceDto.setLine(cmPath.getLine());
                traceDto.setMethod(method);
                traceDto.setRefid(-1);
                traceDto.setText("Not support");
                traceDto.setType("Not support");
                traceDto.setIssue(issueDto);

                traceDtoList.add(traceDto);
            }
        }
        return traceDtoList;
    }

    @Override
    public String generateSignature(@NotNull List<TraceDto> traceList, @NotNull IssueDto issueDto) {
        Collections.sort(traceList, new Comparator<TraceDto>() {
            public int compare(@NotNull TraceDto o1, @NotNull TraceDto o2) {
                String f1 = o1.getType() + o1.getMethod() + o1.getText();
                String f2 = o2.getType() + o2.getMethod() + o2.getText();

                return f1.compareTo(f2);
            }
        });

        String generate = issueDto.getFile() + issueDto.getMethod() + issueDto.getLine() + issueDto.getColumn() + issueDto.getCode() + issueDto.getMessage() + issueDto.getPrefix() + issueDto.getPostfix();
        for (TraceDto trace : traceList) {
            String f1 = trace.getMethod() + trace.getType() + trace.getText() + trace.getLine();
            if (generate == null) generate = "";
            generate += f1;
        }

        try {
            if (generate!=null)
                generate = ReportHelper.md5(generate);
            else
                generate = "";
        } catch (Exception err) {
            err.printStackTrace();
        }
        return generate;
    }
}

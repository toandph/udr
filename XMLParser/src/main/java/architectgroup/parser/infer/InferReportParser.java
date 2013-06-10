package architectgroup.parser.infer;

import architectgroup.fact.dao.DaoFactory;
import architectgroup.fact.dto.IssueDto;
import architectgroup.fact.dto.TraceDto;
import architectgroup.parser.infer.object.Trace;
import architectgroup.parser.k9.object.*;
import architectgroup.parser.xmlreport.ReportParser;
import architectgroup.parser.infer.object.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InferReportParser extends ReportParser {
    //private static final Logger logger = Logger.getLogger(ClassLoader.getSystemClassLoader().getClass());

    @NotNull
    public static String BUG_ROOT_TAG = "bugs";
    @NotNull
    private static String BUG_TAG = "bug";
    @NotNull
    private static String BUG_ATTRIBUTE_TAG = "id";
    @NotNull
    private static String QUALIFIER_TAG = "qualifier_tags";
    @NotNull
    private static String TRACE_TAG = "trace";
    @NotNull
    private static String TRACE_ATTRIBUTE_TAG = "num";
    @NotNull
    private static String TRACE_ELEMENT_TAG = "loc";
    @NotNull
    private static String[] BUG_TAG_NAMES = {"class","kind","type","qualifier","severity","line","procedure","procedure_id","file",
                                         TRACE_TAG, "key",QUALIFIER_TAG};

    @NotNull
    private static String[] QUALIFIER_TAG_NAMES = {"line","assigned_line","value","call_line","call_procedure"};
    @NotNull
    private static String[] TRACE_TAG_NAMES = {"level","file","line","code","description","node"};
    @NotNull
    private static String[] NODE_TAG_NAMES = {"kind","name","name_id","branch"};

    public InferReportParser(String sResourceXml, InputStream is, int type) {
        super(sResourceXml, is, type);
    }

    /* Get all the issues that have the tag name : BUG_TAG */
    @NotNull
    public Map<String, Bug> getIssues() {
        Map<String, Bug> issueMap = new HashMap<String, Bug>();
        issueMap.putAll(getIssues(BUG_TAG));
        return issueMap;
    }

    /* Get list of Trace according to the node */
    @Nullable
    private ArrayList<Trace> getTraces(@Nullable Node node) {
        ArrayList<Trace> inferTraceList = null;
        ArrayList<Trace> tempInferTraceList = new ArrayList<Trace>();
        if (node != null) {
            inferTraceList = new ArrayList<Trace>(); // Initialize the list
            Element traceElement = (Element) node;
            NodeList nodeList = traceElement.getElementsByTagName(TRACE_ELEMENT_TAG);   // Get all node traceBlock

            /* Traverse each trace block */
            int num, level = 0, line = 0;
            String file = null, code = null, description = null, node_kind = null, node_name = null, node_name_id = null, node_branch = null;
            String elementValue;
            Trace inferTrace;
            Node traceBlockNode, traceChildNode;
            Element traceBlockElement;
            Element traceNodeElement;
            for (int i = 0; i < nodeList.getLength(); i++) {
                traceBlockElement = (Element) nodeList.item(i);   // Trace Bock Element i

                elementValue = traceBlockElement.getAttribute(TRACE_ATTRIBUTE_TAG);
                num = elementValue!=null?Integer.parseInt(elementValue):0;

                for (int k=0; k<TRACE_TAG_NAMES.length; k++){
                    traceBlockNode = traceBlockElement.getElementsByTagName(TRACE_TAG_NAMES[k]).item(0);
                    traceChildNode = traceBlockNode.getFirstChild();
                    traceNodeElement = (Element) traceBlockNode;
                    switch (k){
                        case 0:
                            elementValue = traceChildNode!=null?traceChildNode.getNodeValue():null;
                            level = elementValue!=null?Integer.parseInt(elementValue):0;
                            break;
                        case 1:
                            elementValue = traceChildNode!=null?traceChildNode.getNodeValue():null;
                            file = elementValue!=null?elementValue:null;
                            break;
                        case 2:
                            elementValue = traceChildNode!=null?traceChildNode.getNodeValue():null;
                            line = elementValue!=null?Integer.parseInt(elementValue):0;
                            break;
                        case 3:
                            elementValue = traceChildNode!=null?traceChildNode.getNodeValue():null;
                            code = elementValue!=null?elementValue:null;
                            break;
                        case 4:
                            elementValue = traceChildNode!=null?traceChildNode.getNodeValue():null;
                            description = elementValue!=null?elementValue:null;
                            break;
                        case 5:
                            for (int j=0; j<NODE_TAG_NAMES.length; j++){
                               elementValue = traceNodeElement.getAttribute(NODE_TAG_NAMES[j]);
                               switch (j){
                                   case 0:
                                       node_kind = elementValue!=null?elementValue:null;
                                       break;
                                   case 1:
                                       node_name = elementValue!=null?elementValue:null;
                                       break;
                                   case 2:
                                       node_name_id = elementValue!=null?elementValue:null;
                                       break;
                                   case 3:
                                       node_branch = elementValue!=null?elementValue:null;
                               }
                            }
                    }
                }
                inferTrace = new Trace(num, level, file, line, code, description, node_kind, node_name, node_name_id, node_branch, null);
                tempInferTraceList.add(inferTrace);
            }   // End For traverse each trace Block

        } // End If Node != null
        inferTraceList = reorganizeTrace(tempInferTraceList, 0);
        return inferTraceList;
    }
    // sort trace list
    @NotNull
    private ArrayList<Trace> reorganizeTrace(@Nullable ArrayList<Trace> inferTraceList, int level){
        ArrayList<Trace> inferTraces = new ArrayList<Trace>();

        if (inferTraceList ==null || inferTraceList.size()==0)
            return new ArrayList<Trace>();
        else {
            Trace inferTrace;
            while (inferTraceList.size() > 0){
                inferTrace = inferTraceList.get(0);
                if (inferTrace.getLevel()<level)
                    break;
                if (inferTrace.getLevel()==level){
                    inferTraceList.remove(inferTrace);
                    inferTrace.setInferTraceList(reorganizeTrace(inferTraceList, level + 1));
                    inferTraces.add(inferTrace);
                }
            }
        }

        return inferTraces;
    }

    /* Get list of QualifierTag according to the node */
    @Nullable
    private QualifierTag getQualifierTag(@Nullable Node node) {
       QualifierTag qualifierTag = null;
        if (node != null) {
            int line = 0, assigned_line = 0, call_line = 0;
            String value = null, call_procedure = null;
            String elementValue;
            Element qualifierTagElement = (Element) node;

                if (qualifierTagElement.getNodeType()==Node.ELEMENT_NODE){
                    /* Traverse each statusUpdate tag */
                    for (int i=0; i < QUALIFIER_TAG_NAMES.length; i++) {
                        Node qualifierTagNode = qualifierTagElement.getElementsByTagName(QUALIFIER_TAG_NAMES[i]).item(0);
                        elementValue = qualifierTagNode!=null?qualifierTagNode.getNodeValue():null;
                        switch (i){
                            case 0:
                                line = elementValue!=null?Integer.parseInt(elementValue):0;
                                break;
                            case 1:
                                assigned_line = elementValue!=null?Integer.parseInt(elementValue):0;
                                break;
                            case 2:
                                value = elementValue!=null?elementValue:null;
                                break;
                            case 3:
                                call_line = elementValue!=null?Integer.parseInt(elementValue):0;
                                break;
                            case 4:
                                call_procedure = elementValue!=null?elementValue:null;
                        }
                    }
            } // End traverse each statusUpdate tag
            qualifierTag = new QualifierTag(line, assigned_line, value, call_line, call_procedure);
        } // End If Node != null
        return qualifierTag;
    }

    @NotNull
    private Map<String, Bug> getIssues(String nodeName) {
        Map<String, Bug> issueMap = new HashMap<String, Bug>();

        //logger.debug(DEBUG_MESSAGE_START_PARSING);

        Document doc = helper.getRootDocument(sResourceXml, is, type);
        NodeList listOfIssues = doc.getElementsByTagName(nodeName);             // Get all the node with nodeName
        int totalIssues = listOfIssues.getLength();                             // Get the number of issues

        //logger.debug(DEBUG_MESSAGE_TOTAL_NO_OF_ISSUES + totalIssues);

        Node issueNode, elementNode;
        /* traverse each issue (problem tag) */
        for (int i=0; i < totalIssues; i++) {
            issueNode = listOfIssues.item(i);  // Get the node i

            if (issueNode.getNodeType() == Node.ELEMENT_NODE) {
                Bug bug = new Bug();        // Initialize issue object
                Element issueElement = (Element) issueNode;

                bug.setBugID(issueElement.getAttribute(BUG_ATTRIBUTE_TAG));
                for (int k = 0; k < BUG_TAG_NAMES.length; k++) {
                    Node generalNode = issueElement.getElementsByTagName(BUG_TAG_NAMES[k]).item(0);         // Get the node object of a specified tag name.

                    /* Setting history tag */
                    if (BUG_TAG_NAMES[k].equalsIgnoreCase(QUALIFIER_TAG)) {
                        QualifierTag qualifierTag = getQualifierTag(generalNode);
                        bug.setQualifierTag(qualifierTag);
                    }
                    /* Setting trace tag */
                    else if (BUG_TAG_NAMES[k].equalsIgnoreCase(TRACE_TAG)) {
                        ArrayList<Trace> inferTraceList = getTraces(generalNode);
                        bug.setInferTraceList(inferTraceList);
                    }
                    /* Setting for single node */
                    else if (generalNode != null) {
                        /* Call the appropriate set method */
                        try {
                            elementNode = generalNode.getChildNodes().item(0);
                            bug.setAttribute(BUG_TAG_NAMES[k],elementNode!=null?elementNode.getNodeValue():"");
                        } catch (Exception err) {
                                err.printStackTrace();
                        }
                    }
                } // End FOR loop of problem_tag_names[]

                issueMap.put(String.valueOf(i), bug);  // Add to issue map
            } // End If Check Node Is ELEMENT TYPE
        } // End For traverse each PROBLEM tag
        return issueMap;
    }

    @NotNull
    public List<IssueDto> getUniversalIssue() {
        Map<String, Bug> rawIssues = getIssues();
        List<IssueDto> issueList = new ArrayList<IssueDto>();
        for (Object t : rawIssues.keySet()) {
            Bug raw = rawIssues.get(t.toString());
            IssueDto issue = new IssueDto();
            issue.setCitingstatus("Not Supported");
            issue.setCode(raw.getKey());
            issue.setColumn(0);
            issue.setDisplay("Not Supported");
            issue.setFile(raw.getFile());
            issue.setLine(Integer.parseInt(raw.getLine()));
            issue.setMessage(raw.getKind());
            issue.setMethod(raw.getProcedure());
            issue.setPostfix("Not Supported");
            issue.setPrefix("Not Supported");
            issue.setSeveritylevel(Short.parseShort("1"));
            issue.setState("Not Supported");

            // Convert the raw trace list to TraceList DTO Object
            List<architectgroup.parser.infer.object.Trace> rawTraceList = raw.getInferTraceList();
            List<TraceDto> traceDto = getTraceIssueDtoList(rawTraceList, issue);
            issue.setTraceList(traceDto);
            issueList.add(issue);
        }
        return issueList;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Nullable
    private List<TraceDto> getTraceIssueDtoList(List<architectgroup.parser.infer.object.Trace> rawTraceList, IssueDto issueDto) {
        /*
        List<TraceDto> traceDtoList = new ArrayList<TraceDto>();
        if (rawTraceList != null && rawTraceList.size() > 0) {
            architectgroup.parser.infer.object.Trace k9TraceData;
            String file, method;
            int id;
            TraceLine k9TraceLine;
            ArrayList<TraceLine> k9traceLineList;
            for (int i=0; i< rawTraceList.size(); i++) {
                k9TraceData = rawTraceList.get(i);
                file = k9TraceData.getFile();
                method = k9TraceData.getDescription();
                id = Integer.parseInt(k9TraceData.getCode());
                k9traceLineList = k9TraceData.getNum();

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
        */
        return null;
    }

    @NotNull
    public void parse(DaoFactory factory, InputStream file) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

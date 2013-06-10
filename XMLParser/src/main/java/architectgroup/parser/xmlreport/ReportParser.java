package architectgroup.parser.xmlreport;

import architectgroup.fact.dao.DaoFactory;
import architectgroup.fact.dto.IssueDto;
import architectgroup.fact.dto.TraceDto;
import architectgroup.fact.util.Result;
import architectgroup.parser.process.ParserProcess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public abstract class ReportParser extends DefaultHandler {
    @Nullable
    protected ReportHelper helper = ReportHelper.getInstance();

    protected String sResourceXml;
    protected int type;
    protected InputStream is;

    protected SAXParserFactory factory;
    protected  SAXParser saxParser;
    protected ParserProcess _process;
    protected Result errorOccur = Result.PARSE_ERROR;

    protected ReportParser(String sResourceXml, InputStream is, int type) {
        this.sResourceXml = sResourceXml;
        this.is = is;
        this.type = type;
        factory = SAXParserFactory.newInstance();
        try {
            saxParser = factory.newSAXParser();
        } catch (Exception err) {
            err.printStackTrace();
            saxParser = null;
        }
    }
    public void setResources(String sResourceXml, int type) {
        this.sResourceXml = sResourceXml;
        this.type = type;
    }

    /**
     * This function will used for all of the parser.
     * @return the list of the issues.
     */
    @NotNull
    public Result parse(ParserProcess process) {
        this._process = process;
        try {
            saxParser.parse(is, this);
        } catch (Exception err) {
            err.printStackTrace();
        }
        return errorOccur;
    }


    /**
     *
     * @param traceList
     * @param issueDto
     * @return
     */
    public String generateSignature(@NotNull List<TraceDto> traceList, @NotNull IssueDto issueDto) {
        Collections.sort(traceList, new Comparator<TraceDto>() {
            public int compare(@NotNull TraceDto o1, @NotNull TraceDto o2) {
                String f1 = o1.getType() + o1.getMethod() + o1.getText() + o1.getLine();
                String f2 = o2.getType() + o2.getMethod() + o2.getText() + o1.getLine();

                return f1.compareTo(f2);
            }
        });

        // This is check for file name;
        String tmp = issueDto.getFile().replaceAll("\\\\", "/");
        String filename = tmp.substring(tmp.lastIndexOf("/"));
        // This generate below is can not find the existing with exist source
        // String generate = issueDto.getFile() + issueDto.getMethod() + issueDto.getCode() + issueDto.getMessage() + issueDto.getPrefix() + issueDto.getPostfix();
        String generate = filename + issueDto.getMethod() + issueDto.getCode() + issueDto.getMessage() + issueDto.getPrefix() + issueDto.getPostfix();

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

package architectgroup.fact.access.util;

import architectgroup.fact.Const;
import architectgroup.fact.dao.DaoFactory;
import architectgroup.fact.dao.IssueDao;
import architectgroup.fact.dao.IssueSignatureDao;
import architectgroup.fact.dao.TraceDao;
import architectgroup.fact.dto.IssueDto;
import architectgroup.fact.dto.IssueSignatureDto;
import architectgroup.fact.dto.TraceDto;
import architectgroup.fact.util.State;
import architectgroup.parser.process.ParserProcess;
import architectgroup.parser.xmlreport.ReportParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: toandph
 * Date: 4/14/13
 * Time: 1:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class DBProcess implements ParserProcess {
    private TraceDao traceDao;
    private int sizeSignature;

    private int newId = 1;

    private List<String> tmpSignature = new ArrayList<String>();
    private List<IssueDto> buffIssue = new ArrayList<IssueDto>();
    private List<TraceDto> buffTrace = new ArrayList<TraceDto>();
    private IssueSignatureDao issueSignatureDao;
    private IssueDao issueDao;
    private DaoFactory _daoFactory;
    private int _buildId;
    private int _projectId;
    private List<Integer> _buildList;
    private ReportParser parser;

    public DBProcess(ReportParser parser, DaoFactory daoFactory, int projectId, int buildId, List<Integer> buildList) {
        this.parser = parser;
        this._daoFactory = daoFactory;
        this._buildId = buildId;
        this._projectId = projectId;
        this._buildList = buildList;
    }

    public void initialize() {
        issueSignatureDao = _daoFactory.getIssueSignatureDao(_projectId);
        issueDao = _daoFactory.getIssueDao(_projectId, _buildId);
        traceDao = _daoFactory.getTraceDao(_projectId, _buildId);
        sizeSignature = issueSignatureDao.findSize();
    }

    /**
     * Remove the redundant
     * @param issueDto
     * @return true if the issue is valid, else there is a issue the same.
     */
    private boolean validateIssue(IssueDto issueDto) {
        String issueSignature = parser.generateSignature(issueDto.getTraceList(), issueDto);
        if (tmpSignature.contains(issueSignature)) {
            return false;
        } else {
            tmpSignature.add(issueSignature);
        }

        return true;
    }

    /**
     *
     */
    public void processInsertIssue(IssueDto issueDto) {
        List<IssueSignatureDto> listSig = new ArrayList<IssueSignatureDto>();
        List<IssueSignatureDto> listUpdateSig = new ArrayList<IssueSignatureDto>();
        List<IssueDto> listIssue = new ArrayList<IssueDto>();                   // Store the issue temporary, and then insert these at final
        List<TraceDto> listTrace = new ArrayList<TraceDto>();                   // Store the trace temporary, and then insert these at final

        // Store issue to buff
        if (issueDto != null) {
            if (validateIssue(issueDto)) {
                buffIssue.add(issueDto);
                buffTrace.addAll(issueDto.getTraceList());
            }
        }

        if (buffIssue.size() >= Const.MYSQL_WRITE_BUFFER || buffTrace.size() >= Const.MYSQL_WRITE_BUFFER || issueDto == null) {
            if (sizeSignature == 0) {
                for (IssueDto buff : buffIssue) {
                    String issueSignature = parser.generateSignature(buff.getTraceList(), buff);
                    // In case the previous build has no issue. then it must be 0#0..0#1
                    String trend = "";
                    if (_buildList == null || _buildList.size() == 0) {
                        trend = "1";
                    } else {
                        trend = "0";
                        for (int i = 1; i < _buildList.size(); i++) {
                            trend += "#0";
                        }
                        trend += "#1";
                    }
                    IssueSignatureDto issueSignatureDto = new IssueSignatureDto(newId, issueSignature, trend);
                    listSig.add(issueSignatureDto);
                    buff.setId(newId);
                    buff.setState(State.NEW.toString());
                    newId++;
                }
                issueSignatureDao.insert(listSig);
                issueDao.insert(buffIssue);
                traceDao.insert(buffTrace);
            } else if (sizeSignature > 0) {
                int newId = issueSignatureDao.getMaxId() + 1;
                // Check each issue to set the sate //
                if (buffIssue.size() > 0) {
                    for (IssueDto issue : buffIssue) {
                        String issueSignature = parser.generateSignature(issue.getTraceList(), issue);
                        List<IssueSignatureDto> checkSig = issueSignatureDao.findIssueSignatureEquals(issueSignature);
                        if (checkSig.size() > 0){
                            IssueSignatureDto issueSignatureDto = checkSig.get(0);
                            issue.setId(issueSignatureDto.getId());
                            issueSignatureDto.setTrend(issueSignatureDto.getTrend() + "#1");
                            listUpdateSig.add(issueSignatureDto);
                        }
                        else {
                            issue.setId(newId);
                            String trend = "0";
                            if (_buildList != null) {
                                for (int i = 0; i < _buildList.size() - 1; i++) {
                                    trend += "#0";
                                }
                                trend += "#1";
                            }
                            IssueSignatureDto issueSignatureDto = new IssueSignatureDto(newId, issueSignature, trend);
                            listSig.add(issueSignatureDto);
                            newId++;
                        }

                        listIssue.add(issue);
                        listTrace.addAll(issue.getTraceList());
                    }
                }

                issueSignatureDao.update(listUpdateSig);                // Update the signature that already have
                issueSignatureDao.insert(listSig);                      // insert the signature that did not have in the table
                issueDao.insert(listIssue);                             // listIssue is just a small group of buffIssue
                traceDao.insert(listTrace);                             // listTrace is just a small group of buffTrace
            }

            buffIssue = new ArrayList<IssueDto>();  // Reset Buffer
            buffTrace = new ArrayList<TraceDto>();  // Reset Buffer Trace
        }
    }

    public void ending() {
        // Finish ending inserting issue //
        if (buffIssue.size() > 0) {
            processInsertIssue(null);   // Rerun if the size of the buff is less than MAX_BUFF
        }
        if (_buildList != null) {
            issueSignatureDao.updateTrend(_buildList.size() + 1);           // +1 because the build size still not update the newest one.
            issueDao.updateFixedIssue(_buildList.size() + 1);               // Copy the fixed issue from last build to another
            issueDao.updateState(_buildList.size() + 1);                    // Update the state
        }
    }

    public void error(Exception err) {
        err.printStackTrace();
    }
}

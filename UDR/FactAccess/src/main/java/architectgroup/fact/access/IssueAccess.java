package architectgroup.fact.access;

import architectgroup.fact.access.util.FactAccessFactory;
import architectgroup.fact.access.util.FilterCondition;
import architectgroup.fact.util.State;
import architectgroup.fact.dao.*;
import architectgroup.fact.dto.FilterDto;
import architectgroup.fact.dto.IssueDto;
import architectgroup.fact.access.object.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 1/29/13
 * Time: 4:11 PM
 */
public class IssueAccess {
    private FactAccessFactory factAccess;
    private DaoFactory _factory;
    private DatabaseDao _database;

    public IssueAccess(FactAccessFactory factAccess) {
        this.factAccess = factAccess;
        _factory = factAccess.getFactory();
        _database = _factory.getDatabaseDao();
    }

    /**
     *
     * @return the issue List
     */
    public List<IssueDto> getIssues(int projectId, int buildId) {
        IssueDao issueDao = _factory.getIssueDao(projectId, buildId);
        List<IssueDto> issueList = issueDao.findAllIssues();
        return issueList;
    }

    public Map<String, Integer> getIssueState(int projectId, int buildId) {
        IssueDao issueDao = _factory.getIssueDao(projectId, buildId);
        return issueDao.findState();
    }

    /**
     * Get all the issues with the filter
     * @param filter
     * @return
     */
    public List<IssueDto> getIssues(int projectId, int buildId, FilterDto filter) {
        FilterCondition fc = new FilterCondition(filter.getValue());
        IssueDao issueDao = _factory.getIssueDao(projectId, buildId);
        List<IssueDto> issueList = issueDao.findAllIssues(fc.toSQL());
        return issueList;
    }

    /**
     * Return how many issues which is the input state
     * @param state
     * @return
     */
    public int getNumberOfIssueByState(int projectId, int buildId, State state) {
        int numberOfIssue = 0;
        IssueDao issueDao = _factory.getIssueDao(projectId, buildId);
        List<IssueDto> issueList = issueDao.findIssueByState(state);
        numberOfIssue = issueList.size();
        return numberOfIssue;
    }

    public int getNumberOfIssueByState(int projectId, int buildId, State state, FilterDto filter) {
        int numberOfIssue = 0;
        FilterCondition fc = new FilterCondition(filter.getValue());
        IssueDao issueDao = _factory.getIssueDao(projectId, buildId);
        List<IssueDto> issueList = issueDao.findIssueByState(state, fc.toSQL());
        numberOfIssue = issueList.size();

        return numberOfIssue;
    }

    /**
     *
     * @return
     */
    @NotNull
    public List<ChartItem> getNumberOfSeverity(int projectId, int buildId) {
        List<ChartItem> info = new ArrayList<ChartItem>();
        IssueDao issueDao = _factory.getIssueDao(projectId, buildId);
        Map<String, Integer> maps = issueDao.findGroupBySeverity();
        for (String k : maps.keySet()) {
            int p = maps.get(k);
            ChartItem newModel = new ChartItem();
            newModel.setName(getSeverityName(Integer.parseInt(k)));
            newModel.setValue(String.valueOf(p));
            info.add(newModel);
        }
        return info;
    }

    @NotNull
    private String getSeverityName(int id) {
        switch (id) {
            case 1:return "critical";
            case 2:return "severe";
            case 3:return "error";
            case 4:return "unexpected";
            case 5:return "investigate";
            case 6:return "warning";
            case 7:return "suggestion";
            case 8:return "style";
            case 9:return "review";
            case 10:return "info";
        }
        return "Info";
    }

    @NotNull
    public List<ChartItem> getNumberOfSeverity(int projectId, int buildId, FilterDto filter) {
        List<ChartItem> info = new ArrayList<ChartItem>();
        FilterCondition fc = new FilterCondition(filter.getValue());
        IssueDao issueDao = _factory.getIssueDao(projectId, buildId);
        Map<String, Integer> maps = issueDao.findGroupBySeverity(fc.toSQL());

        for (String k : maps.keySet()) {
            int p = maps.get(k);
            ChartItem newModel = new ChartItem();
            newModel.setName(getSeverityName(Integer.parseInt(k)));
            newModel.setValue(String.valueOf(p));
            info.add(newModel);
        }
        return info;
    }

    /**
     *
     * @return ff
     */
    @NotNull
    public List<ChartItem> getNumberOfErrorCode(int projectId, int buildId) {
        List<ChartItem> info = new ArrayList<ChartItem>();
        IssueDao issueDao = _factory.getIssueDao(projectId, buildId);
        Map<String, Integer> maps = issueDao.findGroupByErrorCode();

        for (String k : maps.keySet()) {
            int p = maps.get(k);
            ChartItem newModel = new ChartItem();
            newModel.setName(k);
            newModel.setValue(String.valueOf(p));
            info.add(newModel);
        }
        return info;
    }

    /**
     *
     * @param filter
     * @return
     */
    @NotNull
    public List<ChartItem> getNumberOfErrorCode(int projectId, int buildId, FilterDto filter) {
        List<ChartItem> info = new ArrayList<ChartItem>();

        FilterCondition fc = new FilterCondition(filter.getValue());
        IssueDao issueDao = _factory.getIssueDao(projectId, buildId);
        Map<String, Integer> maps = issueDao.findGroupByErrorCode(fc.toSQL());

        for (String k : maps.keySet()) {
            int p = maps.get(k);
            ChartItem newModel = new ChartItem();
            newModel.setName(k);
            newModel.setValue(String.valueOf(p));
            info.add(newModel);
        }
        return info;
    }

    /**
     * Create JSON Object to show the list of issue
     * @param issueDtoList
     * @param iTotalRecords
     * @param iTotalDisplayRecords
     * @param echo
     * @return
     */
    @NotNull
    public static JSONObject convertToJSON(List<IssueDto> issueDtoList, int iTotalRecords, int iTotalDisplayRecords, int echo){
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();

        result.put("sEcho", echo);
        result.put("iTotalRecords", iTotalRecords);
        result.put("iTotalDisplayRecords", iTotalDisplayRecords);

        for (int i=0; i < issueDtoList.size(); i++){
            IssueDto issueDto = issueDtoList.get(i);
            JSONArray ja = new JSONArray();
            ja.add(issueDto.getId());
            ja.add(issueDto.getCode());
            ja.add(issueDto.getState());
            ja.add(issueDto.getSeverity());
            ja.add(issueDto.getDisplay());
            ja.add(issueDto.getMethod());
            ja.add(issueDto.getFile());
            ja.add(issueDto.getMessage());
            ja.add(issueDto.getCitingstatus());
            array.add(ja);
        }
        result.put("aaData", array);

        return result;
    }

    /**
     *
     * @return the issue List
     */
    public List<IssueDto> getIssues(int projectId, int buildId, String sqlCondition) {
        IssueDao issueDao = _factory.getIssueDao(projectId, buildId);
        List<IssueDto> issueList = issueDao.findAllIssues(sqlCondition);
        return issueList;
    }

    /**
     *
     * @return the issue List
     */
    public int getNumberOfIssues(int projectId, int buildId, String sqlCondition) {
        IssueDao issueDao = _factory.getIssueDao(projectId, buildId);
        int numberOfIssue = issueDao.findNumberOfIssues(sqlCondition);
        return numberOfIssue;
    }

    /**
     *
     * @return the issue List
     */
    public IssueDto getIssueById(int projectId, int buildId, int issueId) {
        IssueDao issueDao = _factory.getIssueDao(projectId, buildId);
        IssueDto issue = issueDao.findIssueById(issueId);
        return issue;
    }

    public int getNextIssueId(int projectId, int buildId, int issueId )  {
        IssueDao issueDao = _factory.getIssueDao(projectId, buildId);
        int issue = issueDao.findNextIssue(issueId);
        return issue;
    }

    public int getPreviousIssueId(int projectId, int buildId, int issueId )  {
        IssueDao issueDao = _factory.getIssueDao(projectId, buildId);
        int issue = issueDao.findPreviousIssue(issueId);
        return issue;
    }
}

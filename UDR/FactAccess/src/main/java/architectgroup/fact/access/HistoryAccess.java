package architectgroup.fact.access;

import architectgroup.fact.Const;
import architectgroup.fact.access.util.FactAccessFactory;
import architectgroup.fact.dao.*;
import architectgroup.fact.dto.HistoryDto;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/18/13
 * Time: 3:30 PM
 */
public class HistoryAccess {
    private FactAccessFactory factAccess;
    private DaoFactory _factory;
    private DatabaseDao _database;

    public HistoryAccess(FactAccessFactory factAccess) {
        this.factAccess = factAccess;
        _factory = factAccess.getFactory();
        _database = _factory.getDatabaseDao();
    }

    /**
     *
     * @param history
     */
    public void add(int projectId, int buildId, HistoryDto history) {
        HistoryDao historyDao = _factory.getHistoryDao(projectId, buildId);
        historyDao.insert(history);
    }

    /**
     *
     * @param projectId
     * @param history
     */
    public void add(int projectId, int buildId, List<HistoryDto> history) {
        HistoryDao historyDao = _factory.getHistoryDao(projectId, buildId);
        historyDao.insert(history);

    }

    /**
     *
     * @param projectId
     * @param issueId
     * @param from
     * @return
     */
    public List<HistoryDto> getHistoryByIssueId(int projectId, int buildId, int issueId, int from) {
        HistoryDao historyDao = _factory.getHistoryDao(projectId, buildId);
        return historyDao.findByIssueId(issueId, from);
    }

    public int getNumberOfHistory(int projectId, int buildId, int issueId) {
        HistoryDao historyDao = _factory.getHistoryDao(projectId, buildId);
        return historyDao.findSize(issueId);
    }

    public int delete(int projectId, int buildId, int historyId) {
        HistoryDao historyDao = _factory.getHistoryDao(projectId, buildId);
        HistoryDto history = historyDao.findIssueById(historyId);
        historyDao.delete(history);
        return 0;
    }


    public JSONObject getHistoryListJson(int projectId, int buildId, int issueId, int from) {
        HistoryDao historyDao = _factory.getHistoryDao(projectId, buildId);
        List<HistoryDto> historyList = historyDao.findByIssueId(issueId, from);
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        for (int i = 0; i < historyList.size(); i++){
            HistoryDto historyDto = historyList.get(i);
            JSONObject jo = new JSONObject();
            jo.put("id", historyDto.getId());
            jo.put("user", historyDto.getUser());
            jo.put("status", historyDto.getStatus());
            jo.put("comment", historyDto.getComment());
            jo.put("date", Const.DATE_FORMAT.format(historyDto.getDate()));
            array.add(jo);
        }
        result.put("history", array);
        return result;
    }
}

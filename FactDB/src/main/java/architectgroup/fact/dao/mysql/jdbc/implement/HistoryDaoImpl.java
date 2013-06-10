package architectgroup.fact.dao.mysql.jdbc.implement;

import architectgroup.fact.Const;
import architectgroup.fact.dao.HistoryDao;
import architectgroup.fact.dao.mysql.jdbc.BaseDao;
import architectgroup.fact.dto.HistoryDto;
import architectgroup.fact.util.Result;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/18/13
 * Time: 3:02 PM
 */
public class HistoryDaoImpl extends BaseDao implements HistoryDao {
    private static final Logger _logger = Logger.getLogger(TraceDaoImpl.class);
    private Connection _conn = null;
    private int _projectId;
    private String _projectName;
    private String _tableName;
    private int _buildId;

    public HistoryDaoImpl(int projectId, int buildId) {
        _buildId = buildId;
        _projectId = projectId;
        _projectName = getDatabaseName(projectId);
        _tableName = Const.HISTORY_PREFIX + buildId;
    }

    /**
     * @{inheritDoc}
     */
    public HistoryDto insert(HistoryDto history) {
        PreparedStatement insertHistories = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("INSERT `%s`.`%s` VALUES (NULL,?,?,?,?,?)", _projectName, _tableName);
                insertHistories = _conn.prepareStatement(sql);
                insertHistories.setString(1, Const.DATE_FORMAT.format(history.getDate()));
                insertHistories.setString(2, history.getUser());
                insertHistories.setString(3, history.getStatus());
                insertHistories.setString(4, history.getComment());
                insertHistories.setInt(5, history.getIssueId());
                insertHistories.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(insertHistories);
            DbUtils.closeQuietly(_conn);
        }
        return history;
    }

    /**
     * @{inheritDoc}
     */
    public Result insert(List<HistoryDto> histories) {
        PreparedStatement insertHistories = null;
        Result result = Result.FAILED;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                _conn.setAutoCommit(false);
                String sql = String.format("INSERT `%s`.`%s` VALUES (NULL,?,?,?,?,?)", _projectName, _tableName);
                insertHistories = _conn.prepareStatement(sql);
                for (HistoryDto history : histories) {
                    insertHistories.setString(1, Const.DATE_FORMAT.format(history.getDate()));
                    insertHistories.setString(2, history.getUser());
                    insertHistories.setString(3, history.getStatus());
                    insertHistories.setString(4, history.getComment());
                    insertHistories.setInt(5, history.getIssueId());
                    insertHistories.addBatch();
                }
                insertHistories.executeBatch();
                _conn.commit();
                result = Result.SUCCESS;
                _logger.debug("Successful executing insert issue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = Result.DB_INSERT_ERROR;
        }
        finally {
            DbUtils.closeQuietly(insertHistories);
            DbUtils.closeQuietly(_conn);
        }
        return result;
    }

    /**
     * @{inheritDoc}
     */
    public HistoryDto update(HistoryDto history) {
        return null;
    }

    /**
     * @{inheritDoc}
     */
    public Result delete(HistoryDto history) {
        PreparedStatement deleteIssues = null;
        Result result = Result.FAILED;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("DELETE FROM  `%s`.`%s` WHERE (id = ?)", _projectName, _tableName);
                deleteIssues = _conn.prepareStatement(sql);
                deleteIssues.setInt(1, history.getId());
                deleteIssues.execute();
                result = Result.SUCCESS;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = Result.DB_DELETE_ERROR;
        }
        finally {
            DbUtils.closeQuietly(deleteIssues);
            DbUtils.closeQuietly(_conn);
        }
        return result;
    }

    public HistoryDto findIssueById(int id) {
        List<HistoryDto> historyDtoList = new ArrayList<HistoryDto>();
        PreparedStatement selectHistoryIssues = null;
        ResultSet rs = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s` WHERE ( id = ?)", _projectName, _tableName);
                selectHistoryIssues = _conn.prepareStatement(sql);
                selectHistoryIssues.setInt(1, id);
                rs = selectHistoryIssues.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        HistoryDto historyDto = new HistoryDto();
                        historyDto.setId(rs.getInt(1));
                        historyDto.setDate(rs.getTimestamp(2));
                        historyDto.setUser(rs.getString(3));
                        historyDto.setStatus(rs.getString(4));
                        historyDto.setComment(rs.getString(5));
                        historyDto.setIssueId(rs.getInt(6));
                        historyDtoList.add(historyDto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, selectHistoryIssues, rs);
        }
        return historyDtoList.get(0);
    }

    /**
     * @{inheritDoc}
     */
    public List<HistoryDto> findByIssueId(int issueId, int from) {
        List<HistoryDto> historyDtoList = new ArrayList<HistoryDto>();
        PreparedStatement selectHistoryIssues = null;
        ResultSet rs = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s` WHERE ( issueid = ?) ORDER BY id DESC LIMIT ?, ?", _projectName, _tableName);
                selectHistoryIssues = _conn.prepareStatement(sql);
                selectHistoryIssues.setInt(1, issueId);
                selectHistoryIssues.setInt(2, from);
                selectHistoryIssues.setInt(3, 5);
                rs = selectHistoryIssues.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        HistoryDto historyDto = new HistoryDto();
                        historyDto.setId(rs.getInt(1));
                        historyDto.setDate(rs.getTimestamp(2));
                        historyDto.setUser(rs.getString(3));
                        historyDto.setStatus(rs.getString(4));
                        historyDto.setComment(rs.getString(5));
                        historyDto.setIssueId(rs.getInt(6));
                        historyDtoList.add(historyDto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(selectHistoryIssues);
            DbUtils.closeQuietly(_conn);
        }
        return historyDtoList;
    }

    /**
     * @{inheritDoc}
     */
    public int findSize(int issueId) {
        int result = 0;
        PreparedStatement selectHistoryIssues = null;
        ResultSet rs = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT COUNT(*) FROM  `%s`.`%s` WHERE ( issueid = ?)", _projectName, _tableName);
                selectHistoryIssues = _conn.prepareStatement(sql);
                selectHistoryIssues.setInt(1, issueId);
                rs = selectHistoryIssues.executeQuery();
                rs.next();
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, selectHistoryIssues, rs);
        }
        return result;
    }
}

package architectgroup.fact.dao.mysql.jdbc.implement;

import architectgroup.fact.Const;
import architectgroup.fact.dao.mysql.jdbc.BaseDao;
import architectgroup.fact.dao.TraceDao;
import architectgroup.fact.dto.IssueDto;
import architectgroup.fact.dto.TraceDto;
import architectgroup.fact.util.Result;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/11/12
 * Time: 11:12 PM
 */
public class TraceDaoImpl extends BaseDao implements TraceDao {
    private static final Logger _logger = Logger.getLogger(TraceDaoImpl.class);
    private Connection _conn = null;
    private int _buildId;
    private int _projectId;
    private String _projectName;
    private String _tableName;

    public TraceDaoImpl(int projectId, int buildId) {
        _buildId = buildId;
        _projectId = projectId;
        _projectName = getDatabaseName(projectId);
        _tableName = Const.TRACE_BUILD_PREFIX + buildId;
    }

    /**
     * Function to create the build Dto from the Result set
     * @param rs the result set
     * @return buildDto
     * @throws SQLException
     */
    public TraceDto mapRow(ResultSet rs) throws SQLException {
        TraceDto traceDto = new TraceDto();
        traceDto.setTraceid(rs.getInt(1));
        traceDto.setFile(rs.getString(2));
        traceDto.setMethod(rs.getString(3));
        traceDto.setLine(rs.getInt(4));
        traceDto.setText(rs.getString(5));
        traceDto.setType(rs.getString(6));
        traceDto.setRefid(rs.getInt(7));
        traceDto.setBlockid(rs.getInt(8));

        if (rs.wasNull()) {
            traceDto = null;
        }
        return traceDto;
    }

    /**
     * set parameter to a statement
     * @param insertTraceIssues the current statement
     * @param trace the trace include parameter
     * @throws SQLException
     */
    public void setInsertParams(PreparedStatement insertTraceIssues, TraceDto trace) throws SQLException {
        if (insertTraceIssues != null && trace != null) {
            insertTraceIssues.setInt(1, trace.getTraceid());
            insertTraceIssues.setString(2, trace.getFile());
            insertTraceIssues.setString(3, trace.getMethod());
            insertTraceIssues.setInt(4, trace.getLine());
            insertTraceIssues.setString(5, trace.getText());
            insertTraceIssues.setString(6, trace.getType());
            insertTraceIssues.setInt(7, trace.getRefid());
            insertTraceIssues.setInt(8, trace.getBlockid());
            insertTraceIssues.setInt(9, trace.getIssue().getId());
        } else {
            _logger.debug("insertTraceIssues or trace is null when insert params to statement");
        }
    }

    /**
     * set parameter to a statement
     * @param updateTraceIssues the current statement
     * @param trace the trace include parameter
     * @throws SQLException
     */
    public void setUpdateParams(PreparedStatement updateTraceIssues, TraceDto trace) throws SQLException {
        if (updateTraceIssues != null && trace != null) {
            updateTraceIssues.setInt(1, trace.getTraceid());
            updateTraceIssues.setString(2, trace.getFile());
            updateTraceIssues.setString(3, trace.getMethod());
            updateTraceIssues.setInt(4, trace.getLine());
            updateTraceIssues.setString(5, trace.getText());
            updateTraceIssues.setString(6, trace.getType());
            updateTraceIssues.setInt(7, trace.getRefid());
            updateTraceIssues.setInt(8, trace.getBlockid());
            updateTraceIssues.setInt(9, trace.getIssue().getId());
            updateTraceIssues.setInt(10, trace.getTraceid());
        } else {
            _logger.debug("updateTraceIssues or trace is null when insert params to statement");
        }
    }

    /**
     * @{inheritDoc}
     */
    public TraceDto insert(TraceDto trace) {
        PreparedStatement insertTraceIssues = null;
        try {
            _logger.debug("Starting insert trace issue");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("INSERT `%s`.`%s` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", _projectName, _tableName);
                insertTraceIssues = _conn.prepareStatement(sql);
                setInsertParams(insertTraceIssues, trace);
                insertTraceIssues.execute();
                _logger.debug("Successful executing insert trace issue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(insertTraceIssues);
            DbUtils.closeQuietly(_conn);
        }
        return trace;
    }

    /**
     * @{inheritDoc}
     */
    public Result insert(List<TraceDto> traces){
        PreparedStatement insertTraceIssues = null;
        Result result = Result.FAILED;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                _conn.setAutoCommit(false);
                String sql = String.format("INSERT `%s`.`%s` VALUES (?,?,?,?,?,?,?,?,?)", _projectName, _tableName);
                insertTraceIssues = _conn.prepareStatement(sql);
                for (TraceDto trace : traces){
                    setInsertParams(insertTraceIssues, trace);
                    insertTraceIssues.addBatch();
                }
                insertTraceIssues.executeBatch();
                _conn.commit();
                result = Result.SUCCESS;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            result = Result.FAILED;
        } finally {
            DbUtils.closeQuietly(insertTraceIssues);
            DbUtils.closeQuietly(_conn);
        }
        return result;
    }

    /**
     * @{inheritDoc}
     */
    public TraceDto update(TraceDto trace) {
        PreparedStatement updateTraceIssues = null;
        try {
            _logger.debug("Starting update trace issue");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("UPDATE `%s`.`%s` SET traceid = ?,file=?,method=?,line=?,text=?,type=?,refid=?,blockid=?,issueid = ? WHERE traceid = ?", _projectName, _tableName);
                updateTraceIssues = _conn.prepareStatement(sql);
                setUpdateParams(updateTraceIssues, trace);
                updateTraceIssues.execute();
                _logger.debug("Successful executing update trace issue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(updateTraceIssues);
            DbUtils.closeQuietly(_conn);
        }

        return trace;
    }

    /**
     * {@inheritDoc}
     */
    public Result delete(TraceDto project) {
        PreparedStatement deleteTraceIssues = null;
        Result result = Result.FAILED;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("DELETE FROM `%s`.`%s` WHERE (traceid=?)", _projectName, _tableName);
                deleteTraceIssues = _conn.prepareStatement(sql);
                deleteTraceIssues.setInt(1, project.getTraceid());
                deleteTraceIssues.execute();
                result = Result.SUCCESS;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = Result.DB_DELETE_ERROR;
        }
        finally {
            DbUtils.closeQuietly(deleteTraceIssues);
            DbUtils.closeQuietly(_conn);
        }
        return result;
    }

    /**
     * @{inheritDoc}
     */
    public List<TraceDto> findTraceByIssue(IssueDto issue) {
        return findTraceByIssueId(issue.getId());
    }

    /**
     * @{inheritDoc}
     */
    public List<TraceDto> findTraceByIssueId(int issueId) {
        List<TraceDto> traceDtoList = new ArrayList<TraceDto>();
        PreparedStatement selectTraceIssues = null;
        ResultSet rs = null;
        try {
            _logger.debug("Starting select trace issue");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = "SELECT * FROM `%s`.`%s` WHERE ( issueid = ?) ORDER BY line";
                selectTraceIssues = _conn.prepareStatement(sql);
                selectTraceIssues.setInt(1, issueId);
                rs = selectTraceIssues.executeQuery();
                if (rs!=null){
                    while(rs.next()){
                        TraceDto traceDto = mapRow(rs);
                        IssueDto issueDto = new IssueDto();
                        issueDto.setId(rs.getInt(9));
                        traceDto.setIssue(issueDto);
                        traceDtoList.add(traceDto);
                    }
                }
                _logger.debug("Successful executing select trace issue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(selectTraceIssues);
            DbUtils.closeQuietly(_conn);
        }

        return traceDtoList;    
    }

    /**
     * @{inheritDoc}
     */
    public List<TraceDto> findAllTraces(){
        List<TraceDto> traceDtoList = new ArrayList<TraceDto>();
        PreparedStatement selectTraceIssues = null;
        ResultSet rs = null;
        try {
            _logger.debug("Starting select trace issue");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s`", _projectName, _tableName);
                selectTraceIssues = _conn.prepareStatement(sql);
                rs = selectTraceIssues.executeQuery();
                if (rs!=null){
                    while(rs.next()){
                        TraceDto traceDto = mapRow(rs);
                        IssueDto issueDto = new IssueDto();
                        issueDto.setId(rs.getInt(9));
                        traceDto.setIssue(issueDto);
                        traceDtoList.add(traceDto);
                    }
                }
                _logger.debug("Successful executing select trace issue");
            }
        } catch (SQLException e) {
            e.printStackTrace();   
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(selectTraceIssues);
            DbUtils.closeQuietly(_conn);
        }

        return traceDtoList;    
    }

    /**
     * @{inheritDoc}
     */
    public List<TraceDto> findTraceInIssueIds(int buildNo, List<Integer> ids){
        List<TraceDto> traceDtoList = new ArrayList<TraceDto>();
        PreparedStatement selectTraceIssues = null;
        ResultSet rs = null;
        try {
            _logger.debug("Starting select trace issue from specific build");
            _conn = this.getConnection();
            if (_conn != null) {
                String issueIds = "";
                for (int id: ids) {
                    issueIds += id + ",";
                }

                if (issueIds.lastIndexOf(",") > 0) {
                    issueIds = issueIds.substring(0, issueIds.lastIndexOf(","));
                }

                String sql = String.format("SELECT *  FROM  `%s`.`%s` WHERE issueid IN (" + issueIds + ")", _projectName, _tableName);
                selectTraceIssues = _conn.prepareStatement(sql);
                rs = selectTraceIssues.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        TraceDto traceDto = mapRow(rs);
                        IssueDto issueDto = new IssueDto();
                        issueDto.setId(rs.getInt(9));
                        traceDto.setIssue(issueDto);
                        traceDtoList.add(traceDto);
                    }
                }
                _logger.debug("Successful executing select trace issue from specific build");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(selectTraceIssues);
            DbUtils.closeQuietly(_conn);
        }

        return traceDtoList;
    }
}
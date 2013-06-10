package architectgroup.fact.dao.mysql.jdbc.implement;

import architectgroup.fact.Const;
import architectgroup.fact.util.Result;
import architectgroup.fact.util.State;
import architectgroup.fact.dao.mysql.jdbc.BaseDao;
import architectgroup.fact.dao.IssueDao;
import architectgroup.fact.dto.IssueDto;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/11/12
 * Time: 10:59 PM
 */
public class IssueDaoImpl extends BaseDao implements IssueDao {
    private int _buildId;
    private int _buildOrder;
    private int _projectId;
    private String _projectName;
    private String _tableName;

    private static final Logger _logger = Logger.getLogger(IssueDaoImpl.class);
    Connection _conn = null;

    public IssueDaoImpl(int projectId, int buildId) {
        super();
        _projectId = projectId;
        _projectName = getDatabaseName(projectId);
        _tableName = Const.ISSUE_BUILD_PREFIX + buildId;
        _buildId = buildId;
        _buildOrder = getBuildOrder(_buildId);
    }

    /**
     * @{inheritDoc}
     */
    public IssueDto mapRow(ResultSet rs) throws SQLException {
        IssueDto issueDto = new IssueDto();
        issueDto.setId(rs.getInt(1));
        issueDto.setFile(rs.getString(2));
        issueDto.setMethod(rs.getString(3));
        issueDto.setLine(rs.getInt(4));
        issueDto.setColumn(rs.getInt(5));
        issueDto.setMessage(rs.getString(6));
        issueDto.setPrefix(rs.getString(7));
        issueDto.setPostfix(rs.getString(8));
        issueDto.setCode(rs.getString(9));
        issueDto.setSeveritylevel(rs.getShort(10));
        issueDto.setCitingstatus(rs.getString(11));
        issueDto.setDisplay(rs.getString(12));
        issueDto.setState(rs.getString(13));
        if (rs.wasNull()) {
            issueDto = null;
        }
        return issueDto;
    }

    /**
     * set parameter to a statement
     * @param insertIssues the current statement
     * @param issue the issue include parameter
     * @throws SQLException
     */
    public void setInsertParams(PreparedStatement insertIssues, IssueDto issue) throws SQLException {
        if (insertIssues != null && issue != null) {
            insertIssues.setInt(1, issue.getId());
            String replacePathBySlash = "";
            if (issue.getFile() != null) {
                replacePathBySlash = issue.getFile().replaceAll("\\\\", "/");
            }
            insertIssues.setString(2, replacePathBySlash);
            insertIssues.setString(3, issue.getMethod());
            insertIssues.setInt(4, issue.getLine());
            insertIssues.setInt(5, issue.getColumn());
            insertIssues.setString(6, issue.getMessage());
            insertIssues.setString(7, issue.getPrefix());
            insertIssues.setString(8, issue.getPostfix());
            insertIssues.setString(9, issue.getCode());
            insertIssues.setInt(10, issue.getSeveritylevel());
            insertIssues.setString(11, issue.getCitingstatus());
            insertIssues.setString(12, issue.getDisplay());
            insertIssues.setString(13, issue.getState());
        } else {
            _logger.debug("insertIssues or issue is null when insert params to statement");
        }
    }

    /**
     * set parameter to a statement
     * @param updateIssues the current statement
     * @param issue the issue include parameter
     * @throws SQLException
     */
    public void setUpdateParams(PreparedStatement updateIssues, IssueDto issue) throws SQLException {
        if (updateIssues != null && issue != null) {
            updateIssues.setString(1, issue.getFile());
            updateIssues.setString(2, issue.getMethod());
            updateIssues.setInt(3, issue.getLine());
            updateIssues.setInt(4, issue.getColumn());
            updateIssues.setString(5, issue.getMessage());
            updateIssues.setString(6, issue.getPrefix());
            updateIssues.setString(7, issue.getPostfix());
            updateIssues.setString(8, issue.getCode());
            updateIssues.setInt(9, issue.getSeveritylevel());
            updateIssues.setString(10, issue.getCitingstatus());
            updateIssues.setString(11, issue.getDisplay());
            updateIssues.setString(12, issue.getState());
            updateIssues.setInt(13, issue.getId());
        } else {
            _logger.debug("updateStatement or issue is null when insert params to statement");
        }
    }

    /**
     * @{inheritDoc}
     */
    public IssueDto insert(IssueDto issue) {
        PreparedStatement insertIssues = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("INSERT `%s`.`%s` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", _projectName, _tableName);
                insertIssues = _conn.prepareStatement(sql);
                setInsertParams(insertIssues, issue);
                insertIssues.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(insertIssues);
            DbUtils.closeQuietly(_conn);
        }
        return issue;
    }

    /**
     * @{inheritDoc}
     */
    public int insert(List<IssueDto> issues) {
        PreparedStatement insertIssues = null;
        int totalRowUpdate = 0;
        boolean updateAll = false;
        boolean updateEmpty = false;
        boolean updateFail = false;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                _conn.setAutoCommit(false);
                String sql = String.format("INSERT `%s`.`%s` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", _projectName, _tableName);
                insertIssues = _conn.prepareStatement(sql);
                for (IssueDto issue : issues) {
                    setInsertParams(insertIssues, issue);
                    insertIssues.addBatch();
                }
                insertIssues.executeBatch();
                _conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            totalRowUpdate = 0;
        }
        finally {
            DbUtils.closeQuietly(insertIssues);
            DbUtils.closeQuietly(_conn);
        }
        return totalRowUpdate;
    }

    /**
     * @{inheritDoc}
     */
    public IssueDto update(IssueDto issue) {
        PreparedStatement updateIssues = null;
        try {
            _logger.debug("Starting update issue");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("UPDATE `%s`.`%s` SET id = ?, file=?, method=?, line=?, `column`=?, message=?, prefix=?, postfix=?, `code`=?, severitylevel=?, citingstatus=?, display=?, state=? WHERE id=?", _projectName, _tableName);
                updateIssues = _conn.prepareStatement(sql);
                setUpdateParams(updateIssues, issue);
                updateIssues.execute();
                _logger.debug("Successful executing update issue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(updateIssues);
            DbUtils.closeQuietly(_conn);
        }
        return issue;
    }

    /**
     * @{inheritDoc}
     */
    public Result update(List<IssueDto> issues) {
        PreparedStatement updateIssues = null;
        Result result = Result.FAILED;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                _conn.setAutoCommit(false);
                String sql = String.format("UPDATE `%s`.`%s` SET file=?, method=?, line=?, `column`=?, message=?, prefix=?, postfix=?, `code`=?, severitylevel=?, citingstatus=?, display=?, state=? WHERE id = ?", _projectName, _tableName);
                updateIssues = _conn.prepareStatement(sql);
                for (IssueDto issue : issues) {
                    setUpdateParams(updateIssues, issue);
                    updateIssues.addBatch();
                }
                updateIssues.executeBatch();
                _conn.commit();
                result = Result.SUCCESS;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = Result.DB_UPDATE_ERROR;
        }
        finally {
            DbUtils.closeQuietly(updateIssues);
            DbUtils.closeQuietly(_conn);
        }
        return result;
    }

    /**
     * @{inheritDoc}
     */
    public Result delete(IssueDto project) {
        PreparedStatement deleteIssues = null;
        Result result = Result.FAILED;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("DELETE FROM  `%s`.`%s` WHERE (id = ?)", _projectName, _tableName);
                deleteIssues = _conn.prepareStatement(sql);
                deleteIssues.setInt(1, project.getId());
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

    /**
     * @{inheritDoc}
     */
    public IssueDto findIssueById(int id) {
        IssueDto issueDto = null;
        PreparedStatement selectIssues = null;
        ResultSet rs = null;
        boolean selectedSuccessfully = false;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT issb.* FROM  `%s`.`%s` issb WHERE ( id = ? )", _projectName, _tableName);
                selectIssues = _conn.prepareStatement(sql);
                selectIssues.setInt(1,id);
                rs = selectIssues.executeQuery();
                if (rs.next()){
                    issueDto = mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, selectIssues, rs);
        }
        return issueDto;
    }

    /**
     * @{inheritDoc}
     */
    public List<IssueDto> findAllIssues() {
        List<IssueDto> issueDtoList = new ArrayList<IssueDto>();
        PreparedStatement selectIssues = null;
        ResultSet rs = null;

        try {
            _logger.debug("Starting select issue");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT issb.* FROM  `%s`.`%s` issb ", _projectName, _tableName);
                selectIssues = _conn.prepareStatement(sql);

                rs = selectIssues.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        IssueDto issueDto = mapRow(rs);
                        issueDtoList.add(issueDto);
                    }
                }
                _logger.debug("Successful executing select issue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(selectIssues);
            DbUtils.closeQuietly(_conn);
        }

        return issueDtoList;
    }

    /**
     * @{inheritDoc}
     */
    public List<IssueDto> findAllIssues(String searchCondition) {
        List<IssueDto> issueDtoList = new ArrayList<IssueDto>();
        PreparedStatement selectIssues = null;
        ResultSet rs = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT issb.* FROM  `%s`.`%s` issb %s",_projectName, _tableName, searchCondition);
                selectIssues = _conn.prepareStatement(sql);

                rs = selectIssues.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        IssueDto issueDto = mapRow(rs);
                        issueDtoList.add(issueDto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, selectIssues, rs);
        }
        return issueDtoList;
    }

    /**
     * @{inheritDoc}
     */
    public int findNumberOfIssues(String searchCondition) {
        int result = 0;
        PreparedStatement selectIssues = null;
        ResultSet rs = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT COUNT(*) FROM  `%s`.`%s` issb %s",_projectName, _tableName, searchCondition);
                selectIssues = _conn.prepareStatement(sql);
                rs = selectIssues.executeQuery();
                if (rs != null){
                    rs.next();
                    result = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, selectIssues, rs);
        }
        return result;
    }

    /**
     * Find all issue by their state (FIXED, OPEN, RE-OCCURED)
     *
     * @param  state  the state in case of FIXED, OPEN, RE-OCCURED.
     * @return the list of issues by their state
     */
    public List<IssueDto> findIssueByState(State state){
        List<IssueDto> issueDtoList = new ArrayList<IssueDto>();
        PreparedStatement issueSelectStatement = null;
        ResultSet rs = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT * FROM `%s`.`%s` WHERE state = ?", _projectName, _tableName);
                issueSelectStatement = _conn.prepareStatement(sql);
                issueSelectStatement.setString(1, state.toString());

                rs = issueSelectStatement.executeQuery();
                if (rs != null){
                    while(rs.next()) {
                        IssueDto issueDto = mapRow(rs);
                        issueDtoList.add(issueDto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, issueSelectStatement, rs);
        }

        return issueDtoList;
    }

    /**
     *
     * @param buildId test
     * @return test
     */
    private int getBuildOrder(int buildId) {
        List<Integer> buildList = new ArrayList<Integer>();
        PreparedStatement buildSelectStatement = null;
        ResultSet rs = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM `%s`.`%s`", _projectName, Const.TABLE_BUILD_NAME);
                buildSelectStatement = _conn.prepareStatement(sql);
                rs = buildSelectStatement.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        buildList.add(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, buildSelectStatement, rs);
        }

        return buildList.indexOf(buildId) + 1;
    }

    /**
     *
     * @param buildOrder  test
     * @return test
     */
    private int getBuildIdFromOrder(int buildOrder) {
        List<Integer> buildList = new ArrayList<Integer>();
        PreparedStatement buildSelectStatement = null;
        ResultSet rs = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM `%s`.`%s`", _projectName, Const.TABLE_BUILD_NAME);
                buildSelectStatement = _conn.prepareStatement(sql);
                rs = buildSelectStatement.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        buildList.add(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, buildSelectStatement, rs);
        }

        return buildList.get(buildOrder-1);
    }

    /**
     * @{inheritDoc}
     */
    public List<IssueDto> findIssueByState(State state, String filter) {
        List<IssueDto> issueDtoList = new ArrayList<IssueDto>();
        PreparedStatement issueSelectStatement = null;
        ResultSet rs = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT issb.*  FROM `%s`.`%s` AS issb WHERE state = ? AND %s", _projectName, _tableName, filter);
                issueSelectStatement = _conn.prepareStatement(sql);
                issueSelectStatement.setString(1, state.toString());
                rs = issueSelectStatement.executeQuery();
                if (rs != null){
                    while(rs.next()) {
                        IssueDto issueDto = mapRow(rs);
                        issueDtoList.add(issueDto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(issueSelectStatement);
            DbUtils.closeQuietly(_conn);
        }

        return issueDtoList;
    }

    /**
     * @{inheritDoc}
     */
    public Map<String, Integer> findGroupByErrorCode(){
        Map<String, Integer> errorCodes = new HashMap<String, Integer>();
        PreparedStatement selectErrorCode = null;
        ResultSet rs = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT code, count(*) FROM `%s`.`%s` group by code", _projectName, _tableName);
                selectErrorCode = _conn.prepareStatement(sql);
                rs = selectErrorCode.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        errorCodes.put(rs.getString(1), rs.getInt(2));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(_conn, selectErrorCode, rs);
        }
        return errorCodes;
    }

    /**
     * @{inheritDoc}
     */
    public Map<String, Integer> findGroupByErrorCode(String filter) {
        Map<String, Integer> errorCodes = new HashMap<String, Integer>();
        PreparedStatement selectErrorCode = null;
        ResultSet rs = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT code, count(*) FROM `%s`.`%s` WHERE %s group by code", _projectName, _tableName, filter);
                selectErrorCode = _conn.prepareStatement(sql);
                rs = selectErrorCode.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        errorCodes.put(rs.getString(1), rs.getInt(2));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(_conn, selectErrorCode, rs);
        }
        return errorCodes;
    }

    /**
     * @{inheritDoc}
     */
    public int findNextIssue(int issueId) {
        IssueDto issueDto = new IssueDto();
        PreparedStatement selectIssues = null;
        ResultSet rs = null;
        boolean selectedSuccessfully = false;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT MIN(id) FROM (SELECT id FROM `%s`.`%s`  WHERE id > %s) test", _projectName, _tableName, issueId);
                selectIssues = _conn.prepareStatement(sql);
                rs = selectIssues.executeQuery();
                if (rs.next()){
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, selectIssues, rs);
        }
        return -1;
    }

    /**
     * @{inheritDoc}
     */
    public int findPreviousIssue(int issueId) {
        IssueDto issueDto = new IssueDto();
        PreparedStatement selectIssues = null;
        ResultSet rs = null;
        boolean selectedSuccessfully = false;
        int prevId = 0;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT MAX(id) FROM (SELECT id FROM `%s`.`%s` WHERE id < %s) test", _projectName, _tableName, issueId );
                selectIssues = _conn.prepareStatement(sql);
                rs = selectIssues.executeQuery();
                if (rs.next()){
                    prevId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, selectIssues, rs);
        }
        return prevId;
    }

    /**
     * @{inheritDoc}
     */
    public Map<String, Integer> findGroupBySeverity(){
        Map<String, Integer> severityMap = new HashMap<String, Integer>();
        PreparedStatement selectSeverity = null;
        ResultSet rs = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT severitylevel, count(severitylevel) FROM `%s`.`%s` group by severitylevel  order by severitylevel asc", _projectName, _tableName);
                selectSeverity = _conn.prepareStatement(sql);
                rs = selectSeverity.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        severityMap.put(String.valueOf(rs.getInt(1)), rs.getInt(2));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, selectSeverity, rs);
        }

        return severityMap;
    }

    /**
     * @{inheritDoc}
     */
    public Map<String, Integer> findGroupBySeverity(String filter) {
        Map<String, Integer> severityMap = new HashMap<String, Integer>();
        PreparedStatement selectSeverity = null;
        ResultSet rs = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT severitylevel, count(severitylevel) FROM `%s`.`%s` WHERE %s group by severitylevel  order by severitylevel asc", _projectName, _tableName, filter);
                selectSeverity = _conn.prepareStatement(sql);
                rs = selectSeverity.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        severityMap.put(String.valueOf(rs.getInt(1)), rs.getInt(2));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, selectSeverity, rs);
        }
        return severityMap;
    }

    /**
     * @{inheritDoc}
     */
    public Map<String, Integer> findState(){
        Map<String, Integer> stateMap = new HashMap<String, Integer>();
        PreparedStatement selectState = null;
        ResultSet rs = null;

        try {
            _logger.debug("Starting select State");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT state, count(*) FROM `%s`.`%s` group by state", _projectName, _tableName);
                selectState = _conn.prepareStatement(sql);
                rs = selectState.executeQuery();
                if (rs!=null){
                    while(rs.next()){
                        stateMap.put(rs.getString(1), rs.getInt(2));
                    }
                }
                _logger.debug("Successful executing select State");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, selectState, rs);
        }
        return stateMap;
    }

    /**
     * Insert the fixed issue to the current build, just for reference.
     * @param buildOrder    test
     */
    public void updateFixedIssue(int buildOrder) {
        if (buildOrder > 1) {
            PreparedStatement deleteSelect = null;
            PreparedStatement insertSelect = null;

            int buildId = getBuildIdFromOrder(buildOrder);
            int prevId = getBuildIdFromOrder(buildOrder - 1);
            try {
                _conn = this.getConnection();
                if (_conn != null) {
                    String sql = String.format("DELETE FROM `%s`.`%s` WHERE state = 'FIXED'", _projectName, _tableName);
                    deleteSelect = _conn.prepareStatement(sql);
                    deleteSelect.executeUpdate();

                    sql = String.format("INSERT INTO `%s`.`%s` \n" +
                            "  SELECT issb.* FROM `%s`.`%s` issb JOIN `%s`.`%s` isss ON isss.id = issb.id WHERE `$$`.`GET_STATE`(trend, ?) = 'FIXED' AND `$$`.`GET_STATE`(trend, ?) != 'FIXED'",
                            _projectName, _tableName, _projectName, _tableName, _projectName, Const.TABLE_ISSUE_SIGNATURE_NAME);
                    insertSelect = _conn.prepareStatement(sql);
                    insertSelect.setInt(1, buildOrder);
                    insertSelect.setInt(2, buildOrder - 1);
                    insertSelect.executeUpdate();

                    // Insert the trace also
                    sql = String.format("INSERT INTO `%s`.`%s` (file, method, line, text, type, refid, blockid, issueid) \n" +
                            "  SELECT issb.file, issb.method, issb.line, issb.text, issb.type, issb.refid, issb.blockid, issb.issueid FROM `%s`.`%s` issb JOIN `%s`.`%s` isss ON isss.id = issb.issueid WHERE `$$`.`GET_STATE`(trend, ?) = 'FIXED' AND `$$`.`GET_STATE`(trend, ?) != 'FIXED'",
                            _projectName, Const.TRACE_BUILD_PREFIX + buildId, _projectName, Const.TRACE_BUILD_PREFIX + prevId, _projectName, Const.TABLE_ISSUE_SIGNATURE_NAME);
                    insertSelect = _conn.prepareStatement(sql);
                    insertSelect.setInt(1, buildOrder);
                    insertSelect.setInt(2, buildOrder - 1);
                    insertSelect.executeUpdate();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                DbUtils.closeQuietly(deleteSelect);
                DbUtils.closeQuietly(insertSelect);
                DbUtils.closeQuietly(_conn);
            }
        }
    }

    /**
     *
     * @param buildOrder test
     */
    public void updateState(int buildOrder) {
        int buildId = getBuildIdFromOrder(buildOrder);
        if (buildOrder > 0) {
            PreparedStatement updateStatement = null;
            try {
                _conn = this.getConnection();
                if (_conn != null) {
                    String sql = String.format("UPDATE `%s`.`%s` issb, `%s`.`%s` isss SET issb.state = `%s`.GET_STATE(isss.trend, ?) WHERE issb.id = isss.id", _projectName, _tableName, _projectName, Const.TABLE_ISSUE_SIGNATURE_NAME, _projectName);
                    updateStatement = _conn.prepareStatement(sql);
                    updateStatement.setInt(1, buildOrder);
                    updateStatement.executeUpdate();

                /* Remove state that is UNKNOWN */
                    sql = String.format("DELETE FROM `%s`.`%s` WHERE state = ?", _projectName, _tableName);
                    updateStatement = _conn.prepareStatement(sql);
                    updateStatement.setString(1, State.UNKNOWN.toString());
                    updateStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                DbUtils.closeQuietly(updateStatement);
                DbUtils.closeQuietly(_conn);
            }
        }
    }
}
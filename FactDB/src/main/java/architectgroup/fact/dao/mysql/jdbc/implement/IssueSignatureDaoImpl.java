package architectgroup.fact.dao.mysql.jdbc.implement;

import architectgroup.fact.Const;
import architectgroup.fact.dao.IssueSignatureDao;
import architectgroup.fact.dao.mysql.jdbc.BaseDao;
import architectgroup.fact.dto.IssueSignatureDto;
import architectgroup.fact.util.Result;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 5/27/12
 * Time: 6:25 PM
 */
public class IssueSignatureDaoImpl extends BaseDao implements IssueSignatureDao {
    private static final Logger _logger = Logger.getLogger(IssueSignatureDaoImpl.class);
    Connection _conn = null;
    private int _projectId;
    private String _projectName;
    private String _tableName;

    public IssueSignatureDaoImpl(int projectId) {
        super();
        _projectId = projectId;
        _projectName = getDatabaseName(projectId);
        _tableName = Const.TABLE_ISSUE_SIGNATURE_NAME;
    }

    /**
     * Function to create the build Dto from the Result set
     * @param rs the result set
     * @return buildDto
     * @throws SQLException
     */
    public IssueSignatureDto mapRow(ResultSet rs) throws SQLException {
        IssueSignatureDto sigDto = new IssueSignatureDto();
        sigDto.setId(rs.getInt(1));
        sigDto.setMethod(rs.getString(2));
        sigDto.setSeverity(rs.getInt(3));
        sigDto.setCode(rs.getString(4));
        sigDto.setTraceSignature(rs.getString(5));
        sigDto.setFirstOccur(rs.getInt(6));
        sigDto.setTrend(rs.getString(7));
        sigDto.setFlag(rs.getInt(8));

        if (rs.wasNull()) {
            sigDto = null;
        }
        return sigDto;
    }

    /**
     * @{inheritDoc}
     */
    public IssueSignatureDto insert(IssueSignatureDto signature) {
        PreparedStatement insertIssues = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("INSERT `%s`.`%s` VALUES (?, ?, ?)", _projectName, _tableName);
                insertIssues = _conn.prepareStatement(sql);
                insertIssues.setInt(1, signature.getId());
                insertIssues.setString(2, signature.getTraceSignature());
                insertIssues.setString(3, signature.getTrend());
                insertIssues.execute();
                _logger.debug("Starting insert issue signature");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(insertIssues);
            DbUtils.closeQuietly(_conn);
        }

        return signature;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @{inheritDoc}
     */
    public List<Integer> insert(List<IssueSignatureDto> signatures) {
        PreparedStatement insertIssues = null;
        List<Integer> ids = new ArrayList<Integer>();
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                _conn.setAutoCommit(false);
                String sql = String.format("INSERT `%s`.`%s` VALUES (?,?,?)", _projectName,  _tableName);
                insertIssues = _conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
                for (IssueSignatureDto signature : signatures) {
                    insertIssues.setInt(1, signature.getId());
                    insertIssues.setString(2, signature.getTraceSignature());
                    insertIssues.setString(3, signature.getTrend());
                    insertIssues.addBatch();
                }

                insertIssues.executeBatch();
                _conn.commit();

                // Set Key
                ResultSet rs = insertIssues.getGeneratedKeys();
                int key;
                while (rs.next()) {
                    key = rs.getInt(1);
                    ids.add(key);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(insertIssues);
            DbUtils.closeQuietly(_conn);
        }

        return ids;
    }

    /**
     * @{inheritDoc}
     */
    public Result update(List<IssueSignatureDto> signatures) {
        PreparedStatement updateIssues = null;
        Result result = Result.FAILED;
        try {
            _logger.debug("Starting update issue signature");
            _conn = this.getConnection();
            if (_conn != null) {
                _conn.setAutoCommit(false);
                String sql = String.format("UPDATE `%s`.`%s` SET tracesignature = ?, trend=? WHERE id = ?", _projectName,  _tableName);
                updateIssues = _conn.prepareStatement(sql);
                for (IssueSignatureDto signature : signatures) {
                    updateIssues.setString(1, signature.getTraceSignature());
                    updateIssues.setString(2, signature.getTrend());
                    updateIssues.setInt(3, signature.getId());
                    updateIssues.addBatch();
                }
                updateIssues.executeBatch();
                _conn.commit();
                result = Result.SUCCESS;
                _logger.debug("Successful executing update issue signature");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = Result.FAILED;
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
    public IssueSignatureDto update(IssueSignatureDto signature) {
        PreparedStatement updateIssues = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("UPDATE `%s`.`%s` SET tracesignature = ?, trend = ? WHERE id = ?", _projectName, _tableName);
                updateIssues = _conn.prepareStatement(sql);
                updateIssues.setString(1, signature.getTraceSignature());
                updateIssues.setString(2, signature.getTrend());
                updateIssues.setInt(3, signature.getId());
                updateIssues.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(updateIssues);
            DbUtils.closeQuietly(_conn);
        }

        return signature;
    }

    /**
     *
     * @param trend test
     * @return test
     */
    public Result addToTrend(String trend) {
        PreparedStatement updateIssues = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("UPDATE `%s`.`%s` SET trend = CONCAT(trend, ?)", _projectName, _tableName);
                updateIssues = _conn.prepareStatement(sql);
                updateIssues.setString(1, trend);
                updateIssues.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(updateIssues);
            DbUtils.closeQuietly(_conn);
        }
        return Result.SUCCESS;
    }

    /**
     * @{inheritDoc}
     */
    public Result delete(IssueSignatureDto signature) {
        PreparedStatement deleteIssues = null;
        Result result = Result.FAILED;
        try {
            _logger.debug("Starting delete issue signature");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("DELETE FROM `%s`.`%s` WHERE ( id = ? )", _projectName, _tableName);
                deleteIssues = _conn.prepareStatement(sql);
                deleteIssues.setInt(1,signature.getId());
                deleteIssues.execute();
                result = Result.SUCCESS;
                _logger.debug("Successful delete insert issue signature");
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
     * Update The Bug Signature After Set the Trend.
     * After update the trends of the issue, the rest issue will be filled in the trends.
     * If the flag = 0 (means not update yet) filled the trends
     * Then reset all the flag to 1.
     */
    /*
    public Result resetIssueSignature() {
        PreparedStatement updateIssueSignature = null;
        Result result = Result.FAILED;
        try {
            _logger.debug("Starting reset the Bug Signature");
            _conn = this.getConnection();

            // First Update the Bug Signature that have flag == 0
            String sql = String.format("UPDATE `%s`.`%s` SET trend = IF(INSTR(trend,'#') > 0, CONCAT(trend, SUBSTR(trend,LENGTH(trend) - INSTR(REVERSE(trend),'#') + 1)), CONCAT(trend,CONCAT('#',trend))), flag = 1 WHERE flag = 0", _projectName, _tableName);
            updateIssueSignature = _conn.prepareStatement(sql);
            updateIssueSignature.execute();

            // Reset all the flag to 0
            sql = String.format("UPDATE `%s`.`%s` SET flag = 0 WHERE flag = 1", _projectName, _tableName);
            updateIssueSignature = _conn.prepareStatement(sql);
            updateIssueSignature.execute();
            result = Result.SUCCESS;
            _logger.debug("Successful reset the Bug Signature");
        } catch (SQLException e) {
            e.printStackTrace();
            result = Result.FAILED;
        }
        finally {
            DbUtils.closeQuietly(updateIssueSignature);
            DbUtils.closeQuietly(_conn);
        }
        return result;
    } */

    /**
     * @{inheritDoc}
     */
    public int getMaxId() {
        PreparedStatement getIdStatement = null;
        ResultSet rs = null;
        int maxId = -1;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT MAX(id) FROM  `%s`.`%s`", _projectName, _tableName);
                getIdStatement = _conn.prepareStatement(sql);
                rs = getIdStatement.executeQuery();
                rs.next();
                maxId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(getIdStatement);
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(_conn);
        }
        return maxId;
    }

    /**
     * @{inheritDoc}
     */
    public IssueSignatureDto findIssueSignatureById(int id) {
        IssueSignatureDto issueSignatureDto = null;
        PreparedStatement selectIssues = null;
        ResultSet rs = null;
        boolean selectedSuccessfully = false;
        try {
            _logger.debug("Starting find Bug Signature By Id");
            _conn = this.getConnection();
            String sql = String.format("SELECT *  FROM  `%s`.`%s` WHERE ( id = ?)", _projectName, _tableName);
            selectIssues = _conn.prepareStatement(sql);
            selectIssues.setInt(1,id);
            rs = selectIssues.executeQuery();
            if (rs.next()){
                issueSignatureDto = mapRow(rs);
                _logger.debug("Successful find Bug Signature By Id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(selectIssues);
            DbUtils.closeQuietly(_conn);
        }
        return issueSignatureDto;
    }

    /**
     * @{inheritDoc}
     */
    /*
    public List<IssueSignatureDto> findIssueSignatureWhereFirstOccur(int firstOccur) {
        List<IssueSignatureDto> issueSignatureDtoList = new ArrayList<IssueSignatureDto>();
        PreparedStatement selectIssues = null;
        ResultSet rs = null;
        boolean selectedSuccessfully = false;
        try {
            _logger.debug("Starting find Bug Signature By firstOccur");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s` WHERE (firstoccur = ?)", _projectName, _tableName);
                selectIssues = _conn.prepareStatement(sql);
                selectIssues.setInt(1,firstOccur);
                rs = selectIssues.executeQuery();
                while (rs.next()){
                    IssueSignatureDto issueSignatureDto = mapRow(rs);
                    issueSignatureDtoList.add(issueSignatureDto);
                }
                _logger.debug("Successful find Bug Signature By firstOccur");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(selectIssues);
            DbUtils.closeQuietly(_conn);
        }

        return issueSignatureDtoList;
    } */

    /**
     * @{inheritDoc}
     */
    public List<IssueSignatureDto> findAll() {
        List<IssueSignatureDto> IssueSignatureDtoList = new ArrayList<IssueSignatureDto>();
        PreparedStatement selectIssues = null;
        ResultSet rs;
        rs = null;
        try {
            _logger.debug("Starting select all issue signature");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s`", _projectName, _tableName);
                selectIssues = _conn.prepareStatement(sql);
                rs = selectIssues.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        IssueSignatureDto IssueSignatureDto = new IssueSignatureDto();
                        IssueSignatureDto.setId(rs.getInt(1));
                        IssueSignatureDto.setMethod(rs.getString(2));
                        IssueSignatureDto.setSeverity(rs.getInt(3));
                        IssueSignatureDto.setCode(rs.getString(4));
                        IssueSignatureDto.setTraceSignature(rs.getString(5));
                        IssueSignatureDto.setFirstOccur(rs.getInt(6));
                        IssueSignatureDto.setTrend(rs.getString(7));
                        IssueSignatureDto.setFlag(rs.getInt(8));
                        IssueSignatureDtoList.add(IssueSignatureDto);
                    }
                }
                _logger.debug("Successful executing select all issue signature");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(selectIssues);
            DbUtils.closeQuietly(_conn);
        }

        return IssueSignatureDtoList;
    }

    /**
     * @{inheritDoc}
     */
    /*
    public List<IssueSignatureDto> findIssueSignatureEquals(String sign_method, String sign_code, String sign_signature, int sign_severityLevel) {
        List<IssueSignatureDto> IssueSignatureDtoList = new ArrayList<IssueSignatureDto>();
        PreparedStatement selectIssues = null;
        ResultSet rs;
        rs = null;
        try {
            _logger.debug("Starting find Bug Signature Equals");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s` WHERE method LIKE ? AND severitylevel="+sign_severityLevel+" AND code LIKE '"+sign_code+"' AND tracesignature LIKE '"+sign_signature+"'", _projectName, _tableName);
                selectIssues = _conn.prepareStatement(sql);
                rs = selectIssues.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        IssueSignatureDto IssueSignatureDto = new IssueSignatureDto();
                        IssueSignatureDto.setId(rs.getInt(1));
                        IssueSignatureDto.setMethod(rs.getString(2));
                        IssueSignatureDto.setSeverity(rs.getInt(3));
                        IssueSignatureDto.setCode(rs.getString(4));
                        IssueSignatureDto.setTraceSignature(rs.getString(5));
                        IssueSignatureDto.setFirstOccur(rs.getInt(6));
                        IssueSignatureDto.setTrend(rs.getString(7));
                        IssueSignatureDto.setFlag(rs.getInt(8));
                        IssueSignatureDtoList.add(IssueSignatureDto);
                    }
                }
            }
            _logger.debug("Successful find Bug Signature Equals");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(selectIssues);
            DbUtils.closeQuietly(_conn);
        }

        return IssueSignatureDtoList;
    } */

    /**
     * @{inheritDoc}
     */
    public List<IssueSignatureDto> findIssueSignatureEquals(String sign_signature) {
        List<IssueSignatureDto> IssueSignatureDtoList = new ArrayList<IssueSignatureDto>();
        PreparedStatement selectIssues = null;
        ResultSet rs;
        rs = null;
        try {
            _logger.debug("Starting find Bug Signature Equals sign_signature");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s` WHERE tracesignature = ?", _projectName, _tableName);
                selectIssues = _conn.prepareStatement(sql);
                selectIssues.setString(1, sign_signature);
                rs = selectIssues.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        IssueSignatureDto IssueSignatureDto = new IssueSignatureDto();
                        IssueSignatureDto.setId(rs.getInt(1));
                        IssueSignatureDto.setTraceSignature(rs.getString(2));
                        IssueSignatureDto.setTrend(rs.getString(3));
                        IssueSignatureDtoList.add(IssueSignatureDto);
                    }
                }
                _logger.debug("Successful executing find Bug Signature Equals sign_signature");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(selectIssues);
            DbUtils.closeQuietly(_conn);
        }

        return IssueSignatureDtoList;
    }

    public int findSize(){
        PreparedStatement select = null;
        ResultSet rs = null;
        int result = 0;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT count(*) FROM `%s` .`%s`", _projectName, _tableName);
                select = _conn.prepareStatement(sql);
                rs = select.executeQuery();
                rs.next();
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(select);
            DbUtils.closeQuietly(_conn);
        }
        return result;
    }


    /**
     *
     * @param numberOfBuild  test
     * @return test
     */
    public Result updateTrend(int numberOfBuild) {
        PreparedStatement updateIssues = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("UPDATE `%s`.`%s`SET trend = IF(LENGTH(trend) < ?, CONCAT(trend, '#0'), trend) ", _projectName, _tableName);
                updateIssues = _conn.prepareStatement(sql);
                updateIssues.setInt(1, numberOfBuild*2 - 1);
                updateIssues.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(updateIssues);
            DbUtils.closeQuietly(_conn);
        }

        return Result.SUCCESS;
    }

    /**
     *
     * @param buildList test
     * @return test
     */
    public Result removeTrend(String buildList) {
        CallableStatement updateIssues = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("{call `%s`.RemoveTrend(?)}", _projectName);
                updateIssues = _conn.prepareCall(sql);
                updateIssues.setString(1, buildList);
                updateIssues.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(updateIssues);
            DbUtils.closeQuietly(_conn);
        }

        return Result.SUCCESS;
    }
}

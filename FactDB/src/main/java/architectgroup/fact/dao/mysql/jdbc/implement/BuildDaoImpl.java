package architectgroup.fact.dao.mysql.jdbc.implement;

import architectgroup.fact.Const;
import architectgroup.fact.dao.BuildDao;
import architectgroup.fact.dao.mysql.jdbc.BaseDao;
import architectgroup.fact.dto.BuildDto;
import architectgroup.fact.util.Result;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/12/12
 * Time: 4:42 PM
 */

public class BuildDaoImpl extends BaseDao implements BuildDao {
    private static final Logger _logger = Logger.getLogger(BuildDaoImpl.class);
    private Connection _conn;
    private int _projectId;

    public BuildDaoImpl(int projectId) {
        super();
        this._projectId = projectId;
    }

    /**
     * Function to create the build Dto from the Result set
     * @param rs the result set
     * @return buildDto
     * @throws SQLException
     */
    public BuildDto mapRow(ResultSet rs) throws SQLException {
        BuildDto buildDto = new BuildDto();
        buildDto.setId(rs.getInt(1));
        buildDto.setName(rs.getString(2));
        buildDto.setDescription(rs.getString(3));
        buildDto.setStartTime(rs.getTimestamp(4));
        buildDto.setEndTime(rs.getTimestamp(5));
        buildDto.setStatus(rs.getString(6));
        buildDto.setVersion(rs.getString(7));
        buildDto.setType(rs.getString(8));
        buildDto.setSize(rs.getInt(9));
        buildDto.setNumberOfNewIssue(rs.getInt(10));
        buildDto.setNumberOfOpenIssue(rs.getInt(11));
        buildDto.setNumberOfFixedIssue(rs.getInt(12));
        buildDto.setCreatedBy(rs.getString(13));

        if (rs.wasNull()) {
            buildDto = null;
        }
        return buildDto;
    }

    /**
     * set parameter to a statement
     * @param insertStatement the current statement
     * @param build the build include parameter
     * @throws SQLException
     */
    public void setInsertParams(PreparedStatement insertStatement, BuildDto build) throws SQLException {
        if (insertStatement != null && build != null) {
            insertStatement.setInt(1, build.getId());
            insertStatement.setString(2, build.getName());
            insertStatement.setString(3, build.getDescription());
            insertStatement.setString(4, Const.DATE_FORMAT.format(build.getStartTime()));
            insertStatement.setString(5, Const.DATE_FORMAT.format(build.getEndTime()));
            insertStatement.setString(6, build.getStatus());
            insertStatement.setString(7, build.getVersion());
            insertStatement.setString(8, build.getType());
            insertStatement.setLong(9, build.getSize());
            insertStatement.setInt(10, build.getNumberOfNewIssue());
            insertStatement.setInt(11, build.getNumberOfOpenIssue());
            insertStatement.setInt(12, build.getNumberOfFixedIssue());
            insertStatement.setString(13, build.getCreatedBy());
        } else {
            _logger.debug("updateStatement or build is null when insert params to statement");
        }
    }

    /**
     * set parameter to a statement
     * @param updateStatement the current statement
     * @param build the build include parameter
     * @throws SQLException
     */
    public void setUpdateParams(PreparedStatement updateStatement, BuildDto build) throws SQLException {
        if (updateStatement != null && build != null) {
            updateStatement.setString(1, build.getName());
            updateStatement.setString(2, build.getDescription());
            updateStatement.setString(3, Const.DATE_FORMAT.format(build.getStartTime()));
            updateStatement.setString(4, Const.DATE_FORMAT.format(build.getEndTime()));
            updateStatement.setString(5, build.getStatus());
            updateStatement.setString(6, build.getVersion());
            updateStatement.setString(7, build.getType());
            updateStatement.setLong(8, build.getSize());
            updateStatement.setInt(9, build.getNumberOfNewIssue());
            updateStatement.setInt(10, build.getNumberOfOpenIssue());
            updateStatement.setInt(11, build.getNumberOfFixedIssue());
            updateStatement.setInt(12, build.getId());
        } else {
            _logger.debug("updateStatement or build is null when insert params to statement");
        }
    }

    /**
     * {@inheritDoc}
     */
    public BuildDto insert(BuildDto build) {
        PreparedStatement buildInsertStatement = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("INSERT `%s`.`build` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", getDatabaseName(_projectId));
                buildInsertStatement = _conn.prepareStatement(sql);
                setInsertParams(buildInsertStatement, build);
                buildInsertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(buildInsertStatement);
            DbUtils.closeQuietly(_conn);
        }
        return build;
    }

    /**
     * {@inheritDoc}
     */
    public BuildDto update(BuildDto build) {
        PreparedStatement buildUpdateStatement = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("UPDATE `%s`.`build` SET name = ?, description = ?, start_time = ?, end_time = ?, status = ?, version = ?, type = ?, " +
                                                                  " size = ?, new = ?, existing = ?, fixed = ? WHERE id = ?", getDatabaseName(_projectId));
                buildUpdateStatement = _conn.prepareStatement(sql);
                setUpdateParams(buildUpdateStatement, build);
                buildUpdateStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(buildUpdateStatement);
            DbUtils.closeQuietly(_conn);
        }
        return build;
    }

    /**
     * {@inheritDoc}
     */
    public Result delete(BuildDto build) {
        PreparedStatement buildDeleteStatement = null;
        Result result = Result.FAILED;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("DELETE FROM `%s`.`build` WHERE (id = ?)", getDatabaseName(_projectId));
                buildDeleteStatement = _conn.prepareStatement(sql);
                buildDeleteStatement.setInt(1, build.getId());
                buildDeleteStatement.execute();
                result = Result.SUCCESS;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = Result.DB_DELETE_ERROR;
        }
        finally {
            DbUtils.closeQuietly(buildDeleteStatement);
            DbUtils.closeQuietly(_conn);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public BuildDto findById(int id) {
        PreparedStatement buildSelectIdStatement = null;
        ResultSet rs = null;
        BuildDto buildDto = new BuildDto();
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT * FROM `%s`.`%s` WHERE (id = ?)", getDatabaseName(_projectId), Const.TABLE_BUILD_NAME);
                buildSelectIdStatement = _conn.prepareStatement(sql);
                buildSelectIdStatement.setInt(1, id);
                rs = buildSelectIdStatement.executeQuery();
                if (rs.next()){
                    buildDto = mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(buildSelectIdStatement);
            DbUtils.closeQuietly(_conn);
        }
        return buildDto;
    }

    /**
     * {@inheritDoc}
     */
    public BuildDto findByName(String name) {
        PreparedStatement buildSelectIdStatement = null;
        ResultSet rs = null;
        BuildDto buildDto = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT * FROM `%s`.`%s` WHERE (name = ?)", getDatabaseName(_projectId), Const.TABLE_BUILD_NAME);
                buildSelectIdStatement = _conn.prepareStatement(sql);
                buildSelectIdStatement.setString(1, name);
                rs = buildSelectIdStatement.executeQuery();
                if (rs.next()){
                    buildDto = mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(buildSelectIdStatement);
            DbUtils.closeQuietly(_conn);
        }
        return buildDto;
    }

    /**
     * {@inheritDoc}
     */
    public BuildDto findLastBuild() {
        PreparedStatement buildSelectStatement = null;
        ResultSet rs = null;
        BuildDto buildDto = null;
        try {
            _conn = this.getConnection();
            String sql = String.format("SELECT * FROM `%s`.`%s` ORDER BY id DESC", getDatabaseName(_projectId), Const.TABLE_BUILD_NAME);
            buildSelectStatement = _conn.prepareStatement(sql);
            rs = buildSelectStatement.executeQuery();
            if (rs.next()){
                buildDto = mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(buildSelectStatement);
            DbUtils.closeQuietly(_conn);
        }
        return buildDto;
    }

    /**
     * {@inheritDoc}
     */
    public List<BuildDto> findLastNBuild(int n) {
        List<BuildDto> buildList = new ArrayList<BuildDto>();
        PreparedStatement buildSelectStatement = null;
        ResultSet rs = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM `%s`.`%s` ORDER BY id DESC LIMIT 0, 5", getDatabaseName(_projectId), Const.TABLE_BUILD_NAME);
                buildSelectStatement = _conn.prepareStatement(sql);
                rs = buildSelectStatement.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        BuildDto buildDto = mapRow(rs);
                        buildList.add(buildDto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(buildSelectStatement);
            DbUtils.closeQuietly(_conn);
        }
        return buildList;
    }

    /**
     * {@inheritDoc}
     */
    public List<BuildDto> findAll() {
        List<BuildDto> buildList = new ArrayList<BuildDto>();
        PreparedStatement buildSelectStatement = null;
        ResultSet rs = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM `%s`.`%s`", getDatabaseName(_projectId), Const.TABLE_BUILD_NAME);
                buildSelectStatement = _conn.prepareStatement(sql);
                rs = buildSelectStatement.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        BuildDto buildDto = mapRow(rs);
                        buildList.add(buildDto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(buildSelectStatement);
            DbUtils.closeQuietly(_conn);
        }
        return buildList;
    }
}


package architectgroup.fact.dao.mysql.jdbc.implement;

import architectgroup.fact.Const;
import architectgroup.fact.dao.mysql.jdbc.BaseDao;
import architectgroup.fact.dao.ProjectDao;
import architectgroup.fact.dto.ProjectDto;
import architectgroup.fact.util.Result;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/3/12
 * Time: 2:44 AM
 */

public class ProjectDaoImpl extends BaseDao implements ProjectDao {
    private static final Logger _logger = Logger.getLogger(ProjectDaoImpl.class);
    Connection _conn = null;

    /**
     *
     * @param rs rs
     * @return  re
     * @throws SQLException
     */
    public ProjectDto mapRow(ResultSet rs) throws SQLException {
        ProjectDto project = new ProjectDto();
        project.setId(rs.getInt(1));
        project.setName(rs.getString(2));
        project.setDescription(rs.getString(3));
        project.setCreatedBy(rs.getString(4));
        project.setCreatedTime(rs.getDate(5));
        project.setStatus(rs.getString(6));
        if (rs.wasNull()) {
            project = null;
        }
        return project;
    }

    /**
     * {@inheritDoc}
     */
    public ProjectDto insert(ProjectDto project) {
        PreparedStatement projectInsertStatement = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("INSERT `%s`.`%s` VALUES (NULL, ?, ?, ?, ?, ?)", Const.METADATA_DATABASE_NAME, Const.TABLE_PROJECT_NAME);
                projectInsertStatement = _conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                projectInsertStatement.setString(1, project.getName());
                projectInsertStatement.setString(2, project.getDescription());
                projectInsertStatement.setString(3, project.getCreatedBy());
                projectInsertStatement.setString(4, Const.DATE_FORMAT.format(project.getCreatedTime()));
                projectInsertStatement.setString(5, project.getStatus());
                projectInsertStatement.executeUpdate();
                ResultSet rs = projectInsertStatement.getGeneratedKeys();
                int key = -1;
                if (rs != null && rs.next()) {
                    key = rs.getInt(1);
                }
                project.setId(key);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(projectInsertStatement);
            DbUtils.closeQuietly(_conn);
        }

        return project;
    }

    /**
     * {@inheritDoc}
     */
    public ProjectDto update(ProjectDto project) {
        PreparedStatement projectUpdateStatement = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("UPDATE `%s`.`%s` SET name = ?, description = ?, created_by = ?, created_time = ?, status = ?  WHERE id = ?", Const.METADATA_DATABASE_NAME, Const.TABLE_PROJECT_NAME);
                projectUpdateStatement = _conn.prepareStatement(sql);
                projectUpdateStatement.setString(1, project.getName());
                projectUpdateStatement.setString(2, project.getDescription());
                projectUpdateStatement.setString(3, project.getCreatedBy());
                projectUpdateStatement.setString(4, Const.DATE_FORMAT.format(project.getCreatedTime()));
                projectUpdateStatement.setString(5, project.getStatus());
                projectUpdateStatement.setInt(6, project.getId());
                projectUpdateStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(projectUpdateStatement);
            DbUtils.closeQuietly(_conn);
        }

        return project;
    }

    /**
     * This is for internal use only
     */
    public void updateMetricName(String name, String full, String brief) {
        PreparedStatement projectUpdateStatement = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("UPDATE `%s`.`%s` SET fullname = ?, briefname = ? WHERE name = ?", "metadata", "metrics");
                projectUpdateStatement = _conn.prepareStatement(sql);
                projectUpdateStatement.setString(1, full);
                projectUpdateStatement.setString(2, brief);
                projectUpdateStatement.setString(3, name);
                projectUpdateStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(projectUpdateStatement);
            DbUtils.closeQuietly(_conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Result delete(ProjectDto project) {
        PreparedStatement projectDeleteStatement = null;
        Result result = Result.FAILED;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("DELETE FROM `%s`.`%s` WHERE (id = ?)", Const.METADATA_DATABASE_NAME, Const.TABLE_PROJECT_NAME);
                projectDeleteStatement = _conn.prepareStatement(sql);
                projectDeleteStatement.setInt(1, project.getId());
                projectDeleteStatement.execute();
                result = Result.SUCCESS;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = Result.DB_DELETE_ERROR;
        }
        finally {
            DbUtils.closeQuietly(projectDeleteStatement);
            DbUtils.closeQuietly(_conn);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public ProjectDto findById(int id) {
        PreparedStatement projectSelectStatement = null;
        ResultSet rs = null;
        ProjectDto project = null;

        try {
            _conn = this.getConnection();
            projectSelectStatement = _conn.prepareStatement("SELECT *  FROM  `" + Const.METADATA_DATABASE_NAME + "`.`" + Const.TABLE_PROJECT_NAME + "` WHERE (id = ?)");
            projectSelectStatement.setInt(1,id);
            rs = projectSelectStatement.executeQuery();
            if (rs.next()){
                project = mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, projectSelectStatement, rs);

        }

        return project;
    }

    /**
     * {@inheritDoc}
     */
    public ProjectDto findByName(String name) {
        PreparedStatement projectSelectStatement = null;
        ResultSet rs = null;
        ProjectDto project = null;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s` WHERE (name = ?)", Const.METADATA_DATABASE_NAME, Const.TABLE_PROJECT_NAME);
                projectSelectStatement = _conn.prepareStatement(sql);
                projectSelectStatement.setString(1, name);
                rs = projectSelectStatement.executeQuery();
                if (rs.next()){
                    project = mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, projectSelectStatement, rs);
        }

        return project;
    }

    public List<ProjectDto> findByUser(String username) {
        List<ProjectDto> projectList = new ArrayList<ProjectDto>();
        PreparedStatement projectSelectStatement = null;
        ResultSet rs;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s` WHERE created_by = ?", Const.METADATA_DATABASE_NAME, Const.TABLE_PROJECT_NAME);
                projectSelectStatement = _conn.prepareStatement(sql);
                projectSelectStatement.setString(1, username) ;
                rs = projectSelectStatement.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        ProjectDto projectDto = mapRow(rs);
                        projectList.add(projectDto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(projectSelectStatement);
            DbUtils.closeQuietly(_conn);
        }
        return projectList;
    }

    /**
     * {@inheritDoc}
     */
    public List<ProjectDto> findAll() {
        List<ProjectDto> projectList = new ArrayList<ProjectDto>();
        PreparedStatement projectSelectStatement = null;
        ResultSet rs = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s`", Const.METADATA_DATABASE_NAME, Const.TABLE_PROJECT_NAME);
                projectSelectStatement = _conn.prepareStatement(sql);
                rs = projectSelectStatement.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        ProjectDto projectDto = mapRow(rs);
                        projectList.add(projectDto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, projectSelectStatement, rs);
        }
        return projectList;
    }
}

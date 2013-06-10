package architectgroup.fact.dao.mysql.jdbc.implement;

import architectgroup.fact.Const;
import architectgroup.fact.dao.FilterDao;
import architectgroup.fact.dao.mysql.jdbc.BaseDao;
import architectgroup.fact.dto.FilterDto;
import architectgroup.fact.util.Result;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/12/12
 * Time: 4:42 PM
 */
public class FilterDaoImpl extends BaseDao implements FilterDao {
    private static final Logger _logger = Logger.getLogger(FilterDaoImpl.class);
    private int _projectId;
    private String _projectName;
    private String _tableName;
    private Connection _conn = null;

    public FilterDaoImpl(int projectId) {
        _projectId = projectId;
        _projectName = getDatabaseName(_projectId);
        _tableName = Const.TABLE_FILTER_NAME;
    }

    /**
     * @{inheritDoc}
     */
    public FilterDto insert(FilterDto filterDto) {
        PreparedStatement insertFilter = null;
        try {
            _logger.debug("Starting insert filter ");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("INSERT INTO `%s`.`%s` VALUES (NULL, ?, ?, ?, ?, ?)", _projectName, _tableName);
                insertFilter = _conn.prepareStatement(sql);
                insertFilter.setString(1, filterDto.getName());
                insertFilter.setString(2, filterDto.getBuild());
                insertFilter.setString(3, filterDto.getValue());
                insertFilter.setString(4, filterDto.getState());
                insertFilter.setString(5, filterDto.getScope());
                insertFilter.execute();
                _logger.debug("Successful executing insert filter");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(insertFilter);
            DbUtils.closeQuietly(_conn);
        }

        return filterDto;
    }

    /**
     * @{inheritDoc}
     */
    public FilterDto update(FilterDto filterDto) {
        PreparedStatement updateFilter = null;
        try {
            _logger.debug("Starting update filter ");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("UPDATE `%s`.`%s` SET name = ?, build = ?, value = ?, state = ?, scope = ? WHERE id = ?", _projectName, _tableName);
                updateFilter = _conn.prepareStatement(sql);
                updateFilter.setString(1, filterDto.getName());
                updateFilter.setString(2, filterDto.getBuild());
                updateFilter.setString(3, filterDto.getValue());
                updateFilter.setString(4, filterDto.getState());
                updateFilter.setString(5, filterDto.getScope());
                updateFilter.setInt(6, filterDto.getId());
                updateFilter.execute();
                _logger.debug("Successful executing update filter ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(updateFilter);
            DbUtils.closeQuietly(_conn);
        }
        return filterDto;
    }

    /**
     * @{inheritDoc}
     */
    public Result delete(FilterDto filterDto) {
        PreparedStatement deleteFilter = null;
        Result result = Result.FAILED;
        try {
            _logger.debug("Starting delete filter ");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("DELETE FROM `%s`.`%s` WHERE (id = ?)", _projectName, _tableName);
                deleteFilter = _conn.prepareStatement(sql);
                deleteFilter.setInt(1, filterDto.getId());
                deleteFilter.execute();
                result = Result.SUCCESS;
                _logger.debug("Successful executing delete filter ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = Result.DB_DELETE_ERROR;
        }
        finally {
            DbUtils.closeQuietly(deleteFilter);
            DbUtils.closeQuietly(_conn);
        }
        return result;
    }

    /**
     * @{inheritDoc}
     */
    public Result delete(List<Integer> filterIds){
        PreparedStatement deleteFilter = null;
        Result result = Result.FAILED;

        try {
            _logger.debug("Starting delete filter ");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("DELETE FROM `%s`.`%s` WHERE (id = ?)", _projectName, _tableName);
                deleteFilter = _conn.prepareStatement(sql);
                for (int id : filterIds) {
                    deleteFilter.setInt(1, id);
                    deleteFilter.addBatch();
                }
                deleteFilter.executeBatch();
                _conn.commit();
                result = Result.SUCCESS;
                _logger.debug("Successful executing delete filters");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = Result.DB_DELETE_ERROR;
        }
        finally {
            DbUtils.closeQuietly(deleteFilter);
            DbUtils.closeQuietly(_conn);
        }
        return result;
    }

    /**
     * @{inheritDoc}
     */
    public FilterDto findFilterById(int id) {
        PreparedStatement selectFilter = null;
        ResultSet rs = null;
        FilterDto filterDto = new FilterDto();
        boolean selectedSuccessfully = false;
        try {
            _logger.debug("Starting select filter ");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT * FROM `%s`.`%s` WHERE (id = ?)", _projectName, _tableName);
                selectFilter = _conn.prepareStatement(sql);
                selectFilter.setInt(1, id);
                rs = selectFilter.executeQuery();
                if (rs.next()){
                    filterDto.setId(rs.getInt(1));
                    filterDto.setName(rs.getString(2));
                    filterDto.setBuild(rs.getString(3));
                    filterDto.setValue(rs.getString(4));
                    filterDto.setState(rs.getString(5));
                    filterDto.setScope(rs.getString(6));
                    selectedSuccessfully = true;
                    _logger.debug("Successful executing select filter ");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(selectFilter);
            DbUtils.closeQuietly(_conn);
        }
        if (selectedSuccessfully)
            return filterDto;
        return null;
    }

    /**
     * @{inheritDoc}
     */
    public List<FilterDto> findAll() {
        PreparedStatement selectFilter = null;
        ResultSet rs = null;
        boolean selectedSuccessfully = false;
        List<FilterDto> filterDtoList = new ArrayList<FilterDto>();
        try {
            _logger.debug("Starting select filter ");
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT * FROM `%s`.`%s`", _projectName, _tableName);
                selectFilter = _conn.prepareStatement(sql);
                rs = selectFilter.executeQuery();
                if (rs != null){
                    while (rs.next()){
                        FilterDto filterDto = new FilterDto();
                        filterDto.setId(rs.getInt(1));
                        filterDto.setName(rs.getString(2));
                        filterDto.setBuild(rs.getString(3));
                        filterDto.setValue(rs.getString(4));
                        filterDto.setState(rs.getString(5));
                        filterDto.setScope(rs.getString(6));
                        filterDtoList.add(filterDto);
                    }
                    selectedSuccessfully = true;
                    _logger.debug("Successful executing select filter ");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(selectFilter);
            DbUtils.closeQuietly(_conn);
        }
        if (selectedSuccessfully)
            return filterDtoList;
        return filterDtoList;
    }
}

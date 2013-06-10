package architectgroup.fact.dao.mysql.jdbc.implement;

import architectgroup.fact.Const;
import architectgroup.fact.dao.EntityDao;
import architectgroup.fact.dao.mysql.jdbc.BaseDao;
import architectgroup.fact.dto.*;
import architectgroup.fact.util.Result;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 5/15/13
 * Time: 5:11 PM
 */
public class EntityDaoImpl extends BaseDao implements EntityDao {
    private static final Logger _logger = Logger.getLogger(EntityDao.class);
    private static int MAX_BLOCK = 2000000;
    private int _buildId;
    private int _projectId;
    private Connection _conn = null;

    public EntityDaoImpl(int projectId, int buildId) {
        super();
        _projectId = projectId;
        _buildId = buildId;
    }

    /**
     *
     * @param rs the result set
     * @return buildDto
     * @throws SQLException
     */
    public EntityDto mapRow(ResultSet rs) throws SQLException {
        EntityDto entityDto = new EntityDto();
        entityDto.setId(rs.getString(1));
        entityDto.setName(rs.getString(2));
        entityDto.setPath(rs.getString(3));
        entityDto.setOwner(rs.getString(4));
        entityDto.setPermission(rs.getString(5));
        entityDto.setModDate(rs.getString(6));
        entityDto.setSize(rs.getLong(7));
        entityDto.setCategory(rs.getString(8));
        entityDto.setBaseClass(rs.getString(9));
        entityDto.setScope(rs.getString(10));
        entityDto.setKind(rs.getString(11));
        entityDto.setDefined(rs.getString(12));
        entityDto.setType(rs.getString(13));
        entityDto.setEntityType(rs.getString(14));

        if (rs.wasNull()) {
            entityDto = null;
        }
        return entityDto;
    }

    /**
     * set parameter to a statement
     * @param insertEntityStatement the current statement
     * @param entity the entity include parameter
     * @throws SQLException
     */
    public void setInsertParams(StringBuilder insertEntityStatement, EntityDto entity) {
        if (insertEntityStatement != null && entity != null) {
            String p = String.format("('%s', '%s', '%s', '%s', '%s', '%s', '%s', %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s'), ", entity.getId(), entity.getFullId(), entity.getName(), entity.getPath(), entity.getOwner(), entity.getPermission(), entity.getModDate(), entity.getSize(),
                                                                                                      entity.getCategory(), entity.getBaseClass(), entity.getScope(), entity.getKind(), entity.getDefined(), entity.getType(), entity.getEntityType());
            insertEntityStatement.append(p);
        } else {
            _logger.debug("insertMetrics or entity is null or metrics is null when insert params to statement");
        }
    }

    /**
     * set parameter to a statement
     * @param insertMetrics the current statement
     * @param entity the entity
     * @param metric the metric dto include parameter
     */
    public void setInsertParamsForMetrics(StringBuilder insertMetrics, EntityDto entity, MetricDto metric) {
        if (insertMetrics != null && entity != null && metric != null) {
            String p = String.format("('%s', '%s', '%s'), ", entity.getId(), metric.getName(), metric.getValue());
            insertMetrics.append(p);
        } else {
            _logger.debug("insertMetrics or entity is null or metrics is null when insert params to statement");
        }
    }

    /**
     * Insert a single Entity to Database
     * @param  entity  the object to be added
     * @return the new Entity has just inserted
     */
    public EntityDto insert(EntityDto entity) {
        Statement insertEntityStatement = null;
        Statement insertMetricsStatement = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("INSERT `%s`.`%s` VALUES ", getDatabaseName(_projectId), Const.ENTITY_BUILD_PREFIX + _buildId);
                StringBuilder insertEntityStr = new StringBuilder(sql);
                setInsertParams(insertEntityStr, entity);
                sql = insertEntityStr.substring(0, insertEntityStr.length() - 2);
                insertEntityStatement = _conn.createStatement();
                insertEntityStatement.executeUpdate(sql);

                sql = String.format("INSERT `%s`.`%s` VALUES ", getDatabaseName(_projectId), Const.ENTITY_METRICS_PREFIX + _buildId);
                StringBuilder insertMetricsStr = new StringBuilder(sql);
                for (MetricDto metric : entity.getMetrics()) {
                    setInsertParamsForMetrics(insertMetricsStr, entity, metric);
                }
                sql = insertMetricsStr.substring(0, insertMetricsStr.length() - 2);
                insertMetricsStatement = _conn.createStatement();
                insertMetricsStatement.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(insertMetricsStatement);
            DbUtils.closeQuietly(insertEntityStatement);
            DbUtils.closeQuietly(_conn);
        }
        return entity;
    }

    /**
     *
     * @param entities the list of entities to insert metric
     */
    private void insertMetricsToDB(List<EntityDto> entities) {
        boolean flag = true;

        String sql = String.format("INSERT `%s`.`%s` VALUES ", getDatabaseName(_projectId), Const.ENTITY_METRICS_PREFIX + _buildId);
        StringBuilder insertMetricsStr = new StringBuilder(sql);

        // Insert Metrics
        for (EntityDto entity : entities) {
            for (MetricDto metric : entity.getMetrics()) {
                setInsertParamsForMetrics(insertMetricsStr, entity, metric);
                flag = true;
                if (insertMetricsStr.length() > MAX_BLOCK) {
                    insertBlock(insertMetricsStr);
                    // Reset the insertMetricsStr
                    flag = false;
                    sql = String.format("INSERT `%s`.`%s` VALUES ", getDatabaseName(_projectId), Const.ENTITY_METRICS_PREFIX + _buildId);
                    insertMetricsStr = new StringBuilder(sql);
                }
            }
        }

        // Insert the remaining
        if (flag) {
            insertBlock(insertMetricsStr);
        }
    }

    /**
     *
     * @param insertEntityStr the string builder that make the long insert INSERT ... VALUES (a,b,c) ... include the ,
     */
    private void insertBlock(StringBuilder insertEntityStr) {
        String sql = insertEntityStr.substring(0, insertEntityStr.length() - 2);  // Cut off the , at the end of the query.
        Statement statement = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                statement = _conn.createStatement();
                statement.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(_conn);
        }
    }

    /**
     * Insert a batch of entities to database
     * @param  entities  the object to be added
     * @return Result of inserted
     */
    public Result insert(List<EntityDto> entities) {
        Result result = Result.SUCCESS;

        String sql = String.format("INSERT `%s`.`%s` VALUES ", getDatabaseName(_projectId), Const.ENTITY_BUILD_PREFIX + _buildId);
        StringBuilder insertEntityStr = new StringBuilder(sql);
        for (EntityDto entity : entities) {
            setInsertParams(insertEntityStr, entity);
        }
        insertBlock(insertEntityStr);
        insertMetricsToDB(entities);

        return result;
    }

    /**
     * Insert a batch of relationships
     * @param  relations  the object to be added
     * @return Result
     */
    public Result insertRelationships(List<RelationshipDto> relations) {
        Statement insertRelationStatement = null;
        Result result = Result.FAILED;

        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("INSERT `%s`.`%s` VALUES ", getDatabaseName(_projectId), Const.ENTITY_RELATIONSHIP_PREFIX + _buildId);
                StringBuilder insertRelationStr = new StringBuilder(sql);
                // Insert Entity
                for (RelationshipDto relation : relations) {
                    String p = String.format("('%s', '%s', '%s', '%s'), ", relation.getEntityA(), relation.getType(), relation.getEntityB(), relation.getValue());
                    insertRelationStr.append(p);
                }
                sql = insertRelationStr.substring(0, insertRelationStr.length() - 2);
                insertRelationStatement = _conn.createStatement();
                insertRelationStatement.executeUpdate(sql);
                result = Result.SUCCESS;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = Result.DB_INSERT_ERROR;
        }
        finally {
            DbUtils.closeQuietly(insertRelationStatement);
            DbUtils.closeQuietly(_conn);
        }
        return result;
    }

    /**
     * There is no need to update the metrics
     * @param  entity  the object to be updated
     * @return just for fun
     */
    public EntityDto update(EntityDto entity) {
        return null;
    }

    /**
     * There is no need to update the metrics
     * @param  entity  the object to be updated
     * @return just for fun
     */
    public Result delete(EntityDto entity) {
        return null;
    }

    /**
     *
     * @param  id  the id of the issue
     * @return entity
     */
    public EntityDto findById(String id) {
        EntityDto entityDto = null;
        PreparedStatement selectEntityIssues = null;
        ResultSet rs = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s` WHERE ( id = ?)", getDatabaseName(_projectId), Const.ENTITY_BUILD_PREFIX + _buildId);
                selectEntityIssues = _conn.prepareStatement(sql);
                selectEntityIssues.setString(1, id);
                rs = selectEntityIssues.executeQuery();
                if (rs != null) {
                    rs.next();
                    entityDto = mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, selectEntityIssues, rs);
        }

        // Set the metric
        List<MetricDto> metrics = findMetrics(id);
        if (entityDto != null)
            entityDto.setMetrics(metrics);

        return entityDto;
    }

    /**
     *
     * @param id id
     * @return list
     */
    public List<MetricDto> findMetrics(String id) {
        List<MetricDto> metricDtoList = new ArrayList<MetricDto>();
        PreparedStatement selectMetrics = null;
        ResultSet rs = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT e.metrics_id, m.name, m.type, e.value  FROM  `%s`.`%s` e JOIN `%s`.`%s` m ON e.metrics_id = m.id WHERE (entity_id = ?)",
                                            getDatabaseName(_projectId), Const.ENTITY_METRICS_PREFIX + _buildId, getDatabaseName(_projectId), Const.TABLE_METRICS);
                selectMetrics = _conn.prepareStatement(sql);
                selectMetrics.setString(1, id);
                rs = selectMetrics.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        MetricDto metricDto = new MetricDto();
                        metricDto.setId(rs.getInt(1));
                        metricDto.setName(rs.getString(2));
                        metricDto.setType(rs.getString(3));
                        metricDto.setValue(rs.getString(4));
                        metricDtoList.add(metricDto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, selectMetrics, rs);
        }
        return metricDtoList;
    }

    /**
     *
     * @param  type  the id of the issue
     * @return return the list
     */
    public List<EntityDto> findByEntityType(String type) {
        List<EntityDto> entityDtoList = new ArrayList<EntityDto>();
        PreparedStatement selectHistoryIssues = null;
        ResultSet rs = null;
        try {
            _conn = this.getConnection();
            if (_conn != null) {
                String sql = String.format("SELECT *  FROM  `%s`.`%s` WHERE ( entity_type = ?)", getDatabaseName(_projectId), Const.ENTITY_METRICS_PREFIX + _buildId);
                selectHistoryIssues = _conn.prepareStatement(sql);
                selectHistoryIssues.setString(1, type);
                rs = selectHistoryIssues.executeQuery();
                if (rs != null){
                    while(rs.next()){
                        EntityDto entityDto = mapRow(rs);
                        String id = rs.getString(1);
                        entityDto.setMetrics(findMetrics(id));  // Set metrics
                        entityDtoList.add(entityDto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(_conn, selectHistoryIssues, rs);
        }
        return entityDtoList;
    }
}

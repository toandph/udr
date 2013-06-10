package architectgroup.fact.dao.mysql.jdbc.implement;

import architectgroup.fact.Const;
import architectgroup.fact.Context;
import architectgroup.fact.DebugMessage;
import architectgroup.fact.dao.DaoUtil;
import architectgroup.fact.dao.DatabaseDao;
import architectgroup.fact.dao.mysql.jdbc.BaseDao;
import architectgroup.fact.dao.mysql.jdbc.MysqlDbStatementBuilder;
import architectgroup.fact.dao.mysql.jdbc.MysqlDbStatementImagixBuilder;
import architectgroup.fact.util.LogHelper;
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
 * Time: 11:22 PM
 */
public class DatabaseDaoImpl extends BaseDao implements DatabaseDao {
    private static final Logger _logger = Logger.getLogger(DatabaseDaoImpl.class);
    private Context _ctx;
    public DatabaseDaoImpl(Context ctx) {
        _ctx = ctx;
    }

    /**
     * Roll back the database, remove the change from database
     * @param databaseName the database want to remove
     * @return the status
     */
    private Result rollBackDatabase(String databaseName) {
        Result result = dropDatabase(databaseName);
        switch (result) {
            case SUCCESS:
                _logger.debug("Rollback the Creation of Database Successfully");
                break;
            case FAILED:
                _logger.debug("Can not Rollback the Creation of Database");
                break;
        }
        return result;
    }

    /**
     * Rollback table
     * @param databaseName  databaseName
     * @param tableName  tableName
     */
    private void rollBackTable(String databaseName, String tableName) {
        Result result = dropTable(databaseName, tableName);
        switch (result) {
            case SUCCESS:
                _logger.debug("Rollback the Creation of Table Successfully");
                break;
            case FAILED:
                _logger.debug("Can not Rollback the Creation of Table");
                break;
        }
    }

    /**
     * @{inheritDoc}
     */
    public boolean checkDatabaseExists(int databaseId) {
        return checkDatabaseExists(getDatabaseName(databaseId));
    }

    /**
     * @{inheritDoc}
     */
    private boolean checkDatabaseExists(String dbName) {
        Connection conn = null;
        Statement dbListStatement = null;
        ResultSet rs = null;
        List<String> dbLists = new ArrayList<String>();

        try {
            conn = this.getConnection();
            dbListStatement = conn.createStatement();
            rs = dbListStatement.executeQuery("SHOW DATABASES");
            while (rs.next()) {
                dbLists.add(rs.getString(1));
            }
        } catch (SQLException err) {
            err.printStackTrace();
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(dbListStatement);
            DbUtils.closeQuietly(conn);
        }

        for (String t : dbLists) {
            if (t.equalsIgnoreCase(dbName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @{inheritDoc}
     */
    public Result createDatabase(String databaseName) {
        if (checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_EXISTS + databaseName);
            return Result.DB_EXIST;
        }

        Connection conn = null;
        Statement createDatabaseStatement = null;
        Result result = Result.FAILED;

        try {
            conn = this.getConnection();
            createDatabaseStatement = conn.createStatement();
            // Create database statement
            createDatabaseStatement.executeUpdate(MysqlDbStatementBuilder.createDatabaseStatement(databaseName));
            // Execute Query
            createDatabaseStatement.executeBatch();
            result = Result.SUCCESS;

            _logger.debug(DebugMessage.CREATED_DATABASE_SUCCESSFULLY + databaseName);
        } catch (SQLException err) {
            this.rollBackDatabase(databaseName);
            result = Result.DB_ERROR;
        } finally {
            DbUtils.closeQuietly(createDatabaseStatement);
            DbUtils.closeQuietly(conn);
        }

        return result;
    }

    /**
     * @{inheritDoc}
     */
    public Result createDatabase(int databaseId) {
        return createDatabase(getDatabaseName(databaseId));
    }

    /**
     * @{inheritDoc}
     */
    public Result dropDatabase(String databaseName) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement dropDatabaseStatement = null;
        Result result = Result.FAILED;

        try {
            conn = this.getConnection();
            dropDatabaseStatement = conn.createStatement();
            dropDatabaseStatement.addBatch(MysqlDbStatementBuilder.dropDatabaseStatement(databaseName));
            dropDatabaseStatement.executeBatch();
            dropDatabaseStatement.close();

            _logger.debug(DebugMessage.DROPPED_DATABASE_SUCCESSFULLY + databaseName);

            result = Result.SUCCESS;
        } catch (Exception err) {
            _logger.fatal(DebugMessage.ERROR_SQL_EXCEPTION + err.getMessage());
            LogHelper.writeToLog(_ctx.getLogFile(), err);

            result = Result.FAILED;
        } finally {
            DbUtils.closeQuietly(dropDatabaseStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    /**
     * @{inheritDoc}
     */
    public Result dropDatabase(int databaseId) {
        return dropDatabase(getDatabaseName(databaseId));
    }

    /**
     * @{inheritDoc}
     */
    public Result createTableBuild(int databaseId) {
        return createTableBuild(getDatabaseName(databaseId));
    }

    /**
     * @{inheritDoc}
     */
    public Result createTableIssueSignature(int databaseId) {
        return createTableIssueSignature(getDatabaseName(databaseId));
    }

    /**
     * @{inheritDoc}
     */
    public Result createTableIssue(int databaseId, int buildId) {
        return createTableIssue(getDatabaseName(databaseId), buildId);
    }

    /**
     * @{inheritDoc}
     */
    public Result createTableFilter(int databaseId) {
        return createTableFilter(getDatabaseName(databaseId));
    }

    /**
     * @{inheritDoc}
     */
    public Result createTableTrace(int databaseId, int buildId) {
        return createTableTrace(getDatabaseName(databaseId), buildId);
    }

    /**
     * @{inheritDoc}
     */
    public Result createTableHistory(int databaseId, int buildId) {
        return createTableHistory(getDatabaseName(databaseId), buildId);
    }

    /**
     * @{inheritDoc}
     */
    public Result createTriggerOnHistory(int databaseId, int buildId) {
        return createTriggerOnHistory(getDatabaseName(databaseId), buildId);
    }

    public Result createTriggerOnHistoryDelete(int databaseId, int buildId) {
        return createTriggerOnHistoryDelete(getDatabaseName(databaseId), buildId);
    }

    /**
     * @{inheritDoc}
     */
    public Result dropTable(int databaseId, String tableName) {
        return dropTable(getDatabaseName(databaseId), tableName);
    }

    /**
     * @{inheritDoc}
     */
    public List<Integer> getBuildIds(int databaseId) {
        return getBuildIds(getDatabaseName(databaseId));
    }

    /**
     * @{inheritDoc}
     */
    public Result createTriggerOnHistory(String databaseName, int buildId) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createTableTraceStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createTableTraceStatement = conn.createStatement();

            // Create Trigger On Table History //
            String createdTrigger = MysqlDbStatementBuilder.createTriggerOnHistoryInsert(databaseName, buildId);
            createTableTraceStatement.executeUpdate(createdTrigger);

            result = Result.SUCCESS;
        } catch (SQLException err) {
            result = Result.DB_CREATE_TRIGGER_ERROR;
        } finally {
            DbUtils.closeQuietly(createTableTraceStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    /**
     * @{inheritDoc}
     */
    public Result createTriggerOnHistoryDelete(String databaseName, int buildId) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createTableTraceStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createTableTraceStatement = conn.createStatement();

            // Create Trigger On Table History //
            String createdTrigger = MysqlDbStatementBuilder.createTriggerOnHistoryDelete(databaseName, buildId);
            createTableTraceStatement.executeUpdate(createdTrigger);

            result = Result.SUCCESS;
        } catch (SQLException err) {
            result = Result.DB_CREATE_TRIGGER_ERROR;
        } finally {
            DbUtils.closeQuietly(createTableTraceStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    /**
     * @{inheritDoc}
     */
    public Result createTableBuild(String databaseName) {
        Connection conn = null;
        Statement createTableBuildStatement = null;
        Result result = Result.FAILED;

        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        try {
            conn = this.getConnection();
            createTableBuildStatement = conn.createStatement();
            // Create Table Build statement
            String createdTableBuildSQL = MysqlDbStatementBuilder.createTableBuild(databaseName);
            createTableBuildStatement.executeUpdate(createdTableBuildSQL);
            result = Result.SUCCESS;

            _logger.debug(DebugMessage.CREATE_TABLE_BUILD_SQL + createdTableBuildSQL);
            _logger.debug(DebugMessage.CREATED_TABLE_SUCCESSFULLY + databaseName + "." + Const.TABLE_BUILD_NAME);
        } catch (SQLException err) {
            result = Result.DB_CREATE_TABLE_ERROR;
        } finally {
            DbUtils.closeQuietly(createTableBuildStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    /**
     * @{inheritDoc}
     */
    public Result createTableIssueSignature(String databaseName) {
        Connection conn = null;
        Statement createTableIssueSignatureStatement = null;
        Result result = Result.FAILED;

        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        try {
            conn = this.getConnection();
            createTableIssueSignatureStatement = conn.createStatement();

            /* Create Table Project statement */
            String createdTableIssueSignatureSQL = MysqlDbStatementBuilder.createTableIssueSignature(databaseName);
            createTableIssueSignatureStatement.executeUpdate(createdTableIssueSignatureSQL);
            result = Result.SUCCESS;

            _logger.debug(DebugMessage.CREATE_TABLE_ISSUE_SIGNATURE_SQL + createdTableIssueSignatureSQL);
            _logger.debug(DebugMessage.CREATED_TABLE_SUCCESSFULLY + databaseName + "." + Const.TABLE_ISSUE_SIGNATURE_NAME);
        } catch (SQLException err) {
            // If failed to create table, then roll back
            this.rollBackTable(databaseName, Const.TABLE_ISSUE_SIGNATURE_NAME);
            result = Result.DB_CREATE_TABLE_ERROR;
        } finally {
            DbUtils.closeQuietly(createTableIssueSignatureStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    /**
     * @{inheritDoc}
     */
    public Result createTableFilter(String databaseName){
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createTableFilterStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createTableFilterStatement = conn.createStatement();

            /* Create Table Project statement */
            String createdTableFilterSQL = MysqlDbStatementBuilder.createTableFilter(databaseName);
            createTableFilterStatement.executeUpdate(createdTableFilterSQL);

            _logger.debug(DebugMessage.CREATE_TABLE_FILTER_SQL + createdTableFilterSQL);
            _logger.debug(DebugMessage.CREATED_TABLE_SUCCESSFULLY + databaseName + "." + Const.TABLE_FILTER_NAME);

            result = Result.SUCCESS;
        } catch (SQLException err) {
            /* If failed to create table, then roll back */
            this.rollBackTable(databaseName, Const.TABLE_FILTER_NAME);

            _logger.debug(DebugMessage.ERROR_SQL_EXCEPTION + err.getMessage());
            LogHelper.writeToLog(_ctx.getLogFile(), err);

            result = Result.FAILED;
        } finally {
            DbUtils.closeQuietly(createTableFilterStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    /**
     * @{inheritDoc}
     */
    public Result createTableIssue(String databaseName, int buildId) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createTableIssueStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createTableIssueStatement = conn.createStatement();

            /* Create Table Project statement */
            System.out.println("create issue table "+buildId+" --databaseName: "+databaseName);
            String createdTableIssueSQL = MysqlDbStatementBuilder.createTableIssue(databaseName, buildId);
            System.out.println("sql: "+createdTableIssueSQL);
            createTableIssueStatement.executeUpdate(createdTableIssueSQL);

            _logger.debug(DebugMessage.CREATE_TABLE_ISSUE_SQL + createdTableIssueSQL);
            _logger.debug(DebugMessage.CREATED_TABLE_SUCCESSFULLY + databaseName + "." + Const.ISSUE_BUILD_PREFIX + buildId);
            result = Result.SUCCESS;
        } catch (SQLException err) {
            /* If failed to create table, then roll back */
            this.rollBackTable(databaseName, Const.ISSUE_BUILD_PREFIX + buildId);

            _logger.debug(DebugMessage.ERROR_SQL_EXCEPTION + err.getMessage());
            LogHelper.writeToLog(_ctx.getLogFile(), err);

            result = Result.FAILED;
        } finally {
            DbUtils.closeQuietly(createTableIssueStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    /**
     * @{inheritDoc}
     */
    public Result createTableTrace(String databaseName, int buildId) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createTableTraceStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createTableTraceStatement = conn.createStatement();

            /* Create Table Project statement */
            String createdTableTraceSQL = MysqlDbStatementBuilder.createTableTrace(databaseName, buildId);
            createTableTraceStatement.executeUpdate(createdTableTraceSQL);

            _logger.debug(DebugMessage.CREATE_TABLE_TRACE_SQL + createdTableTraceSQL);
            _logger.debug(DebugMessage.CREATED_TABLE_SUCCESSFULLY + databaseName + "." + Const.TRACE_BUILD_PREFIX + buildId);
            result = Result.SUCCESS;
        } catch (SQLException err) {
            /* If failed to create table, then roll back */
            this.rollBackTable(databaseName, Const.TRACE_BUILD_PREFIX + buildId);

            _logger.debug(DebugMessage.ERROR_SQL_EXCEPTION + err.getMessage());
            LogHelper.writeToLog(_ctx.getLogFile(), err);

            result = Result.FAILED;
        } finally {
            DbUtils.closeQuietly(createTableTraceStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    /**
     * @{inheritDoc}
     */
    public Result createTableHistory(String databaseName, int buildId) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createTableTraceStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createTableTraceStatement = conn.createStatement();

            /* Create Table Project statement */
            String createdTableTraceSQL = MysqlDbStatementBuilder.createTableHistory(databaseName, buildId);
            createTableTraceStatement.executeUpdate(createdTableTraceSQL);

            result = Result.SUCCESS;
        } catch (SQLException err) {
            /* If failed to create table, then roll back */
            //this.rollBackTable(databaseName, Const.TRACE_BUILD_PREFIX + buildId);
            result = Result.FAILED;
        } finally {
            DbUtils.closeQuietly(createTableTraceStatement);
            DbUtils.closeQuietly(conn);
        }

        return result;
    }

    /**
     * @{inheritDoc}
     */
    public Result dropTable(String databaseName, String tableName) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement dropTableStatement = null;
        Result result = Result.FAILED;

        try {
            conn = this.getConnection();
            dropTableStatement = conn.createStatement();
            dropTableStatement.executeUpdate(MysqlDbStatementBuilder.dropTable(databaseName, tableName));
            result = Result.SUCCESS;

            _logger.debug(DebugMessage.DROP_TABLE_SQL + databaseName + "." + tableName);
            _logger.debug(DebugMessage.CREATED_TABLE_SUCCESSFULLY + databaseName + "." + tableName);
        } catch (SQLException err) {
            err.printStackTrace();
            result = Result.DB_DROP_ERROR;
        } finally {
            DbUtils.closeQuietly(dropTableStatement);
            DbUtils.closeQuietly(conn);
        }

        return result;
    }

    /*
    private Result createTriggerOnIssueSignature(String databaseName) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createStatement = conn.createStatement();
            // Create Trigger On Table Issue Signature //
            String createdTrigger = MysqlDbStatementBuilder.createTriggerOnIssueSignatureUpdate(databaseName);
            createStatement.executeUpdate(createdTrigger);

            result = Result.SUCCESS;
        } catch (SQLException err) {
            result = Result.DB_CREATE_TRIGGER_ERROR;
        } finally {
            DbUtils.closeQuietly(createStatement);
            DbUtils.closeQuietly(conn);
        }

        return result;
    }*/

    /*
    public Result createTriggerOnIssueSignature(int databaseId) {
        return createTriggerOnIssueSignature(getDatabaseName(databaseId));
    } */

    public Result createProcedureRemoveTrend(String databaseName) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createStatement = conn.createStatement();
            // Create Trigger On Table Issue Signature //
            String createdTrigger = MysqlDbStatementBuilder.createProcedureRemoveTrend(databaseName);
            createStatement.executeUpdate(createdTrigger);

            result = Result.SUCCESS;
        } catch (SQLException err) {
            result = Result.DB_ERROR;
        } finally {
            DbUtils.closeQuietly(createStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    public Result createProcedureRemoveTrend(int databaseId) {
        return createProcedureRemoveTrend(getDatabaseName(databaseId));
    }

    public Result createFunctionSplit(String databaseName) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createStatement = conn.createStatement();
            // Create Trigger On Table Issue Signature //
            String createdTrigger = MysqlDbStatementBuilder.createFunctionSplit(databaseName);
            createStatement.executeUpdate(createdTrigger);

            result = Result.SUCCESS;
        } catch (SQLException err) {
            result = Result.DB_ERROR;
        } finally {
            DbUtils.closeQuietly(createStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    public Result createFunctionSplit(int databaseId) {
        return createFunctionSplit(getDatabaseName(databaseId));
    }

    public Result createFunctionGetState(String databaseName) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createStatement = conn.createStatement();
            // Create Trigger On Table Issue Signature //
            String createdTrigger = MysqlDbStatementBuilder.createFunctionGetState(databaseName);
            createStatement.executeUpdate(createdTrigger);

            result = Result.SUCCESS;
        } catch (SQLException err) {
            result = Result.DB_ERROR;
        } finally {
            DbUtils.closeQuietly(createStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    public Result createFunctionGetState(int databaseId) {
        return createFunctionGetState(getDatabaseName(databaseId));
    }

    public Result createFunctionIsZero(String databaseName) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createStatement = conn.createStatement();
            // Create Trigger On Table Issue Signature //
            String createdTrigger = MysqlDbStatementBuilder.createFunctionIsZero(databaseName);
            createStatement.executeUpdate(createdTrigger);

            result = Result.SUCCESS;
        } catch (SQLException err) {
            result = Result.DB_ERROR;
        } finally {
            DbUtils.closeQuietly(createStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    public Result createFunctionIsZero(int databaseId) {
        return createFunctionIsZero(getDatabaseName(databaseId));
    }

    public Result createFunctionRemoveBuild(String databaseName) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createStatement = conn.createStatement();
            // Create Trigger On Table Issue Signature //
            String createdTrigger = MysqlDbStatementBuilder.createFunctionRemoveBuild(databaseName);
            createStatement.executeUpdate(createdTrigger);

            result = Result.SUCCESS;
        } catch (SQLException err) {
            result = Result.DB_ERROR;
        } finally {
            DbUtils.closeQuietly(createStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    public Result createFunctionRemoveBuild(int databaseId) {
        return createFunctionRemoveBuild(getDatabaseName(databaseId));
    }

    /**
     * @{inheritDoc}
     */
    public List<Integer> getBuildIds(String databaseName) {
        List<Integer> buildIds = null;
        Connection conn = null;
        Statement queryBuildIdStatement = null;
        ResultSet buildIdsResultSet = null;

         try {
            conn = this.getConnection();
            queryBuildIdStatement = conn.createStatement();
            String sql = String.format("SHOW TABLES IN `%s`", databaseName);
            buildIdsResultSet = queryBuildIdStatement.executeQuery(sql);
            while (buildIdsResultSet.next()) {
                 // Printing results to the console
                 int id = DaoUtil.getBuildNumberByString(buildIdsResultSet.getString(1));
                 if (id > 0) {
                    if (buildIds == null) {
                        buildIds = new ArrayList<Integer>();
                    }
                    buildIds.add(id);
                 }
            }
            queryBuildIdStatement.close();
        } catch (SQLException err) {
            err.printStackTrace();
        } finally {
            DbUtils.closeQuietly(buildIdsResultSet);
            DbUtils.closeQuietly(queryBuildIdStatement);
            DbUtils.closeQuietly(conn);
        }
        return buildIds;
    }

    public Result createTableMetrics(String databaseName) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createTableMetricsStatement = null;
        Statement fillTableMetricsStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createTableMetricsStatement = conn.createStatement();
            String createdTableIssueSQL = MysqlDbStatementImagixBuilder.createTableMetrics(databaseName);
            createTableMetricsStatement.executeUpdate(createdTableIssueSQL);

            String copyContentStatement = MysqlDbStatementImagixBuilder.fillTableMetrics(databaseName);
            fillTableMetricsStatement = conn.createStatement();
            fillTableMetricsStatement.executeUpdate(copyContentStatement);

            result = Result.SUCCESS;
        } catch (SQLException err) {
            err.printStackTrace();
            result = Result.FAILED;
        } finally {
            DbUtils.closeQuietly(createTableMetricsStatement);
            DbUtils.closeQuietly(fillTableMetricsStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    public Result createTableRelationship(String databaseName) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createTableRelationshipStatement = null;
        Statement fillTableRelationshipStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createTableRelationshipStatement = conn.createStatement();
            String createdTableIssueSQL = MysqlDbStatementImagixBuilder.createTableRelationship(databaseName);
            createTableRelationshipStatement.executeUpdate(createdTableIssueSQL);

            String copyContentStatement = MysqlDbStatementImagixBuilder.fillTableRelationship(databaseName);
            fillTableRelationshipStatement = conn.createStatement();
            fillTableRelationshipStatement.executeUpdate(copyContentStatement);

            result = Result.SUCCESS;
        } catch (SQLException err) {
            err.printStackTrace();
            result = Result.FAILED;
        } finally {
            DbUtils.closeQuietly(createTableRelationshipStatement);
            DbUtils.closeQuietly(fillTableRelationshipStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    public Result createTableEntity(String databaseName, int buildId) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createTableEntityStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createTableEntityStatement = conn.createStatement();
            String createdTableIssueSQL = MysqlDbStatementImagixBuilder.createTableEntity(databaseName, buildId);
            createTableEntityStatement.executeUpdate(createdTableIssueSQL);
            result = Result.SUCCESS;
        } catch (SQLException err) {
            err.printStackTrace();
            result = Result.FAILED;
        } finally {
            DbUtils.closeQuietly(createTableEntityStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    public Result createTableEntityMetric(String databaseName, int buildId) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createTableEntityMetricStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createTableEntityMetricStatement = conn.createStatement();
            String createdTableIssueSQL = MysqlDbStatementImagixBuilder.createTableEntityMetrics(databaseName, buildId);
            createTableEntityMetricStatement.executeUpdate(createdTableIssueSQL);
            result = Result.SUCCESS;
        } catch (SQLException err) {
            err.printStackTrace();
            result = Result.FAILED;
        } finally {
            DbUtils.closeQuietly(createTableEntityMetricStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    public Result createTableEntityRelationship(String databaseName, int buildId) {
        if (!checkDatabaseExists(databaseName)) {
            _logger.debug(DebugMessage.DATABASE_NOT_EXISTS + databaseName);
            return Result.DB_NOT_EXIST;
        }

        Connection conn = null;
        Statement createTableEntityRelationshipStatement = null;
        Result result = Result.FAILED;
        try {
            conn = this.getConnection();
            createTableEntityRelationshipStatement = conn.createStatement();
            String createdTableIssueSQL = MysqlDbStatementImagixBuilder.createTableEntityRelationship(databaseName, buildId);
            createTableEntityRelationshipStatement.executeUpdate(createdTableIssueSQL);
            result = Result.SUCCESS;
        } catch (SQLException err) {
            err.printStackTrace();
            result = Result.FAILED;
        } finally {
            DbUtils.closeQuietly(createTableEntityRelationshipStatement);
            DbUtils.closeQuietly(conn);
        }
        return result;
    }

    public Result createTableMetrics(int databaseId) {
        return createTableMetrics(getDatabaseName(databaseId));
    }

    public Result createTableRelationship(int databaseId) {
        return createTableRelationship(getDatabaseName(databaseId));
    }

    public Result createTableEntity(int databaseId, int buildId) {
        return createTableEntity(getDatabaseName(databaseId), buildId);
    }

    public Result createTableEntityMetric(int databaseId, int buildId) {
        return createTableEntityMetric(getDatabaseName(databaseId), buildId);
    }

    public Result createTableEntityRelationship(int databaseId, int buildId) {
        return createTableEntityRelationship(getDatabaseName(databaseId), buildId);
    }
}

package architectgroup.fact.dao;

import architectgroup.fact.util.Result;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/11/12
 * Time: 11:20 PM
 */
public interface DatabaseDao {
    /**
     *
     * @param databaseId the name is id
     * @return true for database exists
     */
    public boolean checkDatabaseExists(int databaseId);

    /**
     * Create a database for FACT database.
     * Argument is the name of the Database
     * This method returns the status of successfully or fail
     *
     * @param  databaseId  the database name for creating FACT DB.
     * @return      the status (Const.SUCCESSFULLY or Const.FAILED
     */
    public Result createDatabase(int databaseId);

    /**
     * Drop the database.
     * Argument is the name of the Database
     * This method returns the status of successfully or fail
     *
     * @param  databaseId  the database name for switch FACT DB.
     * @return the status (Const.SUCCESSFULLY or Const.FAILED
     */
    public Result dropDatabase(int databaseId);

    /**
     * Create table Build.
     *
     * @param  databaseId  the database name for creating Build in that database.
     * @return the status (Const.SUCCESSFULLY or Const.FAILED
     */
    public Result createTableBuild(int databaseId);

    /**
     * Create table Issue Signature.
     * Issue Signature table is used for store the identified issue.
     * The main purpose to create the primary key for the issue.
     *
     * @param  databaseId  the database name for creating Issue Signature Table in that database.
     * @return the status (Const.SUCCESSFULLY or Const.FAILED
     */
    public Result createTableIssueSignature(int databaseId);

    /**
     * Create table Issue.
     *
     * @param  databaseId the database name for creating Issue table in that database.
     * @param  buildId the Issue Table is with the buildId
     * @return the status (Const.SUCCESSFULLY or Const.FAILED
     */
    public Result createTableIssue(int databaseId, int buildId);

    /**
     * Create table Filter.
     *
     * @param  databaseId the database name for creating Filter table in that database.
     * @return the status (Const.SUCCESSFULLY or Const.FAILED
     */
    public Result createTableFilter(int databaseId);

    /**
     * Create table Trace.
     *
     * @param  databaseId the database name for creating Trace table in that database.
     * @param  buildId the Issue Table is with the buildId
     * @return the status (Const.SUCCESSFULLY or Const.FAILED
     */
    public Result createTableTrace(int databaseId, int buildId);

    /**
     * Create table History.
     *
     * @param  databaseId the database name for creating History table in that database.
     * @param  buildId the Issue Table is with the buildId
     * @return the status (Const.SUCCESSFULLY or Const.FAILED
     */
    public Result createTableHistory(int databaseId, int buildId);

    /**
     * Drop a table.
     *
     * @param  databaseId the database name in which a table to be dropped.
     * @param  tableName the name of the table to be dropped
     * @return the status (Const.SUCCESSFULLY or Const.FAILED
     */
    public Result dropTable(int databaseId, String tableName);

    /**
     * List all the build ids in the database.
     * Argument is the name of the Database
     * <p>
     * This method returns the list of build ids (integer) of this database
     *
     * @param  databaseId  the database name for switch FACT DB.
     * @return the list of build ids.
     */
    @Nullable
    public List<Integer> getBuildIds(int databaseId);

    /**
     * Create trigger on history table
     * @param databaseId
     * @param buildId
     * @return number of problem
     */
    public Result createTriggerOnHistory(int databaseId, int buildId);

    /**
     * Create trigger on history table
     * @param databaseId
     * @param buildId
     * @return number of problem
     */
    public Result createTriggerOnHistoryDelete(int databaseId, int buildId);

    /**
     * Create trigger on issue signature table
     * @param databaseId
     * @return number of problem
     */
    // public Result createTriggerOnIssueSignature(int databaseId);
    public Result createProcedureRemoveTrend(int databaseId);
    public Result createFunctionSplit(int databaseId);
    public Result createFunctionGetState(int databaseId);
    public Result createFunctionIsZero(int databaseId);
    public Result createFunctionRemoveBuild(int databaseId);

    public Result createTableMetrics(int databaseId);
    public Result createTableRelationship(int databaseId);
    public Result createTableEntity(int databaseId, int buildId);
    public Result createTableEntityMetric(int databaseId, int buildId);
    public Result createTableEntityRelationship(int databaseId, int buildId);
}

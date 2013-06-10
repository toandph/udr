package architectgroup.fact.dao;

import architectgroup.fact.Context;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/3/12
 * Time: 2:09 AM
 */

public interface DaoFactory {
    /**
     * The Context include the parameter to connect to database
     * @return Context object
     */
    public Context getContext();

    /**
     * This DAO manipulate the SQL for Build
     * @param projectId the ID of the project
     * @return the BuildAO object
     */
    public BuildDao getBuildDao(int projectId);

    /**
     * This DAO manipulate the SQL for IssueSignature
     * @param projectId the ID of the project
     * @return the IssueSignatureDAO object
     */
    public IssueSignatureDao getIssueSignatureDao(int projectId);

    /**
     * This DAO manipulate the SQL for Build
     * @param projectId the ID of the project
     * @param buildId the ID of the build
     * @return the TraceDAO object
     */
    public TraceDao getTraceDao(int projectId, int buildId);

    /**
     * IssueDAO will manipulate the SQL related with the Issue
     * @param projectId the ID of the project
     * @param buildId the ID of the build
     * @return the IssueDAO object
     */
    public IssueDao getIssueDao(int projectId, int buildId);

    /**
     * Manipulate the SQL related with the History
     * @param projectId the ID of the project
     * @param buildId the ID of the build
     * @return the HistoryDAO object
     */
    public HistoryDao getHistoryDao(int projectId, int buildId);

    /**
     * FilterDAO will manipulate the SQL related with the FilterDAO
     * @param projectId the ID of the project
     * @return the FilterDAO object
     */
    public FilterDao getFilterDao(int projectId);

    /**
     * EntityDao will manipulate the SQL related with the Entity
     * @param projectId the ID of the project
     * @param buildId the ID of the build
     * @return the EntityDao object
     */
    public EntityDao getEntityDao(int projectId, int buildId);

    /**
     * Get The User DAO that support the SQL Manipulation.
     * This DAO will create the object in database level, like create table, create database ...
     * @return the DatabaseDao object
     */
    public DatabaseDao getDatabaseDao();

    /**
     * Get The User DAO that support the SQL Manipulation for Project
     * @return the ProjectDAO object
     */
    public ProjectDao getProjectDao();

    /**
     * Get The User DAO that support the SQL Manipulation for User
     * @return the UserDAO object
     */
    public UserDao getUserDao();
}

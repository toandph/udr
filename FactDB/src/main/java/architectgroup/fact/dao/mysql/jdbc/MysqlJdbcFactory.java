package architectgroup.fact.dao.mysql.jdbc;

import architectgroup.fact.Const;
import architectgroup.fact.Context;
import architectgroup.fact.dao.*;
import architectgroup.fact.dao.mysql.jdbc.implement.*;
import org.apache.commons.dbcp.*;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.sql.Connection;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/3/12
 * Time: 2:28 AM
 */

public class MysqlJdbcFactory implements DaoFactory {
    private static final Logger _logger = Logger.getLogger(MysqlJdbcFactory.class);
    private static DataSource _dataSource;
    private static Context _ctx;
    private FileOutputStream _logOutputStream;

    private ProjectDaoImpl _projectDao;
    private IssueDaoImpl _issueDao;
    private TraceDaoImpl _traceDao;
    private DatabaseDaoImpl _databaseDao;
    private HistoryDaoImpl _historyDao;
    private EntityDaoImpl _entityDao;
    private UserDao _userDao;

    public MysqlJdbcFactory(Context ctx) {
        _ctx = ctx;
        _initializeDao();
    }

    /**
     * Initialize all DAO
     */
    private void _initializeDao() {
        _databaseDao = new DatabaseDaoImpl(_ctx);
        _projectDao = new ProjectDaoImpl();
        _userDao = new UserDaoImpl();
    }

    private static DataSource _getDataSource() {
        if (_dataSource == null) {
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName(Const.MYSQL_JDBC_DRIVER_CLASS);
            ds.setUsername(_ctx.getUsername());
            ds.setPassword(_ctx.getPassword());
            ds.setUrl(_ctx.getUrl());
            ds.setConnectionProperties("characterEncoding=UTF-8;rewriteBatchedStatements=true");
            ds.setMaxActive(32);
            ds.setMaxIdle(16);
            ds.setInitialSize(16);

            ObjectPool connectionPool = new GenericObjectPool(null);
            ConnectionFactory connectionFactory = new DataSourceConnectionFactory(ds);
            PoolableObjectFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);

            _dataSource = new PoolingDataSource(connectionPool);
        }
        return _dataSource;
    }

    public static Connection getConnection() throws Exception {
        return _getDataSource().getConnection();
    }

    public DatabaseDao getDatabaseDao() {
        return _databaseDao;
    }

    public ProjectDao getProjectDao() {
        return _projectDao;
    }

    public BuildDao getBuildDao(int projectId) {
        BuildDao buildDao = new BuildDaoImpl(projectId);
        return buildDao;
    }

    public IssueSignatureDao getIssueSignatureDao(int projectId){
        IssueSignatureDao issueSignatureDao = new IssueSignatureDaoImpl(projectId);
        return issueSignatureDao;
    }

    public TraceDao getTraceDao(int projectId, int buildId) {
         _traceDao = new TraceDaoImpl(projectId, buildId);
        return _traceDao;
    }

    public IssueDao getIssueDao(int projectId, int buildId) {
        _issueDao = new IssueDaoImpl(projectId, buildId);
        return _issueDao;
    }

    public HistoryDao getHistoryDao(int projectId, int buildId) {
        _historyDao = new HistoryDaoImpl(projectId, buildId);
        return _historyDao;
    }

    public EntityDao getEntityDao(int projectId, int buildId) {
        _entityDao = new EntityDaoImpl(projectId, buildId);
        return _entityDao;
    }

    public FilterDao getFilterDao(int projectId){
        FilterDao filterDao = new FilterDaoImpl(projectId);
        return filterDao;
    }

    public UserDao getUserDao() {
        return _userDao;
    }

    public Context getContext() {
        return _ctx;
    }
}


package architectgroup.fact.dao.mysql.jdbc;

import architectgroup.fact.Const;
import org.apache.log4j.Logger;

import java.sql.Connection;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/5/12
 * Time: 2:00 AM
 */
public class BaseDao {
    private static final Logger _logger = Logger.getLogger(BaseDao.class);

    public BaseDao() {

    }

    /**
     * This is the get connection for all of the DAO
     * @return the Connection, it can be null in case of error
     */
    public Connection getConnection() {
        Connection conn;
        try {
            return MysqlJdbcFactory.getConnection();
        } catch (Exception err) {
            conn = null;
            _logger.error("Connection is null caused by: ");

            err.printStackTrace();
        }

        _logger.debug("get Connection done! no error found");
        return conn;
    }

    /**
     * Get the databaseName in string
     * @param id the project Id or database Id
     * @return the databaseName has the format project_%d
     */
    public String getDatabaseName(int id) {
        String databaseName = String.format("%s%d", Const.PRJ_PREFIX, id);
        _logger.debug(String.format("We get the database name: %s", databaseName));
        return databaseName;
    }
}

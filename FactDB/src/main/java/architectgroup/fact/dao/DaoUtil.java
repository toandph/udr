package architectgroup.fact.dao;

import architectgroup.fact.Const;
import architectgroup.fact.Context;
import architectgroup.fact.dao.mysql.jdbc.*;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/9/12
 * Time: 2:10 AM
 */
public class DaoUtil {
    private static DaoFactory instance;

     public static DaoFactory getInstance(Context ctx) {
        switch (ctx.getDbType()) {
            case Const.MYSQL_JDBC_DATABASE:
                    if (instance == null) {
                        instance = new MysqlJdbcFactory(ctx);
                    }
                    break;
            default:
                    instance = null;
        }
        return instance;
    }

    public static int getBuildNumberByString(String s) {
        if (s.startsWith(Const.ISSUE_BUILD_PREFIX)) {
            String number = s.substring(Const.ISSUE_BUILD_PREFIX.length());

            try {
                int id = Integer.parseInt(number);
                return id;
            } catch (NumberFormatException ex) {
            }
        }
        return -1;
    }
}

package architectgroup.fact;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/3/12
 * Time: 2:15 AM
 */
public class Const {
    public static final int MYSQL_JDBC_DATABASE = 1;
    public static final int MYSQL_HIBERNATE_DATABASE = 2;
    public static final int MYSQL_IBATIS_DATABASE = 3;
    public static final int ORACLE_DATABASE = 4;
    public static final int VERSANT_DATABASE = 5;

    public static final String logFileName = "log.txt";
    @NotNull
    public static String MYSQL_JDBC_DRIVER_CLASS = "com.mysql.jdbc.Driver";

    public static final String PRJ_PREFIX = "project_";
    public static final String ENTITY_BUILD_PREFIX = "entity_build_";
    public static final String ENTITY_METRICS_PREFIX = "entity_metrics_build_";
    public static final String ENTITY_RELATIONSHIP_PREFIX = "entity_relationship_build_";

    public static final String ISSUE_BUILD_PREFIX = "issue_build_";
    public static final String TRACE_BUILD_PREFIX = "trace_build_";
    public static final String HISTORY_PREFIX = "history_build_";
    public static final String TABLE_PROJECT_NAME = "projects";
    public static final String TABLE_METRICS = "metrics";
    public static final String TABLE_USER_NAME = "users";
    public static final String TABLE_BUILD_NAME = "build";
    public static final String TABLE_ISSUE_SIGNATURE_NAME = "issue_signature";
    public static final String TABLE_FILTER_NAME = "filter";
    public static final String METADATA_DATABASE_NAME = "metadata";
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static final int MYSQL_WRITE_BUFFER = 50000;
}

package architectgroup.fact.dao.mysql.jdbc;

import org.apache.log4j.Logger;
import architectgroup.fact.DebugMessage;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/3/12
 * Time: 3:39 AM
 */
public class MysqlDbStatementImagixBuilder {
    private static final Logger logger = Logger.getLogger(MysqlDbStatementBuilder.class);

    private final static String DATABASE_NAME_TOKEN = "%DATABASE_NAME%";
    private final static String TABLE_NAME_TOKEN = "%TABLE_NAME%";
    private final static String BUILD_ID_TOKEN = "%BUILD_ID%";

    public static String createTableEntity(String databaseName, @NotNull Integer buildId) {
        String sql = "";

        sql += "CREATE  TABLE `%DATABASE_NAME%`.`entity_build_%BUILD_ID%` (\n" +
                "  `id` VARCHAR(128) NOT NULL ,\n" +
                "  `fullid` VARCHAR(512) NOT NULL ,\n " +
                "  `name` VARCHAR(128) NOT NULL , \n" +
                "  `path` VARCHAR(512) NULL ,\n" +
                "  `owner` VARCHAR(128) NULL ,\n" +
                "  `permission` VARCHAR(45) NULL ,\n" +
                "  `mod_date` VARCHAR(45) NULL ,\n" +
                "  `size` INT NULL ,\n" +
                "  `category` VARCHAR(45) NULL ,\n" +
                "  `base_class` VARCHAR(512) NULL ,\n" +
                "  `scope` VARCHAR(45) NULL ,\n" +
                "  `kind` VARCHAR(45) NULL ,\n" +
                "  `defined` VARCHAR(2500) NULL ,\n" +
                "  `type` VARCHAR(45) NULL ,\n" +
                "  `entity_type` VARCHAR(45) NULL ,\n" +
                "  PRIMARY KEY (`id`) ) ENGINE=MyISAM;";


        sql = sql.replaceAll(DATABASE_NAME_TOKEN, databaseName);
        sql = sql.replaceAll(BUILD_ID_TOKEN, buildId.toString());

        return sql;
    }

    public static String createTableEntityMetrics(String databaseName, @NotNull Integer buildId) {
        String sql = "";
        sql += "CREATE TABLE `%DATABASE_NAME%`.`entity_metrics_build_%BUILD_ID%` (\n" +
                "            `entity_id` varchar(64) NOT NULL,\n" +
                "            `metrics` varchar(100) NOT NULL,\n" +
                "            `value` varchar(100) DEFAULT NULL,\n" +
                "    PRIMARY KEY (`entity_id`,`metrics`) \n" +
                "    ) ENGINE=MyISAM";

        sql = sql.replaceAll(DATABASE_NAME_TOKEN, databaseName);
        sql = sql.replaceAll(BUILD_ID_TOKEN, buildId.toString());
        return sql;
    }

    public static String createTableEntityRelationship(String databaseName, @NotNull Integer buildId) {
        String sql = "";
        sql += "CREATE TABLE `%DATABASE_NAME%`.`entity_relationship_build_%BUILD_ID%` (\n" +
                "  `entity_id` varchar(64) NOT NULL,\n" +
                "  `relation` varchar(45) NOT NULL,\n" +
                "  `entity_id2` varchar(64) NOT NULL,\n" +
                "  `value` varchar(100) DEFAULT NULL, " +
                "  PRIMARY KEY (`entity_id`, `relation`, `entity_id2`) \n" +
                ") ENGINE = MyISAM";

        sql = sql.replaceAll(DATABASE_NAME_TOKEN, databaseName);
        sql = sql.replaceAll(BUILD_ID_TOKEN, buildId.toString());
        return sql;
    }

    public static String createTableMetrics(String databaseName) {
        String sql = "";
        sql += "CREATE TABLE `%DATABASE_NAME%`.`metrics` (\n" +
                "            `name` varchar(128) NOT NULL,\n" +
                "            `type` varchar(256) DEFAULT NULL,\n" +
                "            `fullname` varchar(256) DEFAULT NULL,\n" +
                "            `briefname` varchar(128) DEFAULT NULL,\n" +
                "    PRIMARY KEY (`name`)\n" +
                "    ) ENGINE = MyISAM";

        sql = sql.replaceAll(DATABASE_NAME_TOKEN, databaseName);
        return sql;
    }

    public static String fillTableMetrics(String databaseName) {
        String sql = "";
        sql += "INSERT INTO `%DATABASE_NAME%`.`metrics` SELECT * FROM `metadata`.`metrics`";
        sql = sql.replaceAll(DATABASE_NAME_TOKEN, databaseName);
        return sql;
    }

    public static String createTableRelationship(String databaseName) {
        String sql = "";
        sql += "CREATE TABLE `%DATABASE_NAME%`.`relationship` (\n" +
                "            `name` varchar(45) NOT NULL, \n" +
                "    PRIMARY KEY (`name`)\n" +
                "    ) ENGINE=MyISAM";

        sql = sql.replaceAll(DATABASE_NAME_TOKEN, databaseName);
        return sql;
    }

    public static String fillTableRelationship(String databaseName) {
        String sql = "";
        sql += "INSERT INTO `%DATABASE_NAME%`.`relationship` SELECT * FROM `metadata`.`relationship`";
        sql = sql.replaceAll(DATABASE_NAME_TOKEN, databaseName);
        return sql;
    }

}

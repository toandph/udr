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
public class MysqlDbStatementBuilder {
    private static final Logger logger = Logger.getLogger(MysqlDbStatementBuilder.class);

    private final static String DATABASE_NAME_TOKEN = "%DATABASE_NAME%";
    private final static String TABLE_NAME_TOKEN = "%TABLE_NAME%";
    private final static String BUILD_ID_TOKEN = "%BUILD_ID%";

    public static String createTableIssue(String databaseName, @NotNull Integer buildId) {
        String sql = "";

        sql += "CREATE TABLE `%DATABASE_NAME%`.`issue_build_%BUILD_ID%` (";
          sql += "`id` INT(11) NOT NULL ,";
          sql += "`file` VARCHAR(2048) NOT NULL ,";
          sql += "`method` VARCHAR(512) NOT NULL ,";
          sql += "`line` INT(11) NOT NULL ,";
          sql += "`column` INT(11) NOT NULL ,";
          sql += "`message` TEXT NOT NULL ,";
          sql += "`prefix` VARCHAR(128) NULL DEFAULT NULL ,";
          sql += "`postfix` VARCHAR(128) NULL DEFAULT NULL ,";
          sql += "`code` VARCHAR(256) NULL DEFAULT NULL ,";
          sql += "`severitylevel` TINYINT(4) NOT NULL ,";
          sql += "`citingstatus` VARCHAR(45) NULL DEFAULT NULL ,";
          sql += "`display` VARCHAR(45) NULL DEFAULT NULL ,";
          sql += "`state` VARCHAR(45) NULL DEFAULT NULL ,";
          sql += "PRIMARY KEY (`id`), ";
          sql += "INDEX `signature` (`id` ASC) ,";
          sql += "CONSTRAINT `signature_%BUILD_ID%`";
          sql += "FOREIGN KEY (`id` )";
          sql += "REFERENCES `%DATABASE_NAME%`.`issue_signature` (`id` )";
          sql += "ON DELETE CASCADE ";
          sql += "ON UPDATE NO ACTION )";


        sql = sql.replaceAll(DATABASE_NAME_TOKEN, databaseName);
        sql = sql.replaceAll(BUILD_ID_TOKEN, buildId.toString());

        return sql;
    }

    public static String createTableTrace(String databaseName, @NotNull Integer buildId) {
        String sql = "";
        sql += "CREATE  TABLE `%DATABASE_NAME%`.`trace_build_%BUILD_ID%` (";
          sql += "`id` INT(11) NOT NULL AUTO_INCREMENT,";
          sql += "`file` VARCHAR(2048) NOT NULL ,";
          sql += "`method` VARCHAR(512) NOT NULL ,";
          sql += "`line` INT(11) NOT NULL ,";
          sql += "`text` TEXT NOT NULL ,";
          sql += "`type` VARCHAR(128) NULL DEFAULT NULL ,";
          sql += "`refid` INT(11) NOT NULL ,";
          sql += "`blockid` INT(11) NOT NULL ,";
          sql += "`issueid` INT(11) NOT NULL ,";
          sql += "PRIMARY KEY (`id`) ,";
          sql += "INDEX `FK_trace_issue_%BUILD_ID%` (`issueid` ASC) ,";
          sql += "CONSTRAINT `FK_trace_issue_%BUILD_ID%`";
            sql += "FOREIGN KEY (`issueid` )";
            sql += "REFERENCES `%DATABASE_NAME%`.`issue_build_%BUILD_ID%` (`id` )";
            sql += "ON DELETE NO ACTION ";
            sql += "ON UPDATE NO ACTION)";

        sql = sql.replaceAll(DATABASE_NAME_TOKEN, databaseName);
        sql = sql.replaceAll(BUILD_ID_TOKEN, buildId.toString());

        return sql;
    }

    public static String createTableHistory(String databaseName, @NotNull Integer buildId) {
        String sql = "";
        sql += "CREATE  TABLE `%DATABASE_NAME%`.`history_build_%BUILD_ID%` (";
        sql += "`id` INT(11) NOT NULL AUTO_INCREMENT,";
        sql += "`time` DATETIME NOT NULL ,";
        sql += "`user` VARCHAR(50) NOT NULL ,";
        sql += "`state` VARCHAR(128) NOT NULL ,";
        sql += "`comment` TEXT NOT NULL ,";
        sql += "`issueid` INT(11) NOT NULL ,";
        sql += "PRIMARY KEY (`id`) ,";
        sql += "INDEX `FK_history_issue_%BUILD_ID%` (`issueid` ASC) ,";
        sql += "CONSTRAINT `FK_history_issue_%BUILD_ID%`";
        sql += "FOREIGN KEY (`issueid` )";
        sql += "REFERENCES `%DATABASE_NAME%`.`issue_build_%BUILD_ID%` (`id` )";
        sql += "ON DELETE NO ACTION ";
        sql += "ON UPDATE NO ACTION)";

        sql = sql.replaceAll(DATABASE_NAME_TOKEN, databaseName);
        sql = sql.replaceAll(BUILD_ID_TOKEN, buildId.toString());

        return sql;
    }

    public static String createDatabaseStatement(String databaseName) {
        String sql = "";
        sql += "CREATE SCHEMA `%DATABASE_NAME%` DEFAULT CHARACTER SET utf8 ;";
        sql = sql.replaceAll(DATABASE_NAME_TOKEN,databaseName);

        return sql;
    }

    public static String dropDatabaseStatement(String databaseName) {
        String sql = "";
        sql += "DROP DATABASE `%DATABASE_NAME%`;";
        sql = sql.replaceAll(DATABASE_NAME_TOKEN,databaseName);

        return sql;
    }

    public static String createTableBuild(String databaseName) {
        String sql = "";

        sql += "CREATE  TABLE `%DATABASE_NAME%`.`build` (";
          sql += "`id` INT(11) NOT NULL ,";
          sql += "`name` VARCHAR(256) NOT NULL ,";
          sql += "`description` VARCHAR(512) NULL , ";
          sql += "`start_time` DATETIME NOT NULL ,";
          sql += "`end_time` DATETIME NOT NULL ,";
          sql += "`status` VARCHAR(128) NOT NULL ,";
          sql += "`version` VARCHAR(256) NOT NULL ,";
          sql += "`type` VARCHAR(256) NOT NULL ,";
          sql += "`size` INT(11) NULL DEFAULT '0',";
          sql += "`new` INT(11) NULL DEFAULT '0',";
          sql += "`existing` INT(11) NULL DEFAULT '0', ";
          sql += "`fixed` INT(11) NULL DEFAULT '0', ";
          sql += "`createdby` VARCHAR(128) NOT NULL, ";
          sql += "PRIMARY KEY (`id`) ) ";

        sql = sql.replaceAll(DATABASE_NAME_TOKEN,databaseName);
        return sql;
    }

    public static String createTableIssueSignature(String databaseName) {
        String sql = "";

        sql += "CREATE  TABLE `%DATABASE_NAME%`.`issue_signature` (";
        sql += "`id` INT(11) NOT NULL ,";
        sql += "`tracesignature` VARCHAR(45) NOT NULL ,  ";
        sql += "`trend` VARCHAR(1000) NULL DEFAULT NULL , ";
        sql += "PRIMARY KEY (`id`) , " +
               " UNIQUE INDEX `tracesignature_UNIQUE` (`tracesignature` ASC) ) ";

        sql = sql.replaceAll(DATABASE_NAME_TOKEN,databaseName);

        return sql;
    }

    public static String createTableFilter(String databaseName) {
        String sql = "";

        sql += "CREATE  TABLE `%DATABASE_NAME%`.`filter` (";
        sql += "`id` INT(11) NOT NULL AUTO_INCREMENT ,";
        sql += "`name` VARCHAR(256) NOT NULL ,";
        sql += "`build` VARCHAR(128) NOT NULL , ";
        sql += "`value` VARCHAR(1024) NOT NULL ,  ";
        sql += "`state` VARCHAR(256) NULL, ";
        sql += "`scope` VARCHAR(512) NULL, ";
        sql += "PRIMARY KEY (`id`) ) ";

        sql = sql.replaceAll(DATABASE_NAME_TOKEN,databaseName);
        return sql;
    }

    public static String dropTable(String databaseName, String tableName) {
        String sql = "";
        sql += "DROP TABLE `%DATABASE_NAME%`.`%TABLE_NAME%`";

        sql = sql.replaceAll(DATABASE_NAME_TOKEN,databaseName);
        sql = sql.replaceAll(TABLE_NAME_TOKEN,tableName);

        return sql;
    }

    public static String createTriggerOnHistoryInsert(String databaseName, @NotNull Integer buildId) {
        String sql = "";
        sql += "CREATE TRIGGER `%DATABASE_NAME%`.`trigger_history_%BUILD_ID%` AFTER INSERT ON history_build_%BUILD_ID% ";
        sql += "FOR EACH ROW ";
        sql += "UPDATE issue_build_%BUILD_ID% ";
        sql += "SET citingstatus = NEW.state ";
        sql += "WHERE id = NEW.issueid ";
        sql = sql.replaceAll(DATABASE_NAME_TOKEN,databaseName);
        sql = sql.replaceAll(BUILD_ID_TOKEN, buildId.toString());
        return sql;
    }

    public static String createTriggerOnHistoryDelete(String databaseName, @NotNull Integer buildId) {
        String sql = "CREATE TRIGGER `%DATABASE_NAME%`.`trigger_delete_history_%BUILD_ID%` AFTER DELETE ON history_build_%BUILD_ID%  \n" +
                "FOR EACH ROW \n" +
                "begin  \n" +
                "    SET @state = (SELECT state FROM `%DATABASE_NAME%`.`history_build_%BUILD_ID%` WHERE id = (SELECT max(id) FROM `%DATABASE_NAME%`.`history_build_%BUILD_ID%` WHERE issueid = OLD.issueid AND id != OLD.id));\n" +
                "    UPDATE `%DATABASE_NAME%`.`issue_build_%BUILD_ID%` SET \n" +
                "        citingstatus = IFNULL(@state, 'Analyze') \n" +
                "    WHERE id = OLD.issueid;\n" +
                "end";
        sql = sql.replaceAll(DATABASE_NAME_TOKEN,databaseName);
        sql = sql.replaceAll(BUILD_ID_TOKEN, buildId.toString());
        return sql;
    }

    public static String createTriggerOnIssueSignatureUpdate(String databaseName) {
        String sql = "";
        sql += "CREATE TRIGGER `%DATABASE_NAME%`.`updateTrendTrigger` BEFORE UPDATE ON `%DATABASE_NAME%`.`issue_signature`\n" +
                "  FOR EACH ROW BEGIN\n" +
                "    IF IS_ZERO(NEW.trend) THEN\n" +
                "        DELETE FROM `%DATABASE_NAME%`.`issue_signature` WHERE id = NEW.id;                    \n" +
                "    END IF;    \n" +
                "  END;";
        sql = sql.replaceAll(DATABASE_NAME_TOKEN,databaseName);
        return sql;
    }

    public static String createProcedureRemoveTrend(String databaseName) {
        String sql = "";
        sql += "CREATE PROCEDURE `%DATABASE_NAME%`.`RemoveTrend` (IN bList VARCHAR(1024))\n" +
                "BEGIN         \n" +
                "    UPDATE `%DATABASE_NAME%`.`issue_signature`\n" +
                "    SET trend = `%DATABASE_NAME%`.`REMOVE_BUILD`(trend, bList);\n" +
                "    DELETE FROM `%DATABASE_NAME%`.`issue_signature` WHERE `%DATABASE_NAME%`.`IS_ZERO`(trend) = TRUE; \n" +
                "END";
        sql = sql.replaceAll(DATABASE_NAME_TOKEN, databaseName);
        return sql;
    }

    public static String createFunctionSplit(String databaseName) {
        String sql = "";
        sql += "CREATE FUNCTION `%DATABASE_NAME%`.`strSplit`(x varchar(255), delim varchar(12), pos int) returns varchar(255)\n" +
                "    return replace(substring(substring_index(x, delim, pos), length(substring_index(x, delim, pos - 1)) + 1), delim, '');";
        sql = sql.replaceAll(DATABASE_NAME_TOKEN, databaseName);
        return sql;
    }

    public static String createFunctionGetState(String databaseName) {
        String sql = "";
        sql += "CREATE FUNCTION `%DATABASE_NAME%`.`GET_STATE`(trend VARCHAR(1024), bOrder INT)\n" +
                "  RETURNS VARCHAR(100)\n" +
                "  BEGIN\n" +
                "    IF strSplit(trend, '#', 1) <> '' THEN    \n" +
                "        IF strSplit(trend, '#', 2) = '' THEN\n" +
                "            IF bOrder = 1 AND strSplit(trend, '#', 1) = '1' THEN\n" +
                "                RETURN 'NEW';\n" +
                "            ELSEIF bOrder = 1 THEN\n" +
                "                RETURN 'UNKNOWN';\n" +
                "            END IF;\n" +
                "        ELSE\n" +
                "            IF bOrder = 1 AND strSplit(trend, '#', 1) = '1' THEN\n" +
                "                RETURN 'NEW';\n" +
                "            ELSEIF bOrder = 1 THEN\n" +
                "                RETURN 'UNKNOWN';\n" +
                "            END IF;\n" +
                "            \n" +
                "            IF strSplit(trend, '#', bOrder) = '1' THEN\n" +
                "                IF strSplit(trend, '#', bOrder-1) = '1' THEN\n" +
                "                    RETURN 'EXISTING';\n" +
                "                END IF;   \n" +
                "                \n" +
                "                SET @i = bOrder-1;\n" +
                "                find: LOOP\n" +
                "                    IF strSplit(trend, '#', @i) = '1' THEN\n" +
                "                        RETURN 'REOCCURED';\n" +
                "                    END IF;\n" +
                "                    SET @i = @i - 1;\n" +
                "                    IF (@i = 0) THEN\n" +
                "                        LEAVE find;\n" +
                "                    END IF;\n" +
                "                END LOOP find;                     \n" +
                "                \n" +
                "                RETURN 'NEW';\n" +
                "            ELSE \n" +
                "                SET @i = bOrder-1;\n" +
                "                find: LOOP\n" +
                "                    IF strSplit(trend, '#', @i) = '1' THEN\n" +
                "                        RETURN 'FIXED';\n" +
                "                    END IF;\n" +
                "                    SET @i = @i - 1;\n" +
                "                    IF (@i = 0) THEN\n" +
                "                        LEAVE find;\n" +
                "                    END IF;\n" +
                "                END LOOP find;                                  \n" +
                "            END IF;\n" +
                "        END IF;\n" +
                "    END IF;\n" +
                "    \n" +
                "    RETURN 'UNKNOWN';\n" +
                "  END";
        sql = sql.replaceAll(DATABASE_NAME_TOKEN,databaseName);
        return sql;
    }

    public static String createFunctionIsZero(String databaseName) {
        String sql = "";
        sql += "CREATE FUNCTION `%DATABASE_NAME%`.`IS_ZERO`(trend VARCHAR(1024)) RETURNS BOOLEAN\n" +
                "  BEGIN\n" +
                "    IF strSplit(trend, '#', 1) = '' THEN\n" +
                "        RETURN TRUE;\n" +
                "    ELSEIF strSplit(trend, '#', 2) = '' THEN\n" +
                "        IF strSplit(trend, '#', 1) = '0' THEN\n" +
                "            RETURN TRUE;\n" +
                "        ELSE \n" +
                "            RETURN FALSE;\n" +
                "        END IF;        \n" +
                "    ELSE\n" +
                "        SET @i = 1;\n" +
                "        SET @s = strSplit(trend, '#', @i);\n" +
                "        find: LOOP\n" +
                "            IF @s = '1' THEN\n" +
                "                RETURN FALSE;\n" +
                "            END IF;\n" +
                "            SET @i = @i + 1;\n" +
                "            SET @s = strSplit(trend, '#', @i);\n" +
                "            IF @s = '' THEN\n" +
                "                LEAVE find;\n" +
                "            END IF;\n" +
                "        END LOOP find;           \n" +
                "    END IF;\n" +
                "    RETURN TRUE;\n" +
                "  END";
        sql = sql.replaceAll(DATABASE_NAME_TOKEN,databaseName);
        return sql;
    }

    public static String createFunctionRemoveBuild(String databaseName) {
        String sql = "";
        sql += "CREATE FUNCTION `%DATABASE_NAME%`.`REMOVE_BUILD`(trend VARCHAR(1024), bList VARCHAR(1024)) RETURNS VARCHAR(1024)\n" +
                "  BEGIN\n" +
                "    SET @i = 1;\n" +
                "    SET @s = strSplit(trend, '#', @i);\n" +
                "    SET @result = '';\n" +
                "    find: LOOP\n" +
                "        IF @s = '' THEN\n" +
                "            LEAVE find;\n" +
                "        END IF;\n" +
                "        \n" +
                "        SET @i2 = 1;\n" +
                "        SET @s2 = strSplit(bList, '#', @i2);\n" +
                "        SET @flag = true;\n" +
                "        find2: LOOP\n" +
                "            IF @s2 = '' THEN LEAVE find2; END IF;\n" +
                "            SET @delId = CONVERT(@s2, UNSIGNED INTEGER);\n" +
                "            IF @i = @delId THEN\n" +
                "                SET @flag = false;\n" +
                "                LEAVE find2;\n" +
                "            END IF;\n" +
                "            \n" +
                "            SET @i2 = @i2 + 1;\n" +
                "            SET @s2 = strSplit(bList, '#', @i2); \n" +
                "        END LOOP find2;\n" +
                "        \n" +
                "        IF @flag = true THEN\n" +
                "            IF @result = '' THEN\n" +
                "                SET @result = CONCAT(@result, @s);\n" +
                "            ELSE\n" +
                "                SET @result = CONCAT(@result, '#', @s);\n" +
                "            END IF;                \n" +
                "        END IF;\n" +
                "        \n" +
                "        SET @i = @i + 1;\n" +
                "        SET @s = strSplit(trend, '#', @i);\n" +
                "    END LOOP find;\n" +
                "    \n" +
                "    RETURN @result;\n" +
                "  END";
        sql = sql.replaceAll(DATABASE_NAME_TOKEN,databaseName);
        return sql;
    }
}

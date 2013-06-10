package architectgroup.fact;

import java.util.List;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/5/12
 * Time: 1:32 PM
 */
public class DebugMessage {
    public final static String DROPPED_DATABASE_SUCCESSFULLY = "Dropped Database Successfully: ";
    public final static String DROPPED_TABLE_SUCCESSFULLY = "Dropped Table Successfully: ";

    public final static String CREATED_DATABASE_SUCCESSFULLY = "Created Database Successfully: ";
    public final static String CREATED_TABLE_SUCCESSFULLY = "Created Table Successfully: ";
    public final static String SWITCHED_DATABASE_TO = "Switched Database to: ";
    public final static String DATABASE_EXISTS = "Database Exists: ";
    public final static String DATABASE_NOT_EXISTS = "Database NOT Exists: ";
    public final static String DATABASE_LISTS = "Database Lists: ";

    public final static String ERROR_SQL_EXCEPTION = "[SQL Exception] Occur: ";
    public final static String ERROR_NUMBER_FORMAT = "[Number Exception] Wrong Format Number";
    public final static String CLASS_NOT_FOUND_EXCEPTION = "[Class NOT FOUND Exception] Occur: ";


    public final static String CREATE_TABLE_PROJECT_SQL = "[SQL] Project Creating Statement : ";
    public final static String CREATE_TABLE_ISSUE_SQL = "[SQL] Bug Creating Statement : ";
    public final static String CREATE_TABLE_ISSUE_SIGNATURE_SQL = "[SQL] Bug Signature Creating Statement : ";
    public final static String CREATE_TABLE_FILTER_SQL = "[SQL] Filter Creating Statement : ";
    public final static String CREATE_TABLE_TRACE_SQL = "[SQL] Trace Creating Statement : ";
    public final static String CREATE_TABLE_BUILD_SQL = "[SQL] Build Creating Statement : ";
    public final static String CREATE_TABLE_BUILDINFO_SQL = "[SQL] BuildInfo Creating Statement : ";
    public final static String CREATE_DATABASE_SQL = "[SQL] Creating Database Statement : ";
    public final static String DROP_DATABASE_SQL = "[SQL] Dropping Database Statement : ";
    public final static String DROP_TABLE_SQL = "[SQL] Dropping Table Statement: ";
    public final static String CAN_NOT_FIND_LOG_FILE = "Can not find log file";
    public final static String BUILDIDS_LIST = "This is the Build List : ";
    public final static String NEW_BUILD_IDS = "New Build ID for Database : ";

    public final static String SEPARATOR = " | ";

    public static void printList(@NotNull Logger logger, @NotNull List<String> list) {
        String message = DATABASE_LISTS;
        for (String s : list) {
            message += s + SEPARATOR;
        }
        logger.debug(message);
    }
}


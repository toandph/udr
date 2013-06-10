package architectgroup.fact.util;

import architectgroup.fact.DebugMessage;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/5/12
 * Time: 3:18 PM
 */
public class LogHelper {
    private static final Logger _logger = Logger.getLogger(LogHelper.class);
    public static void writeToLog(String file, @NotNull Exception err) {
         try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(file, true));
            err.printStackTrace(pw);
            pw.close();
         } catch (FileNotFoundException ex) {
            _logger.info(DebugMessage.CAN_NOT_FIND_LOG_FILE);
         }
    }
}

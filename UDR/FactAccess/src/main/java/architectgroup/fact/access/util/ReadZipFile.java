package architectgroup.fact.access.util;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/18/13
 * Time: 10:46 AM
 */
public class ReadZipFile {
    /**
     * Retrieve the text from the zip file
     * @param file
     * @param fileZip
     * @return the content of the text file
     */
    @Nullable
    public static String getTextFromFile(String file, String fileZip) {
        String result = "";
        int temp = 0;
        BufferedReader br = null;
        InputStream input = null;
        try {
            final ZipFile zipFile = new ZipFile(fileZip);
            final Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry zipEntry = entries.nextElement();
                if (!zipEntry.isDirectory()) {
                    final String fileName = zipEntry.getName();
                    if (checkMatch(file, fileName)) {
                        input = zipFile.getInputStream(zipEntry);
                        br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                        char[] cbuf = new char[16384];
                        int curLen = 0;
                        while ((curLen = (br.read(cbuf))) > 0) {
                            result += String.valueOf(cbuf, 0, curLen);
                        }
                        br.close();
                        input.close();
                        temp++;
                        break;
                    }
                }
            }
            zipFile.close();
        }
        catch (IOException ioe) {
            System.err.println("Unhandled exception:");
            ioe.printStackTrace();
            temp = 0;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception err) {
                err.printStackTrace();
            }

            try {
                if (br != null) {
                    input.close();
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

        return (temp == 0 ? null : result);
    }

    /**
     *
     * @param file
     * @param zipFile
     * @return
     */
    public static boolean checkMatch(String file, String zipFile) {
        boolean tmp = false;
        file = file.replace('\\','/');
        file = CommonFunction.encodeFilePath(file);
        zipFile = zipFile.replace('\\','/');
        zipFile = CommonFunction.encodeFilePath(zipFile);

        /*
        if (file.charAt(0) == '/')
            file = file.substring(1);
        if (zipFile.charAt(0) == '/')
            zipFile = zipFile.substring(1);
          */
        // Just need to equal the end is enough
        if (file.equalsIgnoreCase(zipFile)) {
            tmp = true;
        } else if (file.endsWith(zipFile)) {
            tmp = true;
        }

        return tmp;
    }
}

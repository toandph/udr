package architectgroup.parser.xmlreport;

import architectgroup.fact.dto.IssueDto;
import architectgroup.fact.dto.TraceDto;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ReportHelper {
    @Nullable
    private static ReportHelper _instance = null;

    public static final int URL_TYPE = 2;
    public static final int FILE_TYPE = 1;
    public static final int STREAM_TYPE = 3;

    private ReportHelper() {

    }

    @Nullable
    public static synchronized ReportHelper getInstance() {
        if (_instance == null) {
            _instance = new ReportHelper();
        }
        return _instance;
    }

    @Nullable
     public Document getRootDocument(String xmlSource, InputStream is, int type) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = null;

            switch (type) {
                case FILE_TYPE:
                    doc = docBuilder.parse(new File(xmlSource));
                    break;
                case URL_TYPE:
                    doc = docBuilder.parse(new URL(xmlSource).openStream());
                    break;
                case STREAM_TYPE:
                    doc = docBuilder.parse(is);
                    break;
            }

            if (doc != null) {
                doc.getDocumentElement().normalize(); // normalize text representation
                return doc;
            }
        }
        catch (Exception err) {
            err.printStackTrace();
        }

        return null;
    }

    @NotNull
    public static String convertToHex(@NotNull byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }

    @NotNull
    public static String md5(@NotNull String text) {
        MessageDigest md;
        byte[] md5hash = new byte[32];
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            md5hash = md.digest();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return convertToHex(md5hash);
    }
}

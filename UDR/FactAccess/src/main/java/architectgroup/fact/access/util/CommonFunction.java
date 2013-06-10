package architectgroup.fact.access.util;

import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: toandph
 * Date: 3/24/13
 * Time: 1:25 AM
 */
public class CommonFunction {

    /**
     * Method to join array elements of type string
     * @param inputArray Array which contains strings
     * @param glueString String between each array element
     * @return String containing all array elements separated by glue string
     */
    public static String implodeArray(@Nullable List<String> inputArray, String glueString) {
        /** Output variable */
        String output = "";
        if (inputArray != null) {
            if (inputArray.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(inputArray.get(0));

                for (int i=1; i<inputArray.size(); i++) {
                    sb.append(glueString);
                    sb.append(inputArray.get(i));
                }

                output = sb.toString();
            }
        }
        return output;
    }

    public static String implodeArrayInt(@Nullable List<Integer> inputArray, String glueString) {
        /** Output variable */
        String output = "";
        if (inputArray != null) {
            if (inputArray.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(inputArray.get(0));

                for (int i=1; i<inputArray.size(); i++) {
                    sb.append(glueString);
                    sb.append(inputArray.get(i));
                }

                output = sb.toString();
            }
        }
        return output;
    }

    /**
     * Check a string is a number or not
     * @param str number in string
     * @return true or false
     */
    public static boolean isNumeric(String str)
    {
        boolean flag = true;
        try {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe) {
            flag = false;
        }

        return flag;
    }

    /**
     * Get the MAC Address of the Host
     * @return the mac address of the host.
     */
    public static String getHostAddress() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                System.out.println("Change address to first network interface");
                Enumeration<NetworkInterface> t = NetworkInterface.getNetworkInterfaces();
                while (t.hasMoreElements()) {
                    NetworkInterface net = t.nextElement();
                    System.out.println("Change to: " + net.getDisplayName());
                    if (!net.isLoopback()) {
                        network = net;
                        break;
                    }
                }
            }

            if (network != null) {
                byte[] mac = network.getHardwareAddress();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                return sb.toString();
            } else {
                return "Can not find Network Address";
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e){
            e.printStackTrace();
        }

        return "Unknown Host";
    }

    /**
     * Zip also the full path. So the full path must have the local disk like E:/ , in linux it has the / root.
     * So we create this function to encode the path and decode the path
     * E:/ will change to udr(E) and vice versa
     * / will change to udr(root) and vice versa
     */
    public static String encodeFilePath(String path) {
        // Make sure all path is with convention /
        path = path.replaceAll("\\\\","/");
        // Get the first part of the file
        String parent = path;
        if (path.contains("/")) {
            parent = path.substring(0, path.indexOf("/"));
        }

        // In case the parent has nothing means, it is linux so we convert parent to udr(root)
        if (parent.length() == 0) {
            parent = "udr(root)";
        } else if (parent.contains(":")) {   // It is windows local disk, so we convert it to udr(...)
            parent = "udr(" + parent.substring(0, parent.indexOf(":"))+ ")";
        }   // else parent is equal to the normal

        // We add the parent to the root of the path
        String returnEncode = path;
        if (path.contains("/")) {
            returnEncode = parent + path.substring(path.indexOf("/"));
        }

        return returnEncode;
    }

    /**
     * Zip also the full path. So the full path must have the local disk like E:/ , in linux it has the / root.
     * So we create this function to encode the path and decode the path
     * E:/ will change to udr(E) and vice versa
     * / will change to udr(root) and vice versa
     * @param path path of the encoded path. Example: udr(root)/home/...
     * @return normal path
     */
    public static String decodeFilePath(String path) {
        // Make sure all path is with convention /
        path = path.replaceAll("\\\\","/");
        // Get the first part of the file
        String parent = path;
        String realParent = "";
        if (path.contains("/")) {
            parent = path.substring(0, path.indexOf("/"));
        }

        if (parent.equalsIgnoreCase("udr(root)")) {
            realParent = "";
        } else if (parent.startsWith("udr(")) { // Windows local disk can not have 2 more characters. Is it right ?
            realParent = parent.charAt(4) + ":";
        }

        // We add the parent to the root of the path
        String returnDecode = path;
        if (path.contains("/")) {
            returnDecode = realParent + path.substring(path.indexOf("/"));
        }

        return returnDecode;
    }
}

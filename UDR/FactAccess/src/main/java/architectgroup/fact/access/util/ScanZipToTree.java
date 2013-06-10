package architectgroup.fact.access.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ScanZipToTree {
    List<String> fileList;
    @NotNull
    public static String html = "";

    /**
     * Unzip it
     * @param zipFile input zip file
     */
    @NotNull
    public SourceTree scan(String zipFile){
        byte[] buffer = new byte[1024];
        SourceTree root = new SourceTree();
        root.nodeName = "System Model";
        root.isFolder = true;
        root.fullPath = "/";
        root.nodeTree = new ArrayList<SourceTree>();
        try{
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry ze = zis.getNextEntry();
            while(ze != null) {
                String fileName = ze.getName();
                String[] folders = fileName.split("/|\\\\");
                SourceTree tree = root;
                String path = "";
                int i = 1;
                for (String folder : folders) {
                    if (isValid(folder)) {
                        path += folder;
                        SourceTree findNode = find(folder, tree.nodeTree);
                        if (findNode == null) {
                            SourceTree newTree = new SourceTree();
                            newTree.nodeName = folder;
                            if (i < folders.length) {
                                newTree.isFolder = true;
                                path += "\\";
                            } else if (ze.isDirectory()) {
                                newTree.isFolder = true;
                                path += "\\";
                            } else {
                                newTree.isFolder = false;
                            }
                            newTree.fullPath = path;
                            newTree.nodeTree = new ArrayList<SourceTree>();
                            tree.nodeTree.add(newTree);
                            tree = newTree;
                        } else {
                            if (findNode.isFolder) {
                                path += "\\";
                            }
                            tree = findNode;
                        }
                    }
                    i++;
                }
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }

        return root;
    }

    /**
     * find a foler has already in the tree
     * @param folder
     * @param tree
     * @return
     */
    @Nullable
    private SourceTree find(String folder, @NotNull List<SourceTree> tree) {
        for (SourceTree node : tree) {
            if (node.nodeName.equalsIgnoreCase(folder)) {
                return node;
            }
        }
        return null;
    }

    /**
     * check a folder name valid
     * @param folder
     * @return
     */
    private boolean isValid(@Nullable String folder) {
        if (folder != null && folder.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @NotNull
    public static String generateTree(@NotNull SourceTree tree) {
        html = new String("<div id=\"tree\">\n");
        html += "<ul>";
        html += "<li class='noLink' data=\"isFolder:true, addClass: 'ws-wrap', buildPath:''\"><span>System Model</span>";
        generateBlock(tree);
        html += "</li>";
        html += "</ul>";
        html += "  </div>";
        return html;
    }

    public static void generateBlock(@NotNull SourceTree tree) {
        html += "<ul>";
        for (SourceTree st : tree.nodeTree) {
            String boolStr = st.isFolder ? "true" : "false";
            html += "<li class='noLink' data=\"isFolder:" + boolStr + ", addClass: 'ws-wrap', buildPath:'" + st.fullPath + "'\"><span>" + st.nodeName + "</span>";
            generateBlock(st);
            html += "</li>";
        }
        html += "</ul>";
    }

    public static String generateTreeJson(SourceTree tree) {
        return generateBlockJson(tree);
    }

    public static String generateBlockJson(@NotNull SourceTree tree) {
        List<String> objects = new ArrayList<String>();
        for (SourceTree st : tree.nodeTree) {
            String boolStr = st.isFolder ? "true" : "false";
            String children = generateBlockJson(st);
            if (children.length() <= 2) {
                html = "{\"title\":\"" + st.nodeName + "\", \"addClass\":\"ws-wrap\", \"buildPath\":\"" + CommonFunction.decodeFilePath(st.fullPath) + "\"}";
            } else {
                html = "{\"title\":\"" + st.nodeName + "\", \"isFolder\":\"true\", \"addClass\":\"ws-wrap\", \"buildPath\":\"" + CommonFunction.decodeFilePath(st.fullPath) + "\", \"children\":" + children + " }";
            }
            objects.add(html);
        }
        return "[" + CommonFunction.implodeArray(objects, ",") + "]";
    }
}
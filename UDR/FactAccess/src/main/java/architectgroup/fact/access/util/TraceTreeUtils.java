package architectgroup.fact.access.util;

import architectgroup.fact.dto.BuildDto;
import architectgroup.fact.dto.IssueDto;
import architectgroup.fact.dto.ProjectDto;
import architectgroup.fact.dto.TraceDto;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 5/6/12
 * Time: 1:02 PM
 */
public class TraceTreeUtils {
    @NotNull
    private static String html = "";
    private static ProjectDto projectDto;
    private static BuildDto buildDto;
    private static IssueDto issueDto;

    @NotNull
    public static String generateHtmlTree(@NotNull List<TraceDto> traceList, @NotNull ProjectDto project, @NotNull BuildDto build, IssueDto issue) {
        html = new String("<div id=\"tree\">\n");

        html += "<ul>";
        // get all block trace id to create trace tree
        List<String> blockIDs = new ArrayList<String>();
        for (TraceDto traceDto:traceList){
            if (!blockIDs.contains(String.valueOf(traceDto.getBlockid())))
                blockIDs.add(String.valueOf(traceDto.getBlockid()));
        }
        for (TraceDto traceDto:traceList){
            if (blockIDs.contains(String.valueOf(traceDto.getRefid())))
                blockIDs.remove(String.valueOf(traceDto.getRefid()));
        }
        // finish get block id list

        // start create trace tree for each block trace
        for (String id:blockIDs)
            generateBlock(Integer.valueOf(id), traceList, project.getId(), build.getId());

        html += "</ul>";
        html += "  </div>";

        return html;
    }

    @Nullable
    private static List<TraceDto> getBlock(int blockId, @NotNull List<TraceDto> fullList) {
        List<TraceDto> list = null;
        for (TraceDto trace : fullList) {
            if (trace.getBlockid() == blockId) {
                if (list == null) {
                    list = new ArrayList<TraceDto>();
                }
                list.add(trace);
            }
        }
        return list;
    }

    private static void generateBlock(int blockId, @NotNull List<TraceDto> fullList, int projectId, int buildNo) {
        List<TraceDto> list = getBlock(blockId, fullList);
        if (list != null) {
            for (TraceDto trace : list) {
                String fileName = new File(trace.getFile()).getName();
                html += "<li class='noLink' data=\"addClass: 'ws-wrap', file:'"+trace.getFile().replace("\\", "\\\\")+"', line:'"+trace.getLine() +"', project:'" + projectId + "', build:'" + buildNo + "'" + "\"><span>" + fileName + ":" + trace.getLine() + " : " + trace.getText() + "</span>\n";
                if (trace.getRefid() >= 0) {
                    html += "<ul>";
                    generateBlock(trace.getRefid(), fullList, projectId, buildNo);
                    html += "</ul>";
                }
            }
        }
    }
}

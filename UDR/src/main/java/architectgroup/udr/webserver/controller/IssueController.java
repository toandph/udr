package architectgroup.udr.webserver.controller;

import architectgroup.fact.access.*;
import architectgroup.fact.access.util.FactAccessFactory;
import architectgroup.fact.access.util.FilterCondition;
import architectgroup.fact.access.util.ReadZipFile;
import architectgroup.fact.access.util.TraceTreeUtils;
import architectgroup.fact.dto.*;
import architectgroup.fact.util.State;
import architectgroup.udr.webserver.model.SessionModel;
import net.sf.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: AG-VHL-M001
 * Date: 4/26/12
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Scope("request")
public class IssueController {
    @Autowired
    private SessionModel session;
    @Autowired
    private FactAccessFactory factAccess;

    public static String newLine = System.getProperty("line.separator");

    @NotNull
    @RequestMapping(value = "/issue/list")
    public ModelAndView list(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId) {
        ModelAndView modelAndView = new ModelAndView("issue/list");

        String buildIdStr = request.getParameter("buildId");
        int buildId, filterId;
        if (buildIdStr == null || buildIdStr.length() == 0) {
            BuildDto build = new BuildAccess(factAccess).getLastBuild(projectId);
            if (build != null) {
                buildId = build.getId();
            } else {
                buildId = -1;
            }
        } else {
            buildId = Integer.parseInt(buildIdStr);
        }

        if (buildId != -1) {
            String aSearch = request.getParameter("aSearch");
            aSearch = (aSearch == null) ? "" : aSearch;

            String filterIdStr = request.getParameter("filterId");
            if (filterIdStr == null || filterIdStr.length() == 0) {
                filterId = -1;
            } else {
                filterId = Integer.parseInt(filterIdStr);
            }

            BuildDto build = buildId != -1 ? (new BuildAccess(factAccess)).getBuild(projectId, buildId) : null;
            ProjectDto project = (new ProjectAccess(factAccess)).getProject(projectId);
            List<BuildDto> buildList = new BuildAccess(factAccess).getBuildList(projectId);
            List<FilterDto> filterList = buildId != -1 ? new FilterAccess(factAccess).getFilterListOnBuild(projectId, buildId) : null;

            modelAndView.addObject("aSearch", aSearch);
            modelAndView.addObject("buildId", buildId);
            modelAndView.addObject("buildList", buildList);
            modelAndView.addObject("filterId", filterId);
            modelAndView.addObject("filterList", filterList);
            modelAndView.addObject("action","list");

            session.setProject(project);
            session.setBuild(build);
        } else {
            modelAndView = new ModelAndView("issue/nopage");
        }

        modelAndView.addObject("projectId", projectId);
        modelAndView.addObject("controller", "issue");
        return modelAndView;
    }

    @RequestMapping(value = "/issue/list-json")
    @ResponseBody
    public String listJson(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId) {
        String buildIdStr = request.getParameter("buildId");
        int buildId, filterId, sEcho;
        if (buildIdStr == null || buildIdStr.length() == 0) {
            BuildDto build = new BuildAccess(factAccess).getLastBuild(projectId);
            if (build != null) {
                buildId = build.getId();
            } else {
                buildId = -1;
            }
        } else {
            buildId = Integer.parseInt(buildIdStr);
        }

        String filterIdStr = request.getParameter("filterId");
        if (filterIdStr == null || filterIdStr.length() == 0) {
            filterId = -1;
        } else {
            filterId = Integer.parseInt(filterIdStr);
        }

        String sEchoStr = request.getParameter("sEcho");
        if (sEchoStr == null || sEchoStr.length() == 0) {
            sEcho = 1;
        } else {
            sEcho = Integer.parseInt(sEchoStr);
        }

        // Column that sent back to client
        String[] aaColumns = new String[] {"Id", "Code", "State", "SeverityLevel", "Display", "Method", "File", "Message", "CitingStatus"};

        // Paging
        String sLimit = "";
        int iDisplayS = 0;
        int iDisplayL = 0;
        if (request.getParameter("iDisplayStart") != null) {
            String iDisplayStart = request.getParameter("iDisplayStart");
            iDisplayS = Integer.parseInt(iDisplayStart);
        } else {
            iDisplayS = 0;
        }

        if (request.getParameter("iDisplayLength") != null && !request.getParameter("iDisplayLength").equalsIgnoreCase("-1")) {
            String iDisplayLength = request.getParameter("iDisplayLength");
            iDisplayL = Integer.parseInt(iDisplayLength);
        } else {
            iDisplayL = 20;
        }

        sLimit = "LIMIT " + iDisplayS + ", " + iDisplayL;

        // Filtering
        String sWhere = " WHERE ";

        String sSearch = request.getParameter("sSearch");
        if (sSearch != null) {
            // If sSearch is not advance value search
            if (!sSearch.contains(":")) {
                sWhere += " ( ";
                for (int i = 1; i < aaColumns.length; i++) {
                        sWhere += "`" + aaColumns[i] + "` LIKE '%" + sSearch + "%' OR ";
                }
                sWhere = sWhere.substring(0, sWhere.length() - 3);
                sWhere += " ) ";
            } else {    // If sSearch is advance value search
                FilterCondition fc = new FilterCondition(sSearch);
                sWhere += " ( " + fc.toSQL() + " ) ";
            }
        } else {
            sWhere = "";
        }

        if (filterId > 0) {
            if (sWhere.length() > 0) {
                FilterAccess filter = new FilterAccess(factAccess);
                FilterDto filterDto = filter.getFilter(projectId, filterId);
                FilterCondition fc = new FilterCondition(filterDto.getValue());
                sWhere += " AND ( " + fc.toSQL() + " ) ";
            } else {
                sWhere = " WHERE ";
                FilterAccess filter = new FilterAccess(factAccess);
                FilterDto filterDto = filter.getFilter(projectId, filterId);
                FilterCondition fc = new FilterCondition(filterDto.getValue());
                sWhere += " ( " + fc.toSQL() + " ) ";
            }
        }
        if (buildId != -1) {
            IssueAccess issueAccess = new IssueAccess(factAccess);
            int countAll = issueAccess.getNumberOfIssues(projectId, buildId, sWhere);
            List<IssueDto> issueList = issueAccess.getIssues(projectId, buildId, sWhere + " " + sLimit);
            JSONObject resultInJSON = IssueAccess.convertToJSON(issueList, countAll, countAll, sEcho);
            return resultInJSON.toString();
        } else {
            JSONObject resultInJSON = IssueAccess.convertToJSON(new ArrayList<IssueDto>(), 0, 0, sEcho);
            return resultInJSON.toString();
        }

    }

    @NotNull
    @RequestMapping(value = "/issue/detail")
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId, @RequestParam("buildId") int buildId, @RequestParam("issueId") int issueId) {
        ModelAndView modelAndView = new ModelAndView("issue/detail");
        BuildAccess buildAccess = new BuildAccess(factAccess);
        IssueAccess issueAccess = new IssueAccess(factAccess);
        IssueDto issue = issueAccess.getIssueById(projectId, buildId, issueId);
        BuildDto build = buildAccess.getBuild(projectId, buildId);

        String zipFile = factAccess.getFactory().getContext().getBuildUpload() + "/" + projectId + "/" + build.getId() + "/source.zip";
        String fileContent = ReadZipFile.getTextFromFile(issue.getFile(), zipFile);

        ProjectDto project = new ProjectAccess(factAccess).getProject(projectId);
        List<TraceDto> traceList = (new TraceAccess(factAccess)).getTrace(projectId, issueId, buildId);
        issue.setTraceList(traceList);
        String traceTree = TraceTreeUtils.generateHtmlTree(traceList, project, build, issue);
        List<HistoryDto> historyList = (new HistoryAccess(factAccess)).getHistoryByIssueId(projectId, buildId, issueId, 0);

        // Get the file content of the previous build
        if (issue.getState().equalsIgnoreCase(State.FIXED.toString())) {
            BuildDto previousBuild = buildAccess.getPreviousBuild(projectId, buildId);
            if (previousBuild != null) {
                IssueAccess issuePreviousAccess = new IssueAccess(factAccess);
                IssueDto previousIssue = issuePreviousAccess.getIssueById(projectId, previousBuild.getId(), issueId);
                String zipPreviousBuildFile = factAccess.getFactory().getContext().getBuildUpload() + "/" + projectId + "/" + previousBuild.getId() + "/source.zip";
                fileContent = ReadZipFile.getTextFromFile(previousIssue.getFile(), zipPreviousBuildFile);
                traceTree = TraceTreeUtils.generateHtmlTree(traceList, project, previousBuild, previousIssue);
            }
        }

        // Get the role of user here //
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (SimpleGrantedAuthority role : authorities) {
            if (role.getAuthority().equalsIgnoreCase("ROLE_ADMIN")) {
                modelAndView.addObject("allow", true);
            }
        }

        if (fileContent == null) {
            modelAndView.addObject("error", "can-not-find-the-source-file");
            modelAndView.addObject("fileContent", " ");
        } else {
            modelAndView.addObject("error", null);
            modelAndView.addObject("fileContent", fileContent);
        }

        modelAndView.addObject("historyList",historyList);
        modelAndView.addObject("projectId", projectId);
        modelAndView.addObject("build", build);
        modelAndView.addObject("issue", issue);
        modelAndView.addObject("allow", false);
        modelAndView.addObject("traceTree", traceTree);
        modelAndView.addObject("controller","issue");
        modelAndView.addObject("action","detail");
        modelAndView.addObject("previousId", issueAccess.getPreviousIssueId(projectId, buildId, issueId));
        modelAndView.addObject("nextId", issueAccess.getNextIssueId(projectId, buildId, issueId));
        int noOfHistory = (new HistoryAccess(factAccess)).getNumberOfHistory(projectId, buildId, issueId);
        modelAndView.addObject("noOfHistory", noOfHistory);


        session.setProject(project);
        session.setBuild(build);

        return modelAndView;
    }

    @Nullable
    @RequestMapping(value = "/issue/file-json")
    @ResponseBody
    public String fileJson(@RequestParam("projectId") int projectId, @RequestParam("buildId") int buildId, @RequestParam("file") String file) {
        String zipFile = factAccess.getFactory().getContext().getBuildUpload() + "/" + projectId + "/" + buildId + "/source.zip";
        String fileContent = ReadZipFile.getTextFromFile(file, zipFile);
        if (fileContent == null) {
            return "-1";
        }
        return fileContent;
    }

    @NotNull
    @RequestMapping(value = "/issue/help")
    public ModelAndView help(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("issue/help");
        String lan = RequestContextUtils.getLocale(request).getLanguage();

        if (lan.equalsIgnoreCase("en")) {
            modelAndView = new ModelAndView("issue/help");
        } else if (lan.equalsIgnoreCase("ko")) {
            modelAndView = new ModelAndView("issue/help_ko");
        }

        modelAndView.addObject("controller","issue");
        modelAndView.addObject("action","help");

        return modelAndView;
    }

    @RequestMapping(value = "/issue/download")
    @ResponseBody
    public void download(HttpServletResponse response, @RequestParam("type") String type, @RequestParam("projectId") int projectId, @RequestParam("buildId") int buildId, @RequestParam("filterId") int filterId, @RequestParam("sSearch") String sSearch) {
        String[] aaColumns = new String[] {"Id", "Code", "State", "SeverityLevel", "Display", "Method", "File", "Message"};
        // Filtering
        String sWhere = " WHERE ";
        if (sSearch != null) {
            // If sSearch is not advance value search
            if (!sSearch.contains(":")) {
                sWhere += " ( ";
                for (int i = 0; i < aaColumns.length; i++) {
                    sWhere += "`" + aaColumns[i] + "` LIKE '%" + sSearch + "%' OR ";
                }
                sWhere = sWhere.substring(0, sWhere.length() - 3);
                sWhere += " ) ";
            } else {    // If sSearch is advance value search
                FilterCondition fc = new FilterCondition(sSearch);
                sWhere += " ( " + fc.toSQL() + " ) ";
            }
        } else {
            sWhere = "";
        }

        if (filterId > 0) {
            if (sWhere.length() > 0) {
                FilterAccess filter = new FilterAccess(factAccess);
                FilterDto filterDto = filter.getFilter(projectId, filterId);
                FilterCondition fc = new FilterCondition(filterDto.getValue());
                sWhere += " AND ( " + fc.toSQL() + " ) ";
            } else {
                sWhere = " WHERE ";
                FilterAccess filter = new FilterAccess(factAccess);
                FilterDto filterDto = filter.getFilter(projectId, filterId);
                FilterCondition fc = new FilterCondition(filterDto.getValue());
                sWhere += " ( " + fc.toSQL() + " ) ";
            }
        }

        IssueAccess issueAccess = new IssueAccess(factAccess);
        TraceAccess traceAccess = new TraceAccess(factAccess);
        List<IssueDto> issueListAll = issueAccess.getIssues(projectId, buildId, sWhere);
        // Get the trace //
        for (IssueDto issue : issueListAll) {
            issue.setTraceList(traceAccess.getTrace(projectId, issue.getId(), buildId));
        }

        OutputStream out = null;
        try {
            out = response.getOutputStream();
            String p = "";
            if (type.equalsIgnoreCase("csv")) {
                response.setContentType("text/csv;charset=utf-8");
                response.setHeader("Content-Disposition","attachment; filename=data.csv");
                p = toCSV(issueListAll);
            } else if (type.equalsIgnoreCase("xml")) {
                response.setContentType("text/xml;charset=utf-8");
                response.setHeader("Content-Disposition","attachment; filename=data.xml");
                p = toXML(issueListAll);
            } else if (type.equalsIgnoreCase("text")) {
                response.setContentType("text/txt;charset=utf-8");
                response.setHeader("Content-Disposition","attachment; filename=data.txt");
                p = toText(issueListAll);
            } else if (type.equalsIgnoreCase("html")) {
                response.setContentType("text/html;charset=utf-8");
                response.setHeader("Content-Disposition","attachment; filename=data.html");
                p = toHtml(issueListAll);
            }
            out.write(p.getBytes());
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        }
    }

    @NotNull
    private String toCSV(@NotNull List<IssueDto> issuesList) {
        String doc = "";
        for (IssueDto issue : issuesList) {
            doc += issue.getId() + ", " + issue.getFile() + ", " + issue.getLine() + ", " + issue.getColumn() + ", " + issue.getCode() + ", " + issue.getState() + ", " + issue.getSeveritylevel() + ", " + issue.getMethod() + newLine;
        }
        return doc;
    }

    @NotNull
    private String toText(@NotNull List<IssueDto> issuesList) {
        String doc = "";
        for (IssueDto issue : issuesList) {
            doc += issue.getId() + ", " + issue.getFile() + ", " + issue.getLine() + ", " + issue.getColumn() + ", " + issue.getCode() + ", " + issue.getState() + ", " + issue.getSeveritylevel() + ", " + issue.getMethod() + newLine;
        }
        return doc;
    }

    @NotNull
    private String toHtml(List<IssueDto> issuesList) {
        String doc = "<html>" + newLine;
        doc += "<head>" + newLine;
        doc += "</head>" + newLine;
        doc += "<body>" + newLine;
        doc += "<table border='1'>" + newLine;
        doc += "<tr>" + newLine;
            doc += "<td>Id</td> <td>File</td> <td>Line</td> <td>Column</td> <td>Code</td> <td>State</td> <td>Severity</td> <td>Method</td>" + newLine;
        doc += "</tr>" + newLine;
        for (IssueDto issue : issuesList) {
            doc += "<tr>" + newLine;
            doc += "<td>" + issue.getId() + "</td> <td>" + issue.getFile() + "</td> <td>" + issue.getLine() + "</td> <td>" + issue.getColumn() + "</td> <td>" + issue.getCode()+ "</td> <td>" + issue.getState() + "</state> <td>" + issue.getSeveritylevel() + "</td> <td>" + issue.getMethod() + "</td>" + newLine;
            doc += "</tr>" + newLine;
        }
        doc += "</table>" + newLine;
        doc += "</body>" + newLine;
        doc += "</html>" + newLine;
        return doc;
    }

    @NotNull
    private String toXML(@NotNull List<IssueDto> issuesList) {
        String doc = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + newLine;
        doc += "<issues>" + newLine;
        for (IssueDto issue : issuesList) {
            doc += "<issue>" + newLine;
            doc += "<id>" +  issue.getId() + "</id>" + newLine;
            doc += "<line>" +  issue.getLine() + "</line>" + newLine;
            doc += "<column>" +  issue.getColumn()  + "</column>" + newLine;
            doc += "<code>" +  issue.getCode() + "</code>" + newLine;
            doc += "<state>" +  issue.getState() + "</state>" + newLine;
            doc += "<severity>" +  issue.getSeveritylevel() + "</severity>" + newLine;
            doc += "<method>" +  issue.getMethod() + "</method>" + newLine;
            doc += "<trace>" + newLine;
                List<TraceDto> traceBlock = issue.getTraceList();
                if (traceBlock != null) {
                    for (TraceDto trace : traceBlock) {
                        doc += "\t<traceLine block=\""+trace.getBlockid()+"\" method=\""+trace.getMethod()+"\" file=\""+trace.getFile()+"\" line=\""+trace.getLine()+"\" text=\""+trace.getText()+"\"></traceLine>" + newLine;
                    }
                }
            doc += "</trace>" + newLine;
            doc += "</issue>" + newLine;
        }
        doc += "</issues>" + newLine;
        return doc;
    }

    /*
    @ExceptionHandler(Exception.class)
    public ModelAndView handleMyException(Exception exception) {
        ModelAndView mv = new ModelAndView("redirect:/project/outside-list");
        return mv;
    }
    */
}

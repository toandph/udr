package architectgroup.udr.webserver.controller;

import architectgroup.fact.access.HistoryAccess;
import architectgroup.fact.access.IssueAccess;
import architectgroup.fact.access.util.FactAccessFactory;
import architectgroup.fact.dto.HistoryDto;
import architectgroup.fact.dto.IssueDto;
import architectgroup.udr.webserver.model.SessionModel;
import net.sf.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/18/13
 * Time: 3:24 PM
 */
@Controller
@Scope("request")
public class HistoryController {
    @Autowired
    private SessionModel session;
    @Autowired
    private FactAccessFactory factAccess;

    @RequestMapping(value = "/history/add-json")
    @ResponseBody
    public String addJson(HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId, @RequestParam("buildId") int buildId, @RequestParam("issueId") int issueId, @RequestParam("status") String status, @RequestParam("comment") String comment) {
        HistoryDto historyDto = new HistoryDto();
        HistoryAccess historyAccess = new HistoryAccess(factAccess);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        historyDto.setUser(username);
        historyDto.setIssueId(issueId);
        historyDto.setStatus(status);
        historyDto.setDate(new Date());
        historyDto.setComment(comment);
        historyAccess.add(projectId, buildId, historyDto);
        return "Insert OK";
    }

    @RequestMapping(value = "/history/delete-json")
    @ResponseBody
    public String deleteJson(HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId, @RequestParam("buildId") int buildId, @RequestParam("historyId") int historyId) {
        HistoryAccess historyAccess = new HistoryAccess(factAccess);
        historyAccess.delete(projectId, buildId, historyId);
        return "Delete OK";
    }


    @NotNull
    @RequestMapping(value = "/history/get-list-json", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getListJson(HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId, @RequestParam("buildId") int buildId, @RequestParam("issueId") int issueId) {
        String countStr = request.getParameter("from");
        int from = 0;
        if (countStr != null) {
            try {
                from = Integer.parseInt(countStr);
            } catch (NumberFormatException ex) {
                from = 0;
            }
        }
        HistoryAccess access = new HistoryAccess(factAccess);
        JSONObject json = access.getHistoryListJson(projectId, buildId, issueId, from);
        return json.toString();
    }

    @NotNull
    @RequestMapping(value = "/history/edit")
    public ModelAndView edit(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId, @RequestParam("buildId") int buildId, @RequestParam("status") String status, @RequestParam("comment") String comment) {
        ModelAndView modelAndView = new ModelAndView("redirect:/issue/list?projectId=" + projectId + "&buildId=" + buildId);

        String[] buildIdArr = request.getParameterValues("issueId");
        for (String idStr : buildIdArr) {
            int id = Integer.parseInt(idStr);
            HistoryAccess historyAccess = new HistoryAccess(factAccess);
            HistoryDto newHistory = new HistoryDto();
            newHistory.setIssueId(id);
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            newHistory.setUser(username);
            newHistory.setStatus(status);
            newHistory.setComment(comment);
            newHistory.setDate(new Date());
            historyAccess.add(projectId, buildId, newHistory);
        }

        modelAndView.addObject("projectId", projectId);
        modelAndView.addObject("buildId", buildId);
        //modelAndView.addObject("filterId", filterId);
        modelAndView.addObject("action","list");

        return modelAndView;
    }

    @NotNull
    @RequestMapping(value = "/history/edit-all")
    public ModelAndView editAll(HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId, @RequestParam("buildId") int buildId, @RequestParam("status") String status, @RequestParam("comment") String comment) {
        ModelAndView modelAndView = new ModelAndView("redirect:/issue/list?projectId=" + projectId + "&buildId=" + buildId);

        IssueAccess issueAccess = new IssueAccess(factAccess);
        List<IssueDto> list = issueAccess.getIssues(projectId, buildId);
        List<HistoryDto> historyList = new ArrayList<HistoryDto>();
        HistoryAccess historyAccess = new HistoryAccess(factAccess);
        for (IssueDto issue : list) {
            HistoryDto newHistory = new HistoryDto();
            newHistory.setIssueId(issue.getId());
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            newHistory.setUser(username);
            newHistory.setStatus(status);
            newHistory.setComment(comment);
            newHistory.setDate(new Date());
            historyList.add(newHistory);
        }
        historyAccess.add(projectId, buildId, historyList);
        return new ModelAndView("redirect:/issue/list?projectId=" + projectId + "&buildId=" + buildId);
    }

    /*
    @ExceptionHandler(Exception.class)
    public ModelAndView handleMyException(Exception exception) {
        ModelAndView mv = new ModelAndView("redirect:/project/outside-list");
        return mv;
    }
    */
}

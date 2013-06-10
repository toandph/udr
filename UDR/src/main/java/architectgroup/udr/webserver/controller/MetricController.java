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
public class MetricController {
    @Autowired
    private SessionModel session;
    @Autowired
    private FactAccessFactory factAccess;

    @RequestMapping(value = "/metric/detail")
    public ModelAndView detail(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId, @RequestParam("buildId") int buildId) {
        ModelAndView modelAndView = new ModelAndView("redirect:/issue/list?projectId=" + projectId + "&buildId=" + buildId);

        modelAndView.addObject("projectId", projectId);
        modelAndView.addObject("buildId", buildId);
        //modelAndView.addObject("filterId", filterId);
        modelAndView.addObject("action","list");

        return modelAndView;
    }

    @RequestMapping(value = "/metric/chart")
    public ModelAndView chart(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId) {
        ModelAndView modelAndView = new ModelAndView("/metric/chart");

        modelAndView.addObject("projectId", projectId);
        //modelAndView.addObject("filterId", filterId);
        modelAndView.addObject("action","list");

        return modelAndView;
    }
}

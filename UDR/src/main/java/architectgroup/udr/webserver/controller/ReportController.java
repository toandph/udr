package architectgroup.udr.webserver.controller;

import architectgroup.fact.access.BuildAccess;
import architectgroup.fact.access.FilterAccess;
import architectgroup.fact.access.IssueAccess;
import architectgroup.fact.access.ProjectAccess;
import architectgroup.fact.access.object.BuildComparable;
import architectgroup.fact.access.util.CommonFunction;
import architectgroup.fact.access.util.FactAccessFactory;
import architectgroup.fact.util.State;
import architectgroup.fact.dto.BuildDto;
import architectgroup.fact.dto.FilterDto;
import architectgroup.fact.access.object.ChartInfoModel;
import architectgroup.fact.access.object.ChartItem;
import architectgroup.udr.webserver.model.SessionModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 1/22/13
 * Time: 11:33 AM
 */
@Controller
@Scope("request")
public class ReportController extends MultiActionController {
    @Autowired
    private SessionModel session;
    @Autowired
    private FactAccessFactory factAccess;


    @NotNull
    @RequestMapping(value = "/report/chart")
    public ModelAndView chart(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId) {
        ModelAndView modelAndView = new ModelAndView("report/chart");
        ProjectAccess projectAccess = new ProjectAccess(factAccess);
        session.setProject(projectAccess.getProject(projectId));

        BuildAccess buildAccess = new BuildAccess(factAccess);
        List<BuildDto> allBuild = buildAccess.getLastNBuild(projectId, 5);
        Collections.sort(allBuild, new BuildComparable());
        FilterAccess filterAccess = new FilterAccess(factAccess);

        int buildId = 0;
        int filterId = -1;
        boolean noLanguage = false;

        if (allBuild.size() == 0) {
            modelAndView = new ModelAndView("report/noreport");
        } else {
            // Get the build id from URL
            String buildIdStr = request.getParameter("buildId");
            BuildDto build = null;
            if (buildIdStr == null) {
                build = buildAccess.getLastBuild(projectId);
                buildId = build.getId();
            } else {
                try {
                    build = buildAccess.getBuild(projectId, Integer.parseInt(buildIdStr));
                    buildId = build.getId();
                } catch (NumberFormatException ex) {
                    build = buildAccess.getLastBuild(projectId);
                    buildId = build.getId();
                }
            }

            // Get the filter id
            String filterIdStr = request.getParameter("filterId");
            FilterDto filter = null;
            if (filterIdStr == null || filterIdStr.equalsIgnoreCase("-1")) {
                filterId = -1;
            } else {
                try {
                    filter = filterAccess.getFilter(projectId, Integer.parseInt(filterIdStr));
                    filterId = filter.getId();
                } catch (NumberFormatException ex) {
                    filterId = -1;
                }
            }

            String chartType = request.getParameter("type");
            if (chartType == null || chartType.length() == 0 || chartType.equals("state-pie")) {
                chartType = "state-pie";
                modelAndView = new ModelAndView("report/pie");
                modelAndView.addObject("chartTitle", "state-pie");
                modelAndView.addObject("chartTypeSearch", "state");
            } else if (chartType.equals("state-line")) {
                modelAndView = new ModelAndView("report/stack");
                modelAndView.addObject("chartTitle", "build-state-stack");
                modelAndView.addObject("chartTypeSearch", "state");
            } else if (chartType.equals("severity-pie")) {
                modelAndView = new ModelAndView("report/pie");
                modelAndView.addObject("chartTitle", "severity-level-pie");
                modelAndView.addObject("chartTypeSearch", "severity");
            } else if (chartType.equals("error-pie")) {
                modelAndView = new ModelAndView("report/pie");
                noLanguage = true;
                modelAndView.addObject("chartTitle", "error-code-pie");
                modelAndView.addObject("chartTypeSearch", "code");
            }

            ChartInfoModel chartInfo = getChartInfo(chartType, build, allBuild, projectId, filterId);

            modelAndView.addObject("chartInfo", chartInfo);
        }

        // Get filter list
        List<FilterDto> allFilter = filterAccess.getFilterListOnBuild(projectId, buildId);

        modelAndView.addObject("noLanguage", noLanguage);
        modelAndView.addObject("buildList", allBuild);
        modelAndView.addObject("filterList", allFilter);
        modelAndView.addObject("projectId", projectId);
        modelAndView.addObject("filterId", filterId);
        modelAndView.addObject("buildId", buildId);
        modelAndView.addObject("controller","report");
        modelAndView.addObject("action","chart");
        return modelAndView;
    }

    /**
     *
     * @param chartType
     * @param build
     * @param projectId
     * @return the object store the data about chart
     */
    @Nullable
    public ChartInfoModel getChartInfo(@NotNull String chartType, @NotNull BuildDto build, @NotNull List<BuildDto> builds, int projectId, int filterId) {
        if (chartType.equalsIgnoreCase("state-pie")) {
            ChartInfoModel pieChart = statePieChart(projectId, build, filterId);
            return pieChart;
        } else if (chartType.equalsIgnoreCase("state-line")) {
            ChartInfoModel lineChart = stateLineChart(projectId, builds, filterId);
            return lineChart;
        } else if (chartType.equalsIgnoreCase("severity-pie")) {
            ChartInfoModel severityPieChart = severityPieChart(projectId, build, filterId);
            return severityPieChart;
        } else if (chartType.equalsIgnoreCase("error-pie")) {
            ChartInfoModel errorPieChart = errorPieChart(projectId, build, filterId);
            return errorPieChart;
        }
        return null;
    }

    /**
     *
     * @param build
     * @return
     */
    @NotNull
    private ChartInfoModel statePieChart(int projectId, @NotNull BuildDto build, int filterId) {
        ChartInfoModel info = new ChartInfoModel();
        List<ChartItem> items;
        items = new ArrayList<ChartItem>();
        // No filter condition
        if (filterId == -1) {
            IssueAccess issueAccess = new IssueAccess(factAccess);
            Map<String, Integer> stateCountMap = issueAccess.getIssueState(projectId, build.getId());
            int numberOfNew = stateCountMap.get(State.NEW.toString()) == null ? 0 : stateCountMap.get(State.NEW.toString());
            int numberOfFixed = stateCountMap.get(State.FIXED.toString()) == null ? 0 : stateCountMap.get(State.FIXED.toString());
            int numberOfReoccured = stateCountMap.get(State.REOCCURED.toString()) == null ? 0 : stateCountMap.get(State.REOCCURED.toString());
            int numberOfExisting = stateCountMap.get(State.EXISTING.toString()) == null ? 0 : stateCountMap.get(State.EXISTING.toString());

            ChartItem fixed = new ChartItem();
            fixed.setName("fixed");
            fixed.setValue(String.valueOf(numberOfFixed));
            items.add(fixed);

            ChartItem newIssue = new ChartItem();
            newIssue.setName("new");
            newIssue.setValue(String.valueOf(numberOfNew));
            items.add(newIssue);

            ChartItem exist = new ChartItem();
            exist.setName("existing");
            exist.setValue(String.valueOf(numberOfExisting));
            items.add(exist);

            ChartItem reoccured = new ChartItem();
            reoccured.setName("reoccured");
            reoccured.setValue(String.valueOf(numberOfReoccured));
            items.add(reoccured);

            info.setItems(items);
        } else {
            FilterAccess filter = new FilterAccess(factAccess);
            FilterDto filterDto = filter.getFilter(projectId, filterId);
            IssueAccess issueAccess = new IssueAccess(factAccess);

            ChartItem fixed = new ChartItem();
            fixed.setName("fixed");
            fixed.setValue(String.valueOf(issueAccess.getNumberOfIssueByState(projectId, build.getId(), State.FIXED, filterDto)));
            items.add(fixed);

            ChartItem newIssue = new ChartItem();
            newIssue.setName("new");
            newIssue.setValue(String.valueOf(issueAccess.getNumberOfIssueByState(projectId, build.getId(), State.NEW, filterDto)));
            items.add(newIssue);

            ChartItem exist = new ChartItem();
            exist.setName("existing");
            exist.setValue(String.valueOf(issueAccess.getNumberOfIssueByState(projectId, build.getId(), State.EXISTING, filterDto)));
            items.add(exist);

            ChartItem reoccured = new ChartItem();
            reoccured.setName("reoccured");
            reoccured.setValue(String.valueOf(issueAccess.getNumberOfIssueByState(projectId, build.getId(), State.REOCCURED, filterDto)));
            items.add(reoccured);

            info.setItems(items);
        }
        return info;
    }

    /**
     *
     * @param projectId
     * @param builds
     * @param filterId
     * @return
     */
    @NotNull
    private ChartInfoModel stateLineChart(int projectId, @NotNull List<BuildDto> builds, int filterId) {
        ChartInfoModel info = new ChartInfoModel();
        List<String> fixedList = new ArrayList<String>();
        List<String> newList = new ArrayList<String>();
        List<String> openList = new ArrayList<String>();
        List<String> reoccuredList = new ArrayList<String>();
        for (BuildDto build : builds) {
            if (filterId == -1) {
                IssueAccess issueAccess = new IssueAccess(factAccess);
                fixedList.add(String.valueOf(issueAccess.getNumberOfIssueByState(projectId, build.getId(), State.FIXED)));
                openList.add(String.valueOf(issueAccess.getNumberOfIssueByState(projectId, build.getId(), State.EXISTING)));
                newList.add(String.valueOf(issueAccess.getNumberOfIssueByState(projectId, build.getId(), State.NEW)));
                reoccuredList.add(String.valueOf(issueAccess.getNumberOfIssueByState(projectId, build.getId(), State.REOCCURED)));
            } else {
                FilterAccess filter = new FilterAccess(factAccess);
                FilterDto filterDto = filter.getFilter(projectId, filterId);
                IssueAccess issueAccess = new IssueAccess(factAccess);
                fixedList.add(String.valueOf(issueAccess.getNumberOfIssueByState(projectId, build.getId(), State.FIXED, filterDto)));
                openList.add(String.valueOf(issueAccess.getNumberOfIssueByState(projectId, build.getId(), State.EXISTING, filterDto)));
                newList.add(String.valueOf(issueAccess.getNumberOfIssueByState(projectId, build.getId(), State.NEW, filterDto)));
                reoccuredList.add(String.valueOf(issueAccess.getNumberOfIssueByState(projectId, build.getId(), State.REOCCURED, filterDto)));
            }
        }

        List<ChartItem> items;
        items = new ArrayList<ChartItem>();
        ChartItem fixed = new ChartItem();
        fixed.setName("fixed");
        fixed.setValue(CommonFunction.implodeArray(fixedList, "#"));
        items.add(fixed);

        ChartItem newIssue = new ChartItem();
        newIssue.setName("new");
        newIssue.setValue(CommonFunction.implodeArray(newList, "#"));
        items.add(newIssue);

        ChartItem reoccured = new ChartItem();
        reoccured.setName("reoccured");
        reoccured.setValue(CommonFunction.implodeArray(reoccuredList, "#"));
        items.add(reoccured);

        ChartItem exist = new ChartItem();
        exist.setName("existing");
        exist.setValue(CommonFunction.implodeArray(openList, "#"));
        items.add(exist);

        info.setItems(items);
        return info;
    }


    /**
     *
     * @param build
     * @return
     */
    @NotNull
    private ChartInfoModel severityPieChart(int projectId, @NotNull BuildDto build, int filterId) {
        ChartInfoModel info = new ChartInfoModel();
        IssueAccess issueAccess = new IssueAccess(factAccess);

        List<ChartItem> items;
        if (filterId == -1) {
            items = issueAccess.getNumberOfSeverity(projectId, build.getId());
        } else {
            FilterAccess filter = new FilterAccess(factAccess);
            FilterDto filterDto = filter.getFilter(projectId, filterId);
            items = issueAccess.getNumberOfSeverity(projectId, build.getId(), filterDto);
        }

        info.setType("severity-pie");
        info.setItems(items);
        return info;
    }

    /**
     *
     * @param build
     * @return
     */
    @NotNull
    private ChartInfoModel errorPieChart(int projectId, BuildDto build, int filterId) {
        ChartInfoModel info = new ChartInfoModel();

        IssueAccess issueAccess = new IssueAccess(factAccess);
        List<ChartItem> items;
        if (filterId == -1) {
            items = issueAccess.getNumberOfErrorCode(projectId, build.getId());
        } else {
            FilterAccess filter = new FilterAccess(factAccess);
            FilterDto filterDto = filter.getFilter(projectId, filterId);
            items = issueAccess.getNumberOfErrorCode(projectId, build.getId(), filterDto);
        }

        info.setType("error-pie");
        info.setItems(items);
        return info;
    }

    /*
    @ExceptionHandler(Exception.class)
    public ModelAndView handleMyException(Exception exception) {
        ModelAndView mv = new ModelAndView("redirect:/project/outside-list");
        return mv;
    }
    */
}


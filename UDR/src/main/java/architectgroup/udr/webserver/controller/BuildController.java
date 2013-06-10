package architectgroup.udr.webserver.controller;

import architectgroup.fact.access.BuildAccess;
import architectgroup.fact.access.ProjectAccess;
import architectgroup.fact.access.util.FactAccessFactory;
import architectgroup.fact.dto.BuildDto;
import architectgroup.fact.dto.ProjectDto;
import architectgroup.parser.ParserType;
import architectgroup.udr.webserver.model.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 1/22/13
 * Time: 11:33 AM
 */
@Controller
@Scope("request")
public class BuildController {
    @Autowired
    private SessionModel session;
    @Autowired
    private FactAccessFactory factAccess;

    @NotNull
    @RequestMapping(value = "/build/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam("projectId") int projectId) {
        ModelAndView modelAndView = new ModelAndView("build/list");

        List<BuildDto> buildList;
        Map<String, Object> data;
        BuildAccess buildAccess;
        FactAccessFactory accessFactory;

        data  = new HashMap<String, Object>();
        buildAccess = new BuildAccess(factAccess);

        buildList = buildAccess.getBuildList(projectId);
        data.put("buildList", buildList);

        modelAndView.addAllObjects(data);
        modelAndView.addObject("projectId", projectId);
        modelAndView.addObject("controller", "build");
        modelAndView.addObject("action", "list");

        return modelAndView;
    }

    @NotNull
    @RequestMapping(value="/build/add")
    public ModelAndView add(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId, @NotNull @Valid @ModelAttribute("buildUploadModel") BuildUploadModel model, @NotNull BindingResult result) {
        ModelAndView modelAndView = new ModelAndView("build/add");
        if (request.getMethod().equalsIgnoreCase("GET")) {
            /* Initialize the model */
            BuildUploadModel newModel = new BuildUploadModel();
            newModel.setProjectId(projectId);
            BuildAccess access = new BuildAccess(factAccess);
            List<BuildDto> list = access.getBuildList(projectId);
            if (list.size() > 0) {
                newModel.setSourceType(ParserType.valueOf(list.get(0).getType()));
            }
            modelAndView.addObject("buildListSize", list.size());
            modelAndView.addObject("buildUploadModel", newModel);
        } else {
            if (result.hasErrors()) {
                modelAndView.addObject("buildUploadModel", model);
            } else {
                BuildAccess buildAccess = new BuildAccess(factAccess);

                if (!buildAccess.checkBuildExist(projectId, model.getBuildName())) {
                    String username = SecurityContextHolder.getContext().getAuthentication().getName();
                    buildAccess.createNewBuild(projectId, model.getSourceType(), model.getBuildName(), "No specified", "Available", model.getSourceFile(), model.getXmlFile(), model.getImagixFile(), "1.0.0", new Date(), new Date(), username);
                    return new ModelAndView("redirect:/report/chart?projectId=" + model.getProjectId());
                } else {
                    result.reject("build-name-exist");
                }
            }
        }

        modelAndView.addObject("projectId", projectId);
        modelAndView.addObject("sourceTypeList", ParserType.values());
        modelAndView.addObject("controller","build").addObject("action", "add");
        return modelAndView;
    }

    @RequestMapping(value="/build/view-project")
    public ModelAndView viewProject(@RequestParam("projectId") int projectId) {
        ModelAndView modelAndView = new ModelAndView("build/view-project");
        ProjectAccess projectAccess = new ProjectAccess(factAccess);
        ProjectDto detail = projectAccess.getProject(projectId);
        BuildDto build = null;
        if (new BuildAccess(factAccess).getBuildList(projectId).size() > 0) {
            build = new BuildAccess(factAccess).getLastBuild(projectId);
        }
        if (build != null) {
            detail.setLastBuild(build);
        }
        modelAndView.addObject("detail", detail);
        modelAndView.addObject("projectId", projectId);
        modelAndView.addObject("controller","build");
        modelAndView.addObject("action","view-project");

        return modelAndView;
    }

    @NotNull
    @RequestMapping(value="/build/edit-project")
    public ModelAndView editProject(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId, @NotNull @Valid @ModelAttribute("projectModel") ProjectModel projectModel, @NotNull BindingResult result) {
        ModelAndView modelAndView = new ModelAndView("build/edit-project");
        ProjectAccess projectAccess = new ProjectAccess(factAccess);

        if (request.getMethod().equalsIgnoreCase("GET")) {
            ProjectDto prjDto = projectAccess.getProject(projectId);
            prjDto.setLastBuild(new BuildAccess(factAccess).getLastBuild(projectId));
            ProjectModel newModel = new ProjectModel();

            newModel.setName(prjDto.getName());
            newModel.setDescription(prjDto.getDescription());
            newModel.setCreatedBy(prjDto.getCreatedBy());
            newModel.setCreatedDate(prjDto.getCreatedTime().toString());
            if (prjDto.getLastBuild() !=  null) {
                newModel.setType(prjDto.getLastBuild().getType());
            }

            modelAndView.addObject("projectModel", newModel);
        } else {
            if (result.hasErrors()) {
                modelAndView.addObject("projectModel", projectModel);
            } else {
                projectAccess.updateProject(projectId, projectModel.getName(), projectModel.getDescription());
                return new ModelAndView("redirect:/build/view-project?projectId=" + projectId);
            }
        }

        modelAndView.addObject("projectId", projectId);
        modelAndView.addObject("controller","build");
        modelAndView.addObject("action","edit-project");

        return modelAndView;
    }

    @NotNull
    @RequestMapping(value="/build/edit")
    public ModelAndView edit(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId, @RequestParam("buildId") int buildId, @NotNull @Valid @ModelAttribute("buildDetailModel") BuildDetailModel buildDetailModel, @NotNull BindingResult result) {
        ModelAndView modelAndView = new ModelAndView("build/edit");
        BuildAccess buildAccess = new BuildAccess(factAccess);

        if (request.getMethod().equalsIgnoreCase("GET")) {
            /* Initialize the model */

            BuildDetailModel newModel = new BuildDetailModel();
            BuildDto buildDto = buildAccess.getBuildDetail(projectId, buildId);

            newModel.setId(buildDto.getId());
            newModel.setBuildName(buildDto.getName());
            newModel.setCreatedBy(buildDto.getCreatedBy());
            newModel.setDescription(buildDto.getDescription());
            newModel.setSourceType(buildDto.getType());
            newModel.setCreatedDate(buildDto.getEndTime().toString());

            modelAndView.addObject("buildDetailModel", newModel);
        } else {
            if (result.hasErrors()) {
                modelAndView.addObject("buildDetailModel", buildDetailModel);
            } else {
                buildAccess.updateExistingBuild(projectId, buildId, buildDetailModel.getBuildName(), buildDetailModel.getDescription());
                return new ModelAndView("redirect:/build/detail?projectId=" + projectId + "&buildId=" + buildId);
            }
        }

        modelAndView.addObject("projectId", projectId);
        modelAndView.addObject("controller","build");
        modelAndView.addObject("action","edit");

        return modelAndView;
    }

    @NotNull
    @RequestMapping(value="/build/detail")
    public ModelAndView detail(@RequestParam("projectId") int projectId, @RequestParam("buildId") int buildId) {
        ModelAndView modelAndView = new ModelAndView("build/detail");

        BuildAccess buildAccess = new BuildAccess(factAccess);
        BuildDetailModel detail = new BuildDetailModel();
        BuildDto buildDto = buildAccess.getBuildDetail(projectId, buildId);
        detail.setId(buildDto.getId());
        detail.setBuildName(buildDto.getName());
        detail.setDescription(buildDto.getDescription());
        detail.setCreatedDate(buildDto.getEndTime().toString());
        detail.setSourceType(buildDto.getType());
        detail.setCreatedBy(buildDto.getCreatedBy());

        modelAndView.addObject("detail", detail);
        modelAndView.addObject("projectId", projectId);
        modelAndView.addObject("buildId", buildId);
        modelAndView.addObject("controller","build");
        modelAndView.addObject("action","detail");

        return modelAndView;
    }

    @NotNull
    @RequestMapping(value = "/build/delete")
    public ModelAndView delete(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId) {
        // Get multiple buildId
        String[] buildIds = request.getParameterValues("buildId");
        List<Integer> buildIdList = new ArrayList<Integer>();
        for (String idStr : buildIds) {
            int buildId = Integer.parseInt(idStr);
            buildIdList.add(buildId);
        }

        if (buildIdList.size() > 0) {
            BuildAccess buildAccess = new BuildAccess(factAccess);
            buildAccess.deleteBuild(projectId, buildIdList);
        }

        return new ModelAndView("redirect:/build/list?projectId=" + projectId);
    }

    /*
    @ExceptionHandler(Exception.class)
    public ModelAndView handleMyException(Exception exception) {
        ModelAndView mv = new ModelAndView("redirect:/project/outside-list");
        return mv;
    }
    */
}

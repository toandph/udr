/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 1/22/13
 * Time: 11:31 AM
 */

package architectgroup.udr.webserver.controller;

import architectgroup.fact.access.*;
import architectgroup.fact.access.util.FactAccessFactory;
import architectgroup.fact.dto.ProjectDto;
import architectgroup.udr.webserver.model.ProjectModel;
import architectgroup.udr.webserver.model.SessionModel;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.*;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@Scope("request")
public class ProjectController {
    @Autowired
    private SessionModel session;
    @Autowired
    private FactAccessFactory factAccess;

    @RequestMapping(value = "/project/outside-list")
    public ModelAndView outsideList() {
        List<ProjectDto> projectList;
        ModelAndView modelAndView;
        Map<String, Object> data;
        ProjectAccess projectAccess = new ProjectAccess(factAccess);

        modelAndView = new ModelAndView("project/outside-list");
        data  = new HashMap<String, Object>();

        data.put("controller","project");
        data.put("action", "outside-list");
        projectList = projectAccess.getAllProjectWithFullInformation();
        data.put("projectList",projectList);

        modelAndView.addAllObjects(data);
        return modelAndView;
    }

    @RequestMapping(value = "/project/outside-add")
    public ModelAndView outsideAdd(HttpServletRequest request, @Valid @ModelAttribute("projectModel") ProjectModel projectModel, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView("project/outside-add");
        if (request.getMethod().equalsIgnoreCase("GET")) {
            ProjectModel newModel = new ProjectModel();
            modelAndView.addObject("projectModel", newModel);
        } else {
            if (result.hasErrors()) {
                modelAndView.addObject("projectModel", projectModel);
            } else {
                ProjectAccess projectAccess = new ProjectAccess(factAccess);
                UserAccess userAccess = new UserAccess(factAccess);
                projectModel.setUser(userAccess.getAnonymousUser());

                if (projectAccess.checkProjectNameExist(projectModel.getName())) {
                    result.reject("project-already-exists");
                } else {
                    String username = SecurityContextHolder.getContext().getAuthentication().getName();
                    if (username.equalsIgnoreCase("anonymousUser")) {
                        username = "anonymous";
                    }

                    projectAccess.createProject(projectModel.getName(), projectModel.getDescription(), "AVAILABLE", new Date(), username);
                    return new ModelAndView("redirect:/project/outside-list");
                }
            }
        }


        modelAndView.addObject("controller","project");
        modelAndView.addObject("action","outside-add");
        return modelAndView;
    }

    /**
     * Project list page, List all the project in the database.
     * @return /project/list
     */
    @RequestMapping(value = "/project/list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("project/list");
        List<ProjectDto> projectList;
        Map<String, Object> data;
        ProjectAccess projectAccess;

        data  = new HashMap<String, Object>();
        projectAccess = new ProjectAccess(factAccess);
        projectList = projectAccess.getAllProjectWithFullInformation();
        data.put("projectList",projectList);

        modelAndView.addObject("controller","project");
        modelAndView.addObject("action","list");
        modelAndView.addAllObjects(data);
        return modelAndView;
    }

    @RequestMapping(value = "/project/add")
    public ModelAndView add(HttpServletRequest request, @Valid @ModelAttribute("projectModel") ProjectModel projectModel, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView("project/add");

        if (request.getMethod().equalsIgnoreCase("GET")) {
            // Initialize the model //
            ProjectModel newModel = new ProjectModel();
            modelAndView.addObject("projectModel", newModel);
        } else {
            if (result.hasErrors()) {
                modelAndView.addObject("projectModel", projectModel);
            } else {
                ProjectAccess projectAccess = new ProjectAccess(factAccess);
                projectModel.setUser(session.getUser());

                if (projectAccess.checkProjectNameExist(projectModel.getName())) {
                    result.reject("project-already-exists");
                } else {
                    String username = SecurityContextHolder.getContext().getAuthentication().getName();
                    projectAccess.createProject(projectModel.getName(), projectModel.getDescription(), "AVAILABLE", new Date(), username);
                    return new ModelAndView("redirect:/project/list");
                }
            }
        }

        modelAndView.addObject("controller","project");
        modelAndView.addObject("action","add");
        return modelAndView;
    }

    @RequestMapping(value = "/project/delete")
    public ModelAndView delete(HttpServletRequest request) {
        String[] projectIds = request.getParameterValues("projectId");
        for (String idStr : projectIds) {
            int projectId = Integer.parseInt(idStr);
            ProjectAccess projectAccess = new ProjectAccess(factAccess);
            projectAccess.deleteProject(projectId);

            // Remove upload folder
            try {
                FileUtils.deleteDirectory(new File(factAccess.getFactory().getContext().getBuildUpload() + "/" + projectId));
            } catch (IOException er) {
                er.printStackTrace();
            }

            if (session.getProject() != null) {
                if (session.getProject().getId() == projectId) {
                    session.setProject(null);
                    session.setBuild(null);
                }
            }
        }
        return new ModelAndView("redirect:/project/list");
    }
}

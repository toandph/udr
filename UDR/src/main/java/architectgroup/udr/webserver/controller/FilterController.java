package architectgroup.udr.webserver.controller;

import architectgroup.fact.access.BuildAccess;
import architectgroup.fact.access.FilterAccess;
import architectgroup.fact.access.util.FactAccessFactory;
import architectgroup.fact.access.util.ScanZipToTree;
import architectgroup.fact.access.util.SourceTree;
import architectgroup.fact.dto.BuildDto;
import architectgroup.fact.dto.FilterDto;
import architectgroup.udr.webserver.model.FilterModel;
import architectgroup.udr.webserver.model.SessionModel;
import net.sf.ehcache.Element;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
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
public class FilterController {
    @Autowired
    private SessionModel session;
    @Autowired
    private FactAccessFactory factAccess;

    @NotNull
    @RequestMapping(value = "/filter/list")
    public ModelAndView list(@RequestParam("projectId") int projectId) {
        ModelAndView modelAndView = new ModelAndView("filter/list");

        List<FilterDto> filterList;
        Map<String, Object> data;
        FilterAccess filterAccess;
        FactAccessFactory accessFactory;

        data  = new HashMap<String, Object>();
        filterAccess = new FilterAccess(factAccess);

        filterList = filterAccess.getFilterList(projectId);
        data.put("filterList", filterList);

        modelAndView.addAllObjects(data);

        modelAndView.addObject("projectId", projectId);
        modelAndView.addObject("controller","filter");
        modelAndView.addObject("action","list");

        return modelAndView;
    }

    @NotNull
    @RequestMapping(value = "/filter/add")
    public ModelAndView add(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId, @NotNull @Valid @ModelAttribute("filterModel") FilterModel filterModel, @NotNull BindingResult result) {
        ModelAndView modelAndView = new ModelAndView("filter/add");
        boolean noBuild = false;
        if (request.getMethod().equalsIgnoreCase("GET")) {
            /* Initialize the model */
            BuildDto build = (new BuildAccess(factAccess)).getLastBuild(projectId);
            if (build == null) {
                noBuild = true;
            }
            FilterModel newModel = new FilterModel();
            modelAndView.addObject("filterModel", newModel);
        } else {
            if (result.hasErrors()) {
                modelAndView.addObject("filterModel", filterModel);
            } else {
                FilterAccess filterAccess = new FilterAccess(factAccess);
                filterAccess.createFilter(projectId, filterModel.getFilterName(), filterModel.getOnBuild(), filterModel.getValue(), filterModel.getStatus(), filterModel.getState(), filterModel.getScope());
                return new ModelAndView("redirect:/filter/list?projectId=" + projectId);
            }
        }

        modelAndView.addObject("noBuild", noBuild);
        modelAndView.addObject("projectId", projectId);
        modelAndView.addObject("controller","filter");
        modelAndView.addObject("action","add");

        return modelAndView;
    }

    @NotNull
    @RequestMapping(value = "/filter/zip-json")
    @ResponseBody
    public String zipJson(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestParam("projectId") int projectId, @NotNull @Valid @ModelAttribute("filterModel") FilterModel filterModel, @NotNull BindingResult result) {
        if (request.getMethod().equalsIgnoreCase("GET")) {
            BuildDto build = (new BuildAccess(factAccess)).getLastBuild(projectId);
            String zipContent = "";
            Element element;
            if (build != null) {
                if ((element = factAccess.getBuildCache().get(projectId)) != null) {
                    HashMap<Integer, BuildDto> list = (HashMap<Integer, BuildDto>) element.getObjectValue();
                    zipContent = list.get(build.getId()).getZipTree();
                } else {
                    String INPUT_ZIP_FILE = factAccess.getFactory().getContext().getBuildUpload() + "/" + projectId + "/" + build.getId() + "/source.zip";
                    ScanZipToTree unZip = new ScanZipToTree();
                    SourceTree sourceTree = unZip.scan(INPUT_ZIP_FILE);
                    zipContent = unZip.generateTreeJson(sourceTree);
                }
            }
            return zipContent;
        }
        return "";
    }


    @NotNull
    @RequestMapping(value = "/filter/guide")
    public ModelAndView guide() {
        ModelAndView modelAndView = new ModelAndView("redirect:/issue/help#create-filter");

        modelAndView.addObject("controller","filter");
        modelAndView.addObject("action","guide");

        return modelAndView;
    }

    @NotNull
    @RequestMapping(value = "/filter/delete")
    public ModelAndView delete(@NotNull HttpServletRequest request, HttpServletResponse response) {
        String[] buildIds = request.getParameterValues("filterId");
        List<Integer> filterIdList = new ArrayList<Integer>();
        for (String idStr : buildIds) {
            int filterId = Integer.parseInt(idStr);
            filterIdList.add(filterId);
        }
        if (filterIdList.size() > 0) {
            FilterAccess filterAccess = new FilterAccess(factAccess);
            filterAccess.deleteFilters(session.getProject().getId(), filterIdList);
        }

        return new ModelAndView("redirect:/filter/list?projectId=" + session.getProject().getId());
    }

    /*
    @ExceptionHandler(Exception.class)
    public ModelAndView handleMyException(Exception exception) {
        ModelAndView mv = new ModelAndView("redirect:/project/outside-list");
        return mv;
    }
    */
}

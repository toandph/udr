package architectgroup.fact.access;

import architectgroup.fact.access.util.*;
import architectgroup.fact.dao.*;
import architectgroup.fact.dto.FilterDto;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 1/29/13
 * Time: 4:13 PM
 */
public class FilterAccess {
    private FactAccessFactory factAccess;
    private DaoFactory _factory;
    private DatabaseDao _database;

    public FilterAccess(FactAccessFactory factAccess) {
        this.factAccess = factAccess;
        _factory = factAccess.getFactory();
        _database = _factory.getDatabaseDao();
    }

    /**
     *
     * @param projectId
     * @param name
     * @param build
     * @param value
     * @param status
     * @param state
     * @param scope
     */
    public void createFilter(int projectId, String name, String build, @NotNull String value, List<String> status, List<String> state, String scope){
        FilterDto filterDto = new FilterDto();
        filterDto.setName(name);
        filterDto.setBuild(build);
        filterDto.setValue(value);
        filterDto.setStatus(CommonFunction.implodeArray(status, ","));
        filterDto.setState(CommonFunction.implodeArray(state, ","));
        filterDto.setScope(scope);

        // Add new config to advance value
        String advanceValue = value;
        if (value.length() > 0) {
            advanceValue += ";";
        }
        advanceValue += "+status:" + CommonFunction.implodeArray(status, ",") + ";";
        advanceValue += "+state:" + CommonFunction.implodeArray(state, ",") + ";";
        advanceValue += "+scope:" + scope + ";";
        filterDto.setValue(advanceValue);

        FilterDao filterDao = _factory.getFilterDao(projectId);
        filterDao.insert(filterDto);
    }

    /**
     * Find All Filter List
     * @param projectId
     * @return
     */
    public List<FilterDto> getFilterList(int projectId){
        List<FilterDto> filterDtoList;
        FilterDao filterDao = _factory.getFilterDao(projectId);

        filterDtoList = filterDao.findAll();
        return filterDtoList;
    }

    /**
     * Find All Filter List That Apply On A Build
     * @param projectId
     * @param buildId
     * @return
     */
    @NotNull
    public List<FilterDto> getFilterListOnBuild(int projectId, int buildId){
        FilterDao filterDao = _factory.getFilterDao(projectId);
        List<FilterDto> filterDtoList;
        filterDtoList = filterDao.findAll();

        List<FilterDto> result = new ArrayList<FilterDto>();
        for (FilterDto filter : filterDtoList) {
            String onBuild = filter.getBuild();
            if (checkFilterIn(buildId, onBuild)) {
                result.add(filter);
            }
        }
        return result;
    }

    /**
     * Check an integer is in the String list
     * @param buildId
     * @param onBuild
     * @return
     */
    private boolean checkFilterIn(int buildId, String onBuild) {
        String[] builds = onBuild.split(",");
        for (String build : builds) {
            if (build.equalsIgnoreCase("all")) {
                return true;
            } else {
                Integer id = Integer.parseInt(build);
                if (buildId == id) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param projectId
     * @param ids
     */
    public void deleteFilters(int projectId, List<Integer> ids){
        FilterDao filterDao = _factory.getFilterDao(projectId);
        for (Integer id : ids) {
            FilterDto filter = filterDao.findFilterById(id);
            filterDao.delete(filter);
        }
    }

    /**
     *
     * @param projectId
     * @param filterID
     * @return
     */
    public FilterDto getFilter(int projectId, int filterID){
        FilterDao filterDao = _factory.getFilterDao(projectId);
        FilterDto filterDto = filterDao.findFilterById(filterID);
        return filterDto;
    }
}

package architectgroup.fact.dao;

import architectgroup.fact.dto.FilterDto;
import architectgroup.fact.util.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/3/12
 * Time: 11:17 AM
 */
public interface FilterDao {
    /**
     * Insert a Filter to table
     *
     * @param  filterDto  the object to be added
     * @return the filter object which is included the new ID
     */
    @NotNull
    public FilterDto insert(FilterDto filterDto);

    /**
     * Update a BuildDto to table
     *
     * @param  filterDto  the object to be added
     * @return the new filter object
     */
    @NotNull
    public FilterDto update(FilterDto filterDto);

    /**
     * Delete a filter
     *
     * @param  filterDto  the filter object include the id to be deleted
     */
    public Result delete(FilterDto filterDto);

    /**
     * Delete a list of filter
     *
     * @param  filterIds  the list of filter ids to be deleted
     */
    public Result delete(List<Integer> filterIds);

    /**
     * Find a filter base on filter ID
     *
     * @param  id  the id of the filter want to retrieve.
     * @return the filter object
     */
    @Nullable
    public FilterDto findFilterById(int id);

    /**
     * Find the all the filter
     * @return the list of filter object
     */
    @NotNull
    public List<FilterDto> findAll();
}

package architectgroup.fact.dao;

import architectgroup.fact.dto.*;
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
public interface BuildDao {
    /**
     * Insert a BuildDto to table
     *
     * @param  build  the object to be added
     * @return the build object which is included the new ID
     */
    @NotNull
    public BuildDto insert(BuildDto build);

    /**
     * Update a BuildDto to table
     *
     * @param  build  the object to be added
     * @return the new build object
     */
    @NotNull
    public BuildDto update(BuildDto build);

    /**
     * Delete a build
     *
     * @param  build  the build object include the id to be deleted
     */
    public Result delete(BuildDto build);

    /**
     * Find a build base on build ID
     *
     * @param  id  the id of the build want to retrieve.
     * @return the build object
     */
    @Nullable
    public BuildDto findById(int id);

    /**
     * Find a build base on build name
     *
     * @param  name  the id of the build want to retrieve.
     * @return the build object
     */
    @Nullable
    public BuildDto findByName(String name);

    /**
     * Find the latest build
     * @return the last build object
     */
    @Nullable
    public BuildDto findLastBuild();

    /**
     * Find the latest nth build
     * @param n number of builds that need to retrieve
     * @return the list of nth build
     */
    @NotNull
    public List<BuildDto> findLastNBuild(int n);

    /**
     * Find all build
     * @return the all build object
     */
    @NotNull
    public List<BuildDto> findAll();
}

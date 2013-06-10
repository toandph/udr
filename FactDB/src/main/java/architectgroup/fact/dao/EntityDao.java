package architectgroup.fact.dao;

import architectgroup.fact.dto.*;
import architectgroup.fact.util.Result;
import architectgroup.fact.util.State;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/11/12
 * Time: 10:50 PM
 */
public interface EntityDao {
    /**
     * Insert a history to table
     *
     * @param  entity  the object to be added
     * @return the issue object which is included the new ID
     */
    @NotNull
    public EntityDto insert(EntityDto entity);

    /**
     * Insert a entity to table
     *
     * @param  entities  the object to be added
     * @return the issue object which is included the new ID
     */
    public Result insert(List<EntityDto> entities);

    /**
     * Insert a entity to table
     *
     * @param  relations  the object to be added
     * @return the issue object which is included the new ID
     */
    public Result insertRelationships(List<RelationshipDto> relations);

    /**
     * Update a entity to table
     *
     * @param  entity  the object to be updated
     * @return the new issue object
     */
    @Nullable
    public EntityDto update(EntityDto entity);

    /**
     * Delete an entity
     *
     * @param  entity  the issue to be deleted
     */
    public Result delete(EntityDto entity);

    /**
     * Find an entity based on id
     *
     * @param  id  the id of the issue
     * @return the issue object
     */
    public EntityDto findById(String id);

    /**
     * Find an entity based on id
     *
     * @param  type  the id of the issue
     * @return the issue object
     */
    public List<EntityDto> findByEntityType(String type);
}

package architectgroup.fact.dao;

import architectgroup.fact.util.Result;
import architectgroup.fact.util.State;
import architectgroup.fact.dto.FilterDto;
import architectgroup.fact.dto.IssueDto;
import architectgroup.fact.dto.ProjectDto;
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
public interface IssueDao {
    /**
     * Insert a issue to table
     *
     * @param  issue  the object to be added
     * @return the issue object which is included the new ID
     */
    @NotNull
    public IssueDto insert(IssueDto issue);

    /**
     * Insert a list of issues to table
     *
     * @param  issueList  the list of issue to be added
     * @return the number of successfully insert
     */
    public int insert(List<IssueDto> issueList);

    /**
     * Update a issue to table
     *
     * @param  issue  the object to be updated
     * @return the new issue object
     */
    @NotNull
    public IssueDto update(IssueDto issue);

    /**
     * Update a issue to table
     *
     * @param  issues  the object to be updated
     * @return the new issue object
     */
    public Result update(List<IssueDto> issues);

    /**
     * Delete an issue
     *
     * @param  issue  the issue to be deleted
     */
    public Result delete(IssueDto issue);

    /**
     * Find an issue based on id
     *
     * @param  id  the id of the issue
     * @return the issue object
     */
    @Nullable
    public IssueDto findIssueById(int id);

    /**
     * Find all issue of a current project
     *
     * @return the list of issues
     */
    @NotNull
    public List<IssueDto> findAllIssues();

    /**
     * Find all issue base on the search condition SQL
     *
     * @param searchCondition SQL query
     * @return the list of issues
     */
    @NotNull
    public List<IssueDto> findAllIssues(String searchCondition);

    /**
     * Find all issue by state
     *
     * @param state the state of the issue
     * @return the list of issues
     */
    @NotNull
    public List<IssueDto> findIssueByState(State state);

    /**
     * Find all issue by state with a filter
     *
     * @param state the state of the issue
     * @return the list of issues
     */
    @NotNull
    public List<IssueDto> findIssueByState(State state, String filter);

    /**
     *
     * @return
     */
    @NotNull
    public Map<String, Integer> findGroupByErrorCode();

    @NotNull
    public Map<String, Integer> findGroupByErrorCode(String filter);

    public int findNextIssue(int issueId);
    public int findPreviousIssue(int issueId);
    /**
     *
     * @return
     */
    @NotNull
    public Map<String, Integer> findGroupBySeverity();
    @NotNull
    public Map<String, Integer> findGroupBySeverity(String filter);


    @NotNull
    public Map<String, Integer> findState();

    /**
     * Update a fixed issue into current build
     */
    public void updateFixedIssue(int buildOrder);
    public void updateState(int buildOrder);

    public int findNumberOfIssues(String filter);


}

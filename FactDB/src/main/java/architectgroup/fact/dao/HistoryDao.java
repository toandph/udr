package architectgroup.fact.dao;

import architectgroup.fact.util.Result;
import architectgroup.fact.util.State;
import architectgroup.fact.dto.HistoryDto;
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
public interface HistoryDao {
    /**
     * Insert a history to table
     *
     * @param  history  the object to be added
     * @return the issue object which is included the new ID
     */
    @NotNull
    public HistoryDto insert(HistoryDto history);

    /**
     * Insert a history to table
     *
     * @param  history  the object to be added
     * @return the issue object which is included the new ID
     */
    public Result insert(List<HistoryDto> history);

    /**
     * Update a history to table
     *
     * @param  history  the object to be updated
     * @return the new issue object
     */
    @Nullable
    public HistoryDto update(HistoryDto history);

    /**
     * Delete an history
     *
     * @param  history  the issue to be deleted
     */
    public Result delete(HistoryDto history);

    /**
     * Find an history based on id
     *
     * @param  id  the id of the issue
     * @return the issue object
     */
    public HistoryDto findIssueById(int id);

    public List<HistoryDto> findByIssueId(int issueId, int count);

    public int findSize(int issueId);
}

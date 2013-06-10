package architectgroup.fact.dao;

import architectgroup.fact.dto.IssueDto;
import architectgroup.fact.dto.TraceDto;
import architectgroup.fact.util.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/11/12
 * Time: 10:50 PM
 */
public interface TraceDao {
    public TraceDto insert(TraceDto project);
    public Result insert(List<TraceDto> projects);
    public TraceDto update(TraceDto project);
    public Result delete(TraceDto project);
    public List<TraceDto> findTraceByIssue(IssueDto issue);
    public List<TraceDto> findTraceByIssueId(int issueId);
    public List<TraceDto> findAllTraces();
    public List<TraceDto> findTraceInIssueIds(int buildNo, List<Integer> ids);
}
package architectgroup.fact.access;

import architectgroup.fact.access.util.FactAccessFactory;
import architectgroup.fact.dao.*;
import architectgroup.fact.dto.TraceDto;

import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 1/29/13
 * Time: 4:12 PM
 */
public class TraceAccess {
    private FactAccessFactory _factAccess;
    private DaoFactory _factory;

    public TraceAccess(FactAccessFactory factAccess) {
        _factAccess = factAccess;
        _factory = _factAccess.getFactory();
    }

    /**
     *
     * @param projectId
     * @param issueId
     * @param buildId
     * @return
     */
    public List<TraceDto> getTrace(int projectId, int issueId, int buildId){
        TraceDao traceDao = _factory.getTraceDao(projectId, buildId);
        List<TraceDto> traceList = traceDao.findTraceByIssueId(issueId);

        return traceList;
    }
}

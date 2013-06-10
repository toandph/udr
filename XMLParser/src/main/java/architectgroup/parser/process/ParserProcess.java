package architectgroup.parser.process;

import architectgroup.fact.dto.IssueDto;

/**
 * Created with IntelliJ IDEA.
 * User: toandph
 * Date: 4/14/13
 * Time: 1:30 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ParserProcess {
    public void initialize();
    public void processInsertIssue(IssueDto issueDto);
    public void ending();
    public void error(Exception err);
}

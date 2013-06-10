package architectgroup.fact.dao;

import architectgroup.fact.dto.IssueSignatureDto;
import architectgroup.fact.util.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 5/27/12
 * Time: 6:26 PM
 */
public interface IssueSignatureDao {
    /**
     * Insert a signature to the table
     *
     * @param  sig  the object to be added
     * @return the issue object which is included the new ID
     */
    @NotNull
    public IssueSignatureDto insert(IssueSignatureDto sig);

    /**
     * Insert a list of issue signatures to table
     *
     * @param  sigs  the list of issue to be added
     * @return the number of successfully insert
     */
    @NotNull
    public List<Integer> insert(List<IssueSignatureDto> sigs);

    /**
     * Update a signature to table
     *
     * @param  sig  the object to be updated
     * @return the new issue signature object
     */
    @NotNull
    public IssueSignatureDto update(IssueSignatureDto sig);

    /**
     * Update multiple signature
     *
     * @param  sigs  the list of issue signatures to be updated
     * @return the number of successfully updated
     */
    public Result update(List<IssueSignatureDto> sigs);

    /**
     * Delete an signature
     *
     * @param  sig  delete the signature
     */
    public Result delete(IssueSignatureDto sig);

    /**
     *
      * @return
     */
    public int getMaxId();

    public IssueSignatureDto findIssueSignatureById(int id);
    // public List<IssueSignatureDto> findIssueSignatureWhereFirstOccur(int firstOccur);
    public List<IssueSignatureDto> findAll();
    // public List<IssueSignatureDto> findIssueSignatureEquals(String sign_method, String sign_code, String sign_signature, int sign_severityLevel);
    public List<IssueSignatureDto> findIssueSignatureEquals(String sign_signature);

    public int findSize();
    public Result updateTrend(int numberOfBuild);
    public Result addToTrend(String trend);
    public Result removeTrend(String buildList);
}

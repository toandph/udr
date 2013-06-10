package architectgroup.udr.webserver.model;

import architectgroup.udr.webserver.validator.CheckAdvanceValue;
import architectgroup.udr.webserver.validator.CheckFilterOnBuild;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: toandph
 * Date: 3/16/13
 * Time: 8:13 PM
 */
public class FilterModel {
    @NotEmpty(message = "{filter.filter-name-not-empty}")
    private String filterName;
    private String scope;
    @CheckAdvanceValue
    private String value;
    private List<String> state;
    private List<String> status;
    @CheckFilterOnBuild
    private String onBuild;

    public FilterModel() {
        List<String> stateList = new ArrayList<String>();
        stateList.add("New");
        stateList.add("Existing");
        stateList.add("Not in scope");
        stateList.add("Fixed");
        stateList.add("Recurred");
        stateList.add("Obsolete");

        List<String> statusList = new ArrayList<String>();
        statusList.add("Analyze");
        statusList.add("Ignore");
        statusList.add("Not a problem");
        statusList.add("Fix in next release");
        statusList.add("Fix in later release");
        statusList.add("Defer");
        statusList.add("Filter");

        state = stateList;
        status = statusList;

    }

    // Only for stored the zipContent
    private String zipContent;

    public String getZipContent() {
        return zipContent;
    }

    public void setZipContent(String zipContent) {
        this.zipContent = zipContent;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getState() {
        return state;
    }

    public void setState(List<String> state) {
        this.state = state;
    }

    public String getOnBuild() {
        return onBuild;
    }

    public void setOnBuild(String onBuild) {
        this.onBuild = onBuild;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }
}

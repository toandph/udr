package architectgroup.fact.access.object;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: toandph
 * Date: 3/15/13
 * Time: 3:18 AM
 * To change this template use File | Settings | File Templates.
 */

public class ChartInfoModel {
    private String type;
    private List<ChartItem> items;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ChartItem> getItems() {
        return items;
    }

    public void setItems(List<ChartItem> items) {
        this.items = items;
    }
}

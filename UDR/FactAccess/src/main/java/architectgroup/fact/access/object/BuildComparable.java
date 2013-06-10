package architectgroup.fact.access.object;

import architectgroup.fact.dto.BuildDto;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/3/13
 * Time: 4:19 PM
 */
public class BuildComparable implements Comparator<BuildDto> {
    public int compare(@NotNull BuildDto o1, @NotNull BuildDto o2) {
        return (o1.getId() < o2.getId() ? -1 : (o1.getId() == o2.getId() ? 0 : 1));
    }
}

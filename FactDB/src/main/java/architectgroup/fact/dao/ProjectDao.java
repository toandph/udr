package architectgroup.fact.dao;
import architectgroup.fact.dto.*;
import architectgroup.fact.util.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 4/3/12
 * Time: 2:30 AM
 */
public interface ProjectDao {
    public ProjectDto insert(ProjectDto project);
    public ProjectDto update(ProjectDto project);
    public Result delete(ProjectDto project);

    public ProjectDto findById(int id);
    public ProjectDto findByName(String name);
    public List<ProjectDto> findByUser(String username);
    public List<ProjectDto> findAll();

    public void updateMetricName(String name, String full, String brief);
}

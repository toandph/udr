package architectgroup.fact.access;

import architectgroup.fact.access.util.FactAccessFactory;
import architectgroup.fact.dao.*;
import architectgroup.fact.dto.*;

import architectgroup.fact.util.Result;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

public class ProjectAccess {
    private FactAccessFactory factAccess;
    private DaoFactory _factory;
    private DatabaseDao _database;
    private ProjectDao _projectDao;


    public ProjectAccess(FactAccessFactory factAccess) {
        this.factAccess = factAccess;
        _factory = factAccess.getFactory();
        _database = _factory.getDatabaseDao();
        _projectDao = _factory.getProjectDao();

    }

    /**
     * Step to create Project Database
     * 1. Create the database
     * 2. Create the Table BUILD
     * 3. Create the Table ISSUE_SIGNATURE
     * 4. Create the Table FILTER
     * 5. Insert a record to project table in METADATA
     * The Parameter is the Project Name (the database Name)
     * This method returns the status of successfully or fail
     *
     * @return projectDto or null if failed.
     */
    @Nullable
    public ProjectDto createProject(String projectName, String description, String status, Date time, String createdBy) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(projectName);
        projectDto.setDescription(description);
        projectDto.setStatus(status);
        projectDto.setCreatedTime(time);
        projectDto.setCreatedBy(createdBy);
        projectDto = _projectDao.insert(projectDto);

        Result r1 = _database.createDatabase(projectDto.getId());
        Result r2 = _database.createTableBuild(projectDto.getId());
        Result r3 = _database.createTableIssueSignature(projectDto.getId());
        Result r4 = _database.createTableFilter(projectDto.getId());

        _database.createFunctionSplit(projectDto.getId());
        _database.createFunctionIsZero(projectDto.getId());
        _database.createFunctionGetState(projectDto.getId());
        _database.createFunctionRemoveBuild(projectDto.getId());
        _database.createProcedureRemoveTrend(projectDto.getId());
        _database.createTableMetrics(projectDto.getId());
        _database.createTableRelationship(projectDto.getId());


        if (r1.getCode() + r2.getCode() + r3.getCode() + r4.getCode() == 0) {
            System.out.println("Create Project Successfully");

            // Save to cache
                Cache cache = factAccess.getBuildCache();
                cache.put(new Element(projectDto.getId(), new HashMap<Integer, BuildDto>()));

                Cache projectCache = factAccess.getProjectCache();
                Element element;
                List<ProjectDto> projectList;
                if ((element = projectCache.get("projectList")) != null) {
                    projectList = (List<ProjectDto>) element.getObjectValue();
                } else {
                    projectList = new ArrayList<ProjectDto>();
                }
                projectList.add(projectDto);
                projectCache.put(new Element("projectList", projectList));
            // End save to cache

            return projectDto;
        } else {
            System.out.println("Create Project Failed");
            return null;
        }

    }

    /**
     * This is for internal use only
     */
    private void importMetricName() {
        try {
            FileInputStream fi = new FileInputStream("D:/metricsName.xml");
            BufferedInputStream bi = new BufferedInputStream(fi);
            InputStreamReader isr = new InputStreamReader(fi);
            BufferedReader br = new BufferedReader(isr);
            String p;
            while ((p = br.readLine()) != null)  {
                String[] names = p.split("###");
                _projectDao.updateMetricName(names[0], names[1], names[2]);
            }


        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * check the project name is already exist in database or not
     * @param projectName
     * @return true if project name already exist
     */
    public boolean checkProjectNameExist(String projectName) {
        ProjectDto project = _projectDao.findByName(projectName);
        return project != null;
    }

    /**
     * Convert all information into a DTO for easily handle
     * @return successfully or fail
     */
    @NotNull
    public ProjectDto toProjectDto(int id,
                                    String projectName,
                                    String desc,
                                    String stt,
                                    Date createdTime,
                                    String createdBy,
                                    int totalBuilds) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(id);
        projectDto.setName(projectName);
        projectDto.setDescription(desc);
        projectDto.setStatus(stt);
        projectDto.setCreatedTime(createdTime);
        projectDto.setCreatedBy(createdBy);
        projectDto.setTotalOfBuilds(totalBuilds);
        return projectDto;
    }

    public Result deleteProject(int projectId) {
        ProjectDto projectDto = getProject(projectId);
        Result results = _database.dropDatabase(projectId);

        _projectDao.delete(projectDto);

        // Clear cache
        Cache projectCache = factAccess.getProjectCache();
        Element element;
        List<ProjectDto> projectList;
        if ((element = projectCache.get("projectList")) != null) {
            projectList = (List<ProjectDto>) element.getObjectValue();
            int i = 0;
            for (i = 0; i < projectList.size(); i++) {
                if (projectList.get(i).getId() == projectDto.getId()) {
                    projectList.remove(i);
                    break;
                }
            }
        }

        return results;
    }

    /**
     * Get the information of the project by search the id
     * @param id the id of the project want to search for
     * @return project object
     */
    public ProjectDto getProject(int id){
        ProjectDto projectDto;
        ProjectDao projectDao;

        projectDao = _factory.getProjectDao();
        projectDto = projectDao.findById(id);

        return projectDto;
    }

    public List<ProjectDto> getAllProject(){
        ProjectDto projectDto;
        ProjectDao projectDao;
        projectDao = _factory.getProjectDao();
        List<ProjectDto> projectList = projectDao.findAll();
        return projectList;
    }

    public List<ProjectDto> getAllProjectWithFullInformation() {
        ProjectDao projectDao = _factory.getProjectDao();
        BuildAccess _buildAccess = new BuildAccess(factAccess);

        // Read cache
        Cache cache = factAccess.getProjectCache();
        Element element;
        List<ProjectDto> projectList;
        if ((element = cache.get("projectList")) != null) {
            projectList = (List<ProjectDto>) element.getObjectValue();
        } else {
            projectList = projectDao.findAll();
        }

        for (ProjectDto project : projectList) {
            BuildDto build = _buildAccess.getLastBuild(project.getId());
            project.setLastBuild(build);
        }
        return projectList;
    }

    /**
     *
     * @param projectId
     * @param newProjectName
     * @param newDescription
     */
    public void updateProject(int projectId, String newProjectName, String newDescription) {
        ProjectDto project = _projectDao.findById(projectId);
        project.setName(newProjectName);
        project.setDescription(newDescription);
        _projectDao.update(project);

        // Save to cache
        Cache cache = factAccess.getProjectCache();
        Element element;
        List<ProjectDto> projectList;
        if ((element = cache.get("projectList")) != null) {
            projectList = _projectDao.findAll();
            cache.put(new Element("projectList", projectList));
        }
    }
}
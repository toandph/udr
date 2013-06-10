package architectgroup.fact.access;

import architectgroup.fact.access.util.*;
import architectgroup.fact.util.Result;
import architectgroup.fact.util.State;
import architectgroup.fact.dao.*;
import architectgroup.fact.dto.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import architectgroup.parser.ParserType;
import architectgroup.parser.checkmarx.CheckMarxReportSAXParser;
import architectgroup.parser.goanna.GoannaReportSAXParser;
import architectgroup.parser.imagix4d.ImagixMetricSAXParser;
import architectgroup.parser.k9.K9ReportSAXParser;
import architectgroup.fact.access.util.DBProcess;
import architectgroup.parser.process.ParserProcess;
import architectgroup.parser.xmlreport.ReportParser;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.io.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 1/29/13
 * Time: 4:09 PM
 */
public class BuildAccess {
    private FactAccessFactory _factAccess;
    private DaoFactory _factory;
    private DatabaseDao _database;
    private IssueDao _issueDao;
    private ProjectDao _projectDao;
    private String _buildFolder;

    public BuildAccess(FactAccessFactory factAccess) {
        _factAccess = factAccess;
        _factory = _factAccess.getFactory();
        _database = _factory.getDatabaseDao();
        _projectDao = _factory.getProjectDao();
        _buildFolder = _factory.getContext().getBuildUpload();
    }

    /**
     *
     * @param projectId projectId
     * @param build build
     */
    private void recalculateIssueState(int projectId, BuildDto build) {
        // Count the issue
        BuildDao buildDao = _factory.getBuildDao(projectId);
        _issueDao = _factory.getIssueDao(projectId, build.getId());

        Map<String, Integer> stateCountMap = _issueDao.findState();
        int numberOfNew = stateCountMap.get(State.NEW.toString()) == null ? 0 : stateCountMap.get(State.NEW.toString());
        int numberOfFixed = stateCountMap.get(State.FIXED.toString()) == null ? 0 : stateCountMap.get(State.FIXED.toString());
        int numberOfReoccured = stateCountMap.get(State.REOCCURED.toString()) == null ? 0 : stateCountMap.get(State.REOCCURED.toString());
        int numberOfExisting = stateCountMap.get(State.EXISTING.toString()) == null ? 0 : stateCountMap.get(State.EXISTING.toString());
        build.setNumberOfNewIssue(numberOfNew);
        build.setNumberOfFixedIssue(numberOfFixed);
        build.setNumberOfOpenIssue(numberOfNew + numberOfExisting + numberOfReoccured);
        buildDao.update(build);
    }

    /**
     * Create necessary table
     * @param projectId projectId
     * @param newId newId
     */
    private void initNecessaryTable(int projectId, int newId) {
        _database.createTableIssue(projectId, newId);
        _database.createTableTrace(projectId, newId);
        _database.createTableHistory(projectId, newId);
        _database.createTriggerOnHistory(projectId, newId);
        _database.createTriggerOnHistoryDelete(projectId, newId);
        _database.createTableEntity(projectId, newId);
        _database.createTableEntityMetric(projectId, newId);
        _database.createTableEntityRelationship(projectId, newId);
    }

    /**
     * Create new Build Of the Project
     * Includes : create Table Bug and table Trace
     * Return the id of the new build
     *
     * @param  projectId  the id of the current project
     * @param buildType t
     * @param buildName  t
     * @param description    t
     * @param status    t
     * @param sourceFile t
     * @param xmlFile  t
     * @param version  t
     * @param startTime  t
     * @param endTime  t
     * @return int for id of new build
     */
    public int createNewBuild(int projectId, ParserType buildType, String buildName, String description, String status, CommonsMultipartFile sourceFile, CommonsMultipartFile xmlFile, CommonsMultipartFile imagixFile, String version, Date startTime, Date endTime, String createdBy) {
        IssueSignatureDao issueSignatureDao = _factory.getIssueSignatureDao(projectId);
        BuildDao buildDao = _factory.getBuildDao(projectId);
        ProjectDto project = _projectDao.findById(projectId);

        List<Integer> list = _database.getBuildIds(projectId);
        int newId = list != null ? Collections.max(list) + 1 : 1;

        initNecessaryTable(projectId, newId);

        BuildDto newBuildInfo = new BuildDto();
        newBuildInfo.setId(newId);
        newBuildInfo.setName(buildName);
        newBuildInfo.setDescription(description);
        newBuildInfo.setStatus(status);
        newBuildInfo.setSize(sourceFile.getSize());
        newBuildInfo.setVersion(version);
        newBuildInfo.setStartTime(startTime);
        newBuildInfo.setEndTime(endTime);
        newBuildInfo.setCreatedBy(createdBy);
        newBuildInfo.setType(buildType.toString());
        BuildDto build = buildDao.insert(newBuildInfo);

        /* Parse the file */
        Result result = Result.PARSE_ERROR;
        InputStream is = null;
        try {
            is =  xmlFile.getInputStream();
            ParserProcess dbProcess;
            switch (buildType) {
                case K9:
                    ReportParser k9Parser = new K9ReportSAXParser(is);
                    dbProcess = new DBProcess(k9Parser, _factory, projectId, newId, list);
                    result = k9Parser.parse(dbProcess);
                    break;
                case Checkmarx:
                    ReportParser cmParser = new CheckMarxReportSAXParser(is);
                    dbProcess = new DBProcess(cmParser, _factory, projectId, newId, list);
                    result = cmParser.parse(dbProcess);
                    break;
                case Goanna:
                    ReportParser goannaParser = new GoannaReportSAXParser(is);
                    dbProcess = new DBProcess(goannaParser, _factory, projectId, newId, list);
                    result = goannaParser.parse(dbProcess);
                    break;
            }
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException err) {
                err.printStackTrace();
            }
        }

        // Recheck the trend
        if (result != Result.SUCCESS) {
            issueSignatureDao.addToTrend("#0");
        }
        recalculateIssueState(projectId, build);

        // Upload to folder
        try {
            // Make a folder first
            File file = new File(_buildFolder + "/" + projectId + "/" + newBuildInfo.getId());
            boolean isSuccess = file.mkdir();
            File destSource = new File(_buildFolder + "/" + projectId + "/" + newBuildInfo.getId() + "/source.zip");
            File destXml = new File(_buildFolder + "/" + projectId + "/" + newBuildInfo.getId() + "/issue.xml");
            FileUtils.copyInputStreamToFile(sourceFile.getInputStream(), destSource);
            FileUtils.copyInputStreamToFile(xmlFile.getInputStream(), destXml);
        } catch (Exception err) {
            err.printStackTrace();
        }

        // Save Imagix Function
        try {
            if ( imagixFile != null && imagixFile.getSize() > 0) {
                is =  imagixFile.getInputStream();
                ImagixMetricSAXParser parser = new ImagixMetricSAXParser(is, _factory, projectId, newId);
                parser.parse();
            }
        } catch (Exception err) {
               err.printStackTrace();
        }

        // Save to cache
        String INPUT_ZIP_FILE = _buildFolder + "/" + project.getId() + "/" + newBuildInfo.getId() + "/source.zip";
        ScanZipToTree unZip = new ScanZipToTree();
        SourceTree sourceTree = unZip.scan(INPUT_ZIP_FILE);
        String zipContent = ScanZipToTree.generateTreeJson(sourceTree);
        build.setZipTree(zipContent);
        Cache cache = _factAccess.getBuildCache();
        Element element;
        if ((element = cache.get(projectId)) != null) {
            HashMap<Integer, BuildDto> buildInCache = (HashMap<Integer, BuildDto>) element.getObjectValue();
            buildInCache.put(newId, build);
        }
        return newId;
    }

    /**
     * Delete Build of the project
     *
     * @param  projectId  the name of the current project.
     * @param  buildIdList  the id of the current build.
     * @return int for successfully or failed
     */
    public int deleteBuild(int projectId, @NotNull List<Integer> buildIdList) {
        ProjectDto project = _projectDao.findById(projectId);
        BuildDao buildDao = _factory.getBuildDao(projectId);
        IssueSignatureDao issueSignatureDao = _factory.getIssueSignatureDao(projectId);

        // Find the order of the build Id
        List<BuildDto> buildListNew = buildDao.findAll();
        List<Integer> buildIds = new ArrayList<Integer>();
        List<Integer> buildUpdate = new ArrayList<Integer>();       // Order of build will be updated
        for (BuildDto build : buildListNew) {
            buildIds.add(build.getId());
            if (_issueDao == null) _issueDao = _factory.getIssueDao(projectId, build.getId());     // This issuedao only for temporary, it's not really related with build Id
        }

        // Find build will be updated
        for (int i = 0; i < buildIds.size(); i++)  {
            if (!buildIdList.contains(buildIds.get(i))) {
                if (buildIds.get(i) > Collections.min(buildIdList)) {
                     buildUpdate.add(buildIds.get(i));
                }
            }
        }

        // Drop table trace, issue, history
        for (Integer id : buildIdList) {
            BuildDto buildDto = buildDao.findById(id);
            _database.dropTable(projectId, "trace_build_" + id);
            _database.dropTable(projectId, "history_build_" + id);
            _database.dropTable(projectId, "issue_build_" + id);

            if (buildDto != null)
                buildDao.delete(buildDto);     // Remove the record in the build table

            // Remove upload folder
            try {
                FileUtils.deleteDirectory(new File(_buildFolder + "/" + projectId + "/" + id));
            } catch (IOException er) {
                er.printStackTrace();
            }

            // Clear cache
            Cache buildCache = _factAccess.getBuildCache();
            Element element;
            Map<Integer, BuildDto> buildList;
            if ((element = buildCache.get(projectId)) != null) {
                buildList = (Map<Integer, BuildDto>) element.getObjectValue();
                buildList.remove(id);
            }
        }

        // Update status of related build
        issueSignatureDao.removeTrend(CommonFunction.implodeArrayInt(buildIdList,"#"));
        List<BuildDto> buildListAfterDelete = buildDao.findAll();
        int i = 1;
        for (BuildDto build : buildListAfterDelete)  {
            if (buildUpdate.contains(build.getId())) {
                _issueDao.updateFixedIssue(i);    // Update Fix Issued
                _issueDao.updateState(i);

                BuildDto b = buildDao.findById(build.getId());
                recalculateIssueState(projectId, b);

                // Save to cache
                Cache cache = _factAccess.getBuildCache();
                Element element;
                if ((element = cache.get(projectId)) != null) {
                    HashMap<Integer, BuildDto> buildInCache = (HashMap<Integer, BuildDto>) element.getObjectValue();
                    if (buildInCache.get(build.getId()) != null) {
                        b.setZipTree(buildInCache.get(build.getId()).getZipTree());
                    }
                    buildInCache.put(build.getId(), b);
                }
            }
            i++;
        }

        return 1;
    }

    /**
     * Convert all information into a DTO for easily handle
     * @return the build DTO object
     */
    @NotNull
    public BuildDto toBuildDto(int buildID, String name, String description, Date startTime, Date endTime, String stt, String ver, String type, int size) {
        BuildDto buildDto = new BuildDto();

        buildDto.setId(buildID);
        buildDto.setName(name);
        buildDto.setDescription(description);
        buildDto.setStartTime(startTime);
        buildDto.setEndTime(endTime);
        buildDto.setStatus(stt);
        buildDto.setVersion(ver);
        buildDto.setType(type);
        buildDto.setSize(size);

        return buildDto;

    }

    /**
     *
     * @param projectId pId
     * @param buildID bId
     * @return something
     */
    public BuildDto getBuild(int projectId, int buildID){
       Cache cache = _factAccess.getBuildCache();
       Element element;
       if ((element = cache.get(projectId)) != null) {
            HashMap<Integer, BuildDto> buildInCache = (HashMap<Integer, BuildDto>) element.getObjectValue();
            return buildInCache.get(buildID);
       } else {
            BuildDao buildDao = _factory.getBuildDao(projectId);
            BuildDto buildDto = buildDao.findById(buildID);
            return buildDto;
       }
    }

    /**
     * Get Build List
     * @param projectId
     * @return
     */
    public List<BuildDto> getBuildList(int projectId) {
        Cache cache = _factAccess.getBuildCache();
        Element element;
        if ((element = cache.get(projectId)) != null) {
            List<BuildDto> list = new ArrayList<BuildDto>();
            HashMap<Integer, BuildDto> buildInCache = (HashMap<Integer, BuildDto>) element.getObjectValue();
            for (Integer key : buildInCache.keySet()) {
                list.add(buildInCache.get(key));
            }
            return list;
        } else {
            BuildDao buildDao = _factory.getBuildDao(projectId);
            List<BuildDto> buildList = buildDao.findAll();
            return buildList;
        }
    }

    /**
     * Check the build exists or not
     * @param buildName t
     * @return t
     */
    public boolean checkBuildExist(int projectId, String buildName) {
        BuildDao buildDao = _factory.getBuildDao(projectId);
        BuildDto build = buildDao.findByName(buildName);
        if (build == null) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param projectId
     * @return
     */
    public BuildDto getLastBuild(int projectId) {
        Cache cache = _factAccess.getBuildCache();
        Element element;
        if ((element = cache.get(projectId)) != null) {
            HashMap<Integer, BuildDto> buildInCache = (HashMap<Integer, BuildDto>) element.getObjectValue();
            if (buildInCache.size() > 0) {
                int max = Collections.max(buildInCache.keySet());
                return buildInCache.get(max);
            } else {
                return null;
            }
        } else {
            BuildDao buildDao = _factory.getBuildDao(projectId);
            return buildDao.findLastBuild();
        }
    }

    /**
     *
     * @param projectId project Id
     * @param n number of records
     * @return list of build
     */
    public List<BuildDto> getLastNBuild(int projectId, int n) {
        BuildDao buildDao = _factory.getBuildDao(projectId);
        return buildDao.findLastNBuild(n);
    }

    /**
     *
     * @param projectId project
     * @param buildId buildId
     * @return return
     */
    public BuildDto getBuildDetail(int projectId, int buildId) {
        BuildDao buildDao = _factory.getBuildDao(projectId);
        BuildDto buildDto = buildDao.findById(buildId);
        return buildDto;
    }

    /**
     *
     * @param projectId projectId
     * @param buildId   buildId
     * @return test
     */
    public void updateExistingBuild(int projectId, int buildId, String buildName, String description) {
        BuildDao buildDao = _factory.getBuildDao(projectId);
        BuildDto detail = buildDao.findById(buildId);
        if (detail != null) {
            detail.setName(buildName);
            detail.setDescription(description);
            buildDao.update(detail);

            // Save to cache
            Cache cache = _factAccess.getBuildCache();
            Element element;
            if ((element = cache.get(projectId)) != null) {
                HashMap<Integer, BuildDto> buildInCache = (HashMap<Integer, BuildDto>) element.getObjectValue();
                detail.setZipTree(buildInCache.get(detail.getId()).getZipTree());
                buildInCache.put(buildId, detail);
            }
        }
    }

    /**
     * Get the previous build id that next to the current build id
     * @param projectId projectId
     * @param buildId buildId
     * @return null if there is no previous build
     */
    public BuildDto getPreviousBuild(int projectId, int buildId) {
        List<BuildDto> buildList = getBuildList(projectId);
        BuildDto result = null;
        for (BuildDto build : buildList) {
            if (buildId == build.getId()) {
                return result;
            } else {
                result = build;
            }
        }
        return null;
    }
}

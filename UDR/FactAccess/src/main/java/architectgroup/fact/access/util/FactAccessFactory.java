package architectgroup.fact.access.util;

import architectgroup.fact.Const;
import architectgroup.fact.Context;
import architectgroup.fact.dao.DaoFactory;
import architectgroup.fact.dao.DaoUtil;
import architectgroup.fact.dto.BuildDto;
import architectgroup.fact.dto.ProjectDto;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 5/6/12
 * Time: 10:47 AM
 */
public class FactAccessFactory {
    private String _buildUpload;
    private Cache _buildCache;
    private Cache _projectCache;
    DaoFactory _factDB;

    @Autowired
    private CacheManager _cacheManager;

    @NotNull
    private Properties _prop = new Properties();
    private String _host, _user, _pass;

    public FactAccessFactory() {
        this("/fact.properties");
    }

    public FactAccessFactory(String pathToProperties) {
        try {
            _prop.load(getClass().getResourceAsStream(pathToProperties));
            _host = _prop.getProperty("factDB.url");
            _user = _prop.getProperty("factDB.username");
            _pass = _prop.getProperty("factDB.password");
            _buildUpload = _prop.getProperty("fact.upload");

            Context ctx = new Context();
            ctx.setUrl("jdbc:mysql://" + _host);
            ctx.setLogFile("D:/logs/log.txt");
            ctx.setBuildUpload(_buildUpload);
            ctx.setPassword(_pass);
            ctx.setUsername(_user);
            ctx.setDbType(Const.MYSQL_JDBC_DATABASE);

            _factDB = DaoUtil.getInstance(ctx);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void initializeCache() {
        _buildCache = _cacheManager.getCache("build");
        _projectCache = _cacheManager.getCache("project");

        // Cache all the build list
        List<ProjectDto> projects = _factDB.getProjectDao().findAll();
        for (ProjectDto project : projects) {
            List<BuildDto> builds = _factDB.getBuildDao(project.getId()).findAll();

            HashMap<Integer, BuildDto> buildList = new HashMap<Integer, BuildDto>();
            for (BuildDto build : builds) {
                String INPUT_ZIP_FILE = _buildUpload + "/" + project.getId() + "/" + build.getId() + "/source.zip";
                ScanZipToTree unZip = new ScanZipToTree();
                SourceTree sourceTree = unZip.scan(INPUT_ZIP_FILE);
                String zipContent = unZip.generateTreeJson(sourceTree);
                build.setZipTree(zipContent);
                buildList.put(build.getId(), build);
            }
            _buildCache.put(new Element(project.getId(), buildList));
        }
        _projectCache.put(new Element("projectList", projects));
    }

    public DaoFactory getFactory() {
        return _factDB;
    }

    public Cache getBuildCache() {
        return _buildCache;
    }

    public Cache getProjectCache() {
        return _projectCache;
    }
}

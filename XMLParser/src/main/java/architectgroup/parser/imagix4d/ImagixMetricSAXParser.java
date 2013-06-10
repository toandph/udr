package architectgroup.parser.imagix4d;

import architectgroup.fact.dao.DaoFactory;
import architectgroup.fact.dao.EntityDao;
import architectgroup.fact.dto.EntityDto;
import architectgroup.fact.dto.MetricDto;
import architectgroup.fact.dto.RelationshipDto;
import architectgroup.fact.util.Result;
import architectgroup.parser.xmlreport.ReportHelper;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Toan Dang, Architect Group INC
 * User: Administrator
 * Date: 3/23/12
 * Time: 10:56 AM
 */

public class ImagixMetricSAXParser extends DefaultHandler {
    private static String METRIC_ROOT_TAG = "Metrics";
    private static String PROJECT_TAG = "Project";
    private static String ENTITIES_TAG = "Entities";
    private static String ENTITY_TAG = "Entity";
    private static String[] PROJECT_METRIC_TAG = {"PrjKBs","PrjLines","PrjStmts","PrjStmtCnt","PrjCmtCnt","PrjCmtRatio","PrjCppFiles", "PrjCFiles", "PrjHdrFiles", "PrjJavFiles", "PrjJCFiles", "PrjNames", "PrjCls" ,"PrjTrCls" ,"PrjTmpls" , "PrjStrUns", "PrjFncs" ,"PrjTypes" , "PrjRegFncs", "PrjLibFncs", "PrjVars", "PrjGlbVars" ,"PrjLclVars", "PrjMacros"};
    private static String[] DIRECTORY_PROPERTY = {"ID", "Name", "Size", "Path", "Owner", "Permission", "Modification_Date"};
    private static String[] FILE_PROPERTY = {"ID", "Name", "Path", "Size", "Owner", "Permission", "Modification_Date"};
    private static String[] CLASS_PROPERTY = {"ID", "Name", "Kind"};
    private static String[] FUNCTION_PROPERTY = {"ID", "Name", "Scope", "LineNumber"};
    private static String[] VARIABLE_PROPERTY = {"ID", "Name"};
    private static String RELATION_SHIP_TAG = "Relation";

    private HashMap<String, Boolean> checkMap = new HashMap<String, Boolean>();     // check duplicate for entity
    private HashMap<String, Boolean> checkRelMap = new HashMap<String, Boolean>();     // check duplicate for relationship

    protected InputStream is;
    protected DaoFactory _daoFactory;
    protected SAXParserFactory factory;
    protected SAXParser saxParser;
    private int buildId;

    private EntityDto projectEntity;
    private EntityDto entity;
    private List<MetricDto> projectMetrics;
    private List<RelationshipDto> buffRelationship;
    private List<MetricDto> metrics;

    private EntityDao entityDao;
    private MetricDto tmpMetric;
    private String value = "";
    private boolean tagStart = false;
    private String entityType = "";

    private List<EntityDto> buffEntity;

    public ImagixMetricSAXParser(InputStream is, DaoFactory daoFactory, int projectId, int buildId) {
        this.is = is;
        factory = SAXParserFactory.newInstance();
        _daoFactory = daoFactory;
        this.buildId = buildId;
        entityDao = _daoFactory.getEntityDao(projectId, buildId);

        try {
            saxParser = factory.newSAXParser();
        } catch (Exception err) {
            saxParser = null;
            err.printStackTrace();
        }
    }

    /**
     * Parse the XML File
     * @return the result of return success or error
     */
    public Result parse() {
        Result result = Result.SUCCESS;
        try {
            saxParser.parse(is, this);
        } catch (Exception err) {
            err.printStackTrace();
            result = Result.PARSE_ERROR;
        }
        return result;
    }

    @Override
    public void startElement (String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
        tagStart = true;
        if (qName.equalsIgnoreCase(METRIC_ROOT_TAG)) {
            // Initialize
            System.out.println(new Date());
            buffEntity = new ArrayList<EntityDto>();
            buffRelationship = new ArrayList<RelationshipDto>();
            projectEntity = new EntityDto();
            projectEntity.setId("project");
            projectMetrics = new ArrayList<MetricDto>();
        } else if (qName.equalsIgnoreCase(ENTITY_TAG)) {
            entityType = attributes.getValue("type");
            if (entityType.equalsIgnoreCase("directory") || entityType.equalsIgnoreCase("file") || entityType.equalsIgnoreCase("class") || entityType.equalsIgnoreCase("function") || entityType.equalsIgnoreCase("variable")) {
                entity = new EntityDto();
                metrics = new ArrayList<MetricDto>();
            }
        } else if (qName.equalsIgnoreCase(RELATION_SHIP_TAG)) {
            RelationshipDto rel = new RelationshipDto();
            rel.setEntityA(ReportHelper.md5(attributes.getValue("entityA")));
            rel.setEntityB(ReportHelper.md5(attributes.getValue("entityB")));
            rel.setType(attributes.getValue("type"));
            rel.setValue(attributes.getValue("attr"));
            processRel(rel);
        } else if (qName.equalsIgnoreCase("Metric")) {
            if (entityType.equalsIgnoreCase("directory")) {
                metrics = new ArrayList<MetricDto>();
                for (int i = 0; i < attributes.getLength(); i++) {
                    String metricName = attributes.getQName(i);
                    String metricValue = attributes.getValue(i);
                    tmpMetric = new MetricDto();
                    tmpMetric.setType("specd.directory");
                    tmpMetric.setName(metricName);
                    tmpMetric.setValue(metricValue);
                    metrics.add(tmpMetric);
                }
            } else if (entityType.equalsIgnoreCase("file")) {
                metrics = new ArrayList<MetricDto>();
                for (int i = 0; i < attributes.getLength(); i++) {
                    String metricName = attributes.getQName(i);
                    String metricValue = attributes.getValue(i);
                    tmpMetric = new MetricDto();
                    tmpMetric.setType("specd.file");
                    tmpMetric.setName(metricName);
                    tmpMetric.setValue(metricValue);
                    metrics.add(tmpMetric);
                }
            } else if (entityType.equalsIgnoreCase("class")) {
                metrics = new ArrayList<MetricDto>();
                for (int i = 0; i < attributes.getLength(); i++) {
                    String metricName = attributes.getQName(i);
                    String metricValue = attributes.getValue(i);
                    tmpMetric = new MetricDto();
                    tmpMetric.setType("specd.class");
                    tmpMetric.setName(metricName);
                    tmpMetric.setValue(metricValue);
                    metrics.add(tmpMetric);
                }
            } else if (entityType.equalsIgnoreCase("function")) {
                metrics = new ArrayList<MetricDto>();
                for (int i = 0; i < attributes.getLength(); i++) {
                    String metricName = attributes.getQName(i);
                    String metricValue = attributes.getValue(i);
                    tmpMetric = new MetricDto();
                    tmpMetric.setType("specd.function");
                    tmpMetric.setName(metricName);
                    tmpMetric.setValue(metricValue);
                    metrics.add(tmpMetric);
                }
            } else if (entityType.equalsIgnoreCase("variable")) {
                metrics = new ArrayList<MetricDto>();
                for (int i = 0; i < attributes.getLength(); i++) {
                    String metricName = attributes.getQName(i);
                    String metricValue = attributes.getValue(i);
                    tmpMetric = new MetricDto();
                    tmpMetric.setType("specd.variable");
                    tmpMetric.setName(metricName);
                    tmpMetric.setValue(metricValue);
                    metrics.add(tmpMetric);
                }
            }
        } else {
            for (String tag : PROJECT_METRIC_TAG) {
                if (tag.equalsIgnoreCase(qName)) {
                    tmpMetric = new MetricDto();
                    tmpMetric.setType("specd.project");
                    tmpMetric.setName(tag);
                    break;
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase(PROJECT_TAG)) {
            projectEntity.setMetrics(projectMetrics);
            projectEntity.setEntityType("project");
            projectEntity.setFullId("project");
            entityDao.insert(projectEntity);
        }
        // Set Value When ending tag //
        for (String tag : PROJECT_METRIC_TAG) {
            if (tag.equalsIgnoreCase(qName)) {
                tmpMetric.setValue(value);
                projectMetrics.add(tmpMetric);
                break;
            }
        }

        if (entityType.equalsIgnoreCase("directory")) {
            int i;
            for (i = 0; i < DIRECTORY_PROPERTY.length; i++) {
                if (DIRECTORY_PROPERTY[i].equalsIgnoreCase(qName)) {
                    switch (i) {
                        case 0: entity.setId(ReportHelper.md5(value)); entity.setFullId(value); break;
                        case 1: entity.setName(value); break;
                        case 2: entity.setSize(Long.parseLong(value)); break;
                        case 3: entity.setPath(value); break;
                        case 4: entity.setOwner(value); break;
                        case 5: entity.setPermission(value); break;
                        case 6: entity.setModDate(value); break;
                    }
                    break;
                }
            }
            if (qName.equalsIgnoreCase(ENTITY_TAG)) {
                entity.setEntityType("directory");
                entity.setMetrics(metrics);
                process(entity);
            }
        } else if (entityType.equalsIgnoreCase("file")) {
            int i;
            for (i = 0; i < FILE_PROPERTY.length; i++) {
                if (FILE_PROPERTY[i].equalsIgnoreCase(qName)) {
                    switch (i) {
                        case 0: entity.setId(ReportHelper.md5(value)); entity.setFullId(value); break;
                        case 1: entity.setName(value); break;
                        case 2: entity.setPath(value); break;
                        case 3: entity.setSize(Long.parseLong(value)); break;
                        case 4: entity.setOwner(value); break;
                        case 5: entity.setPermission(value); break;
                        case 6: entity.setModDate(value); break;
                    }
                    break;
                }
            }
            if (qName.equalsIgnoreCase(ENTITY_TAG)) {
                entity.setEntityType("file");
                entity.setMetrics(metrics);
                process(entity);
            }
        } else if (entityType.equalsIgnoreCase("class")) {
            int i;
            for (i = 0; i < DIRECTORY_PROPERTY.length; i++) {
                if (DIRECTORY_PROPERTY[i].equalsIgnoreCase(qName)) {
                    switch (i) {
                        case 0: entity.setId(ReportHelper.md5(value)); entity.setFullId(value); break;
                        case 1: entity.setName(value); break;
                        case 2: entity.setKind(value); break;
                    }
                    break;
                }
            }
            if (qName.equalsIgnoreCase(ENTITY_TAG)) {
                entity.setEntityType("class");
                entity.setMetrics(metrics);
                process(entity);
            }
        } else if (entityType.equalsIgnoreCase("function")) {
            int i;
            for (i = 0; i < DIRECTORY_PROPERTY.length; i++) {
                if (DIRECTORY_PROPERTY[i].equalsIgnoreCase(qName)) {
                    switch (i) {
                        case 0: entity.setId(ReportHelper.md5(value)); entity.setFullId(value); break;
                        case 1: entity.setName(value); break;
                        case 2: entity.setScope(value); break;
                    }
                    break;
                }
            }
            if (qName.equalsIgnoreCase(ENTITY_TAG)) {
                entity.setEntityType("function");
                entity.setMetrics(metrics);
                process(entity);
            }
        } else if (entityType.equalsIgnoreCase("variable")) {
            int i;
            for (i = 0; i < DIRECTORY_PROPERTY.length; i++) {
                if (DIRECTORY_PROPERTY[i].equalsIgnoreCase(qName)) {
                    switch (i) {
                        case 0: entity.setId(ReportHelper.md5(value)); entity.setFullId(value); break;
                        case 1: entity.setName(value); break;
                    }
                    break;
                }
            }
            if (qName.equalsIgnoreCase(ENTITY_TAG)) {
                entity.setEntityType("variable");
                entity.setMetrics(metrics);
                process(entity);
            }
        }
        if (qName.equalsIgnoreCase("Entities")) {
            end();
        }

        if (qName.equalsIgnoreCase("Relationships")) {
            endRelationship();
        }
        tagStart = true;
    }

    private void process(EntityDto entity) {
        if (buffEntity.size() < 10000) {
            if (!checkMap.containsKey(entity.getId())) {
                checkMap.put(entity.getId(), true);
                buffEntity.add(entity);
            }
        } else {
            entityDao.insert(buffEntity);
            buffEntity = new ArrayList<EntityDto>();
            System.out.println("Them 1 cai " + 10000);
        }

    }

    private void processRel(RelationshipDto rel) {
        if (buffRelationship.size() < 10000) {
            if (!checkRelMap.containsKey(rel.getEntityA() + rel.getType() + rel.getEntityB())) {
                checkRelMap.put(rel.getEntityA() + rel.getType() + rel.getEntityB(), true);
                buffRelationship.add(rel);
            }
        } else {
            entityDao.insertRelationships(buffRelationship);
            buffRelationship = new ArrayList<RelationshipDto>();
            System.out.println("Them 1 cai relationship " + 10000);
        }
    }


    private void end() {
        if (buffEntity.size() > 0) {
            entityDao.insert(buffEntity);
            // Finish writing the entity
            buffEntity = new ArrayList<EntityDto>();
            System.out.println("finished writing the entity");
        }
    }

    private void endRelationship() {
        if (buffRelationship.size() > 0) {
            entityDao.insertRelationships(buffRelationship);
            // Finish writing the entity
            buffRelationship = new ArrayList<RelationshipDto>();
            System.out.println("finished writing the relationship");
        }
        System.out.println(new Date());
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (tagStart == true) {
            value = new String(ch, start, length);
            tagStart = false;
        }   else {
            value += new String(ch, start, length);
        }
    }
}

package architectgroup.fact.dto;

import java.util.List;
import java.io.Serializable;

public class EntityDto implements Serializable
{
    private String id;
    private String fullId;
    private String name = "-1";
    private String path = "-1";
    private String owner = "-1";
    private String permission = "-1";
    private String mod_date = "-1";
    private long size;
    private String category= "-1" ;
    private String scope = "-1";
    private String kind= "-1";
    private String defined= "-1";
    private String type= "-1";
    private String entityType = "";
    private String baseClass = "-1";
    private List<MetricDto> metrics;
    private List<RelationshipDto> relationship;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getModDate() {
        return mod_date;
    }

    public void setModDate(String mod_date) {
        this.mod_date = mod_date;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDefined() {
        return defined;
    }

    public void setDefined(String defined) {
        this.defined = defined;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MetricDto> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<MetricDto> metrics) {
        this.metrics = metrics;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public List<RelationshipDto> getRelationship() {
        return relationship;
    }

    public void setRelationship(List<RelationshipDto> relationship) {
        this.relationship = relationship;
    }

    public String getBaseClass() {
        return baseClass;
    }

    public void setBaseClass(String baseClass) {
        this.baseClass = baseClass;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }
}

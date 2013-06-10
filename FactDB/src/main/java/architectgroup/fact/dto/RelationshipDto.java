package architectgroup.fact.dto;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 5/15/13
 * Time: 4:39 PM
 */
public class RelationshipDto {
    private String value = "";
    private String type;
    private String entityB;
    private String entityA;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntityB() {
        return entityB;
    }

    public void setEntityB(String entityB) {
        this.entityB = entityB;
    }

    public String getEntityA() {
        return entityA;
    }

    public void setEntityA(String entityA) {
        this.entityA = entityA;
    }
}

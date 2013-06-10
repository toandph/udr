package architectgroup.fact.util;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/28/13
 * Time: 10:00 AM
 */
public enum Result {
    SUCCESS(0, "There is no problem"),
    FAILED(-1, "Fail with unknown result"),
    DB_EXIST(-2, "This database is already exists"),
    DB_NOT_EXIST(-3, "This database is not exists"),
    DB_ERROR(-4, "Some error in database"),
    DB_INSERT_ERROR(-5, "Some error when insert data"),
    DB_UPDATE_ERROR(-6, "Error occur when update data"),
    DB_DELETE_ERROR(-7, "Error occur when delete data"),
    DB_CREATE_TRIGGER_ERROR(-8, "Error occur when create trigger history"),
    DB_CREATE_TABLE_ERROR(-9, "Error occur when create table"),
    DB_DROP_ERROR(-10, "Error occur when drop table"),
    PARSE_ERROR(-11, "Parse error");

    private final int code;
    private final String description;

    private Result(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}

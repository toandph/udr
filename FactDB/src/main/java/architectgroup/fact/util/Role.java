package architectgroup.fact.util;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/28/13
 * Time: 10:00 AM
 */
public enum Role {
    ADMIN(1024, "ROLE_ADMIN"),
    USER(4, "ROLE_USER"),
    ANONYMOUS(1, "ROLE_ANONYMOUS");

    private final int code;
    private final String description;

    private Role(int code, String description) {
        this.code = code;
        this.description = description;
    }

    Role(int code) {
        this.code = code;
        switch (code) {
            case 1024: this.description = "ROLE_ADMIN";
                  break;
            case 4: this.description = "ROLE_USER";
                break;
            default: this.description = "ROLE_ANONYMOUS";
                break;
        }
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

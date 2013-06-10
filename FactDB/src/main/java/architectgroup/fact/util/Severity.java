package architectgroup.fact.util;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/28/13
 * Time: 10:00 AM
 */
public enum Severity {
    CRITICAL(1, "Critical"),
    SEVERE(2, "Severe"),
    ERROR(3, "Error"),
    UNEXPECTED(4, "Unexpected"),
    INVESTIGATE(5, "Investigate"),
    WARNING(6, "Warning"),
    SUGGESTION(7, "Suggestion"),
    STYLE(8, "Style"),
    REVIEW(9, "Review"),
    INFO(10, "Info");

    private final int code;
    private final String description;

    private Severity(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    public static int getCode(String description) {
        if (description.equalsIgnoreCase("Critical")) {
            return 1;
        } else if (description.equalsIgnoreCase("Severe")) {
            return 2;
        } else if (description.equalsIgnoreCase("Error")) {
            return 3;
        } else if (description.equalsIgnoreCase("Unexpected")) {
            return 4;
        } else if (description.equalsIgnoreCase("Investigate")) {
            return 5;
        } else if (description.equalsIgnoreCase("Warning")) {
            return 6;
        } else if (description.equalsIgnoreCase("Suggestion")) {
            return 7;
        } else if (description.equalsIgnoreCase("Style")) {
            return 8;
        } else if (description.equalsIgnoreCase("Review")) {
            return 9;
        } else if (description.equalsIgnoreCase("Info")) {
            return 10;
        } else {
            return 10;
        }
    }
    
    @Override
    public String toString() {
        return code + ": " + description;
    }
}

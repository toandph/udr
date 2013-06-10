package architectgroup.fact.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/28/13
 * Time: 4:59 PM
 */
public class EnumUtil {
    private static final Map<Integer, Role> intToRole = new HashMap<Integer, Role>();
    static {
        for (Role type : Role.values()) {
            intToRole.put(type.getCode(), type);
        }
    }

    private static final Map<Integer, Severity> intToSeverity = new HashMap<Integer, Severity>();
    static {
        for (Severity type : Severity.values()) {
            intToSeverity.put(type.getCode(), type);
        }
    }

    /**
     * Convert from integer to Role
     * @param i
     * @return
     */
    public static Role roleFromInt(int i) {
        Role type = intToRole.get(Integer.valueOf(i));
        if (type == null)
            return Role.ANONYMOUS;
        return type;
    }

    /**
     * Convert from integer to Severity
     * @param i
     * @return
     */
    public static Severity severityFromInt(int i) {
        Severity type = intToSeverity.get(Integer.valueOf(i));
        if (type == null)
            return Severity.INFO;
        return type;
    }
}

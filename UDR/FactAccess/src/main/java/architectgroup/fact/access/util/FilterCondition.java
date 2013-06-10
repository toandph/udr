package architectgroup.fact.access.util;
import architectgroup.fact.util.Severity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/18/13
 * Time: 6:04 PM
 */

class Condition {
    public char ruleMod;
    public String ruleName;
    public String ruleValue;
}

public class FilterCondition {
    @NotNull
    private List<Condition> _condition = new ArrayList<Condition>();

    /**
     *
     * @param filterString
     */
    public FilterCondition(@NotNull String filterString) {
        String[] listFilter = filterString.split(";");
        for (String filter : listFilter) {
            addCondition(filter);
        }
    }

    /**
     *
     * @param conditionString
     */
    public void addCondition(@NotNull String conditionString) {
        Condition newCondition = new Condition();
        String[] conds = conditionString.split(":", 2);

        newCondition.ruleMod = conditionString.charAt(0);
        if (newCondition.ruleMod != '+' && newCondition.ruleMod != '-') {
            newCondition.ruleMod = '+';
        }
        newCondition.ruleName = conds[0].substring(1).toLowerCase();
        newCondition.ruleValue = conds[1];
        _condition.add(newCondition);
    }

    /**
     *
     * @return
     */
    @NotNull
    public String toSQL() {
        String sWhere = "";
        for (Condition cond : _condition) {
            if (sWhere.length() == 0) {
                sWhere += toSQL(cond);
            } else {
                sWhere += " AND " + toSQL(cond);
            }
        }
        return sWhere;
    }

    /**
     *
     * @param cond
     * @return
     */
    @NotNull
    public String toSQL(@NotNull Condition cond) {
        String sql = "";
        String[] values = cond.ruleValue.split(",");
        Map<String, String> ruleNames = new HashMap<String, String>();
        ruleNames.put("scope", "file");
        ruleNames.put("method", "method");
        ruleNames.put("state", "state");
        ruleNames.put("status", "citingstatus");
        ruleNames.put("code", "code");
        ruleNames.put("severity", "severitylevel");

        if (ruleNames.containsKey(cond.ruleName)) {
            String tmp = "";
            if (cond.ruleName.equals("severity")) {
                for (String value : values) {
                    int code = Severity.getCode(value);
                    if (tmp.length() == 0) {
                        tmp += ruleNames.get(cond.ruleName) + " = " + code;
                    } else {
                        tmp += " OR " + ruleNames.get(cond.ruleName) + " = " + code;
                    }
                }
            } else {
                for (String value : values) {
                    value = (value.length() == 0) ? "%%" : value.replace('*', '%');
                    if (tmp.length() == 0) {
                        tmp += ruleNames.get(cond.ruleName) + " LIKE '" + value.replace("\\","\\\\\\\\") + "'";
                    } else {
                        tmp += " OR " + ruleNames.get(cond.ruleName) + " LIKE '" + value.replace("\\","\\\\\\\\") + "'";
                    }
                }
            }
            if (cond.ruleMod == '-') {
                sql += " NOT ( " + tmp + " ) ";
            } else {
                sql += "( " + tmp + " )";
            }
        } else {
            sql = " ( 1 = 1 ) ";
        }
        return sql;
    }
}

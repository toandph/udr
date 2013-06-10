package architectgroup.udr.webserver.validator;

import architectgroup.fact.access.util.CommonFunction;
import org.jetbrains.annotations.Nullable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/4/13
 * Time: 9:13 PM
 */
public class FilterOnBuildValidator implements ConstraintValidator<CheckFilterOnBuild, String> {

    private String extension;

    public void initialize(CheckFilterOnBuild constraintAnnotation) {
    }

    public boolean isValid(@Nullable String object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }
        else {
            if (object.isEmpty()) {
                return false;
            } else {
                String[] b = object.split(",");
                for (String t : b) {
                    if (!t.equalsIgnoreCase("all")) {
                        if (!CommonFunction.isNumeric(t)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

}

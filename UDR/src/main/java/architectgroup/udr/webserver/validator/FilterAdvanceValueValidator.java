package architectgroup.udr.webserver.validator;

import org.jetbrains.annotations.Nullable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/4/13
 * Time: 9:13 PM
 */
public class FilterAdvanceValueValidator implements ConstraintValidator<CheckAdvanceValue, String> {
    public void initialize(CheckAdvanceValue constraintAnnotation) {
    }

    public boolean isValid(@Nullable String object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }
        else {
            if (object.isEmpty()) {
                return true;
            } else {
                Pattern pattern = Pattern.compile("^(([+?\\-?]?[\\w]+:([\\w\\.]+\\,)*([\\w\\.]+))\\;)*([+?\\-?]?[\\w]+:([\\w\\.]+\\,)*([\\w\\.]+))$");
                Matcher matcher = pattern.matcher(object);
                if (matcher.matches()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

}

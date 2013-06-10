package architectgroup.udr.webserver.validator;

import org.jetbrains.annotations.NotNull;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/4/13
 * Time: 9:10 PM
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = FilterOnBuildValidator.class)
@Documented
public @interface CheckFilterOnBuild {

    @NotNull String message() default "{error.filter.on-build-wrong}";

    @NotNull Class<?>[] groups() default {};

    @NotNull Class<? extends Payload>[] payload() default {};
}
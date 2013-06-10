package architectgroup.udr.webserver.validator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/4/13
 * Time: 9:13 PM
 */
public class ExtensionValidator implements ConstraintValidator<CheckExtension, CommonsMultipartFile> {

    private String extension;

    public void initialize(@NotNull CheckExtension constraintAnnotation) {
        this.extension = constraintAnnotation.extension();
    }

    public boolean isValid(@Nullable CommonsMultipartFile object, ConstraintValidatorContext constraintContext) {
        if (object == null) {
            return true;
        }
        else {
            if (object.isEmpty()) {
                return false;
            }

            String fileName = object.getOriginalFilename();
            String objectExtension = fileName.substring(fileName.lastIndexOf('.'));

            if (extension.equalsIgnoreCase(objectExtension)) {
                return true;
            }
            else {
                return false;
            }
        }
    }

}

package architectgroup.udr.webserver.model;

import architectgroup.udr.webserver.validator.CheckExtension;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 5/2/13
 * Time: 5:58 PM
 */
public class LicenseModel {
    private CommonsMultipartFile lic;

    public CommonsMultipartFile getLic() {
        return lic;
    }

    public void setLic(CommonsMultipartFile lic) {
        this.lic = lic;
    }
}

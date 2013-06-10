/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 1/22/13
 * Time: 1:57 PM
 */
package architectgroup.udr.webserver.mvc;
import org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver;

import java.lang.Override;
import java.lang.String;

/* This class map from the url to the controller method
   Example: outside-list --> outsideList()
 */
public class MyInternalPathMethodResolver extends InternalPathMethodNameResolver {
    @Override
    protected String extractHandlerMethodNameFromUrlPath(String uri) {
        String methodName = super.extractHandlerMethodNameFromUrlPath(uri);
        if (methodName.contains("-")) {
            int i = methodName.indexOf("-");
            String find = "-" + methodName.charAt(i+1);
            String replace = find.toUpperCase().substring(1);
            return methodName.replace(find,replace);
        }
        return methodName;
    }
}

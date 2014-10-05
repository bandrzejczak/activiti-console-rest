package pl.edu.pw.ii.ActivitiConsole.api;

import org.restlet.Request;

public class RequestUtils {

    public static String getRelativePath(Request request) {
        String absolutePath = request.getResourceRef().getPath();
        String servicePath = request.getRootRef().getPath();
        if(servicePath == null)
            return absolutePath;
        return absolutePath.substring(servicePath.length());
    }

    public static String createPathRegex(String path) {
        return path.replaceAll("\\{(.*?)\\}", "([^/]*?)");
    }

}

package pl.edu.pw.ii.bpmConsole.rest.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.FilterRegistration;

public class CrossOriginSettings {

    @JsonProperty
    public String allowedOrigins;

    @JsonProperty
    public String allowedMethods;

    @JsonProperty
    public String allowedHeaders;

    @JsonProperty
    public String exposedHeaders;

    public void applySettingsTo(FilterRegistration.Dynamic crossOriginFilter) {
        if(allowedOrigins != null)
            crossOriginFilter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, allowedOrigins);
        if(allowedMethods != null)
            crossOriginFilter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, allowedMethods);
        if(allowedHeaders != null)
            crossOriginFilter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, allowedHeaders);
        if(exposedHeaders != null)
            crossOriginFilter.setInitParameter(CrossOriginFilter.EXPOSED_HEADERS_PARAM, exposedHeaders);
    }
}

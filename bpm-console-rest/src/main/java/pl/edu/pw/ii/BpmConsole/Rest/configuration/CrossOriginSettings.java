package pl.edu.pw.ii.BpmConsole.Rest.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.servlet.FilterRegistration;

public class CrossOriginSettings {

    @JsonProperty
    public String allowedOrigins;

    @JsonProperty
    public String allowedMethods;

    @JsonProperty
    public String allowedHeaders;

    public void applySettingsTo(FilterRegistration.Dynamic crossOriginFilter) {
        if(allowedOrigins != null) crossOriginFilter.setInitParameter("allowedOrigins", allowedOrigins);
        if(allowedMethods != null) crossOriginFilter.setInitParameter("allowedMethods", allowedMethods);
        if(allowedHeaders != null) crossOriginFilter.setInitParameter("allowedHeaders", allowedHeaders);
    }
}

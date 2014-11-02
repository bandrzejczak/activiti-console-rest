package pl.edu.pw.ii.BpmConsole.Rest.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.springframework.context.ApplicationContext;

import javax.validation.constraints.NotNull;

/**
 * Created by bandrzejczak on 25.10.14.
 */
public class BpmConsoleConfiguration extends Configuration {

    @JsonProperty("crossOrigin")
    @NotNull
    public CrossOriginSettings crossOriginConfiguration = new CrossOriginSettings();

    @JsonProperty("db")
    @NotNull
    public DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();

    @JsonProperty
    @NotNull
    public String bpmProcessEngineBuilderClass;

    public ApplicationContext springContext;

}

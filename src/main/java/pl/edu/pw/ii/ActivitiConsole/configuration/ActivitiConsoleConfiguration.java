package pl.edu.pw.ii.ActivitiConsole.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.springframework.context.ApplicationContext;

import javax.validation.constraints.NotNull;

/**
 * Created by bandrzejczak on 25.10.14.
 */
public class ActivitiConsoleConfiguration extends Configuration {

    @JsonProperty("crossOrigin")
    @NotNull
    public CrossOriginSettings crossOriginConfiguration = new CrossOriginSettings();

    @JsonProperty("db")
    @NotNull
    public DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();

    public ApplicationContext springContext;

}

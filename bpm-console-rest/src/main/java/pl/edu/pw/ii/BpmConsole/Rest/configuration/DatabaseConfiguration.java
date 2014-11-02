package pl.edu.pw.ii.BpmConsole.Rest.configuration;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.sql.Driver;

public class DatabaseConfiguration {

    @JsonProperty(required = true)
    @NotNull
    public Type type;

    @JsonProperty(required = true)
    @NotNull
    public String url;

    @JsonProperty(required = true)
    @NotNull
    public String userName;

    @JsonProperty(required = true)
    @NotNull
    public String password;

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum Type{
        mysql(com.mysql.jdbc.Driver.class), h2(org.h2.Driver.class);

        public Driver driver;

        Type(Class<? extends Driver> driver) {
            try {
                this.driver = driver.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}

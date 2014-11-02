package pl.edu.pw.ii.ActivitiConsole.configuration;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

/**
 * Created by bandrzejczak on 26.10.14.
 */
public abstract class DataSourceFactory {

    public static DataSource fromConfiguration(DatabaseConfiguration databaseConfiguration) {
        return new SimpleDriverDataSource(
                databaseConfiguration.type.driver,
                databaseConfiguration.url,
                databaseConfiguration.userName,
                databaseConfiguration.password
        );
    }
}
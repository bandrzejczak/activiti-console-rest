package pl.edu.pw.ii.bpmConsole.rest.configuration;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

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
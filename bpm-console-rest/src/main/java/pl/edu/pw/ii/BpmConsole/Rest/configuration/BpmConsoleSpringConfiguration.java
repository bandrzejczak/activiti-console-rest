package pl.edu.pw.ii.BpmConsole.Rest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.edu.pw.ii.BpmConsole.Interfaces.ProcessEngine;
import pl.edu.pw.ii.BpmConsole.Interfaces.ProcessEngineBuilder;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@ComponentScan("pl.edu.pw.ii.BpmConsole.Rest")
@EnableTransactionManagement //TODO Do I need it now?
public class BpmConsoleSpringConfiguration {

    @Autowired
    private BpmConsoleConfiguration configuration;

    @Bean
    public DataSource dataSource() throws SQLException {
        return DataSourceFactory.fromConfiguration(configuration.databaseConfiguration);
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public ProcessEngine processEngine() throws Exception {
        ProcessEngineBuilder processEngineBuilder =
                (ProcessEngineBuilder) Class
                        .forName(configuration.bpmEngineImplementationClass)
                        .newInstance();
        return processEngineBuilder
                .usingDatabase(configuration.databaseConfiguration.type.toString(), dataSource())
                .build();
    }

}

package pl.edu.pw.ii.bpmConsole.rest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.edu.pw.ii.bpmConsole.interfaces.*;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.InvalidProcessEngineBuilderTypeException;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@ComponentScan("pl.edu.pw.ii.bpmConsole.rest")
public class BpmConsoleSpringConfiguration {

    /**
     * This is the only case of field injection in the application
     * The reason for this is that Spring Configuration Class
     * has to be instantiated with a no-argument constructor
     */
    @Autowired
    private BpmConsoleConfiguration applicationConfiguration;

    @Bean
    public DataSource dataSource() throws SQLException {
        return DataSourceFactory.fromConfiguration(applicationConfiguration.databaseConfiguration);
    }

    @Bean
    public ProcessEngine processEngine() throws Exception {
        Class bpmEngineBuilderClass = Class.forName(applicationConfiguration.bpmProcessEngineBuilderClass);
        if(!ProcessEngineBuilder.class.isAssignableFrom(bpmEngineBuilderClass))
            throw new InvalidProcessEngineBuilderTypeException();
        return constructProcessEngine(bpmEngineBuilderClass);
    }

    private ProcessEngine constructProcessEngine(Class bpmEngineBuilderClass) throws InstantiationException, IllegalAccessException, SQLException {
        ProcessEngineBuilder processEngineBuilder =
                (ProcessEngineBuilder) bpmEngineBuilderClass.newInstance();
        return processEngineBuilder
                .usingDatabase(applicationConfiguration.databaseConfiguration.type.toString(), dataSource())
                .build();
    }

    @Bean
    public UserService userService() throws Exception {
        return processEngine().userService();
    }

    @Bean
    public DeploymentService deploymentService() throws Exception {
        return processEngine().deploymentService();
    }

    @Bean
    public TaskService taskService() throws Exception {
        return processEngine().taskService();
    }

}

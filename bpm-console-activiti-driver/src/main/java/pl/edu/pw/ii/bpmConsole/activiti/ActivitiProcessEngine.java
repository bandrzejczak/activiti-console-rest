package pl.edu.pw.ii.bpmConsole.activiti;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import pl.edu.pw.ii.bpmConsole.activiti.deployment.ActivitiDeploymentService;
import pl.edu.pw.ii.bpmConsole.activiti.task.ActivitiTaskService;
import pl.edu.pw.ii.bpmConsole.activiti.user.ActivitiUserService;
import pl.edu.pw.ii.bpmConsole.interfaces.DeploymentService;
import pl.edu.pw.ii.bpmConsole.interfaces.ProcessEngine;
import pl.edu.pw.ii.bpmConsole.interfaces.TaskService;
import pl.edu.pw.ii.bpmConsole.interfaces.UserService;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ProcessEngineAlreadyInitializedException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ProcessEngineDatabaseException;

import javax.sql.DataSource;
import java.sql.SQLException;

public class ActivitiProcessEngine implements ProcessEngine {

    private org.activiti.engine.ProcessEngine processEngine;
    private DataSource dataSource;
    private String databaseType;

    public ActivitiProcessEngine(String databaseType, DataSource dataSource) {
        this.databaseType = databaseType;
        this.dataSource = dataSource;
    }

    @Override
    public void init() {
        if (processEngine != null)
            throw new ProcessEngineAlreadyInitializedException();
        try {
            processEngine = createProcessEngineConfiguration().buildProcessEngine();
            ProcessEngines.registerProcessEngine(processEngine);
        } catch (SQLException e) {
            throw new ProcessEngineDatabaseException(e);
        }
    }

    public ProcessEngineConfiguration createProcessEngineConfiguration() throws SQLException {
        ProcessEngineConfiguration processEngineConfiguration = new StandaloneProcessEngineConfiguration();
        processEngineConfiguration.setDatabaseType(databaseType);
        processEngineConfiguration.setDataSource(dataSource);
        processEngineConfiguration.setDatabaseSchemaUpdate("true");
        return processEngineConfiguration;
    }

    @Override
    public UserService userService() {
        return new ActivitiUserService(processEngine.getIdentityService());
    }

    @Override
    public DeploymentService deploymentService() {
        return new ActivitiDeploymentService(processEngine.getRepositoryService());
    }

    @Override
    public TaskService taskService() {
        return new ActivitiTaskService(processEngine.getTaskService(), processEngine.getRepositoryService());
    }
}

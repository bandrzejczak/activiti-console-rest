package pl.edu.pw.ii.bpmConsole.activiti;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import pl.edu.pw.ii.bpmConsole.activiti.process.ActivitiDeployment;
import pl.edu.pw.ii.bpmConsole.interfaces.Deployment;
import pl.edu.pw.ii.bpmConsole.interfaces.ProcessEngine;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ProcessEngineAlreadyInitializedException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ProcessEngineDatabaseException;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

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

    //TODO delete it!
    @Override
    public List<String> getUserGroups(String username) {
        return processEngine
                .getIdentityService()
                .createGroupQuery()
                .groupMember(username)
                .list()
                .stream()
                .map(Group::getId)
                .collect(Collectors.toList());
    }

    //TODO delete it!
    @Override
    public boolean checkPassword(String username, String password) {
        return processEngine
                .getIdentityService()
                .checkPassword(username, password);
    }

    //TODO delete it!
    @Override
    public void createUser(String login, String password) {
        org.activiti.engine.identity.User activitiUser = processEngine.getIdentityService().newUser(login);
        activitiUser.setPassword(password);
        processEngine.getIdentityService().saveUser(activitiUser);
    }

    //TODO delete it!
    @Override
    public void createGroup(String name) {
        Group group = processEngine.getIdentityService().newGroup(name);
        processEngine.getIdentityService().saveGroup(group);
    }

    //TODO delete it!
    @Override
    public void createMembership(String login, String groupName) {
        processEngine.getIdentityService().createMembership(login, groupName);
    }

    @Override
    public Deployment createDeployment(File deployment) {
        return new ActivitiDeployment(processEngine.getRepositoryService(), deployment);
    }

    @Override
    public List<String> listDeployments() {
        return processEngine.getRepositoryService().createDeploymentQuery().list().stream().map(org.activiti.engine.repository.Deployment::getName).collect(Collectors.toList());
    }

    public ProcessEngineConfiguration createProcessEngineConfiguration() throws SQLException {
        ProcessEngineConfiguration processEngineConfiguration = new StandaloneProcessEngineConfiguration();
        processEngineConfiguration.setDatabaseType(databaseType);
        processEngineConfiguration.setDataSource(dataSource);
        processEngineConfiguration.setDatabaseSchemaUpdate("true");
        return processEngineConfiguration;
    }
}

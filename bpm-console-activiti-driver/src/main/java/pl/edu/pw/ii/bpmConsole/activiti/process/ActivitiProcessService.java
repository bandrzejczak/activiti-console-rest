package pl.edu.pw.ii.bpmConsole.activiti.process;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import pl.edu.pw.ii.bpmConsole.interfaces.ProcessService;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.NoSuchProcessException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ProcessStartForbiddenException;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.ProcessDefinitionInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.ProcessInstanceInfo;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ActivitiProcessService implements ProcessService {
    private final ProcessEngine processEngine;

    public ActivitiProcessService(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @Override
    public Collection<ProcessDefinitionInfo> listStartableProcesses(AuthUser user) {
        return new ProcessInstances(processEngine).listStartable(user);
    }

    @Override
    public void startProcess(String processDefinitionId, AuthUser user) {
        new ProcessInstances(processEngine).start(processDefinitionId, user);
    }

    @Override
    public List<ProcessInstanceInfo> listProcessInstances() {
        return new ProcessInstances(processEngine).list();
    }

}

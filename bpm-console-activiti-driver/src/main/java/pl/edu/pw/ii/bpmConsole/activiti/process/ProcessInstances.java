package pl.edu.pw.ii.bpmConsole.activiti.process;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import pl.edu.pw.ii.bpmConsole.activiti.task.ActivitiTasks;
import pl.edu.pw.ii.bpmConsole.valueObjects.ProcessInstanceInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.TaskInfo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProcessInstances {
    private final ProcessEngine processEngine;

    public ProcessInstances(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public List<ProcessInstanceInfo> list() {
        return processEngine
                .getHistoryService()
                .createHistoricProcessInstanceQuery()
                .unfinished()
                .list()
                .stream()
                .map(this::mapProcessInstance)
                .collect(Collectors.toList());
    }

    private ProcessInstanceInfo mapProcessInstance(HistoricProcessInstance processInstance) {
        ProcessInstanceInfo processInstanceInfo = new ProcessInstanceInfo();
        processInstanceInfo.processDefinitionId = processInstance.getProcessDefinitionId();
        processInstanceInfo.startTime = processInstance.getStartTime();
        Optional<Task> currentTaskOptional = findCurrentTask(processInstance.getId());
        if (currentTaskOptional.isPresent()) {
            Task currentTask = currentTaskOptional.get();
            processInstanceInfo.assignee = currentTask.getAssignee();
            processInstanceInfo.currentTask = new ActivitiTasks(processEngine).mapTask(currentTask);
        } else
            processInstanceInfo.currentTask = new TaskInfo();
        return processInstanceInfo;
    }

    private Optional<Task> findCurrentTask(String id) {
        return processEngine
                .getTaskService()
                .createTaskQuery()
                .processInstanceId(id)
                .list()
                .stream()
                .findAny();
    }
}

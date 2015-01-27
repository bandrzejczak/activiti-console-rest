package pl.edu.pw.ii.bpmConsole.activiti.user;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import pl.edu.pw.ii.bpmConsole.interfaces.UserRights;
import pl.edu.pw.ii.bpmConsole.valueObjects.Rights;

import java.util.List;
import java.util.Optional;

public class ActivitiUserRights implements UserRights {
    private final ProcessEngine processEngine;
    private final String userId;
    private final List<String> groups;
    private String taskId;

    public ActivitiUserRights(ProcessEngine processEngine, String userId, List<String> groups) {
        this.processEngine = processEngine;
        this.userId = userId;
        this.groups = groups;
    }

    @Override
    public Rights toTask(String taskId) {
        this.taskId = taskId;
        boolean isAssignee = isAssignee();
        boolean isCandidate = isCandidate();
        if (isAssignee && isCandidate)
            return Rights.UNCLAIM;
        if (isAssignee)
            return Rights.WRITE;
        if (isCandidate)
            return Rights.CLAIM;
        return Rights.NONE;
    }

    private Boolean isAssignee() {
        return getTask()
                .map(task -> Boolean.valueOf(userId.equals(task.getAssignee())))
                .orElse(Boolean.FALSE);
    }

    private Boolean isCandidate() {
        return processEngine
                .getTaskService()
                .getIdentityLinksForTask(taskId)
                .stream()
                .anyMatch(i -> IdentityLinkType.CANDIDATE.equals(i.getType()) &&
                        (userId.equals(i.getUserId()) || groups.contains(i.getGroupId())));
    }

    private Optional<Task> getTask() {
        return Optional.ofNullable(
                processEngine
                        .getTaskService()
                        .createTaskQuery()
                        .taskId(taskId)
                        .singleResult()
        );
    }


}

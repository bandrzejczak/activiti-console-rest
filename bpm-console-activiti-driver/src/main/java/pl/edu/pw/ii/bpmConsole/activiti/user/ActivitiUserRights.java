package pl.edu.pw.ii.bpmConsole.activiti.user;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import pl.edu.pw.ii.bpmConsole.interfaces.UserRights;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.AccessDeniedException;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUserGroup;
import pl.edu.pw.ii.bpmConsole.valueObjects.Rights;

import java.util.Optional;

public class ActivitiUserRights implements UserRights {
    private final ProcessEngine processEngine;
    private final AuthUser user;
    private String taskId;

    public ActivitiUserRights(ProcessEngine processEngine, AuthUser user) {
        this.processEngine = processEngine;
        this.user = user;
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
                .map(task -> Boolean.valueOf(user.id.equals(task.getAssignee())))
                .orElse(Boolean.FALSE);
    }

    private Boolean isCandidate() {
        return processEngine
                .getTaskService()
                .getIdentityLinksForTask(taskId)
                .stream()
                .anyMatch(i -> IdentityLinkType.CANDIDATE.equals(i.getType()) &&
                        (user.id.equals(i.getUserId()) || user.groups.contains(i.getGroupId())));
    }

    private Optional<Task> getTask() {
        return Optional.ofNullable(
                processEngine
                        .getTaskService()
                        .createTaskQuery()
                        .active()
                        .taskId(taskId)
                        .singleResult()
        );
    }

    @Override
    public void toUser(String userId) {
        if (!user.isMemberOf(AuthUserGroup.ADMIN) && !user.id.equals(userId))
            throw new AccessDeniedException();
    }


}

package pl.edu.pw.ii.bpmConsole.activiti.task;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ClaimForbiddenException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.NoSuchTaskException;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.edu.pw.ii.bpmConsole.test.AssertJThrowableAssert.assertThrown;

@RunWith(MockitoJUnitRunner.class)
public class TaskClaimingSpec extends AbstractTaskSpec {

    @Test
    public void shouldThrowExceptionIfTaskDoesntExist() {
        //given
        when(taskServiceMock.createTaskQuery().active()).thenReturn(taskQueryMock);
        whenLookingForTaskReturn(null);
        //when - then
        assertThrown(() -> new ActivitiTasks(taskServiceMock, repositoryServiceMock).claim(TASK_ID, USER_ID, USER_GROUPS))
                .isInstanceOf(NoSuchTaskException.class);
    }

    @Test
    public void shouldThrowExceptionIfTaskCannotBeClaimedByUser() {
        //given
        when(taskServiceMock.createTaskQuery().active()).thenReturn(taskQueryMock);
        whenLookingForTaskReturn(new TaskEntity());
        whenLookingForTaskToAssignReturn(Collections.emptyList());
        //when - then
        assertThrown(() -> new ActivitiTasks(taskServiceMock, repositoryServiceMock).claim(TASK_ID, USER_ID, USER_GROUPS))
                .isInstanceOf(ClaimForbiddenException.class);
    }

    @Test
    public void shouldClaimTaskWhenEverythingIsOk() {
        //given
        TaskEntity task = new TaskEntity(TASK_ID);
        task.setProcessDefinitionId(PROCESS_DEFINITION_ID);
        when(taskServiceMock.createTaskQuery().active()).thenReturn(taskQueryMock);
        whenLookingForTaskReturn(task);
        whenLookingForTaskToAssignReturn(Collections.singletonList(task));
        whenLookingForProcessDefinitonNameReturn(PROCESS_DEFINITION_NAME);
        //when
        new ActivitiTasks(taskServiceMock, repositoryServiceMock).claim(TASK_ID, USER_ID, USER_GROUPS);
        //then
        verify(
                taskServiceMock
        ).claim(TASK_ID, USER_ID);
    }

}

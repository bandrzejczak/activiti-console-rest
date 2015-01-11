package pl.edu.pw.ii.bpmConsole.activiti.task;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.ClaimForbiddenException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.NoSuchTaskException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.edu.pw.ii.bpmConsole.test.AssertJThrowableAssert.assertThrown;

@RunWith(MockitoJUnitRunner.class)
public class TaskClaimingSpec {

    public static final String TASK_ID = "1";
    public static final String USER_ID = "user";
    private static final List<String> USER_GROUPS = Arrays.asList("group1", "group2");
    public static final String PROCESS_DEFINITION_ID = "1";
    private static final String PROCESS_DEFINITION_NAME = "name";

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private TaskService taskServiceMock;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RepositoryService repositoryServiceMock;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private TaskQueryMock taskQueryMock;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TaskQueryMock taskQueryMock1;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TaskQueryMock taskQueryMock2;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TaskQueryMock taskQueryMock3;

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

    private void whenLookingForProcessDefinitonNameReturn(String name) {
        ProcessDefinitionEntity processDefinition = new ProcessDefinitionEntity();
        processDefinition.setName(name);
        when(
                repositoryServiceMock
                        .createProcessDefinitionQuery()
                        .processDefinitionId(eq(PROCESS_DEFINITION_ID))
                        .singleResult()
        ).thenReturn(processDefinition);
    }

    private void whenLookingForTaskReturn(Task task) {
        when(
                taskQueryMock
                        .taskId(TASK_ID)
                        .singleResult()
        ).thenReturn(task);
    }

    private void whenLookingForTaskToAssignReturn(List<Task> tasks) {
        when(taskQueryMock.or()).thenReturn(taskQueryMock1);
        when(taskQueryMock1.taskCandidateUser(USER_ID)).thenReturn(taskQueryMock2);
        when(taskQueryMock2.taskCandidateGroupIn(USER_GROUPS)).thenReturn(taskQueryMock3);
        when(taskQueryMock3.endOr().list()).thenReturn(tasks);
    }
}

package pl.edu.pw.ii.bpmConsole.activiti.task;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.mockito.Answers;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public abstract class AbstractTaskSpec {
    static final String TASK_ID = "1";
    static final String USER_ID = "user";
    static final List<String> USER_GROUPS = Arrays.asList("group1", "group2");
    static final String PROCESS_DEFINITION_ID = "1";
    static final String PROCESS_DEFINITION_NAME = "name";

    @Mock
    ProcessEngine processEngineMock;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TaskService taskServiceMock;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    RepositoryService repositoryServiceMock;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    FormService formServiceMock;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TaskQueryMock taskQueryMock;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TaskQueryMock taskQueryMock1;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TaskQueryMock taskQueryMock2;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TaskQueryMock taskQueryMock3;

    @Before
    public void initMocks() {
        when(processEngineMock.getTaskService()).thenReturn(taskServiceMock);
        when(processEngineMock.getRepositoryService()).thenReturn(repositoryServiceMock);
        when(processEngineMock.getFormService()).thenReturn(formServiceMock);
    }

    protected void whenLookingForProcessDefinitonNameReturn(String name) {
        ProcessDefinitionEntity processDefinition = new ProcessDefinitionEntity();
        processDefinition.setName(name);
        when(
                repositoryServiceMock
                        .createProcessDefinitionQuery()
                        .processDefinitionId(Matchers.eq(TaskClaimingSpec.PROCESS_DEFINITION_ID))
                        .singleResult()
        ).thenReturn(processDefinition);
    }

    protected void whenLookingForTaskReturn(Task task) {
        when(
                taskQueryMock
                        .taskId(TaskClaimingSpec.TASK_ID)
                        .singleResult()
        ).thenReturn(task);
    }

    protected void whenLookingForTaskToAssignReturn(List<Task> tasks) {
        when(taskQueryMock.or()).thenReturn(taskQueryMock1);
        when(taskQueryMock1.taskCandidateUser(TaskClaimingSpec.USER_ID)).thenReturn(taskQueryMock2);
        when(taskQueryMock2.taskCandidateGroupIn(TaskClaimingSpec.USER_GROUPS)).thenReturn(taskQueryMock3);
        when(taskQueryMock3.endOr().list()).thenReturn(tasks);
    }
}

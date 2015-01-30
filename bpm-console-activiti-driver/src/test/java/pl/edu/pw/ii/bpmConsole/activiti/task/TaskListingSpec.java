package pl.edu.pw.ii.bpmConsole.activiti.task;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.bpmConsole.valueObjects.AuthUser;
import pl.edu.pw.ii.bpmConsole.valueObjects.TaskInfo;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskListingSpec {

    public static final String USER_ID = "login";
    public static final String PROCESS_DEFINITION_NAME = "name";
    public static final String PROCESS_DEFINITION_ID = "3";
    private static final List<String> USER_GROUPS = Arrays.asList("group1", "group2");
    static final AuthUser USER = new AuthUser(USER_ID, USER_GROUPS);

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TaskService taskServiceMock;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TaskQueryMock taskQueryMock;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TaskQueryMock taskQueryMock1;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TaskQueryMock taskQueryMock2;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TaskQueryMock taskQueryMock3;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    RepositoryService repositoryServiceMock;

    @Mock
    ProcessEngine processEngineMock;

    @Before
    public void initProcessEngineMock() {
        when(processEngineMock.getRepositoryService()).thenReturn(repositoryServiceMock);
        when(processEngineMock.getTaskService()).thenReturn(taskServiceMock);
    }

    @Test
    public void shouldReturnEmptyListIfThereAreNoTasks() {
        //given
        when(
                taskServiceMock
                        .createTaskQuery()
                        .active()
        ).thenReturn(taskQueryMock);
        when(
                taskQueryMock
                        .taskAssignee(USER_ID)
                        .list()
        ).thenReturn(Collections.emptyList());
        //when
        List<TaskInfo> tasks = new ActivitiTasks(processEngineMock).listAssignedTo(USER_ID);
        //then
        assertThat(tasks).isEmpty();
        verifyZeroInteractions(repositoryServiceMock);
    }

    @Test
    public void shouldReturnListOfTasksAssignedOnlyToAGivenUser() {
        //given
        when(
                taskServiceMock
                        .createTaskQuery()
                        .active()
        ).thenReturn(taskQueryMock);
        List<Task> activitiTasks = prepareTasksList();
        when(
                taskQueryMock
                        .taskAssignee(USER_ID)
                        .list()
        ).thenReturn(activitiTasks);
        when(
                repositoryServiceMock
                        .createProcessDefinitionQuery()
                        .processDefinitionId(eq(PROCESS_DEFINITION_ID))
                        .singleResult()
        ).thenReturn(prepareProcessDefinition(PROCESS_DEFINITION_NAME));
        //when
        List<TaskInfo> returnedTasks = new ActivitiTasks(processEngineMock).listAssignedTo(USER_ID);
        //then
        assertThat(returnedTasks).hasSize(2);
        assertThatReturnedTaskHasActivitiTaskData(returnedTasks, activitiTasks);
    }

    @Test
    public void shouldReturnListOfTasksAvailableToAGivenUserWithUserGroups() {
        //given
        when(taskServiceMock.createTaskQuery().active()).thenReturn(taskQueryMock);
        List<Task> activitiTasks = prepareTasksList();
        when(taskQueryMock.or()).thenReturn(taskQueryMock1);
        when(taskQueryMock1.taskCandidateUser(USER_ID)).thenReturn(taskQueryMock2);
        when(taskQueryMock2.taskCandidateGroupIn(USER_GROUPS)).thenReturn(taskQueryMock3);
        when(taskQueryMock3.endOr().list()).thenReturn(activitiTasks);
        when(
                repositoryServiceMock
                        .createProcessDefinitionQuery()
                        .processDefinitionId(eq(PROCESS_DEFINITION_ID))
                        .singleResult()
        ).thenReturn(prepareProcessDefinition(PROCESS_DEFINITION_NAME));
        //when
        List<TaskInfo> returnedTasks = new ActivitiTasks(processEngineMock).listAvailableFor(USER);
        //then
        assertThat(returnedTasks).hasSize(2);
        assertThatReturnedTaskHasActivitiTaskData(returnedTasks, activitiTasks);
    }

    private ProcessDefinition prepareProcessDefinition(String name) {
        ProcessDefinitionEntity processDefinition = new ProcessDefinitionEntity();
        processDefinition.setName(name);
        return processDefinition;
    }

    private List<Task> prepareTasksList() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(createTask(USER_ID, "1", 50, "category", new Date(), null, PROCESS_DEFINITION_ID));
        tasks.add(createTask(USER_ID, "2", 30, "category2", null, new Date(), PROCESS_DEFINITION_ID));
        return tasks;
    }

    private Task createTask(String assignee1, String taskId, int priority,
                            String category, Date dueDate, Date createTime, String processDefinitionId) {
        TaskEntity task = new TaskEntity(taskId);
        task.setAssignee(assignee1);
        task.setPriority(priority);
        task.setDueDate(dueDate);
        task.setCreateTime(createTime);
        task.setCategory(category);
        task.setProcessDefinitionId(processDefinitionId);
        return task;
    }

    private void assertThatReturnedTaskHasActivitiTaskData(List<TaskInfo> returnedTasks, List<Task> activitTasks) {
        for (int i = 0; i < returnedTasks.size(); i++) {
            TaskInfo returnedTask = returnedTasks.get(i);
            Task activitiTask = activitTasks.get(i);
            assertThat(returnedTask.id).isEqualTo(activitiTask.getId());
            assertThat(returnedTask.name).isEqualTo(activitiTask.getName());
            assertThat(returnedTask.priority).isEqualTo(activitiTask.getPriority());
            assertThat(returnedTask.dueDate).isEqualTo(activitiTask.getDueDate());
            assertThat(returnedTask.createTime).isEqualTo(activitiTask.getCreateTime());
            assertThat(returnedTask.processName).isEqualTo(PROCESS_DEFINITION_NAME);
        }
    }

}

package pl.edu.pw.ii.bpmConsole.activiti.task;

import org.activiti.engine.impl.TaskQueryImpl;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfoQuery;
import org.activiti.engine.task.TaskQuery;

//Mockito generics hack
class TaskQueryMock extends TaskQueryImpl implements TaskInfoQuery<TaskQuery, Task> {
}

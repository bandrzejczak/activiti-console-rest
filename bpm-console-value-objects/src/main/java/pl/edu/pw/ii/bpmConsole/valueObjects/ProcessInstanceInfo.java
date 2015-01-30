package pl.edu.pw.ii.bpmConsole.valueObjects;

import java.util.Date;

public class ProcessInstanceInfo {
    public String id;
    public String processDefinitionId;
    public String name;
    public Date startTime;
    public String assignee;
    public TaskInfo currentTask;
}

package pl.edu.pw.ii.bpmConsole.valueObjects;

import java.util.ArrayList;
import java.util.List;

public class FormInfo {
    public TaskInfo task;
    public String description;
    public Rights rights;
    public List<FieldInfo> fields = new ArrayList<>();
}

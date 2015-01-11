package pl.edu.pw.ii.bpmConsole.valueObjects;

import java.util.Map;

public class FieldInfo {
    public String id;
    public String name;
    public String type;
    public String value;
    public Boolean required;
    public Boolean readOnly;
    public Map<String, String> enumOptions;
}

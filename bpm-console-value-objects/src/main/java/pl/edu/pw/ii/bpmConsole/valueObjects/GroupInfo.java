package pl.edu.pw.ii.bpmConsole.valueObjects;

import javax.validation.constraints.NotNull;

public class GroupInfo {
    @NotNull
    public String id;
    @NotNull
    public String name;
    @NotNull
    public GroupType type;
}

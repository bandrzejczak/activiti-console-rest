package pl.edu.pw.ii.bpmConsole.valueObjects;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class GroupInfo {
    @NotNull
    @Length(min = 2, max = 32)
    public String id;
    @NotNull
    @Length(min = 2, max = 32)
    public String name;
    @NotNull
    public GroupType type;
}

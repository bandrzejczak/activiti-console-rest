package pl.edu.pw.ii.bpmConsole.valueObjects;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserInfo {
    @NotNull
    @Length(min = 2, max = 32)
    public String id;
    @NotNull
    @Length(min = 2, max = 32)
    public String firstName;
    @NotNull
    @Length(min = 2, max = 32)
    public String lastName;
    @NotNull
    @Pattern(regexp = "\\b[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}\\b")
    public String email;
    public String currentPassword;
    public String newPassword;
}

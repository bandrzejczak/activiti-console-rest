package pl.edu.pw.ii.bpmConsole.valueObjects;

public enum AuthUserGroup {
    ADMIN("admin");

    public final String name;

    AuthUserGroup(String name) {
        this.name = name;
    }
}

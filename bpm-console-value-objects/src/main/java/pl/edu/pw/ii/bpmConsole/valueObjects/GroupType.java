package pl.edu.pw.ii.bpmConsole.valueObjects;

public enum GroupType {
    SECURITY_ROLE("security-role"), ASSIGNMENT("assignment");

    public final String name;

    private GroupType(String name) {
        this.name = name;
    }

    public static GroupType forName(String name) {
        for (GroupType groupType : values())
            if (groupType.name.equals(name))
                return groupType;
        return null;
    }
}

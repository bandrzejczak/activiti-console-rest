package pl.edu.pw.ii.ActivitiConsole.db;

public enum User {
    ADMIN("admin", "adminpass"),
    MANAGER("manager", "maangerpass"),
    USER("user", "userpass");

    public String login;
    public String password;

    User(String login, String password){
        this.login = login;
        this.password = password;
    }
}

package pl.edu.pw.ii.bpmConsole.rest.db;

import pl.edu.pw.ii.bpmConsole.valueObjects.UserInfo;

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

    public UserInfo toUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.id = login;
        userInfo.newPassword = password;
        return userInfo;
    }
}

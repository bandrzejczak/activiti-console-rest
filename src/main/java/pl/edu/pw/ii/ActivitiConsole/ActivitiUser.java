package pl.edu.pw.ii.ActivitiConsole;

import java.util.List;

/**
 * Created by bandrzejczak on 26.10.14.
 */
public class ActivitiUser {

    public String id;
    public List<String> groups;

    public ActivitiUser(String id, List<String> groups) {
        this.id = id;
        this.groups = groups;
    }
}

package pl.edu.pw.ii.bpmConsole.rest.filters;

import java.util.List;

/**
 * Created by bandrzejczak on 26.10.14.
 */
public class BpmUser {

    public String id;
    public List<String> groups;

    public BpmUser(String id, List<String> groups) {
        this.id = id;
        this.groups = groups;
    }
}

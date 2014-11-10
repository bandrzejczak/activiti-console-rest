package pl.edu.pw.ii.BpmConsole.Rest.resources;

public class ResourceAction {
    public String name;
    public String method;

    public ResourceAction(String name, String method) {
        this.name = name;
        this.method = method;
    }
}

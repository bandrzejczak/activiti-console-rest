package pl.edu.pw.ii.bpmConsole.rest.resources;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.Link;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

public class LinkBuilder {

    private final Class<?> resource;
    private final Link.Builder linkBuilder;

    private LinkBuilder(Class<?> resource){
        this.resource = resource;
        this.linkBuilder = Link.fromResource(resource);
    }

    public static LinkBuilder fromResource(Class<?> resource){
        return new LinkBuilder(resource);
    }

    public LinkBuilder rel(String rel){
        linkBuilder.rel(rel);
        return this;
    }

    public Link build(Object ... params){
        addResourceActions();
        return linkBuilder.build(params);
    }

    private void addResourceActions() {
        try {
            List<ResourceAction> actions = ResourceActionsResolver.forResource(resource).resolve();
            ObjectMapper objectMapper = new ObjectMapper();
            StringWriter actionsWriter = new StringWriter();
            objectMapper.writeValue(actionsWriter, actions);
            linkBuilder.param("actions", actionsWriter.toString().replace("\"", "'"));
        } catch (IOException e) {
            e.printStackTrace();
            linkBuilder.param("actions", "{}");
        }
    }

}

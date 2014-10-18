package pl.edu.pw.ii.ActivitiConsole.api.resources;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pw.ii.ActivitiConsole.api.SpringResource;

import java.util.ArrayList;
import java.util.List;

@SpringResource("/groups")
public class GroupsResource extends ServerResource {

    @Autowired
    private IdentityService identityService;

    @Get
    public List<String> getAuthorizedUserGroups(){
        List<Group> groups = identityService
                .createGroupQuery()
                .groupMember(getLoggedInUserId())
                .list();
        //TODO After migration to java 8 make it pretty :)
        List<String> stringGroups = new ArrayList<>();
        for(Group group : groups)
            stringGroups.add(group.getId());
        return stringGroups;
    }

    private String getLoggedInUserId() {
        return getRequest().getClientInfo().getUser().getIdentifier();
    }
}

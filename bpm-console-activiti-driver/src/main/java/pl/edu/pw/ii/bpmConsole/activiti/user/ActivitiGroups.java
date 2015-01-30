package pl.edu.pw.ii.bpmConsole.activiti.user;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import pl.edu.pw.ii.bpmConsole.valueObjects.GroupInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.GroupType;

import java.util.List;
import java.util.stream.Collectors;

public class ActivitiGroups {
    private final IdentityService identityService;

    public ActivitiGroups(IdentityService identityService) {
        this.identityService = identityService;
    }

    public List<GroupInfo> list() {
        return identityService
                .createGroupQuery()
                .list()
                .stream()
                .map(this::mapGroup)
                .collect(Collectors.toList());
    }

    private GroupInfo mapGroup(Group group) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.id = group.getId();
        groupInfo.name = group.getName();
        groupInfo.type = GroupType.forName(group.getType());
        return groupInfo;
    }
}

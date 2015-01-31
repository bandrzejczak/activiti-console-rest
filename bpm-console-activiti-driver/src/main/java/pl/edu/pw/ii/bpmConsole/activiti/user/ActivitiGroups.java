package pl.edu.pw.ii.bpmConsole.activiti.user;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.*;
import pl.edu.pw.ii.bpmConsole.valueObjects.GroupInfo;
import pl.edu.pw.ii.bpmConsole.valueObjects.GroupType;

import java.util.List;
import java.util.Optional;
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

    public GroupInfo get(String groupId) {
        Group group = findGroup(groupId).orElseThrow(() -> new NoSuchGroupException(groupId));
        return mapGroup(group);
    }

    private GroupInfo mapGroup(Group group) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.id = group.getId();
        groupInfo.name = group.getName();
        groupInfo.type = GroupType.forName(group.getType());
        return groupInfo;
    }

    public void edit(GroupInfo groupInfo, String groupId) {
        Preconditions.checkArgument(creatingNewGroupOrEditingExistingOne(groupInfo, groupId), "You cannot change groups's id");
        Group group = findGroup(groupInfo.id).orElse(newGroup(groupInfo.id));
        if (creatingNewGroupWithExistingId(groupId, group))
            throw new GroupAlreadyExistsException(group.getId());
        updateData(group, groupInfo);
        identityService.saveGroup(group);
    }

    private boolean creatingNewGroupWithExistingId(String groupId, Group group) {
        return groupId == null && !Strings.isNullOrEmpty(group.getName());
    }

    private boolean creatingNewGroupOrEditingExistingOne(GroupInfo groupInfo, String groupId) {
        return groupId == null || groupId.equals(groupInfo.id);
    }

    private Optional<Group> findGroup(String groupId) {
        return Optional.ofNullable(
                identityService
                        .createGroupQuery()
                        .groupId(groupId)
                        .singleResult()
        );
    }

    private Group newGroup(String id) {
        return identityService.newGroup(id);
    }

    private void updateData(Group group, GroupInfo groupInfo) {
        group.setName(groupInfo.name);
        group.setType(groupInfo.type.name);
    }

    public void delete(String groupId) {
        findGroup(groupId).orElseThrow(() -> new NoSuchGroupException(groupId));
        identityService.deleteGroup(groupId);
    }

    public void addMembership(String groupId, String userId) {
        findGroup(groupId).orElseThrow(() -> new NoSuchGroupException(groupId));
        findUser(userId).orElseThrow(() -> new NoSuchUserException(userId));
        if (assignmentExists(groupId, userId))
            throw new AssignmentAlreadyExistsException(userId, groupId);
        identityService.createMembership(userId, groupId);
    }

    public void deleteMembership(String groupId, String userId) {
        findGroup(groupId).orElseThrow(() -> new NoSuchGroupException(groupId));
        findUser(userId).orElseThrow(() -> new NoSuchUserException(userId));
        if (!assignmentExists(groupId, userId))
            throw new NoSuchAssignmentException(userId, groupId);
        identityService.deleteMembership(userId, groupId);
    }

    private Boolean assignmentExists(String groupId, String userId) {
        return identityService
                .createGroupQuery()
                .groupId(groupId)
                .groupMember(userId)
                .singleResult() != null;
    }

    private Optional<User> findUser(String userId) {
        return new ActivitiUsers(identityService).findUser(userId);
    }

    public List<GroupInfo> forUser(String userId) {
        return identityService
                .createGroupQuery()
                .groupMember(userId)
                .list()
                .stream()
                .map(this::mapGroup)
                .collect(Collectors.toList());
    }
}

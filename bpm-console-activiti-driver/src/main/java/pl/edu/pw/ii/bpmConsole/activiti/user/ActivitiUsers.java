package pl.edu.pw.ii.bpmConsole.activiti.user;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.CannotDeleteOwnAccountException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.NoSuchUserException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.UserAlreadyExistsException;
import pl.edu.pw.ii.bpmConsole.interfaces.exceptions.WrongPasswordException;
import pl.edu.pw.ii.bpmConsole.valueObjects.UserInfo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

class ActivitiUsers {
    private final IdentityService identityService;

    public ActivitiUsers(IdentityService identityService) {
        this.identityService = identityService;
    }

    public List<UserInfo> list() {
        return identityService
                .createUserQuery()
                .list()
                .stream()
                .map(this::mapUser)
                .collect(Collectors.toList());
    }

    private UserInfo mapUser(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.id = user.getId();
        userInfo.firstName = user.getFirstName();
        userInfo.lastName = user.getLastName();
        userInfo.email = user.getEmail();
        return userInfo;
    }

    public UserInfo get(String userId) {
        User user = findUser(userId).orElseThrow(() -> new NoSuchUserException(userId));
        return mapUser(user);
    }

    public void edit(UserInfo userInfo, String userId, Boolean isAdmin) {
        Preconditions.checkArgument(creatingNewUserOrEditingExistingOne(userInfo, userId), "You cannot change user's id");
        User user = findUser(userInfo.id).orElse(newUser(userInfo.id));
        if (creatingNewUserWithExistingId(userId, user))
            throw new UserAlreadyExistsException(user.getId());
        validatePasswords(user, userInfo, isAdmin);
        updateData(user, userInfo);
        identityService.saveUser(user);
    }

    private boolean creatingNewUserOrEditingExistingOne(UserInfo userInfo, String userId) {
        return userId == null || userId.equals(userInfo.id);
    }

    Optional<User> findUser(String userId) {
        return Optional.ofNullable(
                identityService
                        .createUserQuery()
                        .userId(userId)
                        .singleResult()
        );
    }

    private boolean creatingNewUserWithExistingId(String userId, User user) {
        return userId == null && !Strings.isNullOrEmpty(user.getPassword());
    }

    private void validatePasswords(User user, UserInfo userInfo, Boolean isAdmin) {
        if (isNewUserWithoutPassword(user, userInfo))
            throw new IllegalArgumentException("No password for new user");
        if (!isAdmin && isExistingUserWhosePasswordsDontMatch(user, userInfo))
            throw new WrongPasswordException();
    }

    private boolean isNewUserWithoutPassword(User user, UserInfo userInfo) {
        return Strings.isNullOrEmpty(user.getPassword())
                && Strings.isNullOrEmpty(userInfo.newPassword);
    }

    private boolean isExistingUserWhosePasswordsDontMatch(User user, UserInfo userInfo) {
        return !Strings.isNullOrEmpty(user.getPassword())
                && !Strings.isNullOrEmpty(userInfo.newPassword)
                && !user.getPassword().equals(userInfo.currentPassword);
    }

    private User newUser(String userId) {
        return identityService.newUser(userId);
    }

    private void updateData(User user, UserInfo userInfo) {
        user.setFirstName(userInfo.firstName);
        user.setLastName(userInfo.lastName);
        user.setEmail(userInfo.email);
        if (!Strings.isNullOrEmpty(userInfo.newPassword))
            user.setPassword(userInfo.newPassword);
    }

    public void delete(String userToDeleteId, String currentUserId) {
        if(Objects.equals(userToDeleteId, currentUserId))
            throw new CannotDeleteOwnAccountException();
        findUser(userToDeleteId).orElseThrow(() -> new NoSuchUserException(userToDeleteId));
        identityService.deleteUser(userToDeleteId);
    }

    public Boolean validateCredentials(String userId, String password) {
        return identityService.checkPassword(userId, password);
    }
}

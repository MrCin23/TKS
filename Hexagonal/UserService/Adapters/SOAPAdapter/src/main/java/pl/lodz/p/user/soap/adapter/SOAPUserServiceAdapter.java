package pl.lodz.p.user.soap.adapter;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.lodz.p.user.core.domain.MongoUUID;
import pl.lodz.p.user.core.domain.user.*;
import pl.lodz.p.user.core.services.service.UserService;
import pl.lodz.p.user.soap.model.SOAPMongoUUID;
import pl.lodz.p.user.ui.SOAPUserServicePort;
import pl.lodz.p.users.LoginUser;
import pl.lodz.p.users.RoleType;
import pl.lodz.p.users.UserType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("SOAPUserServicePort")
@AllArgsConstructor
public class SOAPUserServiceAdapter implements SOAPUserServicePort {

    private final UserService userService;

    @Override
    public UserType createUser(UserType user) {
        return convert(userService.createUser(convert(user)));
    }

    @Override
    public List<UserType> getAllUsers() {
        List<UserType> users = new ArrayList<>();
        for(User user : userService.getAllUsers()) {
            users.add(convert(user));
        }
        return users;
    }

    @Override
    public UserType getUser(UUID uuid) {
        return convert(userService.getUser(uuid));
    }

    @Override
    public void updateUser(UUID uuid, Map<String, Object> fieldsToUpdate) {
        userService.updateUser(uuid, fieldsToUpdate);
    }

    @Override
    public void activateUser(UUID uuid) {
        userService.activateUser(uuid);
    }

    @Override
    public void deactivateUser(UUID uuid) {
        userService.deactivateUser(uuid);
    }

    @Override
    public String getUserByUsername(LoginUser loginUser) {
        return userService.getUserByUsername(loginUser.getUsername(), loginUser.getPassword());
    }

    @Override
    public UserType getUserByUsername(String username) {
        return convert(userService.getUserByUsername(username));
    }

    @Override
    public List<UserType> getUsersByUsername(String username) {
        List<UserType> users = new ArrayList<>();
        for(User user : userService.getUsersByUsername(username)) {
            users.add(convert(user));
        }
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userService.loadUserByUsername(username);
    }

    @Override
    public void invalidateToken(String token) {
        userService.invalidateToken(token);
    }

    @Override
    public boolean checkToken(String token) {
        return userService.checkToken(token);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        userService.changePassword(username, newPassword);
    }

    @Override
    public long size() {
        return userService.size();
    }

    private MongoUUID convert(SOAPMongoUUID r) {
        return new MongoUUID(r.getUuid());
    }
    private SOAPMongoUUID convert(MongoUUID r) {
        return new SOAPMongoUUID(r.getUuid());
    }

    private Role convert(RoleType ent) {
        if (ent == RoleType.ADMIN) {
            return Role.ADMIN;
        }
        else if (ent == RoleType.CLIENT) {
            return Role.CLIENT;
        }
        else if (ent == RoleType.RESOURCE_MANAGER) {
            return Role.RESOURCE_MANAGER;
        }
        return null;
    }
    private RoleType convert(Role role) {
        if (role == Role.ADMIN) {
            return RoleType.ADMIN;
        }
        else if (role == Role.CLIENT) {
            return RoleType.CLIENT;
        }
        else if (role == Role.RESOURCE_MANAGER) {
            return RoleType.RESOURCE_MANAGER;
        }
        return null;
    }

    private UserType convert(User user) {
        UserType userType = new UserType();
        userType.setFirstName(user.getFirstName());
        userType.setSurname(user.getSurname());
        userType.setUsername(user.getUsername());
        userType.setEmailAddress(user.getEmailAddress());
        userType.setActive(user.isActive());
        userType.setRole(convert(user.getRole()));
        return userType;
    }

    private User convert(UserType ent) {
        if (ent == null) {
            return null;
        }
        return switch (ent.getClass().getSimpleName()) {
            case "SOAPClient" -> new Client(ent.getFirstName(), ent.getUsername(), "",
                    ent.getSurname(), ent.getEmailAddress(), new Standard());
            case "SOAPResourceManager" -> new ResourceManager(ent.getFirstName(), ent.getSurname(), ent.getUsername(), "", ent.getEmailAddress());
            case "SOAPAdmin" -> new Admin(ent.getFirstName(), ent.getSurname(), ent.getUsername(), ent.getEmailAddress(), "");
            default -> throw new RuntimeException("Unsupported type: " + ent.getClass().getSimpleName());
        };
    }
}

package pl.lodz.p.user.rest.adapter;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.lodz.p.user.core.domain.MongoUUID;
import pl.lodz.p.user.core.domain.user.*;
import pl.lodz.p.user.rest.model.RESTMongoUUID;
import pl.lodz.p.user.rest.model.dto.LoginDTO;
import pl.lodz.p.user.core.services.service.UserService;
import pl.lodz.p.user.rest.model.user.*;
import pl.lodz.p.user.ui.RESTUserServicePort;
import pl.lodz.p.user.rest.model.user.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RESTUserServiceAdapter implements RESTUserServicePort {

    private final UserService userService;

    @Override
    public RESTUser createUser(RESTUser user) {
        return convert(userService.createUser(convert(user)));
    }

    @Override
    public List<RESTUser> getAllUsers() {
        List<RESTUser> users = new ArrayList<>();
        for(User user : userService.getAllUsers()) {
            users.add(convert(user));
        }
        return users;
    }

    @Override
    public RESTUser getUser(UUID uuid) {
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
    public String getUserByUsername(LoginDTO loginDTO) {
        return userService.getUserByUsername(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @Override
    public RESTUser getUserByUsername(String username) {
        return convert(userService.getUserByUsername(username));
    }

    @Override
    public List<RESTUser> getUsersByUsername(String username) {
        List<RESTUser> users = new ArrayList<>();
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

    private MongoUUID convert(RESTMongoUUID r) {
        return new MongoUUID(r.getUuid());
    }
    private RESTMongoUUID convert(MongoUUID r) {
        return new RESTMongoUUID(r.getUuid());
    }

    private Role convert(RESTRole ent) {
        if (ent == RESTRole.ADMIN) {
            return Role.ADMIN;
        }
        else if (ent == RESTRole.CLIENT) {
            return Role.CLIENT;
        }
        else if (ent == RESTRole.RESOURCE_MANAGER) {
            return Role.RESOURCE_MANAGER;
        }
        return null;
    }
    private RESTRole convert(Role role) {
        if (role == Role.ADMIN) {
            return RESTRole.ADMIN;
        }
        else if (role == Role.CLIENT) {
            return RESTRole.CLIENT;
        }
        else if (role == Role.RESOURCE_MANAGER) {
            return RESTRole.RESOURCE_MANAGER;
        }
        return null;
    }

    private RESTUser convert(User user) {
        return switch (user.getClass().getSimpleName()) {
            case "Client" -> new RESTClient(convert(user.getEntityId()), user.getFirstName(), user.getUsername(), user.getPassword(),
                    user.getSurname(), user.getEmailAddress(), convert(user.getRole()), user.isActive(), new RESTStandard(), ((Client) user).getCurrentRents());
            case "ResourceManager" -> new RESTResourceManager(convert(user.getEntityId()), user.getFirstName(), user.getSurname(), user.getUsername(), user.getPassword(), user.getEmailAddress());
            case "Admin" -> new RESTAdmin(convert(user.getEntityId()), user.getFirstName(), user.getSurname(), user.getUsername(), user.getEmailAddress(), user.getPassword());
            default -> throw new RuntimeException("Unsupported type: " + user.getClass().getSimpleName());
        };
    }

    private User convert(RESTUser ent) {
        if (ent == null) {
            return null;
        }
        return switch (ent.getClass().getSimpleName()) {
            case "RESTClient" -> new Client(convert(ent.getEntityId()), ent.getFirstName(), ent.getUsername(), ent.getPassword(),
                    ent.getSurname(), ent.getEmailAddress(), convert(ent.getRole()), ent.isActive(), new Standard(), ((RESTClient) ent).getCurrentRents());
            case "RESTResourceManager" -> new ResourceManager(convert(ent.getEntityId()), ent.getFirstName(), ent.getSurname(), ent.getUsername(), ent.getPassword(), ent.getEmailAddress());
            case "RESTAdmin" -> new Admin(convert(ent.getEntityId()), ent.getFirstName(), ent.getSurname(), ent.getUsername(), ent.getEmailAddress(), ent.getPassword());
            default -> throw new RuntimeException("Unsupported type: " + ent.getClass().getSimpleName());
        };
    }
}

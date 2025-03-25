package pl.lodz.p.repo.user.adapter;

import lombok.AllArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.user.*;
import pl.lodz.p.infrastructure.user.UAdd;
import pl.lodz.p.infrastructure.user.UGet;
import pl.lodz.p.infrastructure.user.URemove;
import pl.lodz.p.infrastructure.user.UUpdate;
import pl.lodz.p.repo.MongoUUIDEnt;
import pl.lodz.p.repo.user.repo.UserRepository;
import pl.lodz.p.repo.user.data.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class UserAdapter implements UGet, UUpdate, URemove, UAdd {

    private final UserRepository userRepository;

    @Override
    public void add(User user) {
        userRepository.add(convert(user));
    }

    @Override
    public long size() {
        return userRepository.size();
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        for (UserEnt ent : userRepository.getUsers()) {
            users.add(convert(ent));
        }
        return users;
    }

    @Override
    public User getUserByID(MongoUUID uuid) {
        return convert(userRepository.getUserByID(convert(uuid)));
    }

    @Override
    public User getUserByUsername(String username) {
        return convert(userRepository.getUserByUsername(username));
    }

    @Override
    public List<User> getUsersByUsername(String username) {
        List<User> users = new ArrayList<>();
        for (UserEnt ent : userRepository.getUsersByUsername(username)) {
            users.add(convert(ent));
        }
        return users;
    }

    @Override
    public void remove(User user) {
        userRepository.remove(convert(user));
    }

    @Override
    public void update(MongoUUID uuid, Map<String, Object> fieldsToUpdate) {
        userRepository.update(convert(uuid), fieldsToUpdate);
    }

    @Override
    public void update(MongoUUID uuid, String field, Object value) {
        userRepository.update(convert(uuid), field, value);
    }

    private UserEnt convert(User user) {
        return switch (user.getClass().getSimpleName()) {
            case "Client" -> new ClientEnt(convert(user.getEntityId()), user.getFirstName(), user.getUsername(), user.getPassword(),
                    user.getSurname(), user.getEmailAddress(), convert(user.getRole()), user.isActive(), new StandardEnt(), ((Client) user).getCurrentRents());
            case "ResourceManager" -> new ResourceManagerEnt(convert(user.getEntityId()), user.getFirstName(), user.getSurname(), user.getUsername(), user.getPassword(), user.getEmailAddress());
            case "Admin" -> new AdminEnt(convert(user.getEntityId()), user.getFirstName(), user.getSurname(), user.getUsername(), user.getEmailAddress(), user.getPassword());
            default -> throw new RuntimeException("Unsupported type: " + user.getClass().getSimpleName());
        };
    }

    private User convert(UserEnt ent) {
        if (ent == null) {
            return null;
        }
        return switch (ent.getClass().getSimpleName()) {
            case "ClientEnt" -> new Client(convert(ent.getEntityId()), ent.getFirstName(), ent.getUsername(), ent.getPassword(),
                    ent.getSurname(), ent.getEmailAddress(), convert(ent.getRoleEnt()), ent.isActive(), new Standard(), ((ClientEnt) ent).getCurrentRents());
            case "ResourceManagerEnt" -> new ResourceManager(convert(ent.getEntityId()), ent.getFirstName(), ent.getSurname(), ent.getUsername(), ent.getPassword(), ent.getEmailAddress());
            case "AdminEnt" -> new Admin(convert(ent.getEntityId()), ent.getFirstName(), ent.getSurname(), ent.getUsername(), ent.getEmailAddress(), ent.getPassword());
            default -> throw new RuntimeException("Unsupported type: " + ent.getClass().getSimpleName());
        };
    }

    private MongoUUID convert(MongoUUIDEnt ent) {
        MongoUUID uuid = new MongoUUID();
        try {
            PropertyUtils.copyProperties(uuid, ent);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed: " + e);
        }
        return uuid;
    }

    private MongoUUIDEnt convert(MongoUUID uuid) {
        MongoUUIDEnt ent = new MongoUUIDEnt();
        try {
            PropertyUtils.copyProperties(ent, uuid);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed: " + e);
        }
        return ent;
    }

    private Role convert(RoleEnt ent) {
        if (ent == RoleEnt.ADMIN) {
            return Role.ADMIN;
        }
        else if (ent == RoleEnt.CLIENT) {
            return Role.CLIENT;
        }
        else if (ent == RoleEnt.RESOURCE_MANAGER) {
            return Role.RESOURCE_MANAGER;
        }
        return null;
    }

    private RoleEnt convert(Role role) {
        if (role == Role.ADMIN) {
            return RoleEnt.ADMIN;
        }
        else if (role == Role.CLIENT) {
            return RoleEnt.CLIENT;
        }
        else if (role == Role.RESOURCE_MANAGER) {
            return RoleEnt.RESOURCE_MANAGER;
        }
        return null;
    }
}

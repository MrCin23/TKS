package pl.lodz.p.repo.adapter;

import com.mongodb.client.model.Filters;
import lombok.AllArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.user.Admin;
import pl.lodz.p.core.domain.user.Client;
import pl.lodz.p.core.domain.user.ResourceManager;
import pl.lodz.p.core.domain.user.User;
import pl.lodz.p.port.infrastructure.user.UAdd;
import pl.lodz.p.port.infrastructure.user.UGet;
import pl.lodz.p.port.infrastructure.user.URemove;
import pl.lodz.p.port.infrastructure.user.UUpdate;
import pl.lodz.p.repo.model.MongoUUIDEnt;
import pl.lodz.p.repo.model.user.AdminEnt;
import pl.lodz.p.repo.model.user.ClientEnt;
import pl.lodz.p.repo.model.user.ResourceManagerEnt;
import pl.lodz.p.repo.model.user.UserEnt;
import pl.lodz.p.repo.repository.UserRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class UserAdapter implements UGet, UUpdate, URemove, UAdd {
    private UserRepository userRepository;

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
        UserEnt ent = switch (user.getClass().getSimpleName()) {
            case "Client" -> new ClientEnt();
            case "ResourceManager" -> new ResourceManagerEnt();
            case "Admin" -> new AdminEnt();
            default -> throw new RuntimeException("Unsupported type: " + user.getClass().getSimpleName());
        };
        try {
            PropertyUtils.copyProperties(ent, user);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed" + e);
        }
        return ent;
    }

    private User convert(UserEnt ent) {
        User user = switch (ent.getClass().getSimpleName()) {
            case "ClientEnt" -> new Client();
            case "ResourceManagerEnt" -> new ResourceManager();
            case "AdminEnt" -> new Admin();
            default -> throw new RuntimeException("Unsupported type: " + ent.getClass().getSimpleName());
        };
        try {
            PropertyUtils.copyProperties(user, ent);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed" + e);
        }
        return user;
    }

    private MongoUUID convert(MongoUUIDEnt ent) {
        MongoUUID uuid = new MongoUUID();
        try {
            PropertyUtils.copyProperties(uuid, ent);
        } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed: " + e);
        }
        return uuid;
    }

    private MongoUUIDEnt convert(MongoUUID uuid) {
        MongoUUIDEnt ent = new MongoUUIDEnt();
        try {
            PropertyUtils.copyProperties(ent, uuid);
        } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed: " + e);
        }
        return ent;
    }
}

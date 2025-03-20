package pl.lodz.p.port.infrastructure.user;

import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.user.User;

import java.util.List;

public interface UGet {
    long size();
    List<User> getUsers();
    User getUserByID(MongoUUID uuid);
    User getUserByUsername(String username);
    List<User> getUsersByUsername(String username);
}

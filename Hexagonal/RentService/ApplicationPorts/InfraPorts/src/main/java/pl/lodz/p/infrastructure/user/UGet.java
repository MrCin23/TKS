package pl.lodz.p.infrastructure.user;

import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.user.User;

import java.util.List;
@Component
public interface UGet {
    long size();
    List<User> getUsers();
    User getUserByID(MongoUUID uuid);
    User getUserByUsername(String username);
    List<User> getUsersByUsername(String username);
}

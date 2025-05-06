package pl.lodz.p.core.services.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import pl.lodz.p.core.domain.exception.DeactivatedUserException;
import pl.lodz.p.core.domain.exception.WrongPasswordException;
import pl.lodz.p.core.domain.user.User;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.user.UserPrincipal;
import pl.lodz.p.core.services.security.JwtTokenProvider;
import pl.lodz.p.infrastructure.user.UAdd;
import pl.lodz.p.infrastructure.user.UGet;
import pl.lodz.p.infrastructure.user.URemove;
import pl.lodz.p.infrastructure.user.UUpdate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UGet uGet;
    private final UAdd uAdd;
    private final URemove uRemove;
    private final UUpdate uUpdate;

    private JwtTokenProvider tokenProvider;

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public User createUser(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        if(uGet.getUserByID(user.getEntityId()) == null) {
            uAdd.add(user);
            return user;
        }
        throw new RuntimeException("User with id " + user.getEntityId() + " already exists");
    }

    public List<User> getAllUsers() {
        List<User> users = uGet.getUsers();
        if(users == null || users.isEmpty()) {
            throw new RuntimeException("No users found");
        }
        return uGet.getUsers();
    }

    public User getUser(UUID uuid) {
        User user = uGet.getUserByID(new MongoUUID(uuid));
        if(user == null) {
            throw new RuntimeException("User with id " + uuid + " does not exist");
        }
        return user;
    }

    public void updateUser(UUID uuid, Map<String, Object> fieldsToUpdate) {
        if(uGet.getUserByID(new MongoUUID(uuid)) == null) {
            throw new RuntimeException("User with id " + uuid + " does not exist");
        }
        uUpdate.update(new MongoUUID(uuid), fieldsToUpdate);
    }

    public void activateUser(UUID uuid) {
        if(uGet.getUserByID(new MongoUUID(uuid)) == null) {
            throw new RuntimeException("User with id " + uuid + " does not exist");
        }
        uUpdate.update(new MongoUUID(uuid), "active", true);
    }

    public void deactivateUser(UUID uuid) {
        if(uGet.getUserByID(new MongoUUID(uuid)) == null) {
            throw new RuntimeException("User with id " + uuid + " does not exist");
        }
        uUpdate.update(new MongoUUID(uuid), "active", false);
    }

    public String getUserByUsername(String username, String password) {
        if(uGet.getUserByUsername(username) == null) {
            throw new RuntimeException("User with username " + username + " does not exist");
        }
        User user = uGet.getUserByUsername(username);
        if(!user.isActive()) {
            throw new DeactivatedUserException("User with username " + username + " does not exist");
        }
        if(user.checkPassword(password)) {
            return tokenProvider.generateToken(username, user.getRole());
        } else {
            throw new WrongPasswordException("Wrong password");
        }
    }

    public User getUserByUsername(String username) {
        if(uGet.getUsersByUsername(username) == null || uGet.getUsersByUsername(username).isEmpty()) {
            throw new RuntimeException("No users with username " + username + " found");
        }
        return uGet.getUserByUsername(username);
    }

    public List<User> getUsersByUsername(String username) {
        if(uGet.getUsersByUsername(username) == null || uGet.getUsersByUsername(username).isEmpty()) {
            throw new RuntimeException("No users with username " + username + " found");
        }
        return uGet.getUsersByUsername(username);
    }

    public UserDetails loadUserByUsername(String username) {
        User user = uGet.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User with login " + username + " not found");
        }
        return new UserPrincipal(user);
    }

    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean checkToken(String token) {
        return blacklistedTokens.contains(token);
    }

    public void changePassword(String username, String newPassword){
        uUpdate.update(uGet.getUserByUsername(username).getEntityId(), "password", BCrypt.hashpw(newPassword, BCrypt.gensalt()));
    }

    public long size() {
        return uGet.size();
    }
}

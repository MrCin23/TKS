package pl.lodz.p.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import pl.lodz.p.dto.LoginDTO;
import pl.lodz.p.exception.DeactivatedUserException;
import pl.lodz.p.exception.WrongPasswordException;
import pl.lodz.p.model.user.User;
import pl.lodz.p.model.MongoUUID;
import pl.lodz.p.model.user.UserPrincipal;
import pl.lodz.p.repository.UserRepository;
import pl.lodz.p.security.JwtTokenProvider;
import pl.lodz.p.service.IUserService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class UserService implements IUserService, UserDetailsService {

    private UserRepository repo;

    private JwtTokenProvider tokenProvider;

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    @Override
    public User createUser(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        if(repo.getUserByID(user.getEntityId()) == null) {
            repo.add(user);
            return user;
        }
        throw new RuntimeException("User with id " + user.getEntityId() + " already exists");
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = repo.getUsers();
        if(users == null || users.isEmpty()) {
            throw new RuntimeException("No users found");
        }
        return repo.getUsers();
    }

    @Override
    public User getUser(UUID uuid) {
        User user = repo.getUserByID(new MongoUUID(uuid));
        if(user == null) {
            throw new RuntimeException("User with id " + uuid + " does not exist");
        }
        return user;
    }

    @Override
    public void updateUser(UUID uuid, Map<String, Object> fieldsToUpdate) {
        if(repo.getUserByID(new MongoUUID(uuid)) == null) {
            throw new RuntimeException("User with id " + uuid + " does not exist");
        }
        repo.update(new MongoUUID(uuid), fieldsToUpdate);
    }

    @Override
    public void activateUser(UUID uuid) {
        if(repo.getUserByID(new MongoUUID(uuid)) == null) {
            throw new RuntimeException("User with id " + uuid + " does not exist");
        }
        repo.update(new MongoUUID(uuid), "active", true);
    }

    @Override
    public void deactivateUser(UUID uuid) {
        if(repo.getUserByID(new MongoUUID(uuid)) == null) {
            throw new RuntimeException("User with id " + uuid + " does not exist");
        }
        repo.update(new MongoUUID(uuid), "active", false);
    }

//    @Override
//    public void deleteUser(UUID uuid) {
//        repo.remove(repo.getUserByID(new MongoUUID(uuid)));
//    }

    @Override
    public String getUserByUsername(LoginDTO loginDTO) {
        if(repo.getUserByUsername(loginDTO.getUsername()) == null) {
            throw new RuntimeException("User with username " + loginDTO.getUsername() + " does not exist");
        }
        User user = repo.getUserByUsername(loginDTO.getUsername());
        if(!user.isActive()) {
            throw new DeactivatedUserException("User with username " + loginDTO.getUsername() + " does not exist");
        }
        if(user.checkPassword(loginDTO.getPassword())) {
            return tokenProvider.generateToken(loginDTO.getUsername(), user.getRole());
        } else {
            throw new WrongPasswordException("Wrong password");
        }
    }

    @Override
    public User getUserByUsername(String username) {
        if(repo.getUsersByUsername(username) == null || repo.getUsersByUsername(username).isEmpty()) {
            throw new RuntimeException("No users with username " + username + " found");
        }
        return repo.getUserByUsername(username);
    }

    @Override
    public List<User> getUsersByUsername(String username) {
        if(repo.getUsersByUsername(username) == null || repo.getUsersByUsername(username).isEmpty()) {
            throw new RuntimeException("No users with username " + username + " found");
        }
        return repo.getUsersByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = repo.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User with login " + username + " not found");
        }
        return new UserPrincipal(user);
    }

    @Override
    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
    }

    @Override
    public boolean checkToken(String token) {
        return blacklistedTokens.contains(token);
    }

    @Override
    public void changePassword(String username, String newPassword){
        repo.update(repo.getUserByUsername(username).getEntityId(), "password", BCrypt.hashpw(newPassword, BCrypt.gensalt()));
    }
}

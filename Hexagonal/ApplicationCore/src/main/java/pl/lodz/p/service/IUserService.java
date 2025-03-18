package pl.lodz.p.service;

import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.dto.LoginDTO;
import pl.lodz.p.model.user.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IUserService {
    User createUser(User user);

    List<User> getAllUsers();

    User getUser(UUID uuid);

    void updateUser(UUID uuid, Map<String, Object> fieldsToUpdate);

    void activateUser(UUID uuid);

    void deactivateUser(UUID uuid);

    //void deleteUser(UUID uuid);

    String getUserByUsername(LoginDTO loginDTO);

    User getUserByUsername(String username);

    List<User> getUsersByUsername(String username);

    UserDetails loadUserByUsername(String username);

    void invalidateToken(String token);

    boolean checkToken(String token);

    void changePassword(String username, String newPassword);
}

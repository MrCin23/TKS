package pl.lodz.p.user.ui;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.lodz.p.user.rest.model.dto.LoginDTO;
import pl.lodz.p.user.rest.model.user.RESTUser;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public interface RESTUserServicePort {
    RESTUser createUser(RESTUser user);

    List<RESTUser> getAllUsers();

    RESTUser getUser(UUID uuid);

    void updateUser(UUID uuid, Map<String, Object> fieldsToUpdate);

    void activateUser(UUID uuid);

    void deactivateUser(UUID uuid);

    void deleteUser(UUID uuid);

    String getUserByUsername(LoginDTO loginDTO);

    RESTUser getUserByUsername(String username);

    List<RESTUser> getUsersByUsername(String username);

    UserDetails loadUserByUsername(String username);

    void invalidateToken(String token);

    boolean checkToken(String token);

    void changePassword(String username, String newPassword);
}

package pl.lodz.p.ui;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.lodz.p.soap.model.dto.LoginDTO;
import pl.lodz.p.soap.model.user.SOAPUser;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public interface SOAPUserServicePort {
    SOAPUser createUser(SOAPUser user);

    List<SOAPUser> getAllUsers();

    SOAPUser getUser(UUID uuid);

    void updateUser(UUID uuid, Map<String, Object> fieldsToUpdate);

    void activateUser(UUID uuid);

    void deactivateUser(UUID uuid);

    String getUserByUsername(LoginDTO loginDTO);

    SOAPUser getUserByUsername(String username);

    List<SOAPUser> getUsersByUsername(String username);

    UserDetails loadUserByUsername(String username);

    void invalidateToken(String token);

    boolean checkToken(String token);

    void changePassword(String username, String newPassword);
}

package pl.lodz.p.ui;

import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.soap.model.dto.LoginDTO;
import pl.lodz.p.users.LoginUser;
import pl.lodz.p.users.UserType;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SOAPUserServicePort {
    UserType createUser(UserType user);

    List<UserType> getAllUsers();

    UserType getUser(UUID uuid);

    void updateUser(UUID uuid, Map<String, Object> fieldsToUpdate);

    void activateUser(UUID uuid);

    void deactivateUser(UUID uuid);

    String getUserByUsername(LoginUser loginUser);

    UserType getUserByUsername(String username);

    List<UserType> getUsersByUsername(String username);

    UserDetails loadUserByUsername(String username);

    void invalidateToken(String token);

    boolean checkToken(String token);

    void changePassword(String username, String newPassword);

    long size();
}

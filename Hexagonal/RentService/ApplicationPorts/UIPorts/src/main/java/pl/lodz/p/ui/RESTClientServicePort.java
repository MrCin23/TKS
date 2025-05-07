package pl.lodz.p.ui;

import org.springframework.stereotype.Component;
import pl.lodz.p.rest.model.dto.LoginDTO;
import pl.lodz.p.rest.model.user.RESTClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public interface RESTClientServicePort {
    RESTClient createClient(RESTClient user);

    List<RESTClient> getAllClients();

    RESTClient getClient(UUID uuid);

    void updateClient(UUID uuid, Map<String, Object> fieldsToUpdate);

    void activateClient(UUID uuid);

    void deactivateClient(UUID uuid);


    RESTClient getClientByUsername(String username);

    List<RESTClient> getClientsByUsername(String username);

    void invalidateToken(String token);

    boolean checkToken(String token);

    void changePassword(String username, String newPassword);
}

package pl.lodz.p.core.services.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.user.Client;
//import pl.lodz.p.core.services.security.JwtTokenProvider;
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
public class ClientService implements UserDetailsService {

    private final UGet uGet;
    private final UAdd uAdd;
    private final URemove uRemove;
    private final UUpdate uUpdate;

//    private JwtTokenProvider tokenProvider;

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public Client createClient(Client client) {
        if(uGet.getClientByID(client.getEntityId()) == null) {
            uAdd.add(client);
            return client;
        }
        throw new RuntimeException("Client with id " + client.getEntityId() + " already exists");
    }

    public List<Client> getAllClients() {
        List<Client> users = uGet.getClients();
        if(users == null || users.isEmpty()) {
            throw new RuntimeException("No users found");
        }
        return uGet.getClients();
    }

    public Client getClient(UUID uuid) {
        Client user = uGet.getClientByID(new MongoUUID(uuid));
        if(user == null) {
            throw new RuntimeException("Client with id " + uuid + " does not exist");
        }
        return user;
    }

    public void updateClient(UUID uuid, Map<String, Object> fieldsToUpdate) {
        if(uGet.getClientByID(new MongoUUID(uuid)) == null) {
            throw new RuntimeException("Client with id " + uuid + " does not exist");
        }
        uUpdate.update(new MongoUUID(uuid), fieldsToUpdate);
    }

    public void activateClient(UUID uuid) {
        if(uGet.getClientByID(new MongoUUID(uuid)) == null) {
            throw new RuntimeException("Client with id " + uuid + " does not exist");
        }
        uUpdate.update(new MongoUUID(uuid), "active", true);
    }

    public void deactivateClient(UUID uuid) {
        if(uGet.getClientByID(new MongoUUID(uuid)) == null) {
            throw new RuntimeException("Client with id " + uuid + " does not exist");
        }
        uUpdate.update(new MongoUUID(uuid), "active", false);
    }


    public Client getClientByUsername(String username) {
        if(uGet.getClientsByUsername(username) == null || uGet.getClientsByUsername(username).isEmpty()) {
            throw new RuntimeException("No users with username " + username + " found");
        }
        return uGet.getClientByUsername(username);
    }

    public List<Client> getClientsByUsername(String username) {
        if(uGet.getClientsByUsername(username) == null || uGet.getClientsByUsername(username).isEmpty()) {
            throw new RuntimeException("No users with username " + username + " found");
        }
        return uGet.getClientsByUsername(username);
    }

    public UserDetails loadUserByUsername(String username) {
        Client client = uGet.getClientByUsername(username);
        if (client == null) {
            throw new RuntimeException("Client with login " + username + " not found");
        }
        return client;
    }


    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean checkToken(String token) {
        return blacklistedTokens.contains(token);
    }

    public void changePassword(String username, String newPassword){
        uUpdate.update(uGet.getClientByUsername(username).getEntityId(), "password", BCrypt.hashpw(newPassword, BCrypt.gensalt()));
    }

    public long size() {
        return uGet.size();
    }
}

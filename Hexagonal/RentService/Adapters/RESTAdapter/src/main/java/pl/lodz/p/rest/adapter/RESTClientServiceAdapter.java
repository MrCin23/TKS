package pl.lodz.p.rest.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.user.*;
import pl.lodz.p.rest.model.RESTMongoUUID;
import pl.lodz.p.core.services.service.ClientService;
import pl.lodz.p.rest.model.user.*;
import pl.lodz.p.ui.RESTClientServicePort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RESTClientServiceAdapter implements RESTClientServicePort {

    private final ClientService clientService;

    @Override
    public RESTClient createClient(RESTClient user) {
        return convert(clientService.createClient(convert(user)));
    }

    @Override
    public List<RESTClient> getAllClients() {
        List<RESTClient> users = new ArrayList<>();
        for(Client user : clientService.getAllClients()) {
            users.add(convert(user));
        }
        return users;
    }

    @Override
    public RESTClient getClient(UUID uuid) {
        return convert(clientService.getClient(uuid));
    }

    @Override
    public void updateClient(UUID uuid, Map<String, Object> fieldsToUpdate) {
        clientService.updateClient(uuid, fieldsToUpdate);
    }

    @Override
    public void activateClient(UUID uuid) {
        clientService.activateClient(uuid);
    }

    @Override
    public void deactivateClient(UUID uuid) {
        clientService.deactivateClient(uuid);
    }


    @Override
    public RESTClient getClientByUsername(String username) {
        return convert(clientService.getClientByUsername(username));
    }

    @Override
    public List<RESTClient> getClientsByUsername(String username) {
        List<RESTClient> users = new ArrayList<>();
        for(Client user : clientService.getClientsByUsername(username)) {
            users.add(convert(user));
        }
        return users;
    }


    @Override
    public void invalidateToken(String token) {
        clientService.invalidateToken(token);
    }

    @Override
    public boolean checkToken(String token) {
        return clientService.checkToken(token);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        clientService.changePassword(username, newPassword);
    }

    private MongoUUID convert(RESTMongoUUID r) {
        return new MongoUUID(r.getUuid());
    }
    private RESTMongoUUID convert(MongoUUID r) {
        return new RESTMongoUUID(r.getUuid());
    }

    private Role convert(RESTRole ent) {
        if (ent == RESTRole.ADMIN) {
            return Role.ADMIN;
        }
        else if (ent == RESTRole.CLIENT) {
            return Role.CLIENT;
        }
        else if (ent == RESTRole.RESOURCE_MANAGER) {
            return Role.RESOURCE_MANAGER;
        }
        return null;
    }
    private RESTRole convert(Role role) {
        if (role == Role.ADMIN) {
            return RESTRole.ADMIN;
        }
        else if (role == Role.CLIENT) {
            return RESTRole.CLIENT;
        }
        else if (role == Role.RESOURCE_MANAGER) {
            return RESTRole.RESOURCE_MANAGER;
        }
        return null;
    }

    private RESTClient convert(Client user) {
        return new RESTClient(convert(user.getEntityId()), user.getUsername(), user.isActive(), new RESTStandard(), user.getCurrentRents());
    }

    private Client convert(RESTClient ent) {
        if (ent == null) {
            return null;
        }
        return new Client(convert(ent.getEntityId()), ent.getUsername(), new Standard(), ent.getCurrentRents(), ent.isActive());
    }
}

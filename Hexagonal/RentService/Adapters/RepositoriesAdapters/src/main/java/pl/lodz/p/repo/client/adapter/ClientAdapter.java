package pl.lodz.p.repo.client.adapter;

import lombok.AllArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.user.Client;
import pl.lodz.p.core.domain.user.Role;
import pl.lodz.p.core.domain.user.Standard;
import pl.lodz.p.infrastructure.user.UAdd;
import pl.lodz.p.infrastructure.user.UGet;
import pl.lodz.p.infrastructure.user.URemove;
import pl.lodz.p.infrastructure.user.UUpdate;
import pl.lodz.p.repo.MongoUUIDEnt;
import pl.lodz.p.repo.client.data.ClientEnt;
import pl.lodz.p.repo.client.data.RoleEnt;
import pl.lodz.p.repo.client.data.StandardEnt;
import pl.lodz.p.repo.client.repo.ClientRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class ClientAdapter implements UGet, UUpdate, URemove, UAdd {

    private final ClientRepository clientRepository;

    @Override
    public void add(Client user) {
        clientRepository.add(convert(user));
    }

    @Override
    public long size() {
        return clientRepository.size();
    }

    @Override
    public List<Client> getClients() {
        List<Client> users = new ArrayList<>();
        for (ClientEnt ent : clientRepository.getClients()) {
            users.add(convert(ent));
        }
        return users;
    }

    @Override
    public Client getClientByID(MongoUUID uuid) {
        return convert(clientRepository.getClientByID(convert(uuid)));
    }

    @Override
    public Client getClientByUsername(String username) {
        return convert(clientRepository.getClientByUsername(username));
    }

    @Override
    public List<Client> getClientsByUsername(String username) {
        List<Client> users = new ArrayList<>();
        for (ClientEnt ent : clientRepository.getClientsByUsername(username)) {
            users.add(convert(ent));
        }
        return users;
    }

    @Override
    public void remove(Client user) {
        clientRepository.remove(convert(user));
    }

    @Override
    public void update(MongoUUID uuid, Map<String, Object> fieldsToUpdate) {
        clientRepository.update(convert(uuid), fieldsToUpdate);
    }

    @Override
    public void update(MongoUUID uuid, String field, Object value) {
        clientRepository.update(convert(uuid), field, value);
    }

    private ClientEnt convert(Client user) {
        return new ClientEnt(convert(user.getEntityId()), user.getUsername(), user.isActive(), new StandardEnt(), user.getCurrentRents());
    }

    private Client convert(ClientEnt ent) {
        if (ent == null) {
            return null;
        }
        return new Client(convert(ent.getEntityId()), ent.getUsername(), new Standard(), ent.getCurrentRents(), ent.isActive());
    }

    private MongoUUID convert(MongoUUIDEnt ent) {
        MongoUUID uuid = new MongoUUID();
        try {
            PropertyUtils.copyProperties(uuid, ent);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed: " + e);
        }
        return uuid;
    }

    private MongoUUIDEnt convert(MongoUUID uuid) {
        MongoUUIDEnt ent = new MongoUUIDEnt();
        try {
            PropertyUtils.copyProperties(ent, uuid);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Property copying failed: " + e);
        }
        return ent;
    }

    private Role convert(RoleEnt ent) {
        if (ent == RoleEnt.ADMIN) {
            return Role.ADMIN;
        }
        else if (ent == RoleEnt.CLIENT) {
            return Role.CLIENT;
        }
        else if (ent == RoleEnt.RESOURCE_MANAGER) {
            return Role.RESOURCE_MANAGER;
        }
        return null;
    }

    private RoleEnt convert(Role role) {
        if (role == Role.ADMIN) {
            return RoleEnt.ADMIN;
        }
        else if (role == Role.CLIENT) {
            return RoleEnt.CLIENT;
        }
        else if (role == Role.RESOURCE_MANAGER) {
            return RoleEnt.RESOURCE_MANAGER;
        }
        return null;
    }
}

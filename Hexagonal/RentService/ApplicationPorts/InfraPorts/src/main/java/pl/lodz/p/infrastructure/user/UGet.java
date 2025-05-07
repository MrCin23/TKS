package pl.lodz.p.infrastructure.user;

import org.springframework.stereotype.Component;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.user.Client;

import java.util.List;
@Component
public interface UGet {
    long size();
    List<Client> getClients();
    Client getClientByID(MongoUUID uuid);
    Client getClientByUsername(String username);
    List<Client> getClientsByUsername(String username);
}

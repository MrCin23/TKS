package pl.lodz.p.manager;


import lombok.Getter;
import pl.lodz.p.model.user.Client;
import pl.lodz.p.model.user.ClientType;
import pl.lodz.p.model.MongoUUID;
import pl.lodz.p.model.user.User;
import pl.lodz.p.repository.UserRepository;

import java.util.List;
import java.util.Map;

@Getter
//ClientManager jako Singleton
public final class UserManager {
    private static UserManager instance;
    private final UserRepository clientsRepository;

    public UserManager() {
        clientsRepository = new UserRepository();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void registerExistingClient(User user) {
        clientsRepository.add(user);
    }

    public void registerClient(String firstName, String surname, String username, String password, String emailAddress, ClientType clientType) {
        Client client = new Client(firstName, surname, username, password, emailAddress, clientType);
        registerExistingClient(client);
    }

    public void unregisterClient(Client client) {
        clientsRepository.remove(client);
    }

    public void update(MongoUUID uuid, Map<String, Object> fieldsToUpdate) {
        clientsRepository.update(uuid, fieldsToUpdate);
    }

    public void update(MongoUUID uuid, String field, Object value) {
        clientsRepository.update(uuid, field, value);
    }

    //METHODS-----------------------------------

//    public String getAllClientsReport() {
//        return this.clientsRepository.getUsers().toString();
//    }
//    public List<Client> getAllClients() {
//        return this.clientsRepository.getUsers();
//    }
//    public Client getClient(MongoUUID uuid) {
//        return (Client) clientsRepository.getUser(uuid);
//    }
//
//    public int getClientsAmount() {
//        return clientsRepository.getClients().size();
//    }
}


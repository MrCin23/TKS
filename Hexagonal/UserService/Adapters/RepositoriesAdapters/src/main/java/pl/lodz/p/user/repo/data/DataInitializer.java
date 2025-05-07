package pl.lodz.p.user.repo.data;

import com.mongodb.client.model.IndexOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.springframework.security.crypto.bcrypt.BCrypt;
import pl.lodz.p.user.repo.user.data.*;
import pl.lodz.p.user.repo.user.repo.UserRepository;
import pl.lodz.p.user.repo.user.data.*;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataInitializer {
    private final UserRepository clientRepo = new UserRepository();
    List<UserEnt> clients;
    public void init(){
        clients = new ArrayList<>();
        initClient();
    }

    public void dropAndCreateClient(){
        clientRepo.getDatabase().getCollection("users").drop();
        clientRepo.getDatabase().createCollection("users");
        clientRepo.getDatabase().getCollection("users").createIndex(
                new Document("username", 1),
                new IndexOptions().unique(true)
        );
    }

    public void initClient(){
        clients = new ArrayList<>();
        clients.add(new ClientEnt("Bart", "Fox", "Idontexist", "a","BFox@tul.com", new PremiumEnt()));
        clients.add(new ClientEnt("Michael", "Corrugated", "DON_IAS","a", "MCorrugated@ias.pas.p.lodz.pl", new PremiumEnt()));
        clients.add(new ClientEnt("Matthew", "Tar", "MTar","a", "MTar@TarVSCorrugated.com", new PremiumEnt()));
        clients.add(new ClientEnt("Martin", "Bricky", "Brickman","a", "IntelEnjoyer@whatisonpage4035.com", new StandardEnt()));
        clients.add(new ClientEnt("Juan", "Escobar", "JEscobar","a", "JEscobar@colombianSnow.com", new StandardEnt()));
        clients.add(new AdminEnt("John Paul", "II", "jp2gmd","a", "kremowki@barka.va"));
        clients.add(new ResourceManagerEnt("Frank", "Pepper", "pepper","a", "zgon@delta.p.lodz.pl"));
        for(UserEnt client: clients ){
            client.setPassword(BCrypt.hashpw(client.getPassword(), BCrypt.gensalt()));
            clientRepo.add(client);
        }
    }
}

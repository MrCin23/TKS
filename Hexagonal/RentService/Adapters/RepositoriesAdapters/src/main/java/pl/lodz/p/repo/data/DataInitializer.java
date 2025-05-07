package pl.lodz.p.repo.data;

import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.springframework.security.crypto.bcrypt.BCrypt;
import pl.lodz.p.repo.rent.repo.RentRepository;
import pl.lodz.p.repo.client.repo.ClientRepository;
import pl.lodz.p.repo.vm.repo.VMachineRepository;
import pl.lodz.p.repo.rent.data.RentEnt;
import pl.lodz.p.repo.client.data.*;
import pl.lodz.p.repo.vm.data.AppleArchEnt;
import pl.lodz.p.repo.vm.data.VMachineEnt;
import pl.lodz.p.repo.vm.data.x86Ent;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataInitializer {
    private final ClientRepository clientRepo = new ClientRepository();
    private final RentRepository rentRepo = new RentRepository();
    private final VMachineRepository vmRepo = new VMachineRepository();
    List<ClientEnt> clients;
    List<RentEnt> rents;
    List<VMachineEnt> vms;
    public void init(){
        clients = new ArrayList<>();
        rents = new ArrayList<>();
        vms = new ArrayList<>();
        initClient();
        initVM();
        initRent();
    }

    public void dropAndCreateClient(){
        clientRepo.getDatabase().getCollection("users").drop();
        clientRepo.getDatabase().createCollection("users");
        clientRepo.getDatabase().getCollection("users").createIndex(
                new Document("username", 1),
                new IndexOptions().unique(true)
        );
    }

    public void dropAndCreateVMachine(){
        vmRepo.getDatabase().getCollection("vMachines").drop();
        ValidationOptions validationOptions = new ValidationOptions().validator(
                        Document.parse("""
            {
                $jsonSchema: {
                    "bsonType": "object",
                    "required": [ "_id", "_clazz", "CPUNumber", "ramSize", "isRented", "actualRentalPrice" ],
                    "properties": {
                        "_id" : {
                            "bsonType": "string",
                            "minLength": 36,
                            "maxLength": 36
                        }
                        "_clazz" : {
                            "bsonType": "string"
                        }
                        "CPUNumber" : {
                            "bsonType": "int"
                            "minimum" : 1
                        }
                        "ramSize" : {
                            "bsonType": "string"
                        }
                        "isRented" : {
                            "bsonType": "int",
                            "minimum" : 0,
                            "maximum" : 1,
                            "description": "must be 1 for rented and for available"
                        }
                        "actualRentalPrice" : {
                            "bsonType": "double"
                        }
                        "manufacturer" : {
                            "bsonType": "string"
                        }
                    }
                }
            }
        """))
                .validationAction (ValidationAction.ERROR);
        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions() .validationOptions (validationOptions);
        vmRepo.getDatabase().createCollection("vMachines", createCollectionOptions);
    }

    public void dropAndCreateRent(){
        rentRepo.getDatabase().getCollection("rents").drop();
        rentRepo.getDatabase().getCollection("users").drop();
        rentRepo.getDatabase().getCollection("vMachines").drop();
        rentRepo.getDatabase().createCollection("vMachines");
        rentRepo.getDatabase().createCollection("users");
        rentRepo.getDatabase().createCollection("rents");
    }

    public void initClient(){
        clients = new ArrayList<>();
        clients.add(new ClientEnt("Idontexist", new PremiumEnt(), true));
        clients.add(new ClientEnt("DON_IAS", new PremiumEnt(), true));
        clients.add(new ClientEnt("MTar", new PremiumEnt(), true));
        clients.add(new ClientEnt("Brickman", new StandardEnt(), true));
        clients.add(new ClientEnt("JEscobar", new StandardEnt(), true));
        for(ClientEnt client: clients ){
            clientRepo.add(client);
        }
    }

    public void initVM(){
        vms = new ArrayList<>();
        vms.add(new AppleArchEnt(4, "4GB"));
        vms.add(new AppleArchEnt(24, "128GB"));
        vms.add(new x86Ent(8, "8GB", "AMD"));
        vms.add(new x86Ent(16, "32GB", "Intel"));
        vms.add(new x86Ent(128, "256GB", "Other"));
        vms.add(new x86Ent(128, "256GB", "Other"));
        for(VMachineEnt vm: vms ){
            vmRepo.add(vm);
        }
    }

    public void initRent(){
        rents = new ArrayList<>();
        initClient();
        initVM();
        rents.add(new RentEnt((ClientEnt)clients.get(0), vms.get(0), LocalDateTime.of(2024,11,21,21,37)));
//        rents.add(new RentEnt((ClientEnt)clients.get(0), vms.get(2), LocalDateTime.of(2024,10,26,21,37)));
//        rents.add(new RentEnt((ClientEnt)clients.get(3), vms.get(3), LocalDateTime.of(2023,10,26,21,37)));
        rents.add(new RentEnt((ClientEnt)clients.get(4), vms.get(1), LocalDateTime.of(2023,11,11,11,11)));
//        rents.add(new RentEnt((ClientEnt)clients.get(1), vms.get(4), LocalDateTime.of(2011,11,11,11,11)));
        rentRepo.add(rents.get(0));
        rentRepo.add(rents.get(1));
//        rentRepo.add(rents.get(2));
//        rentRepo.add(rents.get(3));
//        rentRepo.add(rents.get(4));
    }
}

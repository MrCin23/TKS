package pl.lodz.p.repo.repository;

import com.mongodb.MongoCommandException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import pl.lodz.p.repo.model.MongoUUIDEnt;
import pl.lodz.p.repo.model.RentEnt;
import pl.lodz.p.repo.model.VMachineEnt;
import pl.lodz.p.repo.model.user.ClientEnt;
import pl.lodz.p.repo.model.user.UserEnt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class RentRepository extends AbstractMongoRepository {
    private final MongoCollection<RentEnt> rents;
    private final MongoCollection<VMachineEnt> vMachines;
    private final MongoCollection<UserEnt> clients;

    public RentRepository() {
        super.initDbConnection();
        MongoIterable<String> list = this.getDatabase().listCollectionNames();
        for (String name : list) {
            if (name.equals("rents")) {
                this.getDatabase().getCollection(name).drop();
                break;
            }
        }
        this.getDatabase().createCollection("rents");

        this.rents = this.getDatabase().getCollection("rents", RentEnt.class);
        this.vMachines = this.getDatabase().getCollection("vMachines", VMachineEnt.class);
        this.clients = this.getDatabase().getCollection("users", UserEnt.class);
    }

    public void endRent(MongoUUIDEnt uuid, LocalDateTime endTime){
        ClientSession session = getMongoClient().startSession();
        try {
            session.startTransaction();

            Bson filter1 = Filters.eq("_id", uuid.getUuid());
            RentEnt rent = rents.find(filter1).first();
            if(rent == null){
                throw new RuntimeException("RentEnt not found");
            } else if(rent.getEndTime() == null) {
                rent.endRent(endTime);
                Bson update1 = Updates.set("endTime", rent.getEndTime());
                rents.updateOne(session, filter1, update1);
                update1 = Updates.set("rentCost", rent.getRentCost());
                rents.updateOne(session, filter1, update1);
            }

            if(endTime.isBefore(rent.getBeginTime())){
                throw new RuntimeException("RentEnt cannot be ended before it has even begun! Aborting transaction");
            }

            Bson filter = Filters.eq("_id", rent.getVMachine().getEntityId().getUuid().toString());
            Bson update = Updates.inc("isRented", -1);
            vMachines.updateOne(session, filter, update);

            Bson filter2 = Filters.eq("_id", rent.getClientEnt().getEntityId().getUuid());
            Bson update2 = Updates.inc("currentRents", -1);
            vMachines.updateOne(session, filter2, update2);
            clients.updateOne(session, filter2, update2);
            session.commitTransaction();
        } catch (MongoCommandException ex) {
            session.abortTransaction();
        } finally {
            session.close();
        }
    }

    public void update(long id, Map<String, Object> fieldsToUpdate) {

    }

    public void add(RentEnt rent) {
        ClientSession session = getMongoClient().startSession();
        UserEnt client;
        try {
            session.startTransaction();
//            VMachine vm = getVMachineById(rent.getVMachine().getEntityId().getUuid());
//            if(vm.isRented()>0){
//                throw new RuntimeException("I really shouldnt have to do this");
//            }
            Bson clientFilter = Filters.eq("_id", rent.getClientEnt().getEntityId().getUuid());
            Bson updateClientFilter = Updates.inc("currentRents", 1);
            clients.updateOne(session, clientFilter, updateClientFilter);
            Bson currentRentsFilter = Filters.lt("currentRents", rent.getClientEnt().getClientTypeEnt().getMaxRentedMachines());
            client = clients.find(Filters.and(clientFilter, currentRentsFilter)).first();
            if(client == null || !client.isActive()){
                throw new RuntimeException("Client doesnt exist or is not active");
            }
            Bson filter = Filters.eq("_id", rent.getVMachine().getEntityId().getUuid().toString());
            Bson update = Updates.inc("isRented", 1);
            vMachines.updateOne(session, filter, update);
            rents.insertOne(rent);
            session.commitTransaction();
        } catch (Exception ex) {
            session.abortTransaction();
        } finally {
            session.close();
        }

    }

    public long size(boolean active) {

        return 0;
    }

    public long size() {
        return rents.find().into(new ArrayList<>()).size();
    }

    public List<RentEnt> getRents(boolean active) {
        return rents.find().into(new ArrayList<>());
    }

//    public List<RentEnt> getClientRents(MongoUUID clientId) {
//        Bson filter1 = Filters.eq("client._id", clientId.getUuid());
//        return rents.find(filter1).into(new ArrayList<>());
//    }

    public List<RentEnt> getClientRents(String username) {
        Bson filter1 = Filters.eq("client.username", username);
        return rents.find(filter1).into(new ArrayList<>());
    }

    public List<RentEnt> getClientRents(UUID uuid) {
        Bson filter1 = Filters.eq("client._id", uuid);
        return rents.find(filter1).into(new ArrayList<>());
    }

    public List<RentEnt> getClientRents(MongoUUIDEnt clientId, boolean active) {
        Bson filter1 = Filters.eq("client._id", clientId.getUuid());
        Bson filter2;
        if(active){
            filter2 = Filters.eq("endTime", null);
        } else {
            filter2 = Filters.ne("endTime", null);
        }
        Bson filter3 = Filters.and(filter1, filter2);
        return rents.find(filter3).into(new ArrayList<>());
    }

    public List<RentEnt> getVMachineRents(MongoUUIDEnt vmId, boolean active) {
        Bson filter1 = Filters.eq("vMachine._id", vmId.getUuid().toString());
        Bson filter2;
        if(active){
            filter2 = Filters.eq("endTime", null);
        } else {
            filter2 = Filters.ne("endTime", null);
        }
        Bson filter3 = Filters.and(filter1, filter2);

        return rents.find(filter3).into(new ArrayList<>());
    }

    public List<RentEnt> getRents() {
        return rents.find().into(new ArrayList<>());
    }

    public RentEnt getRentByID(MongoUUIDEnt uuid) {
        Bson filter = Filters.eq("_id", uuid.getUuid());
        return rents.find(filter).first();
    }

    public List<RentEnt> findBy(String field, Object value) {
        return rents.find(Filters.eq(field, value)).into(new ArrayList<>());
    }

    public List<RentEnt> findByNegation(String field, Object value) {
        return rents.find(Filters.ne(field, value)).into(new ArrayList<>());
    }

    public void remove(MongoUUIDEnt uuid) {
        ClientSession session = getMongoClient().startSession();
        ClientEnt client;
        try {
            Bson idFilter = Filters.eq("_id", uuid.getUuid());
            Bson activeFilter = Filters.eq("endTime", null);
            Bson filter = Filters.and(idFilter, activeFilter);
            RentEnt rentToDelete = rents.find(filter).first();
            if (rentToDelete != null) {
                Bson clientUpdate = Updates.inc("currentRents", -1);
                Bson vMachineUpdate = Updates.inc("isRented", -1);
                clients.updateOne(session, Filters.eq("_id", rentToDelete.getClientEnt().getEntityId().getUuid()), clientUpdate);
                vMachines.updateOne(session, Filters.eq("_id", rentToDelete.getVMachine().getEntityId().getUuid().toString()), vMachineUpdate);
                rents.deleteOne(filter);
            } else {
                throw new RuntimeException("RentEnt do not exist");
            }
        }
        catch (MongoCommandException ex) {
            session.abortTransaction();
        }
        finally {
            session.close();
        }
    }
//
//    private VMachine getVMachineById(UUID vMachineId) {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://localhost:8081/REST/api/vmachine/" + vMachineId;
//        try {
//            return restTemplate.getForObject(url, VMachine.class);
//        } catch (Exception e) {
//            throw new RuntimeException("Request GET http://localhost:8081/REST/api/vmachine/" + vMachineId + " failed: " + e);
//        }
//    }
}

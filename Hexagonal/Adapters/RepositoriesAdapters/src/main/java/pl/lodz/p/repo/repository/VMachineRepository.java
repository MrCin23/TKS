package pl.lodz.p.repo.repository;

import com.mongodb.MongoCommandException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import pl.lodz.p.repo.model.MongoUUIDEnt;
import pl.lodz.p.repo.model.VMachineEnt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class VMachineRepository extends AbstractMongoRepository {
    private final String collectionName = "vMachines";
    private final MongoCollection<VMachineEnt> vMachines;

    public VMachineRepository() {
        super.initDbConnection();
        MongoIterable<String> list = this.getDatabase().listCollectionNames();
        for (String name : list) {
            if (name.equals(collectionName)) {
                this.getDatabase().getCollection(name).drop();
                break;
            }
        }

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
                        "CPUManufacturer" : {
                            "bsonType": "string"
                        }
                    }
                }
            }
        """))
        .validationAction (ValidationAction.ERROR);
        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions() .validationOptions (validationOptions);
        this.getDatabase().createCollection(collectionName, createCollectionOptions);

        this.vMachines = this.getDatabase().getCollection(collectionName, VMachineEnt.class);
    }

    public void update(MongoUUIDEnt uuid, Map<String, Object> fieldsToUpdate) {
        ClientSession session = getMongoClient().startSession();
        try {
            session.startTransaction();
            Bson filter = Filters.eq("_id", uuid.getUuid().toString());
            Bson update;
            for (Map.Entry<String, Object> entry : fieldsToUpdate.entrySet()) {
                String fieldName = entry.getKey();
                Object fieldValue = entry.getValue();
                if(Objects.equals(fieldName, "isRented")){
                    if((int)fieldValue == 1) {
                        update = Updates.inc("isRented", 1);
                    } else {
                        update = Updates.inc("isRented", -1);
                    }
                } else {
                    update = Updates.set(fieldName,fieldValue);
                }
                vMachines.updateOne(session, filter, update);
            }
            session.commitTransaction();
        } catch (MongoCommandException ex) {
            session.abortTransaction();
        } finally {
            session.close();
        }
    }

    public void update(MongoUUIDEnt uuid, String field, Object value) {
        ClientSession session = getMongoClient().startSession();
        try {
            session.startTransaction();
            Bson filter = Filters.eq("_id", uuid.getUuid().toString());
            Bson update;
            if(Objects.equals(field, "isRented")){
                if((int)value == 1) {
                    update = Updates.inc("isRented", 1);
                } else {
                    update = Updates.inc("isRented", -1);
                }
            } else {
                update = Updates.set(field,value);
            }
            vMachines.updateOne(session, filter, update);
            session.commitTransaction();
        } catch (MongoCommandException ex) {
            session.abortTransaction();
        } finally {
            session.close();
        }
    }

    public void add(VMachineEnt vMachine) {
        vMachines.insertOne(vMachine);
    }

    public void remove(VMachineEnt vMachine) {
        Bson filter = Filters.eq("_id", vMachine.getEntityId().getUuid().toString());
        VMachineEnt deletedVMachine = vMachines.findOneAndDelete(filter);
    }

    public List<VMachineEnt> getVMachines() {
        return vMachines.find().into(new ArrayList<>());
    }

    public VMachineEnt getVMachineByID(MongoUUIDEnt uuid) {
        Bson filter = Filters.eq("_id", uuid.getUuid().toString());
        return vMachines.find(filter).first();
    }
}

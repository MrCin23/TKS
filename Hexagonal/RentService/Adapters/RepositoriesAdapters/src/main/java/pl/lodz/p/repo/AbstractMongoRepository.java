package pl.lodz.p.repo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import pl.lodz.p.repo.codec.CodecProvider;
import pl.lodz.p.repo.client.data.*;
import pl.lodz.p.repo.vm.data.AppleArchEnt;
import pl.lodz.p.repo.vm.data.x86Ent;

@Getter
@Setter
public abstract class AbstractMongoRepository implements AutoCloseable {
//    private final ConnectionString connectionString = new ConnectionString(
//            "mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=replica_set_single");
private ConnectionString connectionString = new ConnectionString(
        "mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_single"); //&authSource=admin
    private final MongoCredential credential = MongoCredential.createCredential(
            "admin", "admin", "adminpassword".toCharArray());
//    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
//            PojoCodecProvider.builder().automatic(true)
//                    .register(ClientType.class, Admin.class, Standard.class)
//                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION)).build());
    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
            PojoCodecProvider.builder().automatic(true)
                    .register(PremiumEnt.class).register(StandardEnt.class).register(x86Ent.class).register(AppleArchEnt.class)
                    .register(ClientEnt.class)
                    .conventions(Conventions.DEFAULT_CONVENTIONS).build());
    private MongoClient mongoClient;
    private MongoDatabase database;


    protected void initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(new CodecProvider()),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                        ))
                .build();
        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("vmrental");
    }

    @Override
    public void close() throws Exception {
        this.mongoClient.close();
    }
}
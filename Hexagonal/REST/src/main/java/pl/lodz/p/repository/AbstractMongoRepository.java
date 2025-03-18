package pl.lodz.p.repository;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import pl.lodz.p.codec.CodecProvider;
import pl.lodz.p.model.*;
import pl.lodz.p.model.user.*;

@Getter
public abstract class AbstractMongoRepository implements AutoCloseable {
//    private final ConnectionString connectionString = new ConnectionString(
//            "mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=replica_set_single");
private final ConnectionString connectionString = new ConnectionString(
        "mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_single"); //&authSource=admin
    private final MongoCredential credential = MongoCredential.createCredential(
            "admin", "admin", "adminpassword".toCharArray());
//    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
//            PojoCodecProvider.builder().automatic(true)
//                    .register(ClientType.class, Admin.class, Standard.class)
//                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION)).build());
    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
            PojoCodecProvider.builder().automatic(true)
                    .register(Premium.class).register(Standard.class).register(x86.class).register(AppleArch.class)
                    .register(Client.class).register(Admin.class).register(ResourceManager.class)
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
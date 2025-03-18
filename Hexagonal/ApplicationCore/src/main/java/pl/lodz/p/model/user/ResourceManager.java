package pl.lodz.p.model.user;

import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import pl.lodz.p.model.MongoUUID;

import java.util.UUID;

@NoArgsConstructor
@BsonDiscriminator(value="ResourceManager", key="_clazz")
public class ResourceManager extends User{
    public ResourceManager(String firstName, String surname, String username, String password, String emailAddress) {
        super(new MongoUUID(UUID.randomUUID()), firstName, username, password, surname, emailAddress, Role.RESOURCE_MANAGER, true);
    }

    public ResourceManager(MongoUUID uuid, String firstName, String surname, String username, String password, String emailAddress) {
        super(uuid, firstName, username, password, surname, emailAddress, Role.RESOURCE_MANAGER, true);
    }

    @Override
    public String toString() {
        return super.toString() + "::ResourceManager{}";
    }
}

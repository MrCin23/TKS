package pl.lodz.p.repo.user;

import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import pl.lodz.p.repo.MongoUUIDEnt;

import java.util.UUID;

@NoArgsConstructor
@BsonDiscriminator(value="ResourceManager", key="_clazz")
public class ResourceManagerEnt extends UserEnt {
    public ResourceManagerEnt(String firstName, String surname, String username, String password, String emailAddress) {
        super(new MongoUUIDEnt(UUID.randomUUID()), firstName, username, password, surname, emailAddress, RoleEnt.RESOURCE_MANAGER, true);
    }

    public ResourceManagerEnt(MongoUUIDEnt uuid, String firstName, String surname, String username, String password, String emailAddress) {
        super(uuid, firstName, username, password, surname, emailAddress, RoleEnt.RESOURCE_MANAGER, true);
    }

    @Override
    public String toString() {
        return super.toString() + "::ResourceManager{}";
    }
}

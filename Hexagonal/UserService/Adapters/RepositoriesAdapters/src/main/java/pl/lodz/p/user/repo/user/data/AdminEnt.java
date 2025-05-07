package pl.lodz.p.user.repo.user.data;

import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import pl.lodz.p.user.repo.MongoUUIDEnt;

import java.util.UUID;

@NoArgsConstructor
@BsonDiscriminator(value="Admin", key="_clazz")
public class AdminEnt extends UserEnt {
    public AdminEnt(String firstName, String surname, String username, String password, String emailAddress) {
        super(new MongoUUIDEnt(UUID.randomUUID()), firstName, username, password, surname, emailAddress, RoleEnt.ADMIN, true);
    }

    public AdminEnt(MongoUUIDEnt uuid, String firstName, String surname, String username, String emailAddress, String password) {
        super(uuid, firstName, username, password, surname, emailAddress, RoleEnt.ADMIN, true);
    }

    @Override
    public String toString() {
        return super.toString() + "::Admin{}";
    }
}

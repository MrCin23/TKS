package pl.lodz.p.repo.model.user;

import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import pl.lodz.p.repo.model.MongoUUIDEnt;

import java.util.UUID;

@NoArgsConstructor
@BsonDiscriminator(value="Admin", key="_clazz")
public class AdminEnt extends UserEnt {
    public AdminEnt(String firstName, String surname, String username, String password, String emailAddress) {
        super(new MongoUUIDEnt(UUID.randomUUID()), firstName, username, password, surname, emailAddress, RoleEnt.ADMIN, true);
    }

//    public Admin(MongoUUID uuid, String firstName, String surname, String username, String emailAddress) {
//        super(uuid, firstName, username, surname, emailAddress, Role.ADMIN, true);
//    }

    @Override
    public String toString() {
        return super.toString() + "::Admin{}";
    }
}

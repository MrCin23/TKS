package pl.lodz.p.model.user;

import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import pl.lodz.p.model.MongoUUID;

import java.util.UUID;

@NoArgsConstructor
@BsonDiscriminator(value="Admin", key="_clazz")
public class Admin extends User{
    public Admin(String firstName, String surname, String username, String password, String emailAddress) {
        super(new MongoUUID(UUID.randomUUID()), firstName, username, password, surname, emailAddress, Role.ADMIN, true);
    }

//    public Admin(MongoUUID uuid, String firstName, String surname, String username, String emailAddress) {
//        super(uuid, firstName, username, surname, emailAddress, Role.ADMIN, true);
//    }

    @Override
    public String toString() {
        return super.toString() + "::Admin{}";
    }
}

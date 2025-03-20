package pl.lodz.p.core.domain.user;

import lombok.NoArgsConstructor;
import pl.lodz.p.core.domain.MongoUUID;

import java.util.UUID;

@NoArgsConstructor
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

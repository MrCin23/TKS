package pl.lodz.p.user.rest.model.user;

import lombok.NoArgsConstructor;
import pl.lodz.p.user.rest.model.RESTMongoUUID;

import java.util.UUID;

@NoArgsConstructor
public class RESTAdmin extends RESTUser {
    public RESTAdmin(String firstName, String surname, String username, String password, String emailAddress) {
        super(new RESTMongoUUID(UUID.randomUUID()), firstName, username, password, surname, emailAddress, RESTRole.ADMIN, true);
    }

    public RESTAdmin(RESTMongoUUID uuid, String firstName, String surname, String username, String emailAddress, String password) {
        super(uuid, firstName, username, password, surname, emailAddress, RESTRole.ADMIN, true);
    }

    @Override
    public String toString() {
        return super.toString() + "::Admin{}";
    }
}

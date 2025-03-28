package pl.lodz.p.soap.model.user;

import lombok.NoArgsConstructor;
import pl.lodz.p.rest.model.RESTMongoUUID;

import java.util.UUID;

@NoArgsConstructor
public class SOAPAdmin extends SOAPUser {
    public SOAPAdmin(String firstName, String surname, String username, String password, String emailAddress) {
        super(new RESTMongoUUID(UUID.randomUUID()), firstName, username, password, surname, emailAddress, SOAPRole.ADMIN, true);
    }

    public SOAPAdmin(RESTMongoUUID uuid, String firstName, String surname, String username, String emailAddress, String password) {
        super(uuid, firstName, username, password, surname, emailAddress, SOAPRole.ADMIN, true);
    }

    @Override
    public String toString() {
        return super.toString() + "::Admin{}";
    }
}

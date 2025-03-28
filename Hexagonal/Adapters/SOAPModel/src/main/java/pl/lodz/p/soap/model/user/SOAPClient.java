package pl.lodz.p.soap.model.user;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.rest.model.RESTMongoUUID;
import pl.lodz.p.rest.model.user.RESTClientType;
import pl.lodz.p.rest.model.user.RESTRole;
import pl.lodz.p.rest.model.user.RESTUser;

import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
public class SOAPClient extends RESTUser {
    private RESTClientType RESTClientType;
    private int currentRents;

    @Override
    public String toString() {
        return super.toString() + "::Client{" +
                "clientType=" + RESTClientType +
                ", currentRents=" + currentRents +
                '}';
    }

    public SOAPClient(String firstName,
                      String surname,
                      String username,
                      String password,
                      String emailAddress,
                      RESTClientType RESTClientType) {
        super(new RESTMongoUUID(UUID.randomUUID()), firstName, username, password, surname, emailAddress, pl.lodz.p.rest.model.user.RESTRole.CLIENT, true);
        this.RESTClientType = RESTClientType;
        this.currentRents = 0;
    }

    public SOAPClient(RESTMongoUUID userId,
                      String firstName,
                      String username,
                      String password,
                      String surname,
                      String emailAddress,
                      RESTRole RESTRole,
                      boolean active,
                      RESTClientType RESTClientType,
                      int currentRents) {
        super(userId, firstName, username, password, surname, emailAddress, RESTRole, active);
        this.RESTClientType = RESTClientType;
        this.currentRents = currentRents;
    }
}

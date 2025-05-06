package pl.lodz.p.rest.model.user;


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
public class RESTClient extends RESTUser {
    private RESTClientType clientType;
    private int currentRents;

    @Override
    public String toString() {
        return super.toString() + "::Client{" +
                "clientType=" + clientType +
                ", currentRents=" + currentRents +
                '}';
    }

    public RESTClient(String firstName,
                      String surname,
                      String username,
                      String password,
                      String emailAddress,
                      RESTClientType RESTClientType) {
        super(new RESTMongoUUID(UUID.randomUUID()), firstName, username, password, surname, emailAddress, pl.lodz.p.rest.model.user.RESTRole.CLIENT, true);
        this.clientType = RESTClientType;
        this.currentRents = 0;
    }

    public RESTClient(RESTMongoUUID userId,
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
        this.clientType = RESTClientType;
        this.currentRents = currentRents;
    }
}

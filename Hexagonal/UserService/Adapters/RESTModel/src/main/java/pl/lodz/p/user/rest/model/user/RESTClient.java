package pl.lodz.p.user.rest.model.user;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.user.rest.model.RESTMongoUUID;

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
        super(new RESTMongoUUID(UUID.randomUUID()), firstName, username, password, surname, emailAddress, RESTRole.CLIENT, true);
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

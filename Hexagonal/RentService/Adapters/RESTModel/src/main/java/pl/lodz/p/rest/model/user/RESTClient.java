package pl.lodz.p.rest.model.user;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.rest.model.RESTAbstractEntityMgd;
import pl.lodz.p.rest.model.RESTMongoUUID;

import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
public class RESTClient extends RESTAbstractEntityMgd {
    private String username;
    private RESTClientType clientType;
    private int currentRents;
    private boolean active;

    @Override
    public String toString() {
        return super.toString() + "::Client{" +
                "clientType=" + clientType +
                ", currentRents=" + currentRents +
                '}';
    }

    public RESTClient(String username,
                      RESTClientType RESTClientType,
                      boolean active) {
        super(new RESTMongoUUID(UUID.randomUUID()));
        this.username = username;
        this.clientType = RESTClientType;
        this.currentRents = 0;
        this.active = active;
    }

    public RESTClient(RESTMongoUUID userId,
                      String username,
                      boolean active,
                      RESTClientType RESTClientType,
                      int currentRents) {
        super(userId);
        this.username = username;
        this.active = active;
        this.clientType = RESTClientType;
        this.currentRents = currentRents;
    }
}

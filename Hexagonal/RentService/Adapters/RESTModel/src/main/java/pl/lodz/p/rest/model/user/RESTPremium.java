package pl.lodz.p.rest.model.user;

import pl.lodz.p.rest.model.RESTMongoUUID;
import pl.lodz.p.rest.model.user.RESTClientType;

import java.util.UUID;

public class RESTPremium extends RESTClientType {


    public RESTPremium() {
        super(new RESTMongoUUID(UUID.randomUUID()), 10, "Admin");
    }


    @Override
    public String toString() {
        return "Premium " + this.getClass().getSimpleName();
    }
}

package pl.lodz.p.soap.model.user;

import pl.lodz.p.rest.model.RESTMongoUUID;
import pl.lodz.p.rest.model.user.RESTClientType;

import java.util.UUID;

public class SOAPPremium extends RESTClientType {


    public SOAPPremium() {
        super(new RESTMongoUUID(UUID.randomUUID()), 10, "Admin");
    }


    @Override
    public String toString() {
        return "Premium " + this.getClass().getSimpleName();
    }
}

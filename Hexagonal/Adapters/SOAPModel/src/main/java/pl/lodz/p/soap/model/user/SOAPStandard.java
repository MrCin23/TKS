package pl.lodz.p.soap.model.user;

import pl.lodz.p.rest.model.RESTMongoUUID;
import pl.lodz.p.rest.model.user.RESTClientType;

import java.util.UUID;

public class SOAPStandard extends RESTClientType {

    public SOAPStandard() {
        super(new RESTMongoUUID(UUID.randomUUID()), 3, "Standard");
    }

    @Override
    public String toString() {
        return "Standard " + this.getClass().getSimpleName();
    }
}

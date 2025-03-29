package pl.lodz.p.soap.model.user;


import pl.lodz.p.soap.model.SOAPMongoUUID;

import java.util.UUID;

public class SOAPStandard extends SOAPClientType {

    public SOAPStandard() {
        super(new SOAPMongoUUID(UUID.randomUUID()), 3, "Standard");
    }

    @Override
    public String toString() {
        return "Standard " + this.getClass().getSimpleName();
    }
}

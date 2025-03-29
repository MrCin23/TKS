package pl.lodz.p.soap.model.user;

import pl.lodz.p.soap.model.SOAPMongoUUID;

import java.util.UUID;

public class SOAPPremium extends SOAPClientType {


    public SOAPPremium() {
        super(new SOAPMongoUUID(UUID.randomUUID()), 10, "Admin");
    }


    @Override
    public String toString() {
        return "Premium " + this.getClass().getSimpleName();
    }
}

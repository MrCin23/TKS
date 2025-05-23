package pl.lodz.p.broker.dto;

import pl.lodz.p.user.rest.model.RESTMongoUUID;

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

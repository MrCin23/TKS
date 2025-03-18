package pl.lodz.p.model.user;

import pl.lodz.p.model.MongoUUID;

import java.util.UUID;

public class Premium extends ClientType {


    public Premium() {
        super(new MongoUUID(UUID.randomUUID()), 10, "Admin");
    }


    @Override
    public String toString() {
        return "Premium " + this.getClass().getSimpleName();
    }
}

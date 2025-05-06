package pl.lodz.p.core.domain.user;

import pl.lodz.p.core.domain.MongoUUID;

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

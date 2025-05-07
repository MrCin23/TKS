package pl.lodz.p.user.core.domain.user;

import pl.lodz.p.user.core.domain.MongoUUID;

import java.util.UUID;

public class Standard extends ClientType{

    public Standard() {
        super(new MongoUUID(UUID.randomUUID()), 3, "Standard");
    }

    @Override
    public String toString() {
        return "Standard " + this.getClass().getSimpleName();
    }
}

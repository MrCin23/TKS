package pl.lodz.p.repo.model.user;

import pl.lodz.p.repo.model.MongoUUIDEnt;

import java.util.UUID;

public class StandardEnt extends ClientTypeEnt {

    public StandardEnt() {
        super(new MongoUUIDEnt(UUID.randomUUID()), 3, "Standard");
    }

    @Override
    public String toString() {
        return "Standard " + this.getClass().getSimpleName();
    }
}

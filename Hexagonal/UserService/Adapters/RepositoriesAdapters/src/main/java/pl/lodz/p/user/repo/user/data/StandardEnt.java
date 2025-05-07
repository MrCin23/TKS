package pl.lodz.p.user.repo.user.data;

import pl.lodz.p.user.repo.MongoUUIDEnt;

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

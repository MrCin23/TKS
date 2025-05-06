package pl.lodz.p.repo.user.data;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import pl.lodz.p.repo.MongoUUIDEnt;

import java.util.UUID;

@BsonDiscriminator(value="admin", key="_clazz")
public class PremiumEnt extends ClientTypeEnt {


    public PremiumEnt() {
        super(new MongoUUIDEnt(UUID.randomUUID()), 10, "Admin");
    }


    @Override
    public String toString() {
        return "Premium " + this.getClass().getSimpleName();
    }
}

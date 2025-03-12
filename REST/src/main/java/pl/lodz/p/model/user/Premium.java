package pl.lodz.p.model.user;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import pl.lodz.p.model.MongoUUID;

import java.util.UUID;

@BsonDiscriminator(value="admin", key="_clazz")
public class Premium extends ClientType {


    public Premium() {
        super(new MongoUUID(UUID.randomUUID()), 10, "Admin");
    }


    @Override
    public String toString() {
        return "Premium " + this.getClass().getSimpleName();
    }
}

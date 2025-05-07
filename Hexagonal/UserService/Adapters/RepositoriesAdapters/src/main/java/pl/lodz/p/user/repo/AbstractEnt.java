package pl.lodz.p.user.repo;

import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public abstract class AbstractEnt implements Serializable {
    @BsonProperty("_id")
    @BsonId
    private MongoUUIDEnt entityId;

    public AbstractEnt() {
        entityId = new MongoUUIDEnt(UUID.randomUUID());
    } //to byc moze cos popsuje

    @BsonCreator
    public AbstractEnt(@BsonProperty MongoUUIDEnt entityId) {
        this.entityId = entityId;
    }
}
package pl.lodz.p.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public abstract class AbstractEntityMgd implements Serializable {
    @BsonProperty("_id")
    @BsonId
    private MongoUUID entityId;

    public AbstractEntityMgd() {
        entityId = new MongoUUID(UUID.randomUUID());
    } //to byc moze cos popsuje

    @BsonCreator
    public AbstractEntityMgd(@BsonProperty MongoUUID entityId) {
        this.entityId = entityId;
    }
}
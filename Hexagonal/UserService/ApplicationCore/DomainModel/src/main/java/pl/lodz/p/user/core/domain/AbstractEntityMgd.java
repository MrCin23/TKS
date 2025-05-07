package pl.lodz.p.user.core.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public abstract class AbstractEntityMgd implements Serializable {
    private MongoUUID entityId;

    public AbstractEntityMgd() {
        entityId = new MongoUUID(UUID.randomUUID());
    } //to byc moze cos popsuje

    public AbstractEntityMgd(MongoUUID entityId) {
        this.entityId = entityId;
    }
}
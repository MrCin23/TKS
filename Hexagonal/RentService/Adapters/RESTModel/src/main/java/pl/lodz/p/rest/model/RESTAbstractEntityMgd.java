package pl.lodz.p.rest.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public abstract class RESTAbstractEntityMgd implements Serializable {
    private RESTMongoUUID entityId;

    public RESTAbstractEntityMgd() {
        entityId = new RESTMongoUUID(UUID.randomUUID());
    } //to byc moze cos popsuje

    public RESTAbstractEntityMgd(RESTMongoUUID entityId) {
        this.entityId = entityId;
    }
}
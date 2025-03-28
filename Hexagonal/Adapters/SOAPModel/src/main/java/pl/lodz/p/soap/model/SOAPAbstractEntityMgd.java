package pl.lodz.p.soap.model;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.rest.model.RESTMongoUUID;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public abstract class SOAPAbstractEntityMgd implements Serializable {
    private RESTMongoUUID entityId;

    public SOAPAbstractEntityMgd() {
        entityId = new RESTMongoUUID(UUID.randomUUID());
    } //to byc moze cos popsuje

    public SOAPAbstractEntityMgd(RESTMongoUUID entityId) {
        this.entityId = entityId;
    }
}
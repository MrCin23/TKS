package pl.lodz.p.soap.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public abstract class SOAPAbstractEntityMgd implements Serializable {
    private SOAPMongoUUID entityId;

    public SOAPAbstractEntityMgd() {
        entityId = new SOAPMongoUUID(UUID.randomUUID());
    } //to byc moze cos popsuje

    public SOAPAbstractEntityMgd(SOAPMongoUUID entityId) {
        this.entityId = entityId;
    }
}
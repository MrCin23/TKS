package pl.lodz.p.user.soap.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class SOAPAbstractEntityMgd implements Serializable {
    @XmlElement(namespace = "http://p.lodz.pl/users")
    private SOAPMongoUUID entityId;

    public SOAPAbstractEntityMgd() {
        entityId = new SOAPMongoUUID(UUID.randomUUID());
    } //to byc moze cos popsuje

    public SOAPAbstractEntityMgd(SOAPMongoUUID entityId) {
        this.entityId = entityId;
    }
}
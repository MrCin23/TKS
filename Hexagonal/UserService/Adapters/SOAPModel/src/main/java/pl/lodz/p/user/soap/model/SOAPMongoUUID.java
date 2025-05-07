package pl.lodz.p.user.soap.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class SOAPMongoUUID {
    private UUID uuid;

    public SOAPMongoUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}

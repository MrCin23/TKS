package pl.lodz.p.soap.model.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.soap.model.SOAPAbstractEntityMgd;
import pl.lodz.p.soap.model.SOAPMongoUUID;

@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "_clazz"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SOAPStandard.class, name = "standard"),
        @JsonSubTypes.Type(value = SOAPPremium.class, name = "admin")
})
public abstract class SOAPClientType extends SOAPAbstractEntityMgd {

    protected int maxRentedMachines;

    protected String name;

    public SOAPClientType(SOAPMongoUUID uuid,
                          int maxRentedMachines,
                          String name){
        super(uuid);
        this.maxRentedMachines = maxRentedMachines;
        this.name = name;
    }

    public String toString() {
        return "Class: " + this.getClass().getSimpleName() + " " + this.getMaxRentedMachines() + " " + this.getName();
    }
}

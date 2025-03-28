package pl.lodz.p.soap.model.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.rest.model.RESTAbstractEntityMgd;
import pl.lodz.p.rest.model.RESTMongoUUID;
import pl.lodz.p.rest.model.user.RESTStandard;

@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "_clazz"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RESTStandard.class, name = "standard"),
        @JsonSubTypes.Type(value = SOAPPremium.class, name = "admin")
})
public abstract class SOAPClientType extends RESTAbstractEntityMgd {

    protected int maxRentedMachines;

    protected String name;

    public SOAPClientType(RESTMongoUUID uuid,
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

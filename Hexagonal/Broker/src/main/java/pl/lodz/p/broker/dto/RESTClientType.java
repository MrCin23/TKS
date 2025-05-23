package pl.lodz.p.broker.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.user.rest.model.RESTAbstractEntityMgd;
import pl.lodz.p.user.rest.model.RESTMongoUUID;
import pl.lodz.p.user.rest.model.user.RESTPremium;

@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "_clazz"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RESTStandard.class, name = "standard"),
        @JsonSubTypes.Type(value = RESTPremium.class, name = "admin")
})
public abstract class RESTClientType extends RESTAbstractEntityMgd {

    protected int maxRentedMachines;

    protected String name;

    public RESTClientType(RESTMongoUUID uuid,
                          int maxRentedMachines,
                          String name){
        super(uuid);
        this.maxRentedMachines = maxRentedMachines;
        this.name = name;
    }
}

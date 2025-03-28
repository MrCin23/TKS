package pl.lodz.p.soap.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "_clazz"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SOAPAppleArch.class, name = "applearch"),
        @JsonSubTypes.Type(value = SOAPx86.class, name = "x86")
})
public abstract class SOAPVMachine extends SOAPAbstractEntityMgd {

    @Min(1)
    private int CPUNumber;

    private String ramSize;

    @Min(0)
    private int isRented;

    protected float actualRentalPrice;

    public SOAPVMachine(int CPUNumber, String ramSize, int isRented) {
        super(new SOAPMongoUUID(UUID.randomUUID()));
        this.CPUNumber = CPUNumber;
        this.ramSize = ramSize;
        this.isRented = isRented;
    }

    public SOAPVMachine() {
        super(new SOAPMongoUUID(UUID.randomUUID()));
    }

    public SOAPVMachine(SOAPMongoUUID uuid, int CPUNumber,
                        String ramSize, int isRented) {
        super(uuid);
        this.CPUNumber = CPUNumber;
        this.ramSize = ramSize;
        this.isRented = isRented;
    }

    public int isRented() {
        return isRented;
    }

    public void setRented(int rented) {
        isRented = rented;
    }

    public float getActualRentalPrice() {
        return 0;
    }

    @Override
    public String toString() {
        return "VMachine{" +
                "CPUNumber=" + CPUNumber +
                ", ramSize='" + ramSize + '\'' +
                ", isRented=" + isRented +
                ", actualRentalPrice=" + actualRentalPrice +
                '}';
    }
};



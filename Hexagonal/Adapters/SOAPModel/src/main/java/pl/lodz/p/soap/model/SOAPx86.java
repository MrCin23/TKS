package pl.lodz.p.soap.model;


import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.rest.model.RESTMongoUUID;
import pl.lodz.p.rest.model.RESTVMachine;

@Getter
@Setter
public class SOAPx86 extends RESTVMachine {


    private String manufacturer;

    public SOAPx86(int CPUNumber, String ramSize, String manufacturer) {
        super(CPUNumber, ramSize, 0);
        this.manufacturer = manufacturer;
        this.actualRentalPrice = getActualRentalPrice();
    }

    public SOAPx86() {
        super();
    }


    public SOAPx86(RESTMongoUUID uuid, int CPUNumber, String ramSize,
                   int isRented, String manufacturer) {
        super(uuid, CPUNumber, ramSize, isRented);
        this.manufacturer = manufacturer;
        this.actualRentalPrice = getActualRentalPrice();
    }

    @Override
    public float getActualRentalPrice() {

        float basePrice = 10;
        int ramInGB = Integer.parseInt(getRamSize().replaceAll("[^0-9]", ""));
        if (ramInGB > 4) {
            int timesDoubled = (int) (Math.log(ramInGB / 4) / Math.log(2));
            for (int i = 0; i < timesDoubled; i++) {
                basePrice += basePrice / 2;
            }
        }

        float threadMultiplier = getCPUNumber() / 2.0f;

        float manufacturerMultiplier = 1;
        if (manufacturer.equalsIgnoreCase("Intel")) {
            manufacturerMultiplier = 3;
        } else if (manufacturer.equalsIgnoreCase("AMD")) {
            manufacturerMultiplier = 2;
        }

        return basePrice * threadMultiplier * manufacturerMultiplier;
    }

    public String toString() {
        return "x86 architecture: " + this.getEntityId().toString() + " " + this.getCPUNumber() + " " + this.getRamSize() + " " + this.isRented() + " " + this.getManufacturer() + " " + this.getActualRentalPrice();
    }
}

package pl.lodz.p.core.domain;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class x86 extends VMachine {


    private String CPUManufacturer;

    public x86(int CPUNumber, String ramSize, String CPUManufacturer) {
        super(CPUNumber, ramSize, 0);
        this.CPUManufacturer = CPUManufacturer;
        this.actualRentalPrice = getActualRentalPrice();
    }

    public x86() {
        super();
    }


    public x86(MongoUUID uuid, int CPUNumber, String ramSize,
               int isRented, String CPUManufacturer) {
        super(uuid, CPUNumber, ramSize, isRented);
        this.CPUManufacturer = CPUManufacturer;
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
        if (CPUManufacturer.equalsIgnoreCase("Intel")) {
            manufacturerMultiplier = 3;
        } else if (CPUManufacturer.equalsIgnoreCase("AMD")) {
            manufacturerMultiplier = 2;
        }

        return basePrice * threadMultiplier * manufacturerMultiplier;
    }

    public String toString() {
        return "x86 architecture: " + this.getEntityId().toString() + " " + this.getCPUNumber() + " " + this.getRamSize() + " " + this.isRented() + " " + this.getCPUManufacturer() + " " + this.getActualRentalPrice();
    }
}

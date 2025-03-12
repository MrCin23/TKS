package unit.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.model.AppleArch;
import pl.lodz.p.model.MongoUUID;
import pl.lodz.p.model.VMachine;
import pl.lodz.p.model.x86;

import java.util.UUID;

public class VMachineTests {

    VMachine apple;
    VMachine x86;

    @BeforeEach
    public void setUp() {
        apple = new AppleArch(new MongoUUID(UUID.fromString("9c97c890-dfcd-4cb9-a4bc-5e3c27eba80f")), 8, "16GB", 0);
        x86 = new x86(new MongoUUID(UUID.fromString("9c97c890-dfcd-4cb9-a4bc-5e3c27eba80f")),16, "32GB", 0, "Intel");
    }

    @Test
    public void testToString() {
        Assertions.assertEquals("AppleArch: 9c97c890-dfcd-4cb9-a4bc-5e3c27eba80f 8 16GB 0 1800.0", apple.toString());
        Assertions.assertEquals("x86 architecture: 9c97c890-dfcd-4cb9-a4bc-5e3c27eba80f 16 32GB 0 Intel 810.0", x86.toString());
    }

    @Test
    public void testIsRented() {
        Assertions.assertEquals(0, apple.isRented());
        Assertions.assertEquals(0, x86.isRented());
    }

    @Test
    public void testSetIsRented() {
        Assertions.assertEquals(0, apple.isRented());
        Assertions.assertEquals(0, x86.isRented());
        apple.setRented(1);
        x86.setRented(1);
        Assertions.assertEquals(1, apple.isRented());
        Assertions.assertEquals(1, x86.isRented());
    }

    @Test
    public void testGetActualRentalPrice() {
        Assertions.assertEquals(1800, apple.getActualRentalPrice());
        apple.setCPUNumber(16);
        Assertions.assertEquals(3600, apple.getActualRentalPrice());
        AppleArch appleArch = new AppleArch(2, "2GB");
        Assertions.assertEquals(200, appleArch.getActualRentalPrice());
        appleArch.setCPUNumber(16);
        Assertions.assertEquals(1600, appleArch.getActualRentalPrice());
        x86 AMD = new x86(16, "32GB", "AMD");
        x86 other = new x86(new MongoUUID(UUID.fromString("9c97c890-dfcd-4cb9-a4bc-5e3c27eba80f")),16, "2GB", 0, "Other");
        Assertions.assertEquals(810, x86.getActualRentalPrice());
        x86.setCPUNumber(8);
        Assertions.assertEquals(405, x86.getActualRentalPrice());
        Assertions.assertEquals(540, AMD.getActualRentalPrice());
        AMD.setCPUNumber(8);
        Assertions.assertEquals(270, AMD.getActualRentalPrice());
        Assertions.assertEquals(80, other.getActualRentalPrice());
        other.setCPUNumber(8);
        Assertions.assertEquals(40, other.getActualRentalPrice());
    }
}

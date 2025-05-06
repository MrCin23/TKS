package vmachine.data;

import org.junit.jupiter.api.Test;
import pl.lodz.p.repo.MongoUUIDEnt;
import pl.lodz.p.repo.vm.data.AppleArchEnt;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AppleArchEntTests {

    @Test
    public void testAppleArchConstructor() {
        AppleArchEnt pom = new AppleArchEnt();
        AppleArchEnt appleArch = new AppleArchEnt(8, "16GB");
        assertNotNull(appleArch);
        assertEquals(8, appleArch.getCPUNumber());
        assertEquals("16GB", appleArch.getRamSize());
    }

    @Test
    public void testAppleArchGetActualRentalPrice() {
        AppleArchEnt appleArch = new AppleArchEnt(8, "16GB");
        float expectedPrice = 10 * 20 * 2 * 4.5f;
        assertEquals(expectedPrice, appleArch.getActualRentalPrice(), 0.01f);
    }

    @Test
    public void testAppleArchGetActualRentalPriceForSmallRAM() {
        AppleArchEnt appleArch = new AppleArchEnt(8, "4GB");
        float expectedPrice = 10 * 20 * 1 * 4;
        assertEquals(expectedPrice, appleArch.getActualRentalPrice(), 0.01f);
    }

    @Test
    public void testToString() {
        AppleArchEnt appleArch = new AppleArchEnt(new MongoUUIDEnt(UUID.fromString("00000000-0000-0000-0000-000000000000")), 4, "16GB", 0);
        assertEquals("AppleArch: 00000000-0000-0000-0000-000000000000 4 16GB 0 900.0", appleArch.toString());
    }
}

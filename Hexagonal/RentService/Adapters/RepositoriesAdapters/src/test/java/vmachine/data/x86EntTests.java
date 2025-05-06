package vmachine.data;

import org.junit.jupiter.api.Test;
import pl.lodz.p.repo.MongoUUIDEnt;
import pl.lodz.p.repo.vm.data.x86Ent;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class x86EntTests {

    @Test
    public void testX86Constructor() {
        x86Ent x86Ent = new x86Ent();
        x86Ent x86 = new x86Ent(8, "16GB", "Intel");
        assertNotNull(x86);
        assertEquals(8, x86.getCPUNumber());
        assertEquals("16GB", x86.getRamSize());
        assertEquals("Intel", x86.getManufacturer());
    }

    @Test
    public void testX86GetActualRentalPrice() {
        x86Ent x86 = new x86Ent(8, "16GB", "Intel");
        float expectedPrice = 270;
        assertEquals(expectedPrice, x86.getActualRentalPrice(), 0.01f);
    }

    @Test
    public void testX86GetActualRentalPriceWithAMD() {
        x86Ent x86 = new x86Ent(8, "16GB", "AMD");
        float expectedPrice = 180;
        assertEquals(expectedPrice, x86.getActualRentalPrice(), 0.01f);
    }

    @Test
    public void testX86GetActualRentalPriceForSmallRAM() {
        x86Ent x86 = new x86Ent(8, "4GB", "AMD");
        float expectedPrice = 20 * 1 * 2 * 2;
        assertEquals(expectedPrice, x86.getActualRentalPrice(), 0.01f);
    }

    @Test
    public void testToString() {
        x86Ent x86 = new x86Ent(new MongoUUIDEnt(UUID.fromString("00000000-0000-0000-0000-000000000000")), 8, "16GB", 0, "Intel");
        assertEquals("x86 architecture: 00000000-0000-0000-0000-000000000000 8 16GB 0 Intel 270.0", x86.toString());
    }
}


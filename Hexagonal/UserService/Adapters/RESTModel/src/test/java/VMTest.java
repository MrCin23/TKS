import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.rest.model.RESTAppleArch;
import pl.lodz.p.rest.model.RESTMongoUUID;
import pl.lodz.p.rest.model.RESTx86;

import java.util.UUID;

public class VMTest {
    @Test
    void toStringTests() {
        RESTAppleArch appleArch  = new RESTAppleArch(new RESTMongoUUID(UUID.fromString("11111111-1111-1111-1111-111111111111")), 24,"128GB", 0);
        RESTAppleArch appleArch2  = new RESTAppleArch(24,"128GB");
        RESTAppleArch appleArch3= new RESTAppleArch();
        RESTx86 x86  = new RESTx86(new RESTMongoUUID(UUID.fromString("11111111-1111-1111-1111-111111111111")), 24,"128GB", 0, "AMD");
        RESTx86 x862  = new RESTx86(24,"128GB", "AMD");
        RESTx86 x863 = new RESTx86();
        Assertions.assertEquals("AppleArch: RESTMongoUUID{uuid=11111111-1111-1111-1111-111111111111} 24 128GB 0 0.0", appleArch.toString());
        Assertions.assertTrue(appleArch2.toString().contains(" 24 128GB 0 0.0"));
        Assertions.assertTrue(appleArch3.toString().contains(" 0 null 0 0.0"));
        Assertions.assertEquals("x86 architecture: RESTMongoUUID{uuid=11111111-1111-1111-1111-111111111111} 24 128GB 0 AMD 0.0", x86.toString());
        Assertions.assertTrue(x862.toString().contains(" 24 128GB 0 AMD 0.0"));
        Assertions.assertTrue(x863.toString().contains(" 0 null 0 null 0.0"));
    }
}

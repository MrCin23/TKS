import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.rest.model.RESTAppleArch;
import pl.lodz.p.rest.model.RESTMongoUUID;
import pl.lodz.p.rest.model.RESTRent;
import pl.lodz.p.rest.model.user.RESTClient;

import java.time.LocalDateTime;
import java.util.UUID;

public class RentTest {
    @Test
    void testRent() {
        RESTClient client = new RESTClient();
        RESTAppleArch appleArch  = new RESTAppleArch(new RESTMongoUUID(UUID.fromString("11111111-1111-1111-1111-111111111111")), 24,"128GB", 0);
        RESTRent rent = new RESTRent(new RESTMongoUUID(UUID.fromString("11111111-1111-1111-1111-111111111111")), client, appleArch, LocalDateTime.of(2025,1,1,1,1), LocalDateTime.of(2025,1,1,1,1), 0);
        RESTRent rent3 = new RESTRent(new RESTMongoUUID(UUID.fromString("11111111-1111-1111-1111-111111111111")), client, appleArch, LocalDateTime.of(2025,1,1,1,1), null, 0);
        RESTRent rent2 = new RESTRent();
        Assertions.assertTrue(rent.toString().contains("Client{clientType=null, currentRents=0}, vMachine=AppleArch: RESTMongoUUID{uuid=11111111-1111-1111-1111-111111111111} 24 128GB 0 0.0, beginTime=2025-01-01T01:01, endTime=2025-01-01T01:01, rentCost=0.0}"));
        Assertions.assertTrue(rent3.toString().contains("Client{clientType=null, currentRents=0}, vMachine=AppleArch: RESTMongoUUID{uuid=11111111-1111-1111-1111-111111111111} 24 128GB 0 0.0, beginTime=2025-01-01T01:01, endTime=null, rentCost=0.0}"));
        Assertions.assertEquals("Rent{client=null, vMachine=null, beginTime=null, endTime=null, rentCost=0.0}", rent2.toString());
    }
}

package unit.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.core.domain.user.ClientType;
import pl.lodz.p.core.domain.user.Premium;
import pl.lodz.p.core.domain.user.Standard;

public class ClientTypeTests {

    @Test
    public void standardClientTypeTest() {
        ClientType clientType = new Standard();
        Assertions.assertEquals("Standard Standard", clientType.toString());
    }

    @Test
    public void premiumClientTypeTest() {
        ClientType clientType = new Premium();
        Assertions.assertEquals("Premium Premium", clientType.toString());
    }
}

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.user.rest.model.RESTMongoUUID;
import pl.lodz.p.user.rest.model.user.*;

import java.util.UUID;

public class UserTest {

    @Test
    void standardToStringTest() {
        RESTStandard standard = new RESTStandard();
        Assertions.assertEquals("Standard RESTStandard", standard.toString());
    }

    @Test
    void clientToStringTest() {
        RESTClient client = new RESTClient();
        Assertions.assertEquals("User{firstName='null', surname='null', username='null', emailAddress='null', role=null, active=false}::Client{clientType=null, currentRents=0}", client.toString());
    }

    @Test
    void premiumToStringTest() {
        RESTPremium premium = new RESTPremium();
        Assertions.assertEquals("Premium RESTPremium", premium.toString());
    }

    @Test
    void adminTest() {
        RESTAdmin admin = new RESTAdmin("admin", "admin", "admin", "admin@admin.com", "admin@admin.com");
        RESTAdmin admin2 = new RESTAdmin(new RESTMongoUUID(UUID.randomUUID()), "admin", "admin", "admin", "admin@admin.com", "admin@admin.com");
        Assertions.assertEquals("User{firstName='admin', surname='admin', username='admin', emailAddress='admin@admin.com', role=ADMIN, active=true}::Admin{}", admin.toString());
        Assertions.assertEquals("User{firstName='admin', surname='admin', username='admin', emailAddress='admin@admin.com', role=ADMIN, active=true}::Admin{}", admin2.toString());
    }

    @Test
    void managerTest() {
        RESTResourceManager resManager = new RESTResourceManager("resManager", "resManager", "resManager", "resManager@resManager.com", "resManager@resManager.com");
        RESTResourceManager resManager2 = new RESTResourceManager(new RESTMongoUUID(UUID.randomUUID()), "resManager", "resManager", "resManager", "resManager@resManager.com", "resManager@resManager.com");
        Assertions.assertEquals("User{firstName='resManager', surname='resManager', username='resManager', emailAddress='resManager@resManager.com', role=RESOURCE_MANAGER, active=true}::ResourceManager{}", resManager.toString());
        Assertions.assertEquals("User{firstName='resManager', surname='resManager', username='resManager', emailAddress='resManager@resManager.com', role=RESOURCE_MANAGER, active=true}::ResourceManager{}", resManager2.toString());
    }

    @Test
    void principalTest() {
        RESTAdmin admin = new RESTAdmin("admin", "admin", "admin", "admin@admin.com", "admin@admin.com");
        RESTUserPrincipal principal = new RESTUserPrincipal(admin);
        Assertions.assertEquals("[ADMIN]", principal.getAuthorities().toString());
        Assertions.assertEquals("admin", principal.getUsername());
        Assertions.assertEquals("admin@admin.com", principal.getPassword());
    }
}

package unit.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.user.core.domain.MongoUUID;
import pl.lodz.p.user.core.domain.user.*;

import java.util.UUID;

public class UserTests {

    @Test
    public void createClient() {
        User user = new Client(new MongoUUID(UUID.randomUUID()), "John", "JDoe", "a", "Doe", "jdoe@example.com", Role.CLIENT, true, new Standard(), 0);
        Assertions.assertEquals("User{firstName='John', surname='Doe', username='JDoe', emailAddress='jdoe@example.com', role=CLIENT, active=true}::Client{clientType=Standard Standard, currentRents=0}", user.toString());
    }

    @Test
    public void createAdmin() {
        User user = new Admin("John", "Doe", "JDoe", "a", "jdoe@example.com");
        Assertions.assertEquals("User{firstName='John', surname='Doe', username='JDoe', emailAddress='jdoe@example.com', role=ADMIN, active=true}::Admin{}", user.toString());
    }

    @Test
    public void createResourceManager() {
        User user = new ResourceManager(new MongoUUID(UUID.randomUUID()),"John", "Doe", "JDoe", "a", "jdoe@example.com");
        Assertions.assertEquals("User{firstName='John', surname='Doe', username='JDoe', emailAddress='jdoe@example.com', role=RESOURCE_MANAGER, active=true}::ResourceManager{}", user.toString());
    }

//    @Test
//    public void checkPassword() {
//        User user = new Client(new MongoUUID(UUID.randomUUID()), "John", "JDoe", "a", "Doe", "jdoe@example.com", Role.CLIENT, true, new Standard(), 0);
//        Assertions.assertTrue(user.checkPassword("a"));
//    }

}

package user.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.core.domain.user.Admin;
import pl.lodz.p.repo.MongoUUIDEnt;
import pl.lodz.p.repo.user.data.*;

import java.util.UUID;

public class UserDataTest {
    @Test
    public void constructorTests() {
        UserEnt user1 = new AdminEnt();
        UserEnt user2 = new ClientEnt();
        UserEnt user3 = new ResourceManagerEnt();

        UserEnt user4 = new AdminEnt("tom", "tom", "tom", "tom", "tom");
        UserEnt user5 = new ClientEnt("tom", "tom", "tom", "tom", "tom", new StandardEnt());
        UserEnt user6 = new ClientEnt("tom", "tom", "tom", "tom", "tom", new PremiumEnt());
        UserEnt user7 = new ResourceManagerEnt("tom", "tom", "tom", "tom", "tom");

        UserEnt user8 = new AdminEnt(new MongoUUIDEnt(UUID.randomUUID()), "tom","tom","tom","tom","tom");
        UserEnt user9 = new ClientEnt(new MongoUUIDEnt(UUID.randomUUID()), "tom","tom","tom","tom", "tom", RoleEnt.CLIENT,true, new StandardEnt(), 0);
        UserEnt user10 = new ResourceManagerEnt(new MongoUUIDEnt(UUID.randomUUID()),"tom", "tom", "tom", "tom", "tom");

        Assertions.assertEquals("User{firstName='null', surname='null', username='null', emailAddress='null', role=null, active=false}::Admin{}", user1.toString());
        Assertions.assertEquals("User{firstName='null', surname='null', username='null', emailAddress='null', role=null, active=false}::Client{clientType=null, currentRents=0}", user2.toString());
        Assertions.assertEquals("User{firstName='null', surname='null', username='null', emailAddress='null', role=null, active=false}::ResourceManager{}", user3.toString());
        Assertions.assertEquals("User{firstName='tom', surname='tom', username='tom', emailAddress='tom', role=ADMIN, active=true}::Admin{}", user4.toString());
        Assertions.assertEquals("User{firstName='tom', surname='tom', username='tom', emailAddress='tom', role=CLIENT, active=true}::Client{clientType=Standard StandardEnt, currentRents=0}", user5.toString());
        Assertions.assertEquals("User{firstName='tom', surname='tom', username='tom', emailAddress='tom', role=CLIENT, active=true}::Client{clientType=Premium PremiumEnt, currentRents=0}", user6.toString());
        Assertions.assertEquals("User{firstName='tom', surname='tom', username='tom', emailAddress='tom', role=RESOURCE_MANAGER, active=true}::ResourceManager{}", user7.toString());
        Assertions.assertEquals("User{firstName='tom', surname='tom', username='tom', emailAddress='tom', role=ADMIN, active=true}::Admin{}", user8.toString());
        Assertions.assertEquals("User{firstName='tom', surname='tom', username='tom', emailAddress='tom', role=CLIENT, active=true}::Client{clientType=Standard StandardEnt, currentRents=0}", user9.toString());
        Assertions.assertEquals("User{firstName='tom', surname='tom', username='tom', emailAddress='tom', role=RESOURCE_MANAGER, active=true}::ResourceManager{}", user10.toString());
    }

    @Test
    public void principalTest() {
        UserEnt user9 = new ClientEnt(new MongoUUIDEnt(UUID.fromString("11111111-1111-1111-1111-111111111111")), "tom","tom","tom","tom", "tom", RoleEnt.CLIENT,true, new StandardEnt(), 0);
        UserPrincipalEnt userprincipal = new UserPrincipalEnt(user9);
        Assertions.assertEquals("[CLIENT]", userprincipal.getAuthorities().toString());
        Assertions.assertEquals("tom", userprincipal.getPassword());
        Assertions.assertEquals("tom", userprincipal.getUsername());
    }
}

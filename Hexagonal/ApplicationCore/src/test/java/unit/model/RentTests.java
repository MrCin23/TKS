package unit.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.model.AppleArch;
import pl.lodz.p.model.MongoUUID;
import pl.lodz.p.model.Rent;
import pl.lodz.p.model.VMachine;
import pl.lodz.p.model.user.Client;
import pl.lodz.p.model.user.Role;
import pl.lodz.p.model.user.Standard;
import pl.lodz.p.model.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class RentTests {
    Client client;
    VMachine vMachine;
    Rent rent;

    @BeforeEach
    public void setUp() {
        client = new Client("John", "Doe",  "JDoe","a", "jdoe@example.com", new Standard());
        vMachine = new AppleArch(new MongoUUID(UUID.fromString("577d1a4e-5577-4265-badb-5439cf4d8fc8")), 4, "8GB", 0);
    }

    @Test
    public void testCreateRent() {
        Rent rent1 = new Rent(client, vMachine, LocalDateTime.of(2015, 7, 12, 21, 37));
        Assertions.assertInstanceOf(Rent.class, rent1);
        rent1.endRent(LocalDateTime.of(2015, 7, 12, 21, 37));
        Rent rent2 = new Rent(new MongoUUID(UUID.randomUUID()), client, vMachine, LocalDateTime.of(2015, 7, 12, 21, 37), LocalDateTime.of(2015, 7, 13, 21, 37), 0);
        Assertions.assertInstanceOf(Rent.class, rent2);
        Assertions.assertEquals("Rent{client=User{firstName='John', surname='Doe', username='JDoe', emailAddress='jdoe@example.com', role=CLIENT, active=true}::Client{clientType=Standard Standard, currentRents=0}, vMachine=AppleArch: 577d1a4e-5577-4265-badb-5439cf4d8fc8 4 8GB 0 600.0, beginTime=2015-07-12T21:37, endTime=2015-07-13T21:37, rentCost=1200.0}", rent2.toString());
        Rent rent3 = new Rent();
        Assertions.assertInstanceOf(Rent.class, rent3);
        Rent rent4 = new Rent(new MongoUUID(UUID.randomUUID()), client, vMachine, LocalDateTime.of(2015, 7, 12, 21, 37), null, 0);
        Assertions.assertInstanceOf(Rent.class, rent4);
    }

    @Test
    public void testEndRent() {
        ///Positive tests
        Rent rent = new Rent(client, vMachine, LocalDateTime.of(2015, 7, 12, 21, 37));
        Assertions.assertNull(rent.getEndTime());
        rent.endRent(LocalDateTime.of(2015, 7, 14, 21, 37));
        Assertions.assertEquals(LocalDateTime.of(2015, 7, 14, 21, 37), rent.getEndTime());
        Assertions.assertEquals(1800, rent.getRentCost());
        Rent endedRent = new Rent(new MongoUUID(UUID.randomUUID()), client, vMachine, LocalDateTime.of(2015, 7, 12, 21, 37), LocalDateTime.of(2015, 7, 14, 21, 37), 0);
        Assertions.assertEquals(LocalDateTime.of(2015, 7, 14, 21, 37), endedRent.getEndTime());
        Assertions.assertEquals(1800, endedRent.getRentCost());
        Rent nullableEndTimeRent = new Rent(new MongoUUID(UUID.randomUUID()), client, vMachine, LocalDateTime.of(2015, 7, 12, 21, 37), null, 0);
        Assertions.assertInstanceOf(Rent.class, nullableEndTimeRent);
        Assertions.assertNull(nullableEndTimeRent.getEndTime());
//        nullableEndTimeRent.endRent(null);
//        Assertions.assertTrue(nullableEndTimeRent.getRentCost() > 0);
    }

    @Test
    public void testRentRentedVMachine() {
        Rent rent = new Rent(client, vMachine, LocalDateTime.of(2015, 7, 12, 21, 37));
        Assertions.assertInstanceOf(Rent.class, rent);
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            new Rent(new MongoUUID(UUID.randomUUID()), client, vMachine, LocalDateTime.of(2015, 7, 12, 21, 37), LocalDateTime.of(2015, 7, 13, 21, 37), 0);
        });
        Assertions.assertEquals("this VMachine is already rented", thrown.getMessage());
    }
}

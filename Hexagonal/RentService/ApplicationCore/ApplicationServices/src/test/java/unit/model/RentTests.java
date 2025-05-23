package unit.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.core.domain.AppleArch;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.Rent;
import pl.lodz.p.core.domain.VMachine;
import pl.lodz.p.core.domain.user.Client;
import pl.lodz.p.core.domain.user.Standard;

import java.time.LocalDateTime;
import java.util.UUID;

public class RentTests {
    Client client;
    VMachine vMachine;
    Rent rent;

    @BeforeEach
    public void setUp() {
        client = new Client(new MongoUUID(UUID.randomUUID()), "JDoe", new Standard(), 0, true);
        vMachine = new AppleArch(new MongoUUID(UUID.fromString("577d1a4e-5577-4265-badb-5439cf4d8fc8")), 4, "8GB", 0);
    }

    @Test
    public void testCreateRent() {
        Rent rent1 = new Rent(client, vMachine, LocalDateTime.of(2015, 7, 12, 21, 37));
        rent1.endRent(LocalDateTime.of(2015, 7, 12, 21, 37));
        Rent rent2 = new Rent(new MongoUUID(UUID.randomUUID()), client, vMachine, LocalDateTime.of(2015, 7, 12, 21, 37), LocalDateTime.of(2015, 7, 13, 21, 37), 0);
        Assertions.assertTrue(rent2.toString().contains("Client{clientType=Standard Standard, currentRents=0}, vMachine=AppleArch: 577d1a4e-5577-4265-badb-5439cf4d8fc8 4 8GB 0 600.0, beginTime=2015-07-12T21:37, endTime=2015-07-13T21:37, rentCost=1200.0}"));
        Rent rent3 = new Rent();
        Rent rent4 = new Rent(new MongoUUID(UUID.randomUUID()), client, vMachine, LocalDateTime.of(2015, 7, 12, 21, 37), null, 0);
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
        Assertions.assertNull(nullableEndTimeRent.getEndTime());
//        nullableEndTimeRent.endRent(null);
//        Assertions.assertTrue(nullableEndTimeRent.getRentCost() > 0);
    }

    @Test
    public void testRentRentedVMachine() {
        Rent rent = new Rent(client, vMachine, LocalDateTime.of(2015, 7, 12, 21, 37));
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            new Rent(new MongoUUID(UUID.randomUUID()), client, vMachine, LocalDateTime.of(2015, 7, 12, 21, 37), LocalDateTime.of(2015, 7, 13, 21, 37), 0);
        });
        Assertions.assertEquals("this VMachine is already rented", thrown.getMessage());
    }
}

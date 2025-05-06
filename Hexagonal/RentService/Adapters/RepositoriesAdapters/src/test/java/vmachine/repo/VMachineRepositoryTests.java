package vmachine.repo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;
import pl.lodz.p.repo.vm.data.AppleArchEnt;
import pl.lodz.p.repo.vm.data.VMachineEnt;
import pl.lodz.p.repo.MongoUUIDEnt;
import pl.lodz.p.repo.vm.repo.VMachineRepository;

import java.util.*;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class VMachineRepositoryTests {

    private final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:7.0.2")).withExposedPorts(27017);;
    private VMachineRepository vMachineRepo;
    private MongoUUIDEnt testUUID;
    VMachineEnt vMachine;

    @BeforeEach
    public void setUp() {
        mongoDBContainer.start();
        String connectionString = mongoDBContainer.getReplicaSetUrl();
        vMachineRepo = new VMachineRepository();
        vMachineRepo.setConnectionString(new ConnectionString(connectionString));
        testUUID = new MongoUUIDEnt();
        testUUID.setUuid(UUID.randomUUID());
        vMachine = new AppleArchEnt(testUUID, 4, "16GB", 0);
    }

    @AfterEach
    public void tearDown() {
        mongoDBContainer.stop();
    }

    @Test
    public void testAddVMachine() {
        vMachineRepo.add(vMachine);

        VMachineEnt fetchedVMachine = vMachineRepo.getVMachineByID(testUUID);
        Assertions.assertNotNull(fetchedVMachine);
        Assertions.assertEquals(testUUID.getUuid(), fetchedVMachine.getEntityId().getUuid());
    }

    @Test
    public void testRemoveVMachine() {
        vMachineRepo.add(vMachine);
        vMachineRepo.remove(vMachine);

        VMachineEnt fetchedVMachine = vMachineRepo.getVMachineByID(testUUID);
        Assertions.assertNull(fetchedVMachine);
    }

    @Test
    public void testGetVMachines() {
        vMachineRepo.add(vMachine);

        List<VMachineEnt> vmachines = vMachineRepo.getVMachines();
        Assertions.assertFalse(vmachines.isEmpty());
        Assertions.assertTrue(vmachines.stream().anyMatch(vm -> vm.getEntityId().equals(testUUID)));
    }

    @Test
    public void testGetVMachineByID() {
        vMachineRepo.add(vMachine);

        VMachineEnt fetchedVMachine = vMachineRepo.getVMachineByID(testUUID);
        Assertions.assertNotNull(fetchedVMachine);
        Assertions.assertEquals(testUUID.getUuid(), fetchedVMachine.getEntityId().getUuid());
    }

    @Test
    public void testUpdate() {
        vMachineRepo.add(vMachine);

        VMachineEnt existingVMachine = vMachineRepo.getVMachineByID(testUUID);
        Assertions.assertNotNull(existingVMachine, "VMachine not found before update.");

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("ramSize", "32GB");

        vMachineRepo.update(testUUID, fieldsToUpdate);

        VMachineEnt updatedVMachine = vMachineRepo.getVMachineByID(testUUID);
        Assertions.assertEquals("32GB", updatedVMachine.getRamSize());
    }

    @Test
    public void testUpdateIsRented() {
        vMachineRepo.add(vMachine);

        VMachineEnt existingVMachine = vMachineRepo.getVMachineByID(testUUID);
        Assertions.assertNotNull(existingVMachine, "VMachine not found before update.");

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("isRented", 1);

        vMachineRepo.update(testUUID, fieldsToUpdate);

        VMachineEnt updatedVMachine = vMachineRepo.getVMachineByID(testUUID);
        Assertions.assertEquals(1, updatedVMachine.isRented());
    }

    @Test
    public void testUpdateIsNotRented() {
        vMachine.setIsRented(1);
        vMachineRepo.add(vMachine);

        VMachineEnt existingVMachine = vMachineRepo.getVMachineByID(testUUID);
        Assertions.assertNotNull(existingVMachine, "VMachine not found before update.");

        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("isRented", -1);

        vMachineRepo.update(testUUID, fieldsToUpdate);

        VMachineEnt updatedVMachine = vMachineRepo.getVMachineByID(testUUID);
        Assertions.assertEquals(0, updatedVMachine.isRented());
    }

    @Test
    public void testUpdateSingleField() {
        vMachineRepo.add(vMachine);
        VMachineEnt existingVMachine = vMachineRepo.getVMachineByID(testUUID);
        Assertions.assertNotNull(existingVMachine, "VMachine not found before update.");
        vMachineRepo.update(testUUID, "ramSize", "64GB");

        VMachineEnt updatedVMachine = vMachineRepo.getVMachineByID(testUUID);
        Assertions.assertEquals("64GB", updatedVMachine.getRamSize());
    }

    @Test
    public void testUpdateSingleFieldIsRented() {
        vMachineRepo.add(vMachine);
        VMachineEnt existingVMachine = vMachineRepo.getVMachineByID(testUUID);
        Assertions.assertNotNull(existingVMachine, "VMachine not found before update.");
        vMachineRepo.update(testUUID, "isRented", 1);

        VMachineEnt updatedVMachine = vMachineRepo.getVMachineByID(testUUID);
        Assertions.assertEquals(1, updatedVMachine.isRented());
    }

    @Test
    public void testUpdateSingleFieldIsNotRented() {
        vMachine.setIsRented(1);
        vMachineRepo.add(vMachine);
        VMachineEnt existingVMachine = vMachineRepo.getVMachineByID(testUUID);
        Assertions.assertNotNull(existingVMachine, "VMachine not found before update.");
        vMachineRepo.update(testUUID, "isRented", 0);

        VMachineEnt updatedVMachine = vMachineRepo.getVMachineByID(testUUID);
        Assertions.assertEquals(0, updatedVMachine.isRented());
    }

    @Test
    public void testSize() {
        vMachineRepo.add(vMachine);
        Long size = vMachineRepo.size();
        Assertions.assertEquals(1, size);
    }


}


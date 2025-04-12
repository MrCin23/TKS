package user.repo;

import com.mongodb.ConnectionString;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;
import pl.lodz.p.repo.MongoUUIDEnt;
import pl.lodz.p.repo.user.data.*;
import pl.lodz.p.repo.user.repo.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UserRepoTest {

    private final MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:7.0.2")).withExposedPorts(27017);

    private UserRepository userRepo;
    private MongoUUIDEnt testUUID;
    private ClientEnt user;

    @BeforeEach
    public void setUp() {
        mongoDBContainer.start();
        String connectionString = mongoDBContainer.getReplicaSetUrl();

        userRepo = new UserRepository();
        userRepo.setConnectionString(new ConnectionString(connectionString));

        testUUID = new MongoUUIDEnt();
        testUUID.setUuid(UUID.randomUUID());

        user = new ClientEnt(
                testUUID,
                "John",
                "JDoe",
                "password",
                "Doe",
                "jdoe@example.com",
                RoleEnt.CLIENT,
                true,
                new StandardEnt(),
                0
        );
    }

    @AfterEach
    public void tearDown() {
        mongoDBContainer.stop();
    }

    @Test
    public void testAddUser() {
        userRepo.add(user);

        UserEnt fetched = userRepo.getUserByID(testUUID);
        assertNotNull(fetched);
        assertEquals(testUUID.getUuid(), fetched.getEntityId().getUuid());
    }

    @Test
    public void testDuplicateUsernameThrows() {
        userRepo.add(user);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> userRepo.add(user));
        Assertions.assertTrue(thrown.getMessage().contains("Write operation error on server mongodb1:27017. Write error: WriteError{code=11000, message='E11000 duplicate key error collection: vmrental.users"));
    }

    @Test
    public void testRemoveUser() {
        userRepo.add(user);
        userRepo.remove(user);

        assertNull(userRepo.getUserByID(testUUID));
    }

    @Test
    public void testSize() {
        userRepo.add(user);
        assertEquals(1, userRepo.size());
    }

    @Test
    public void testGetUsers() {
        userRepo.add(user);
        List<UserEnt> users = userRepo.getUsers();
        assertFalse(users.isEmpty());
    }

    @Test
    public void testGetUserByID() {
        userRepo.add(user);
        UserEnt fetched = userRepo.getUserByID(testUUID);
        assertNotNull(fetched);
        assertEquals("JDoe", fetched.getUsername());
    }

    @Test
    public void testGetUserByUsername() {
        userRepo.add(user);
        UserEnt fetched = userRepo.getUserByUsername("JDoe");
        assertNotNull(fetched);
        assertEquals("jdoe@example.com", fetched.getEmailAddress());
    }

    @Test
    public void testGetUsersByUsernameRegex() {
        userRepo.add(user);
        List<UserEnt> matches = userRepo.getUsersByUsername("Doe");
        assertEquals(1, matches.size());
    }

    @Test
    public void testUpdateSingleField() {
        userRepo.add(user);
        userRepo.update(testUUID, "firstName", "Johnny");

        UserEnt updated = userRepo.getUserByID(testUUID);
        assertEquals("Johnny", updated.getFirstName());
    }

    @Test
    public void testUpdateMultipleFields() {
        userRepo.add(user);
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", "Jane");
        updates.put("surname", "Smith");

        userRepo.update(testUUID, updates);
        UserEnt updated = userRepo.getUserByID(testUUID);
        assertEquals("Jane", updated.getFirstName());
        assertEquals("Smith", updated.getSurname());
    }

    @Test
    public void testUpdateCurrentRentsIncrement() {
        userRepo.add(user);
        userRepo.update(testUUID, "currentRents", 1);

        ClientEnt updated = (ClientEnt) userRepo.getUserByID(testUUID);
        assertEquals(1, updated.getCurrentRents());
    }

    @Test
    public void testUpdateCurrentRentsDecrement() {
        user.setCurrentRents(1);
        userRepo.add(user);
        userRepo.update(testUUID, "currentRents", -1);

        ClientEnt updated = (ClientEnt)userRepo.getUserByID(testUUID);
        assertEquals(0, updated.getCurrentRents());
    }
}

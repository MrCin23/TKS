package user.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.user.*;
import pl.lodz.p.repo.MongoUUIDEnt;
import pl.lodz.p.repo.user.adapter.UserAdapter;
import pl.lodz.p.repo.user.data.*;
import pl.lodz.p.repo.user.repo.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserAdapterTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAdapter adapter;

    private UUID uuid;
    private MongoUUID domainUUID;
    private MongoUUIDEnt uuidEnt;

    private Client domainClient;
    private ClientEnt clientEnt;

    @BeforeEach
    void setup() {
        uuid = UUID.randomUUID();

        domainUUID = new MongoUUID(uuid);
        uuidEnt = new MongoUUIDEnt();
        uuidEnt.setUuid(uuid);

        domainClient = new Client(domainUUID, "John", "jdoe", "pass123", "Doe", "jdoe@email.com", Role.CLIENT, true, new Standard(), 0);
        clientEnt = new ClientEnt(uuidEnt, "John", "jdoe", "pass123", "Doe", "jdoe@email.com", RoleEnt.CLIENT, true, new StandardEnt(), 0);
    }

    @Test
    void shouldAddUser() {
        adapter.add(domainClient);
        verify(userRepository).add(any(ClientEnt.class));
    }

    @Test
    void shouldGetSize() {
        when(userRepository.size()).thenReturn(5L);
        assertEquals(5L, adapter.size());
    }

    @Test
    void shouldGetUsers() {
        when(userRepository.getUsers()).thenReturn(List.of(clientEnt));
        List<User> result = adapter.getUsers();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void shouldGetUserByID() {
        when(userRepository.getUserByID(any())).thenReturn(clientEnt);

        User result = adapter.getUserByID(domainUUID);

        assertTrue(result instanceof Client);
        assertEquals("jdoe", result.getUsername());
    }

    @Test
    void shouldGetUserByUsername() {
        when(userRepository.getUserByUsername("jdoe")).thenReturn(clientEnt);
        User result = adapter.getUserByUsername("jdoe");

        assertNotNull(result);
        assertEquals("Doe", result.getSurname());
    }

    @Test
    void shouldGetUsersByUsername() {
        when(userRepository.getUsersByUsername("jdoe")).thenReturn(List.of(clientEnt));
        List<User> result = adapter.getUsersByUsername("jdoe");

        assertEquals(1, result.size());
        assertEquals("jdoe", result.get(0).getUsername());
    }

    @Test
    void shouldRemoveUser() {
        adapter.remove(domainClient);
        verify(userRepository).remove(any(ClientEnt.class));
    }

    @Test
    void shouldUpdateFieldMap() {
        Map<String, Object> updates = Map.of("emailAddress", "new@email.com");
        adapter.update(domainUUID, updates);
        verify(userRepository).update(any(MongoUUIDEnt.class), eq(updates));
    }

    @Test
    void shouldUpdateSingleField() {
        adapter.update(domainUUID, "active", false);
        verify(userRepository).update(any(MongoUUIDEnt.class), eq("active"), eq(false));
    }

//    @Test
//    void shouldConvertRole() {
//        assertEquals(RoleEnt.ADMIN, adapter.convert(Role.ADMIN));
//        assertEquals(Role.CLIENT, adapter.convert(RoleEnt.CLIENT));
//    }
//
//    @Test
//    void shouldConvertUUIDToEntityAndBack() {
//        MongoUUIDEnt ent = adapter.convert(domainUUID);
//        MongoUUID back = adapter.convert(ent);
//
//        assertEquals(domainUUID.getUuid(), ent.getUuid());
//        assertEquals(domainUUID.getUuid(), back.getUuid());
//    }

    @Test
    void shouldReturnNullForNullEntity() {
        assertNull(adapter.getUserByUsername("nonexistent"));
    }

//    @Test
//    void shouldThrowOnUnsupportedDomainType() {
//        User invalid = mock(User.class);
//        when(invalid.getClass()).thenReturn((Class) Object.class);
//
//        assertThrows(RuntimeException.class, () -> {
//            var method = UserAdapter.class.getDeclaredMethod("convert", User.class);
//            method.setAccessible(true);
//            method.invoke(adapter, invalid);
//        });
//    }
//
//    @Test
//    void shouldThrowOnUnsupportedEntityType() {
//        UserEnt invalid = mock(UserEnt.class);
//        when(invalid.getClass()).thenReturn((Class) Object.class);
//
//        assertThrows(RuntimeException.class, () -> {
//            var method = UserAdapter.class.getDeclaredMethod("convert", UserEnt.class);
//            method.setAccessible(true);
//            method.invoke(adapter, invalid);
//        });
//    }
}

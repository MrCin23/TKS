package unit.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.exception.DeactivatedUserException;
import pl.lodz.p.core.domain.exception.WrongPasswordException;
import pl.lodz.p.core.domain.user.Client;
import pl.lodz.p.core.domain.user.Role;
import pl.lodz.p.core.domain.user.Standard;
import pl.lodz.p.core.domain.user.User;
import pl.lodz.p.core.services.security.JwtTokenProvider;
import pl.lodz.p.core.services.service.UserService;
import pl.lodz.p.infrastructure.user.UAdd;
import pl.lodz.p.infrastructure.user.UGet;
import pl.lodz.p.infrastructure.user.URemove;
import pl.lodz.p.infrastructure.user.UUpdate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UGet uGet;

    @Mock
    private UAdd uAdd;

    @Mock
    private URemove uRemove;

    @Mock
    private UUpdate uUpdate;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private UserService userService;

    private User user;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
        user = new Client(new MongoUUID(uuid), "John", "JDoe", BCrypt.hashpw("a", BCrypt.gensalt()), "Doe", "jdoe@example.com", Role.CLIENT, true, new Standard(), 0);
    }

    @Test
    void createUser_Success() {
        when(uGet.getUserByID(user.getEntityId())).thenReturn(null);
        userService.createUser(user);
        verify(uAdd, times(1)).add(any(User.class));
    }

    @Test
    void createUser_AlreadyExists() {
        when(uGet.getUserByID(user.getEntityId())).thenReturn(user);
        assertThrows(RuntimeException.class, () -> userService.createUser(user));
    }

    @Test
    void getUser_Success() {
        when(uGet.getUserByID(new MongoUUID(uuid))).thenReturn(user);
        User result = userService.getUser(uuid);
        assertEquals(user, result);
    }

    @Test
    void getUser_NotFound() {
        when(uGet.getUserByID(new MongoUUID(uuid))).thenReturn(null);
        assertThrows(RuntimeException.class, () -> userService.getUser(uuid));
    }

    @Test
    void getUserByUsername_Success() {
        when(uGet.getUserByUsername("JDoe")).thenReturn(user);
        when(uGet.getUsersByUsername("JDoe")).thenReturn(Collections.singletonList(user));
        User result = userService.getUserByUsername("JDoe");
        assertEquals(user, result);
    }

    @Test
    void getUserByUsername_Null() {
        when(uGet.getUsersByUsername("testUser")).thenReturn(null);
        assertThrows(RuntimeException.class, () -> userService.getUserByUsername("testUser"));
    }

    @Test
    void getUserByUsername_EmptyList() {
        when(uGet.getUsersByUsername("testUser")).thenReturn(Collections.emptyList());
        assertThrows(RuntimeException.class, () -> userService.getUserByUsername("testUser"));
    }

    @Test
    void getAllUsers_Success() {
        when(uGet.getUsers()).thenReturn(Collections.singletonList(user));
        List<User> result = userService.getAllUsers();
        assertEquals(user, result.getFirst());
    }

    @Test
    void getAllUsers_Null() {
        when(uGet.getUsers()).thenReturn(null);
        Exception exception = assertThrows(RuntimeException.class, () -> userService.getAllUsers());
        assertEquals("No users found", exception.getMessage());
    }

    @Test
    void getAllUsers_EmptyList() {
        when(uGet.getUsers()).thenReturn(Collections.emptyList());
        Exception exception = assertThrows(RuntimeException.class, () -> userService.getAllUsers());
        assertEquals("No users found", exception.getMessage());
    }


    @Test
    void authenticateUser_Success() {
        when(uGet.getUserByUsername("JDoe")).thenReturn(user);
        when(tokenProvider.generateToken(anyString(), any())).thenReturn("token");
        String token = userService.getUserByUsername("JDoe", "a");
        assertNotNull(token);
    }

    @Test
    void authenticateUser_NotFound() {
        when(uGet.getUserByUsername("JDoe")).thenReturn(null);
        Exception exception = assertThrows(RuntimeException.class, () -> userService.getUserByUsername("JDoe", "wrongPassword"));
        assertEquals("User with username JDoe does not exist", exception.getMessage());
    }

    @Test
    void authenticateUser_WrongPassword() {
        when(uGet.getUserByUsername("JDoe")).thenReturn(user);
        assertThrows(WrongPasswordException.class, () -> userService.getUserByUsername("JDoe", "wrongPassword"));
    }

    @Test
    void authenticateUser_Deactivated() {
        user.setActive(false);
        when(uGet.getUserByUsername("testUser")).thenReturn(user);
        assertThrows(DeactivatedUserException.class, () -> userService.getUserByUsername("testUser", "password"));
    }

    @Test
    void updateUser_Success() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("email", "new@example.com");
        when(uGet.getUserByID(new MongoUUID(uuid))).thenReturn(user);
        userService.updateUser(uuid, fields);
        verify(uUpdate, times(1)).update(new MongoUUID(uuid), fields);
    }

    @Test
    void updateUser_NotFound() {
        when(uGet.getUserByID(new MongoUUID(uuid))).thenReturn(null);
        assertThrows(RuntimeException.class, () -> userService.updateUser(uuid, new HashMap<>()));
    }

    @Test
    void activateUser_Success() {
        when(uGet.getUserByID(new MongoUUID(uuid))).thenReturn(user);
        userService.activateUser(uuid);
        verify(uUpdate, times(1)).update(new MongoUUID(uuid), "active", true);
    }

    @Test
    void activateUser_NotFound() {
        when(uGet.getUserByID(new MongoUUID(uuid))).thenReturn(null);
        assertThrows(RuntimeException.class, () -> userService.activateUser(uuid));
    }

    @Test
    void deactivateUser_Success() {
        when(uGet.getUserByID(new MongoUUID(uuid))).thenReturn(user);
        userService.deactivateUser(uuid);
        verify(uUpdate, times(1)).update(new MongoUUID(uuid), "active", false);
    }

    @Test
    void deactivateUser_NotFound() {
        when(uGet.getUserByID(new MongoUUID(uuid))).thenReturn(null);
        assertThrows(RuntimeException.class, () -> userService.deactivateUser(uuid));
    }

    @Test
    void checkToken_Blacklisted() {
        userService.invalidateToken("token123");
        assertTrue(userService.checkToken("token123"));
    }

    @Test
    void checkToken_NotBlacklisted() {
        assertFalse(userService.checkToken("token123"));
    }

    @Test
    void changePassword_Success() {
        when(uGet.getUserByUsername("testUser")).thenReturn(user);
        userService.changePassword("testUser", "newPassword");
        verify(uUpdate, times(1)).update(eq(user.getEntityId()), eq("password"), anyString());
    }

    @Test
    void loadUserByUsername_Success() {
        when(uGet.getUserByUsername("testUser")).thenReturn(user);
        assertEquals(user.getUsername(), userService.loadUserByUsername("testUser").getUsername());
    }
    @Test
    void loadUserByUsername_Null() {
        when(uGet.getUserByUsername("JDoe")).thenReturn(null);
        Exception exception = assertThrows(RuntimeException.class, () -> userService.loadUserByUsername("JDoe"));
        assertEquals("User with login JDoe not found", exception.getMessage());
    }

    @Test
    void getUsersByUsername_Success() {
        when(uGet.getUsersByUsername("JDoe")).thenReturn(Collections.singletonList(user));
        List<User> result = userService.getUsersByUsername("JDoe");
        assertEquals(1, result.size());
    }

    @Test
    void getUsersByUsername_Null() {
        when(uGet.getUsersByUsername("JDoe")).thenReturn(null);
        Exception exception = assertThrows(RuntimeException.class, () -> userService.getUsersByUsername("JDoe"));
        assertEquals("No users with username JDoe found", exception.getMessage());
    }

    @Test
    void getUsersByUsername_EmptyList() {
        when(uGet.getUsersByUsername("JDoe")).thenReturn(Collections.emptyList());
        Exception exception = assertThrows(RuntimeException.class, () -> userService.getUsersByUsername("JDoe"));
        assertEquals("No users with username JDoe found", exception.getMessage());
    }

    @Test
    void size() {
        when(uGet.size()).thenReturn(1L);
        assertEquals(1, userService.size());
    }
}


package adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.core.domain.user.*;
import pl.lodz.p.core.services.service.UserService;
import pl.lodz.p.rest.adapter.RESTUserServiceAdapter;
import pl.lodz.p.rest.model.dto.LoginDTO;
import pl.lodz.p.rest.model.user.*;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.rest.model.RESTMongoUUID;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RESTUserServiceAdapterTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private RESTUserServiceAdapter restUserServiceAdapter;

    private User user;
    private RESTUser restUser;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
        RESTClient restClient = new RESTClient(new RESTMongoUUID(uuid), "jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", RESTRole.CLIENT, true, new RESTStandard(), 0);
        Client client = new Client(new MongoUUID(uuid), "jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", Role.CLIENT, true, new Standard(), 0);
        restUser = restClient;
        user = client;
    }

    @Test
    void testCreateUser() {
        when(userService.createUser(any(User.class))).thenReturn(user);

        RESTUser result = restUserServiceAdapter.createUser(restUser);

        assertEquals(restUser.getEntityId(), result.getEntityId());
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void testCreateResourceManager() {
        RESTUser resourceManager = new RESTResourceManager(new RESTMongoUUID(uuid), "jan", "janowski", "janek", "janeczek", "janeczek@janko.jan");
        when(userService.createUser(any(User.class))).thenReturn(user);

        RESTUser result = restUserServiceAdapter.createUser(resourceManager);

        assertEquals(resourceManager.getEntityId(), result.getEntityId());
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void testCreateAdmin() {
        RESTUser admin = new RESTAdmin(new RESTMongoUUID(uuid), "jan", "janowski", "janek", "janeczek", "janeczek@janko.jan");
        when(userService.createUser(any(User.class))).thenReturn(user);

        RESTUser result = restUserServiceAdapter.createUser(admin);

        assertEquals(admin.getEntityId(), result.getEntityId());
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));

        List<RESTUser> result = restUserServiceAdapter.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(restUser.getEntityId(), result.get(0).getEntityId());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUser() {
        when(userService.getUser(uuid)).thenReturn(user);

        RESTUser result = restUserServiceAdapter.getUser(uuid);

        assertEquals(restUser.getEntityId(), result.getEntityId());
        verify(userService, times(1)).getUser(uuid);
    }

    @Test
    void testUpdateUser() {
        UUID userId = UUID.randomUUID();
        Map<String, Object> fieldsToUpdate = Map.of("firstName", "Janek");

        doNothing().when(userService).updateUser(userId, fieldsToUpdate);

        restUserServiceAdapter.updateUser(userId, fieldsToUpdate);

        verify(userService, times(1)).updateUser(userId, fieldsToUpdate);
    }

    @Test
    void testActivateUser() {
        UUID userId = UUID.randomUUID();

        doNothing().when(userService).activateUser(userId);

        restUserServiceAdapter.activateUser(userId);

        verify(userService, times(1)).activateUser(userId);
    }

    @Test
    void testDeactivateUser() {
        UUID userId = UUID.randomUUID();

        doNothing().when(userService).deactivateUser(userId);

        restUserServiceAdapter.deactivateUser(userId);

        verify(userService, times(1)).deactivateUser(userId);
    }

    @Test
    void testGetUserByUsername() {
        String username = "janek";
        String password = "janeczek";

        when(userService.getUserByUsername(username, password)).thenReturn("Token123");

        String result = restUserServiceAdapter.getUserByUsername(new LoginDTO(username, password));

        assertEquals("Token123", result);
        verify(userService, times(1)).getUserByUsername(username, password);
    }

    @Test
    void testLoadUserByUsername() {
        String username = "janek";

        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername(username)).thenReturn(userDetails);

        UserDetails result = restUserServiceAdapter.loadUserByUsername(username);

        assertEquals(userDetails, result);
        verify(userService, times(1)).loadUserByUsername(username);
    }

    @Test
    void testCheckToken() {
        String token = "Token123";

        when(userService.checkToken(token)).thenReturn(true);

        boolean result = restUserServiceAdapter.checkToken(token);

        assertTrue(result);
        verify(userService, times(1)).checkToken(token);
    }

    @Test
    void testInvalidateToken() {
        String token = "Token123";

        doNothing().when(userService).invalidateToken(token);

        restUserServiceAdapter.invalidateToken(token);

        verify(userService, times(1)).invalidateToken(token);
    }

    @Test
    void testChangePassword() {
        String username = "janek";
        String newPassword = "newPassword123";

        doNothing().when(userService).changePassword(username, newPassword);

        restUserServiceAdapter.changePassword(username, newPassword);

        verify(userService, times(1)).changePassword(username, newPassword);
    }
}

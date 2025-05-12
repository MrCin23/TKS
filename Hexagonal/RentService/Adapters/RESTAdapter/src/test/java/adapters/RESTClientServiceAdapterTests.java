package adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.p.core.domain.user.*;
import pl.lodz.p.core.services.service.ClientService;
import pl.lodz.p.rest.adapter.RESTClientServiceAdapter;
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
class RESTClientServiceAdapterTests {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private RESTClientServiceAdapter restClientServiceAdapter;

    private Client user;
    private RESTClient restClient;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
        restClient = new RESTClient(new RESTMongoUUID(uuid), "janek", true, new RESTStandard(), 0);
        user = new Client(new MongoUUID(uuid),"janek", new Standard(), 0, true);
    }

    @Test
    void testCreateClient() {
        when(clientService.createClient(any(Client.class))).thenReturn(user);

        RESTClient result = restClientServiceAdapter.createClient(restClient);

        assertEquals(restClient.getEntityId(), result.getEntityId());
        verify(clientService, times(1)).createClient(any(Client.class));
    }

    @Test
    void testGetAllClients() {
        when(clientService.getAllClients()).thenReturn(Collections.singletonList(user));

        List<RESTClient> result = restClientServiceAdapter.getAllClients();

        assertEquals(1, result.size());
        assertEquals(restClient.getEntityId(), result.get(0).getEntityId());
        verify(clientService, times(1)).getAllClients();
    }

    @Test
    void testGetClient() {
        when(clientService.getClient(uuid)).thenReturn(user);

        RESTClient result = restClientServiceAdapter.getClient(uuid);

        assertEquals(restClient.getEntityId(), result.getEntityId());
        verify(clientService, times(1)).getClient(uuid);
    }

    @Test
    void testUpdateClient() {
        UUID userId = UUID.randomUUID();
        Map<String, Object> fieldsToUpdate = Map.of("firstName", "Janek");

        doNothing().when(clientService).updateClient(userId, fieldsToUpdate);

        restClientServiceAdapter.updateClient(userId, fieldsToUpdate);

        verify(clientService, times(1)).updateClient(userId, fieldsToUpdate);
    }

    @Test
    void testActivateClient() {
        UUID userId = UUID.randomUUID();

        doNothing().when(clientService).activateClient(userId);

        restClientServiceAdapter.activateClient(userId);

        verify(clientService, times(1)).activateClient(userId);
    }

    @Test
    void testDeactivateClient() {
        UUID userId = UUID.randomUUID();

        doNothing().when(clientService).deactivateClient(userId);

        restClientServiceAdapter.deactivateClient(userId);

        verify(clientService, times(1)).deactivateClient(userId);
    }

    @Test
    void testCheckToken() {
        String token = "Token123";

        when(clientService.checkToken(token)).thenReturn(true);

        boolean result = restClientServiceAdapter.checkToken(token);

        assertTrue(result);
        verify(clientService, times(1)).checkToken(token);
    }

    @Test
    void testInvalidateToken() {
        String token = "Token123";

        doNothing().when(clientService).invalidateToken(token);

        restClientServiceAdapter.invalidateToken(token);

        verify(clientService, times(1)).invalidateToken(token);
    }

    @Test
    void testChangePassword() {
        String username = "janek";
        String newPassword = "newPassword123";

        doNothing().when(clientService).changePassword(username, newPassword);

        restClientServiceAdapter.changePassword(username, newPassword);

        verify(clientService, times(1)).changePassword(username, newPassword);
    }
}

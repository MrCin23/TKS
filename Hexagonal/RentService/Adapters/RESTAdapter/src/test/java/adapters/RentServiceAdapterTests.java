package adapters;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.p.core.domain.AppleArch;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.Rent;
import pl.lodz.p.core.domain.VMachine;
import pl.lodz.p.core.domain.user.Client;
import pl.lodz.p.core.domain.user.Role;
import pl.lodz.p.core.domain.user.Standard;
import pl.lodz.p.core.services.service.RentService;
import pl.lodz.p.rest.adapter.RentServiceAdapter;
import pl.lodz.p.rest.model.RESTAppleArch;
import pl.lodz.p.rest.model.RESTMongoUUID;
import pl.lodz.p.rest.model.RESTRent;
import pl.lodz.p.rest.model.RESTVMachine;
import pl.lodz.p.rest.model.dto.RentDTO;
import pl.lodz.p.rest.model.user.RESTClient;
import pl.lodz.p.rest.model.user.RESTRole;
import pl.lodz.p.rest.model.user.RESTStandard;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentServiceAdapterTests {

    @Mock
    private RentService rentService;

    @InjectMocks
    private RentServiceAdapter rentServiceAdapter;

    private Rent rent;
    private RESTRent restrent;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
        RESTClient restClient = new RESTClient(new RESTMongoUUID(uuid), "jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", RESTRole.CLIENT, true, new RESTStandard(), 0);
        Client client = new Client(new MongoUUID(uuid), "jan", "janowski", "janek", "janeczek", "janeczek@janko.jan", Role.CLIENT, true, new Standard(), 0);
        RESTVMachine restVMachine = new RESTAppleArch(new RESTMongoUUID(uuid), 4, "8GB", 0);
        VMachine vMachine = new AppleArch(new MongoUUID(uuid), 4, "8GB", 0);
        rent = new Rent(new MongoUUID(uuid), client, vMachine, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 100.0);
        restrent = new RESTRent(new RESTMongoUUID(uuid), restClient, restVMachine, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 100.0);
    }

    @Test
    void testCreateRent() {
        RentDTO rentDTO = new RentDTO("janowski", uuid, LocalDateTime.now());

        when(rentService.createRent(rentDTO.getUsername(), rentDTO.getVmId(), rentDTO.getStartTime())).thenReturn(rent);

        RESTRent result = rentServiceAdapter.createRent(rentDTO);

        assertEquals(restrent.getEntityId(), result.getEntityId());
        verify(rentService, times(1)).createRent(rentDTO.getUsername(), rentDTO.getVmId(), rentDTO.getStartTime());
    }

    @Test
    void testGetAllRents() {
        when(rentService.getAllRents()).thenReturn(Collections.singletonList(rent));

        List<RESTRent> result = rentServiceAdapter.getAllRents();

        assertEquals(1, result.size());
        verify(rentService, times(1)).getAllRents();
    }

    @Test
    void testGetRent() {
        UUID rentId = UUID.randomUUID();

        when(rentService.getRent(rentId)).thenReturn(rent);

        RESTRent result = rentServiceAdapter.getRent(rentId);

        assertEquals(restrent.getEntityId(), result.getEntityId());
        verify(rentService, times(1)).getRent(rentId);
    }

    @Test
    void testGetActiveRents() {
        when(rentService.getActiveRents()).thenReturn(Collections.singletonList(rent));

        List<RESTRent> result = rentServiceAdapter.getActiveRents();

        assertEquals(1, result.size());
        verify(rentService, times(1)).getActiveRents();
    }

    @Test
    void testEndRent() {
        UUID rentId = UUID.randomUUID();
        LocalDateTime endDate = LocalDateTime.now().plusHours(2);

        doNothing().when(rentService).endRent(rentId, endDate);

        rentServiceAdapter.endRent(rentId, endDate);

        verify(rentService, times(1)).endRent(rentId, endDate);
    }

    @Test
    void testRemoveRent() {
        UUID rentId = UUID.randomUUID();

        doNothing().when(rentService).removeRent(rentId);

        rentServiceAdapter.removeRent(rentId);

        verify(rentService, times(1)).removeRent(rentId);
    }

    @Test
    void testGetArchivedRents() {
        when(rentService.getArchivedRents()).thenReturn(Collections.singletonList(rent));

        List<RESTRent> result = rentServiceAdapter.getArchivedRents();

        assertEquals(1, result.size());
        verify(rentService, times(1)).getArchivedRents();
    }

    @Test
    void testGetClientAllRentsWithAuthHeader() {
        String authHeader = "Bearer some-auth-token";

        when(rentService.getClientAllRents(authHeader)).thenReturn(Collections.singletonList(rent));

        List<RESTRent> result = rentServiceAdapter.getClientAllRents(authHeader);

        assertEquals(1, result.size());
        verify(rentService, times(1)).getClientAllRents(authHeader);
    }

    @Test
    void testGetClientAllRentsWithUUID() {
        UUID clientId = UUID.randomUUID();

        when(rentService.getClientAllRents(clientId)).thenReturn(Collections.singletonList(rent));

        List<RESTRent> result = rentServiceAdapter.getClientAllRents(clientId);

        assertEquals(1, result.size());
        verify(rentService, times(1)).getClientAllRents(clientId);
    }

    @Test
    void testGetClientActiveRents() {
        UUID clientId = UUID.randomUUID();

        when(rentService.getClientActiveRents(clientId)).thenReturn(Collections.singletonList(rent));

        List<RESTRent> result = rentServiceAdapter.getClientActiveRents(clientId);

        assertEquals(1, result.size());
        verify(rentService, times(1)).getClientActiveRents(clientId);
    }

    @Test
    void testGetVMachineActiveRent() {
        UUID vmId = UUID.randomUUID();

        when(rentService.getVMachineActiveRent(vmId)).thenReturn(rent);

        RESTRent result = rentServiceAdapter.getVMachineActiveRent(vmId);

        assertEquals(restrent.getEntityId(), result.getEntityId());
        verify(rentService, times(1)).getVMachineActiveRent(vmId);
    }

}


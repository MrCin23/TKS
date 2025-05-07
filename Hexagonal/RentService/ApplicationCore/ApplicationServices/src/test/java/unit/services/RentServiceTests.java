package unit.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.Rent;
import pl.lodz.p.core.domain.VMachine;
import pl.lodz.p.core.domain.user.Client;
import pl.lodz.p.core.domain.user.ClientType;
import pl.lodz.p.core.services.security.JwtTokenProvider;
import pl.lodz.p.core.services.service.RentService;
import pl.lodz.p.infrastructure.rent.*;
import pl.lodz.p.infrastructure.user.UGet;
import pl.lodz.p.infrastructure.vmachine.VMGet;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentServiceTests {

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private RentAdd rentAdd;
    @Mock
    private RentGet rentGet;
    @Mock
    private RentRemove rentRemove;
    @Mock
    private RentSize rentSize;
    @Mock
    private RentEnd rentEnd;
    @Mock
    private VMGet vmGet;
    @Mock
    private UGet uGet;

    @InjectMocks
    private RentService rentService;

    private Client client;
    private VMachine vmachine;
    private Rent rent;
    private UUID clientId;
    private UUID vmId;
    private UUID rentId;
    private LocalDateTime startTime;

    @BeforeEach
    void setUp() {
        clientId = UUID.randomUUID();
        vmId = UUID.randomUUID();
        rentId = UUID.randomUUID();
        startTime = LocalDateTime.now();

        client = mock(Client.class);
        vmachine = mock(VMachine.class);
        rent = mock(Rent.class);
    }

    @Test
    void createRent_Success() {
        ClientType clientType = mock(ClientType.class);
        when(clientType.getMaxRentedMachines()).thenReturn(1);

        when(client.getClientType()).thenReturn(clientType);
        when(client.getCurrentRents()).thenReturn(0);
        when(uGet.getClientByUsername(anyString())).thenReturn(client);

        when(vmachine.isRented()).thenReturn(0);
        when(vmGet.getVMachineByID(any())).thenReturn(vmachine);

        Rent createdRent = rentService.createRent("user1", vmId, startTime);

        verify(rentAdd).add(any(Rent.class));
        assertNotNull(createdRent);
    }


    @Test
    void createRent_ClientNotFound() {
        when(uGet.getClientByUsername(anyString())).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () ->
                rentService.createRent("user1", vmId, startTime));

        assertEquals("Client not found", exception.getMessage());
    }

    @Test
    void createRent_VMNotFound() {
        when(uGet.getClientByUsername(anyString())).thenReturn(client);
        when(vmGet.getVMachineByID(any())).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () ->
                rentService.createRent("user1", vmId, startTime));

        assertEquals("VMachine not found", exception.getMessage());
    }

    @Test
    void createRent_RentedVM() {
        when(uGet.getClientByUsername(anyString())).thenReturn(client);
        when(vmGet.getVMachineByID(any())).thenReturn(vmachine);
        when(vmachine.isRented()).thenReturn(1);

        Exception exception = assertThrows(RuntimeException.class, () ->
                rentService.createRent("user1", vmId, startTime));

        assertEquals("VMachine already rented", exception.getMessage());
    }

    @Test
    void createRent_TooManyRents() {
        ClientType clientType = mock(ClientType.class);
        when(clientType.getMaxRentedMachines()).thenReturn(1);

        when(client.getClientType()).thenReturn(clientType);
        when(client.getCurrentRents()).thenReturn(1);
        when(uGet.getClientByUsername(anyString())).thenReturn(client);

        when(vmachine.isRented()).thenReturn(0);
        when(vmGet.getVMachineByID(any())).thenReturn(vmachine);

        Exception exception = assertThrows(RuntimeException.class, () -> rentService.createRent("user1", vmId, startTime));
        assertTrue(exception.getMessage().contains("Client is not permitted to rent more machines:"));
    }

    @Test
    void getAllRents_NotEmpty() {
        when(rentGet.getRents()).thenReturn(List.of(rent));

        List<Rent> rents = rentService.getAllRents();

        assertFalse(rents.isEmpty());
    }

    @Test
    void getAllRents_Null() {
        when(rentGet.getRents()).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> rentService.getAllRents());

        assertEquals("Rents not found", exception.getMessage());
    }

    @Test
    void getAllRents_EmptyList() {
        when(rentGet.getRents()).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(RuntimeException.class, () -> rentService.getAllRents());

        assertEquals("Rents not found", exception.getMessage());
    }

    @Test
    void endRent_Success() {
        UUID rentId = UUID.randomUUID();
        LocalDateTime endDate = LocalDateTime.now();

        rentService.endRent(rentId, endDate);

        verify(rentEnd).endRent(any(MongoUUID.class), eq(endDate));
    }

    @Test
    void removeRent() {
        rentService.removeRent(vmId);
        verify(rentRemove).remove(any(MongoUUID.class));
    }

    @Test
    void getActiveRents_Success() {
        when(rentGet.findBy("endTime", null)).thenReturn(List.of(rent));
        List<Rent> activeRents = rentService.getActiveRents();
        assertFalse(activeRents.isEmpty());
    }

    @Test
    void getActiveRents_Null() {
        when(rentGet.findBy("endTime", null)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> rentService.getActiveRents());
    }

    @Test
    void getActiveRents_EmptyList() {
        when(rentGet.findBy("endTime", null)).thenReturn(Collections.emptyList());
        assertThrows(RuntimeException.class, () -> rentService.getActiveRents());
    }

    @Test
    void getArchivedRents_Success() {
        when(rentGet.findByNegation("endTime", null)).thenReturn(List.of(rent));
        List<Rent> archivedRents = rentService.getArchivedRents();
        assertFalse(archivedRents.isEmpty());
    }

    @Test
    void getArchivedRents_Null() {
        when(rentGet.findByNegation("endTime", null)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> rentService.getArchivedRents());
    }

    @Test
    void getArchivedRents_EmptyList() {
        when(rentGet.findByNegation("endTime", null)).thenReturn(List.of());
        assertThrows(RuntimeException.class, () -> rentService.getArchivedRents());
    }

    @Test
    void getRent_Success() {
        when(rentGet.getRentByID(any())).thenReturn(rent);
        Rent foundRent = rentService.getRent(rentId);
        assertNotNull(foundRent);
    }

    @Test
    void getRent_ThrowsException_WhenNotFound() {
        when(rentGet.getRentByID(any())).thenReturn(null);
        assertThrows(RuntimeException.class, () -> rentService.getRent(rentId));
    }

    @Test
    void getClientAllRents_Success() {
        when(jwtTokenProvider.getLogin(anyString())).thenReturn("user1");
        when(rentGet.getClientRents(anyString())).thenReturn(List.of(rent));
        List<Rent> rents = rentService.getClientAllRents("Bearer token");
        assertFalse(rents.isEmpty());
    }

    @Test
    void getClientAllRents_UUID_Success() {
        when(rentGet.getClientRents(clientId)).thenReturn(List.of(rent));
        List<Rent> rents = rentService.getClientAllRents(clientId);
        assertFalse(rents.isEmpty());
    }

    @Test
    void getClientAllRents_UUID_EmptyList() {
        when(rentGet.getClientRents(clientId)).thenReturn(List.of());
        assertThrows(RuntimeException.class, () -> rentService.getClientAllRents(clientId));
    }

    @Test
    void getClientAllRents_ThrowsException_WhenNoRents() {
        when(jwtTokenProvider.getLogin(anyString())).thenReturn("user1");
        when(rentGet.getClientRents(anyString())).thenReturn(Collections.emptyList());
        assertThrows(RuntimeException.class, () -> rentService.getClientAllRents("Bearer token"));
    }

    @Test
    void getClientActiveRents_Success() {
        when(rentGet.getClientRents(any(MongoUUID.class), eq(true))).thenReturn(List.of(rent));
        List<Rent> rents = rentService.getClientActiveRents(clientId);
        assertFalse(rents.isEmpty());
    }

    @Test
    void getClientActiveRents_Null() {
        when(rentGet.getClientRents(any(MongoUUID.class), eq(true))).thenReturn(null);
        assertThrows(RuntimeException.class, () -> rentService.getClientActiveRents(clientId));
    }

    @Test
    void getClientActiveRents_EmptyList() {
        when(rentGet.getClientRents(any(MongoUUID.class), eq(true))).thenReturn(List.of());
        assertThrows(RuntimeException.class, () -> rentService.getClientActiveRents(clientId));
    }

    @Test
    void getClientArchivedRents_Success() {
        when(rentGet.getClientRents(any(MongoUUID.class), eq(false))).thenReturn(List.of(rent));
        List<Rent> rents = rentService.getClientArchivedRents(clientId);
        assertFalse(rents.isEmpty());
    }

    @Test
    void getClientArchivedRents_Null() {
        when(rentGet.getClientRents(any(MongoUUID.class), eq(false))).thenReturn(null);
        assertThrows(RuntimeException.class, () -> rentService.getClientArchivedRents(clientId));
    }

    @Test
    void getClientArchivedRents_EmptyList() {
        when(rentGet.getClientRents(any(MongoUUID.class), eq(false))).thenReturn(List.of());
        assertThrows(RuntimeException.class, () -> rentService.getClientArchivedRents(clientId));
    }

    @Test
    void getVMachineActiveRent_Success() {
        when(rentGet.getVMachineRents(any(MongoUUID.class), eq(true))).thenReturn(List.of(rent));
        Rent activeRent = rentService.getVMachineActiveRent(vmId);
        assertNotNull(activeRent);
    }

    @Test
    void getVMachineActiveRent_Null() {
        when(rentGet.getVMachineRents(any(MongoUUID.class), eq(true))).thenReturn(null);
        Exception exception = assertThrows(RuntimeException.class, () -> rentService.getVMachineActiveRent(vmId));
        assertEquals("VMachine with UUID:" + vmId + " is not currently rented", exception.getMessage());
    }

    @Test
    void getVMachineArchivedRents_Success() {
        when(rentGet.getVMachineRents(any(MongoUUID.class), eq(false))).thenReturn(List.of(rent));
        List<Rent> archivedRents = rentService.getVMachineArchivedRents(vmId);
        assertFalse(archivedRents.isEmpty());
    }

    @Test
    void getVMachineArchivedRents_Null() {
        when(rentGet.getVMachineRents(any(MongoUUID.class), eq(false))).thenReturn(null);
        assertThrows(RuntimeException.class, () -> rentService.getVMachineArchivedRents(vmId));
    }

    @Test
    void getVMachineArchivedRents_EmptyList() {
        when(rentGet.getVMachineRents(any(MongoUUID.class), eq(false))).thenReturn(List.of());
        assertThrows(RuntimeException.class, () -> rentService.getVMachineArchivedRents(vmId));
    }


}


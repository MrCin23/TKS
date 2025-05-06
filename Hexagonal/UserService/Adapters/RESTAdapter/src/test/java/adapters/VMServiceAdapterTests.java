package adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.p.core.domain.*;
import pl.lodz.p.core.services.service.VMachineService;
import pl.lodz.p.rest.adapter.VMServiceAdapter;
import pl.lodz.p.rest.model.*;
import pl.lodz.p.ui.RESTVMServicePort;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VMServiceAdapterTests {

    @Mock
    private VMachineService vMachineService;

    @InjectMocks
    private VMServiceAdapter vmServiceAdapter;

    private VMachine vMachine;
    private RESTVMachine restVMachine;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
        vMachine = new AppleArch(new MongoUUID(uuid), 4, "8GB", 0);
        restVMachine = new RESTAppleArch(new RESTMongoUUID(uuid), 4, "8GB", 0);
    }

    @Test
    void testCreateVMachine() {
        when(vMachineService.createVMachine(any(VMachine.class))).thenReturn(vMachine);

        RESTVMachine result = vmServiceAdapter.createVMachine(restVMachine);

        assertEquals(restVMachine.getEntityId(), result.getEntityId());
        verify(vMachineService, times(1)).createVMachine(any(VMachine.class));
    }

    @Test
    void testCreateX86() {
        RESTVMachine pomVMachine = new RESTx86(new RESTMongoUUID(uuid), 4, "8GB", 0, "AMD");
        when(vMachineService.createVMachine(any(VMachine.class))).thenReturn(vMachine);

        RESTVMachine result = vmServiceAdapter.createVMachine(pomVMachine);

        assertEquals(pomVMachine.getEntityId(), result.getEntityId());
        verify(vMachineService, times(1)).createVMachine(any(VMachine.class));
    }

    @Test
    void testGetAllVMachines() {
        when(vMachineService.getAllVMachines()).thenReturn(Collections.singletonList(vMachine));

        List<RESTVMachine> result = vmServiceAdapter.getAllVMachines();

        assertEquals(1, result.size());
        verify(vMachineService, times(1)).getAllVMachines();
    }

    @Test
    void testGetVMachine() {
        when(vMachineService.getVMachine(uuid)).thenReturn(vMachine);

        RESTVMachine result = vmServiceAdapter.getVMachine(uuid);

        assertEquals(restVMachine.getEntityId(), result.getEntityId());
        verify(vMachineService, times(1)).getVMachine(uuid);
    }

    @Test
    void testUpdateVMachine() {
        // Test to update VM with mock service interaction
        doNothing().when(vMachineService).updateVMachine(any(UUID.class), any());

        vmServiceAdapter.updateVMachine(uuid, Collections.singletonMap("ramSize", "16GB"));

        verify(vMachineService, times(1)).updateVMachine(any(UUID.class), any());
    }

    @Test
    void testDeleteVMachine() {
        // Test for deleting a VM
        doNothing().when(vMachineService).deleteVMachine(uuid);

        vmServiceAdapter.deleteVMachine(uuid);

        verify(vMachineService, times(1)).deleteVMachine(uuid);
    }
}

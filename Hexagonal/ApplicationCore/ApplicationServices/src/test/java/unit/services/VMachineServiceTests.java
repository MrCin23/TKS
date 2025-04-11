package unit.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.p.core.domain.AppleArch;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.VMachine;
import pl.lodz.p.core.services.service.VMachineService;
import pl.lodz.p.infrastructure.vmachine.VMAdd;
import pl.lodz.p.infrastructure.vmachine.VMGet;
import pl.lodz.p.infrastructure.vmachine.VMRemove;
import pl.lodz.p.infrastructure.vmachine.VMUpdate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VMachineServiceTests {

    @Mock
    private VMGet vmget;

    @Mock
    private VMAdd vmadd;

    @Mock
    private VMRemove vmremove;

    @Mock
    private VMUpdate vmupdate;

    @InjectMocks
    private VMachineService vmachineService;

    private VMachine vm;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
        vm = new AppleArch(new MongoUUID(uuid), 4, "8GB", 0);
    }

    @Test
    void createVMachine_Success() {
        when(vmget.getVMachineByID(vm.getEntityId())).thenReturn(null);

        VMachine createdVM = vmachineService.createVMachine(vm);

        assertEquals(vm, createdVM);
        verify(vmadd).add(vm);
    }

    @Test
    void createVMachine_AlreadyExists() {
        when(vmget.getVMachineByID(vm.getEntityId())).thenReturn(vm);

        Exception exception = assertThrows(RuntimeException.class, () -> vmachineService.createVMachine(vm));

        assertEquals("VMachine with id " + vm.getEntityId() + " already exists", exception.getMessage());
        verify(vmadd, never()).add(any());
    }

    @Test
    void getAllVMachines_Success() {
        List<VMachine> vmList = Collections.singletonList(vm);
        when(vmget.getVMachines()).thenReturn(vmList);

        List<VMachine> result = vmachineService.getAllVMachines();

        assertEquals(1, result.size());
        assertEquals(vm, result.get(0));
    }

    @Test
    void getAllVMachines_NoMachinesFound() {
        when(vmget.getVMachines()).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(RuntimeException.class, () -> vmachineService.getAllVMachines());

        assertEquals("No vmachines found", exception.getMessage());
    }

    @Test
    void getAllVMachines_Null() {
        when(vmget.getVMachines()).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> vmachineService.getAllVMachines());

        assertEquals("No vmachines found", exception.getMessage());
    }

    @Test
    void getVMachine_Success() {
        when(vmget.getVMachineByID(new MongoUUID(uuid))).thenReturn(vm);

        VMachine result = vmachineService.getVMachine(uuid);

        assertEquals(vm, result);
    }

    @Test
    void getVMachine_NotFound() {
        when(vmget.getVMachineByID(new MongoUUID(uuid))).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> vmachineService.getVMachine(uuid));

        assertEquals("VMachine with id " + uuid + " does not exist", exception.getMessage());
    }

    @Test
    void updateVMachine_Success() {
        Map<String, Object> updateFields = Map.of("cpu", "Intel");

        when(vmget.getVMachineByID(new MongoUUID(uuid))).thenReturn(vm);

        assertDoesNotThrow(() -> vmachineService.updateVMachine(uuid, updateFields));

        verify(vmupdate).update(new MongoUUID(uuid), updateFields);
    }

    @Test
    void updateVMachine_NotFound() {
        when(vmget.getVMachineByID(new MongoUUID(uuid))).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> vmachineService.updateVMachine(uuid, Map.of()));

        assertEquals("VMachine with id " + uuid + " does not exist", exception.getMessage());
    }

    @Test
    void deleteVMachine_Success() {
        when(vmget.getVMachineByID(new MongoUUID(uuid))).thenReturn(vm);

        assertDoesNotThrow(() -> vmachineService.deleteVMachine(uuid));

        verify(vmremove).remove(vm);
    }

    @Test
    void deleteVMachine_NotFound() {
        when(vmget.getVMachineByID(new MongoUUID(uuid))).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> vmachineService.deleteVMachine(uuid));

        assertEquals("VMachine with id " + uuid + " does not exist", exception.getMessage());
        verify(vmremove, never()).remove(any());
    }

    @Test
    void deleteVMachine_CurrentlyRented() {
        vm.setIsRented(1);
        when(vmget.getVMachineByID(new MongoUUID(uuid))).thenReturn(vm);

        Exception exception = assertThrows(RuntimeException.class, () -> vmachineService.deleteVMachine(uuid));

        assertEquals("VMachine with id " + uuid + " is currently rented", exception.getMessage());
        verify(vmremove, never()).remove(any());
    }

    @Test
    void size_Success() {
        when(vmget.size()).thenReturn(10L);

        long size = vmachineService.size();

        assertEquals(10L, size);
    }
}

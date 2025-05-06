package vmachine.adapter;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.p.core.domain.AppleArch;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.VMachine;
import pl.lodz.p.repo.MongoUUIDEnt;
import pl.lodz.p.repo.vm.adapter.VMachineAdapter;
import pl.lodz.p.repo.vm.repo.VMachineRepository;
import pl.lodz.p.repo.vm.data.AppleArchEnt;
import pl.lodz.p.repo.vm.data.VMachineEnt;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VMachineAdapterTests {

    @Mock
    private VMachineRepository vMachineRepo;

    @InjectMocks
    private VMachineAdapter vMachineAdapter;

    private VMachine mockVMachine;
    private VMachineEnt mockVMachineEnt;
    private MongoUUID mockUUID;
    private MongoUUIDEnt mockUUIDEnt;

    @BeforeEach
    public void setUp() {
        mockUUID = new MongoUUID(UUID.randomUUID());
        mockUUIDEnt = new MongoUUIDEnt(UUID.randomUUID());
        mockVMachine = new AppleArch(mockUUID, 4, "16GB", 0);
        mockVMachineEnt = new AppleArchEnt(mockUUIDEnt, 4, "16GB", 0);
    }

    @Test
    public void testAdd() {
        doNothing().when(vMachineRepo).add(any(VMachineEnt.class));

        vMachineAdapter.add(mockVMachine);

        verify(vMachineRepo, times(1)).add(any(VMachineEnt.class));
    }

    @Test
    public void testGetVMachines() {
        when(vMachineRepo.getVMachines()).thenReturn(Collections.singletonList(mockVMachineEnt));

        var result = vMachineAdapter.getVMachines();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof AppleArch);
    }

    @Test
    public void testGetVMachineByID() {
        when(vMachineRepo.getVMachineByID(any())).thenReturn(mockVMachineEnt);

        VMachine result = vMachineAdapter.getVMachineByID(mockUUID);

        assertNotNull(result);
        assertEquals(mockVMachine.getClass(), result.getClass());
    }

    @Test
    public void testGetVMachineByID_x86() {
        VMachine vMachine = mockVMachine = new AppleArch(mockUUID, 4, "16GB", 0);
        VMachineEnt vMachineEnt = mockVMachineEnt = new AppleArchEnt(mockUUIDEnt, 4, "16GB", 0);
        when(vMachineRepo.getVMachineByID(any())).thenReturn(vMachineEnt);

        VMachine result = vMachineAdapter.getVMachineByID(mockUUID);

        assertNotNull(result);
        assertEquals(mockVMachine.getClass(), result.getClass());
    }

    @Test
    public void testSize() {
        when(vMachineRepo.size()).thenReturn(1L);
        assertEquals(1, vMachineAdapter.size());
    }

    @Test
    public void testRemove() {
        doNothing().when(vMachineRepo).remove(any(VMachineEnt.class));

        vMachineAdapter.remove(mockVMachine);

        verify(vMachineRepo, times(1)).remove(any(VMachineEnt.class));
    }

    @Test
    public void testUpdate() {
        Map<String, Object> fieldsToUpdate = Map.of("ramSize", 32);
        doNothing().when(vMachineRepo).update(any(MongoUUIDEnt.class), anyMap());

        vMachineAdapter.update(mockUUID, fieldsToUpdate);

        verify(vMachineRepo, times(1)).update(any(MongoUUIDEnt.class), anyMap());
    }

    @Test
    public void testUpdateSingleField() {
        doNothing().when(vMachineRepo).update(any(MongoUUIDEnt.class), anyString(), any());

        vMachineAdapter.update(mockUUID, "ramSize", 32);

        verify(vMachineRepo, times(1)).update(any(MongoUUIDEnt.class), anyString(), any());
    }
}

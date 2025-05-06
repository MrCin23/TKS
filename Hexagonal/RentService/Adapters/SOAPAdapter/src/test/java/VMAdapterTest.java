import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.p.core.domain.AppleArch;
import pl.lodz.p.core.domain.MongoUUID;
import pl.lodz.p.core.domain.VMachine;
import pl.lodz.p.core.domain.x86;
import pl.lodz.p.core.services.service.VMachineService;
import pl.lodz.p.soap.adapter.SOAPVMServiceAdapter;
import pl.lodz.p.vmachines.AppleArchSOAP;
import pl.lodz.p.vmachines.VMachineSOAP;
import pl.lodz.p.vmachines.X86SOAP;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VMAdapterTest {

    @Mock
    private VMachineService vMachineService;

    @InjectMocks
    private SOAPVMServiceAdapter adapter;

    private x86 domainX86;
    private X86SOAP soapX86;

    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();

        domainX86 = new x86(
                new MongoUUID(uuid),
                4,
                "8GB",
                1,
                "Intel"
        );
        domainX86.setActualRentalPrice(100);

        soapX86 = new X86SOAP();
        soapX86.setEntityId(uuid.toString());
        soapX86.setRamSize("8GB");
        soapX86.setCPUNumber(4);
        soapX86.setIsRented(1);
        soapX86.setManufacturer("Intel");
        soapX86.setActualRentalPrice(100);
    }

    @Test
    void shouldCreateVMachine() {
        when(vMachineService.createVMachine(any(VMachine.class))).thenReturn(domainX86);

        VMachineSOAP result = adapter.createVMachine(soapX86);

        assertNotNull(result);
        assertEquals("8GB", result.getRamSize());
        verify(vMachineService).createVMachine(any(VMachine.class));
    }

    @Test
    void shouldGetAllVMachines() {
        when(vMachineService.getAllVMachines()).thenReturn(List.of(domainX86));

        List<VMachineSOAP> result = adapter.getAllVMachines();

        assertEquals(1, result.size());
        assertEquals(4, result.get(0).getCPUNumber());
    }

    @Test
    void shouldGetVMachineByUUID() {
        when(vMachineService.getVMachine(uuid)).thenReturn(domainX86);

        VMachineSOAP result = adapter.getVMachine(uuid);

        assertEquals(uuid.toString(), result.getEntityId());
        assertEquals(1, result.getIsRented());
    }

    @Test
    void shouldUpdateVMachine() {
        Map<String, Object> updates = Map.of("ramSize", 16384);

        adapter.updateVMachine(uuid, updates);

        verify(vMachineService).updateVMachine(uuid, updates);
    }

    @Test
    void shouldDeleteVMachine() {
        adapter.deleteVMachine(uuid);

        verify(vMachineService).deleteVMachine(uuid);
    }

    @Test
    void shouldConvertNullVMachineToSOAP() {
        assertNull(adapter.createVMachine(null));
    }

//    @Test
//    void shouldThrowOnUnsupportedSOAPType() {
//        VMachineSOAP unknown = mock(VMachineSOAP.class);
//        when(unknown.getClass()).thenReturn(Object.class);
//
//        assertThrows(RuntimeException.class, () -> adapter.createVMachine(unknown));
//    }
//
//    @Test
//    void shouldThrowOnUnsupportedDomainType() {
//        VMachine unknownDomain = mock(VMachine.class);
//        when(unknownDomain.getClass()).thenReturn(Object.class);
//
//        assertThrows(RuntimeException.class, () -> {
//            // Use reflection to call private method convert(VMachine)
//            var method = SOAPVMServiceAdapter.class.getDeclaredMethod("convert", VMachine.class);
//            method.setAccessible(true);
//            method.invoke(adapter, unknownDomain);
//        });
//    }
}

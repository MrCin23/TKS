
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.soap.endpoint.VMachineEndpoint;
import pl.lodz.p.soap.model.request.GetUserByUsernameRequest;
import pl.lodz.p.soap.model.request.HelloRequest;
import pl.lodz.p.ui.SOAPUserServicePort;
import pl.lodz.p.ui.SOAPVMServicePort;
import pl.lodz.p.users.*;
import pl.lodz.p.vmachines.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VMEndpointTest {
    private MockMvc mockMvc;
    private static final VMachineSOAP vmsoap = new X86SOAP();
    private static final String id = "11111111-1111-1111-1111-111111111111";

    @Mock
    private SOAPVMServicePort vmServicePort;

    @InjectMocks
    private VMachineEndpoint vmsoapEndpoint;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(vmServicePort).build();
    }

    @BeforeAll
    public static void setupClass() {
        vmsoap.setActualRentalPrice(100);
        vmsoap.setActualRentalPrice(10);
        vmsoap.setEntityId(id);
        vmsoap.setIsRented(0);
        vmsoap.setRamSize("1GB");
    }

    @Test
    void getVMachineByID_shouldReturnCorrectVMachine() {
        GetVMachineRequest request = new GetVMachineRequest();
        request.setId(id);
        when(vmServicePort.getVMachine(UUID.fromString(id))).thenReturn(vmsoap);
        GetVMachineResponse response = vmsoapEndpoint.getVMachineByID(request);
        Assertions.assertEquals(vmsoap.toString(), response.getVmachine().toString());
    }

    @Test
    void getAllVMachines_shouldReturnListOfVMachines() {
        GetAllVMachinesRequest request = new GetAllVMachinesRequest();
        when(vmServicePort.getAllVMachines()).thenReturn(List.of(vmsoap));
        GetAllVMachinesResponse response = vmsoapEndpoint.getVMachinesByID(request);
        Assertions.assertEquals(1, response.getVmachines().getVmachine().size());
        Assertions.assertEquals(vmsoap.toString(), response.getVmachines().getVmachine().get(0).toString());
    }

    @Test
    void getAllVMachines_emptyList() {
        when(vmServicePort.getAllVMachines()).thenReturn(Collections.emptyList());
        GetAllVMachinesResponse response = vmsoapEndpoint.getVMachinesByID(new GetAllVMachinesRequest());
        Assertions.assertEquals(0, response.getVmachines().getVmachine().size());
    }
}

package pl.lodz.p.soap.endpoint;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.lodz.p.ui.SOAPVMServicePort;
import pl.lodz.p.vmachines.*;

import java.util.UUID;

@Endpoint
public class VMachineEndpoint {
    private static final String NAMESPACE_URI = "http://p.lodz.pl/vmachines";
    private final SOAPVMServicePort vMachineServicePort;

    public VMachineEndpoint(SOAPVMServicePort vMachineServicePort) {
        this.vMachineServicePort = vMachineServicePort;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getVMachineRequest")
    @ResponsePayload
    public GetVMachineResponse getVMachineByID(@RequestPayload GetVMachineRequest request) {
        System.out.println(request.getId());
        GetVMachineResponse response = new GetVMachineResponse();
        response.setVmachine(vMachineServicePort.getVMachine(UUID.fromString(request.getId())));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllVMachinesRequest")
    @ResponsePayload
    public GetAllVMachinesResponse getVMachinesByID(@RequestPayload GetAllVMachinesRequest request) {
        GetAllVMachinesResponse response = new GetAllVMachinesResponse();
        VMachineList list = new VMachineList();
        list.getVmachine().addAll(vMachineServicePort.getAllVMachines());
        response.setVmachines(list);
        return response;
    }
}

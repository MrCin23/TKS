package controllers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import pl.lodz.p.rest.controller.VMachineController;
import pl.lodz.p.rest.model.RESTAppleArch;
import pl.lodz.p.rest.model.RESTVMachine;
import pl.lodz.p.rest.model.dto.UuidDTO;
import pl.lodz.p.ui.RESTVMServicePort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RESTVMachineControllerTests {

    private MockMvc mockMvc;
    private final UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private final UuidDTO dto = new UuidDTO(uuid.toString());
    private final RESTVMachine vmachine = new RESTAppleArch(4, "8GB");

    @Mock
    private RESTVMServicePort vMachineServicePort;

    @InjectMocks
    private VMachineController vMachineController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(vMachineController).build();
    }

    @Test
    public void createVMachine_CREATED_Test() {
        BindingResult bindingResult = new BeanPropertyBindingResult(vmachine, "vmachine");
        when(vMachineServicePort.createVMachine(any())).thenReturn(vmachine);
        ResponseEntity<Object> response = vMachineController.create(vmachine, bindingResult);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void createVMachine_BAD_REQUEST_Test() {
        BindingResult bindingResult = new BeanPropertyBindingResult(vmachine, "vmachine");
        bindingResult.addError(new ObjectError("vmachine", "test error"));
        ResponseEntity<Object> response = vMachineController.create(vmachine, bindingResult);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void getAllVMachines_OK_Test() {
        List<RESTVMachine> vms = Collections.singletonList(vmachine);
        when(vMachineServicePort.getAllVMachines()).thenReturn(vms);
        ResponseEntity<Object> response = vMachineController.getAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getAllVMachines_NOT_FOUND_Test() {
        when(vMachineServicePort.getAllVMachines()).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = vMachineController.getAll();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getVMachine_OK_Test() {
        when(vMachineServicePort.getVMachine(uuid)).thenReturn(vmachine);
        ResponseEntity<Object> response = vMachineController.getVMachine(dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getVMachine_NOT_FOUND_Test() {
        when(vMachineServicePort.getVMachine(uuid)).thenThrow(new RuntimeException());
        ResponseEntity<Object> response = vMachineController.getVMachine(dto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateVMachine_NO_CONTENT_Test() {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        fieldsToUpdate.put("name", "New VM Name");
        BindingResult bindingResult = new BeanPropertyBindingResult(fieldsToUpdate, "fieldsToUpdate");
        doNothing().when(vMachineServicePort).updateVMachine(uuid, fieldsToUpdate);
        ResponseEntity<Object> response = vMachineController.updateVMachine(dto, fieldsToUpdate, bindingResult);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void updateVMachine_BAD_REQUEST_Test() {
        Map<String, Object> fieldsToUpdate = new HashMap<>();
        BindingResult bindingResult = new BeanPropertyBindingResult(fieldsToUpdate, "fieldsToUpdate");
        bindingResult.addError(new ObjectError("fieldsToUpdate", "test error"));
        ResponseEntity<Object> response = vMachineController.updateVMachine(dto, fieldsToUpdate, bindingResult);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteVMachine_NO_CONTENT_Test() {
        doNothing().when(vMachineServicePort).deleteVMachine(uuid);
        ResponseEntity<Object> response = vMachineController.deleteVMachine(dto);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void deleteVMachine_BAD_REQUEST_Test() {
        doThrow(new RuntimeException()).when(vMachineServicePort).deleteVMachine(uuid);
        ResponseEntity<Object> response = vMachineController.deleteVMachine(dto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}


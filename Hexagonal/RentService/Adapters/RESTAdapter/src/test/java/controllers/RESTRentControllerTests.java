package controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import pl.lodz.p.rest.controller.RentController;
import pl.lodz.p.rest.model.RESTAppleArch;
import pl.lodz.p.rest.model.RESTRent;
import pl.lodz.p.rest.model.dto.RentDTO;
import pl.lodz.p.rest.model.user.RESTClient;
import pl.lodz.p.rest.model.user.RESTStandard;
import pl.lodz.p.ui.RentServicePort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RESTRentControllerTests {

    private MockMvc mockMvc;

    @Mock
    private RentServicePort rentServicePort;

    @InjectMocks
    private RentController rentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rentController).build();
    }

    @Test
    void testGetAllRents() throws Exception {
        List<RESTRent> rents = new ArrayList<>();
        RESTRent restRent = new RESTRent(new RESTClient("JDoe", new RESTStandard(), true), new RESTAppleArch(4, "8GB"), LocalDateTime.now());
        rents.add(restRent);
        when(rentServicePort.getAllRents()).thenReturn(rents);

        mockMvc.perform(get("/rent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetAllRentsNotFound() throws Exception {
        when(rentServicePort.getAllRents()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/rent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetRentNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(rentServicePort.getRent(uuid)).thenThrow(new RuntimeException("No rent found"));

        mockMvc.perform(get("/rent/{uuid}", uuid))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No rent found"));
    }

    @Test
    void testCreateRent() throws Exception {
        RentDTO rentDTO = new RentDTO();
        when(rentServicePort.createRent(any())).thenReturn(new RESTRent());

        mockMvc.perform(post("/rent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(rentDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void testEndRent() throws Exception {
        UUID uuid = UUID.randomUUID();
        doNothing().when(rentServicePort).endRent(eq(uuid), any());

        mockMvc.perform(put("/rent/end/{uuid}", uuid))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteRent() throws Exception {
        UUID uuid = UUID.randomUUID();
        doNothing().when(rentServicePort).removeRent(uuid);

        mockMvc.perform(delete("/rent/{uuid}", uuid))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetRent() throws Exception {
        UUID uuid = UUID.randomUUID();
        RESTRent restRent = new RESTRent(new RESTClient("JDoe", new RESTStandard(), true), new RESTAppleArch(4, "8GB"), LocalDateTime.now());
        when(rentServicePort.getRent(uuid)).thenReturn(restRent);

        mockMvc.perform(get("/rent/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.client.firstName").value("John"))
                .andExpect(jsonPath("$.vmachine.ramSize").value("8GB"));
    }

    @Test
    void testGetActiveRents() throws Exception {
        List<RESTRent> rents = new ArrayList<>();
        RESTRent restRent = new RESTRent(new RESTClient("JDoe", new RESTStandard(), true), new RESTAppleArch(4, "8GB"), LocalDateTime.now());
        rents.add(restRent);
        when(rentServicePort.getActiveRents()).thenReturn(rents);

        mockMvc.perform(get("/rent/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetArchivedRents() throws Exception {
        List<RESTRent> rents = new ArrayList<>();
        RESTRent restRent = new RESTRent(new RESTClient("JDoe", new RESTStandard(), true), new RESTAppleArch(4, "8GB"), LocalDateTime.now());
        rents.add(restRent);
        when(rentServicePort.getArchivedRents()).thenReturn(rents);

        mockMvc.perform(get("/rent/archived"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetClientAllRents() throws Exception {
        List<RESTRent> rents = new ArrayList<>();
        RESTRent restRent = new RESTRent(new RESTClient("JDoe", new RESTStandard(), true), new RESTAppleArch(4, "8GB"), LocalDateTime.now());
        rents.add(restRent);
        when(rentServicePort.getClientAllRents(anyString())).thenReturn(rents);

        mockMvc.perform(get("/rent/all/client")
                        .header("Authorization", "Bearer someValidToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetClientAllRentsNotFound() throws Exception {
        when(rentServicePort.getClientAllRents(anyString())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/rent/all/client")
                        .header("Authorization", "Bearer someValidToken"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetClientActiveRents() throws Exception {
        UUID uuid = UUID.randomUUID();
        List<RESTRent> rents = new ArrayList<>();
        RESTRent restRent = new RESTRent(new RESTClient("JDoe", new RESTStandard(), true), new RESTAppleArch(4, "8GB"), LocalDateTime.now());
        rents.add(restRent);
        when(rentServicePort.getClientActiveRents(uuid)).thenReturn(rents);

        mockMvc.perform(get("/rent/active/client/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetVMachineActiveRent() throws Exception {
        UUID uuid = UUID.randomUUID();
        RESTRent restRent = new RESTRent(new RESTClient("JDoe", new RESTStandard(), true), new RESTAppleArch(4, "8GB"), LocalDateTime.now());
        when(rentServicePort.getVMachineActiveRent(uuid)).thenReturn(restRent);

        mockMvc.perform(get("/rent/active/vmachine/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.client.firstName").value("John"))
                .andExpect(jsonPath("$.vmachine.ramSize").value("8GB"));
    }

    @Test
    void testGetVMachineActiveRentNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(rentServicePort.getVMachineActiveRent(uuid)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/rent/active/vmachine/{uuid}", uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetClientArchivedRents() throws Exception {
        UUID uuid = UUID.randomUUID();
        List<RESTRent> rents = new ArrayList<>();
        RESTRent restRent = new RESTRent(new RESTClient("JDoe", new RESTStandard(), true), new RESTAppleArch(4, "8GB"), LocalDateTime.now());
        rents.add(restRent);
        when(rentServicePort.getClientArchivedRents(uuid)).thenReturn(rents);

        mockMvc.perform(get("/rent/archived/client/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetVMachineArchivedRents() throws Exception {
        UUID uuid = UUID.randomUUID();
        List<RESTRent> rents = new ArrayList<>();
        RESTRent restRent = new RESTRent(new RESTClient("JDoe", new RESTStandard(), true), new RESTAppleArch(4, "8GB"), LocalDateTime.now());
        rents.add(restRent);
        when(rentServicePort.getVMachineArchivedRents(uuid)).thenReturn(rents);

        mockMvc.perform(get("/rent/archived/vmachine/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testCreateRentConflict() throws Exception {
        RentDTO rentDTO = new RentDTO();
        when(rentServicePort.createRent(any())).thenThrow(new IllegalArgumentException("Invalid rent data"));

        mockMvc.perform(post("/rent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(rentDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void testEndRentBadRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        doThrow(new RuntimeException("Rent not found")).when(rentServicePort).endRent(eq(uuid), any());

        mockMvc.perform(put("/rent/end/{uuid}", uuid))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteRentBadRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        doThrow(new RuntimeException("No rent found")).when(rentServicePort).removeRent(uuid);

        mockMvc.perform(delete("/rent/{uuid}", uuid))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetVMachineArchivedRentsNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(rentServicePort.getVMachineArchivedRents(uuid)).thenThrow(new RuntimeException("VMachine not found"));

        mockMvc.perform(get("/rent/archived/vmachine/{uuid}", uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetArchivedRentsNotFound() throws Exception {
        when(rentServicePort.getArchivedRents()).thenThrow(new IllegalArgumentException("Invalid request for archived rents"));

        mockMvc.perform(get("/rent/archived"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetActiveRentsNotFound() throws Exception {
        when(rentServicePort.getActiveRents()).thenThrow(new IllegalArgumentException("Invalid request for archived rents"));

        mockMvc.perform(get("/rent/active"))
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetClientActiveRentsNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(rentServicePort.getClientActiveRents(uuid)).thenThrow(new RuntimeException("Client has no active rents"));

        mockMvc.perform(get("/rent/active/client/{uuid}", uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetRentBadRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(rentServicePort.getRent(uuid)).thenThrow(new IllegalArgumentException("Bad request"));

        mockMvc.perform(get("/rent/{uuid}", uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateRentWithValidationErrors() throws Exception {
        RentDTO rentDTO = new RentDTO();

        BindingResult bindingResult = new BeanPropertyBindingResult(rentDTO, "rentDTO");
        bindingResult.addError(new ObjectError("rentDTO", "test validation error"));
        HttpServletRequest request = mock(HttpServletRequest.class);
        ResponseEntity<Object> response = rentController.createRent(rentDTO, bindingResult, request);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertTrue(response.getBody().toString().contains("test validation error"));
    }

    @Test
    void testExpiredJwtException() throws Exception {
        when(rentServicePort.getClientAllRents(anyString())).thenThrow(
                new ExpiredJwtException(
                        null, null, "Expired JWT token", null));

        mockMvc.perform(get("/rent/all/client")
                        .header("Authorization", "Bearer expiredToken"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetClientAllRentsByUUID() throws Exception {
        UUID clientId = UUID.randomUUID();
        RESTRent rent1 = new RESTRent(new RESTClient("JDoe", new RESTStandard(), true), new RESTAppleArch(4, "8GB"), LocalDateTime.now());
        List<RESTRent> rents = new ArrayList<>();
        rents.add(rent1);

        when(rentServicePort.getClientAllRents(clientId)).thenReturn(rents);

        mockMvc.perform(get("/rent/all/client/{clientId}", clientId.toString())
                        .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testGetClientAllRentsByUUIDNotFound() throws Exception {
        UUID clientId = UUID.randomUUID();
        when(rentServicePort.getClientAllRents(clientId)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/rent/all/client/{clientId}", clientId.toString())
                        .header("Authorization", "Bearer validToken"))
                .andExpect(status().isNotFound());
    }
}


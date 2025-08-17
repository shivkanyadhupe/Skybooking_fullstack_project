package com.flight.fareservice;

import com.flight.fareservice.controller.FareController;
import com.flight.fareservice.entity.Fare;
import com.flight.fareservice.service.FareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class FareControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FareService fareService;

    @InjectMocks
    private FareController fareController;

    @SuppressWarnings("deprecation")
	@BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fareController).build();
    }

    @Test
    public void testGetFareById_Found() throws Exception {
        Fare fare = new Fare();
        fare.setId(1L);
        fare.setFlightNumber("AI202");
        fare.setTicketType("Economy");
        fare.setPrice(500.0);

        when(fareService.getFareById(1L)).thenReturn(fare);

        mockMvc.perform(get("/fare/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.flightNumber").value("AI202"))
                .andExpect(jsonPath("$.ticketType").value("Economy"))
                .andExpect(jsonPath("$.price").value(500.0));

        verify(fareService, times(1)).getFareById(1L);
    }

    @Test
    public void testGetFareById_NotFound() throws Exception {
        when(fareService.getFareById(999L)).thenReturn(null);

        mockMvc.perform(get("/fare/999"))
                .andExpect(status().isNotFound());

        verify(fareService, times(1)).getFareById(999L);
    }
}

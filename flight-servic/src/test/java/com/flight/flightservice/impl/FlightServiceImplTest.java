package com.flight.flightservice.impl;

import com.flight.flightservice.dto.FlightDto;
import com.flight.flightservice.entity.Flight;
import com.flight.flightservice.exception.FlightNotFoundException;
import com.flight.flightservice.repository.FlightRepository;
import com.flight.flightservice.service.impl.FlightServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlightServiceImplTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightServiceImpl flightService;

    private Flight flight;
    private FlightDto flightDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("AI-101");
        flight.setAirline("Air India");
        flight.setSource("Delhi");
        flight.setDestination("Mumbai");
        flight.setDepartureTime(LocalDateTime.of(2025, 4, 20, 10, 30));
        flight.setArrivalTime(LocalDateTime.of(2025, 4, 20, 12, 30));
        flight.setTotalSeats(150);
        flight.setAvailableSeats(100);
        flight.setFare(new BigDecimal("4500.00"));

        flightDto = new FlightDto();
        flightDto.setId(1L);
        flightDto.setFlightNumber("AI-101");
        flightDto.setAirline("Air India");
        flightDto.setSource("Delhi");
        flightDto.setDestination("Mumbai");
        flightDto.setDepartureTime(LocalDateTime.of(2025, 4, 20, 10, 30));
        flightDto.setArrivalTime(LocalDateTime.of(2025, 4, 20, 12, 30));
        flightDto.setTotalSeats(150);
        flightDto.setAvailableSeats(100);
        flightDto.setFare(new BigDecimal("4500.00"));
    }

    @Test
    public void testAddFlight() {
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);
        FlightDto saved = flightService.addFlight(flightDto);
        assertEquals("AI-101", saved.getFlightNumber());
    }

    @Test
    public void testGetFlightById() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        FlightDto found = flightService.getFlightById(1L);
        assertEquals("Delhi", found.getSource());
    }

    @Test
    public void testGetFlightById_NotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(FlightNotFoundException.class, () -> flightService.getFlightById(1L));
    }

    @Test
    public void testDeleteFlight() {
        doNothing().when(flightRepository).deleteById(1L);
        flightService.deleteFlight(1L);
        verify(flightRepository, times(1)).deleteById(1L);
    }
}

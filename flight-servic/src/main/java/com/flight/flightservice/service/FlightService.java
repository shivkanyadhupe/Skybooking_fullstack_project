package com.flight.flightservice.service;

import com.flight.flightservice.dto.FlightDto;
import java.util.List;

public interface FlightService {
    FlightDto addFlight(FlightDto flightDto);
    FlightDto updateFlight(Long id, FlightDto flightDto);
    void deleteFlight(Long id);
    FlightDto getFlightById(Long id);
    List<FlightDto> getAllFlights();
    List<FlightDto> searchFlights(String source, String destination, String date);
}

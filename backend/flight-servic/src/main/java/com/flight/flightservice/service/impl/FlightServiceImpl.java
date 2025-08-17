package com.flight.flightservice.service.impl;

import com.flight.flightservice.dto.FlightDto;
import com.flight.flightservice.entity.Flight;
import com.flight.flightservice.exception.FlightNotFoundException;
import com.flight.flightservice.repository.FlightRepository;
import com.flight.flightservice.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository flightRepository;

    private FlightDto convertToDto(Flight flight) {
        FlightDto dto = new FlightDto();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setAirline(flight.getAirline());
        dto.setSource(flight.getSource());
        dto.setDestination(flight.getDestination());
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());
        dto.setTotalSeats(flight.getTotalSeats());
        dto.setAvailableSeats(flight.getAvailableSeats());
        dto.setFare(flight.getFare()); 
        return dto;
    }

    private Flight convertToEntity(FlightDto dto) {
        Flight flight = new Flight();
        flight.setId(dto.getId());
        flight.setFlightNumber(dto.getFlightNumber());
        flight.setAirline(dto.getAirline());
        flight.setSource(dto.getSource());
        flight.setDestination(dto.getDestination());
        flight.setDepartureTime(dto.getDepartureTime());
        flight.setArrivalTime(dto.getArrivalTime());
        flight.setTotalSeats(dto.getTotalSeats());
        flight.setAvailableSeats(dto.getAvailableSeats());
        flight.setFare(dto.getFare()); 
        return flight;
    }

    @Override
    public FlightDto addFlight(FlightDto flightDto) {
        Flight flight = convertToEntity(flightDto);
        return convertToDto(flightRepository.save(flight));
    }

    @Override
    public FlightDto updateFlight(Long id, FlightDto dto) {
        Flight existing = flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found"));
        existing.setFlightNumber(dto.getFlightNumber());
        existing.setAirline(dto.getAirline());
        existing.setSource(dto.getSource());
        existing.setDestination(dto.getDestination());
        existing.setDepartureTime(dto.getDepartureTime());
        existing.setArrivalTime(dto.getArrivalTime());
        existing.setTotalSeats(dto.getTotalSeats());
        existing.setAvailableSeats(dto.getAvailableSeats());
        existing.setFare(dto.getFare()); // ✅ mapping fare
        return convertToDto(flightRepository.save(existing));
    }

    @Override
    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }

    @Override
    public FlightDto getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found"));
        return convertToDto(flight);
    }

    @Override
    public List<FlightDto> getAllFlights() {
        return flightRepository.findAll()
                .stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<FlightDto> searchFlights(String source, String destination, String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime start = localDate.atStartOfDay();
        LocalDateTime end = localDate.atTime(LocalTime.MAX);
        return flightRepository
                .findBySourceAndDestinationAndDepartureTimeBetween(source, destination, start, end)
                .stream().map(this::convertToDto).collect(Collectors.toList());
    }
}

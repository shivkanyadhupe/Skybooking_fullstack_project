package com.flight.flightservice.controller;

import com.flight.flightservice.dto.FlightDto;
import com.flight.flightservice.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @PostMapping
    public FlightDto addFlight(@RequestBody FlightDto dto) {
        return flightService.addFlight(dto);
    }

    @PutMapping("/{id}")
    public FlightDto updateFlight(@PathVariable Long id, @RequestBody FlightDto dto) {
        return flightService.updateFlight(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.ok("Flight deleted successfully");
    }

    @GetMapping("/{id}")
    public FlightDto getFlight(@PathVariable Long id) {
        return flightService.getFlightById(id);
    }

    @GetMapping
    public List<FlightDto> getAllFlights() {
        return flightService.getAllFlights();
    }

    @GetMapping("/search")
    public List<FlightDto> searchFlights(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam String date) {
        return flightService.searchFlights(source, destination, date);
    }
}

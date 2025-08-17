package com.flight.fareservice.controller;

import com.flight.fareservice.entity.Fare;
import com.flight.fareservice.service.FareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fare")
public class FareController {

    @Autowired
    private FareService fareService;

    // ✅ Create a new fare
    @PostMapping("/create")
    public Fare createFare(@RequestBody Fare fare) {
        return fareService.createFare(fare);
    }

    // ✅ Get fare by flightNumber and ticketType
    @GetMapping
    public Fare getFare(@RequestParam String flightNumber, @RequestParam String ticketType) {
        return fareService.getFare(flightNumber, ticketType);
    }

    // ✅ Get fare by ID
    @GetMapping("/{id}")
    public ResponseEntity<Fare> getFareById(@PathVariable Long id) {
        Fare fare = fareService.getFareById(id);
        if (fare != null) {
            return new ResponseEntity<>(fare, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ✅ Get all fares
    @GetMapping("/all")
    public List<Fare> getAllFares() {
        return fareService.getAllFares();
    }

    // ✅ Delete fare by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFare(@PathVariable Long id) {
        boolean deleted = fareService.deleteFareById(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ✅ Update fare by ID
    @PutMapping("/{id}")
    public ResponseEntity<Fare> updateFare(@PathVariable Long id, @RequestBody Fare updatedFare) {
        Fare fare = fareService.updateFare(id, updatedFare);
        if (fare != null) {
            return new ResponseEntity<>(fare, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

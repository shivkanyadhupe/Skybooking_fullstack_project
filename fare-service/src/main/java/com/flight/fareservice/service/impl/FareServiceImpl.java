package com.flight.fareservice.service.impl;

import com.flight.fareservice.entity.Fare;
import com.flight.fareservice.repository.FareRepository;
import com.flight.fareservice.service.FareService;
import com.flight.fareservice.exception.FareNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FareServiceImpl implements FareService {

    @Autowired
    private FareRepository fareRepository;

    @Override
    public Fare createFare(Fare fare) {
        return fareRepository.save(fare);
    }

    @Override
    public Fare getFare(String flightNumber, String ticketType) {
        return fareRepository.findByFlightNumberAndTicketType(flightNumber, ticketType)
                .orElseThrow(() -> new FareNotFoundException("Fare not found for flight number: " + flightNumber + " and ticket type: " + ticketType));
    }

    @Override
    public List<Fare> getAllFares() {
        return fareRepository.findAll();
    }

    @Override
    public Fare getFareById(Long id) {
        return fareRepository.findById(id)
                .orElseThrow(() -> new FareNotFoundException("Fare not found with ID: " + id));
    }

    // ✅ Update fare by ID
    @Override
    public Fare updateFare(Long id, Fare updatedFare) {
        return fareRepository.findById(id).map(existingFare -> {
            existingFare.setFlightNumber(updatedFare.getFlightNumber());
            existingFare.setTicketType(updatedFare.getTicketType());
            existingFare.setPrice(updatedFare.getPrice());
            return fareRepository.save(existingFare);
        }).orElseThrow(() -> new FareNotFoundException("Fare not found with ID: " + id));
    }

    // ✅ Delete fare by ID
    @Override
    public boolean deleteFareById(Long id) {
        if (fareRepository.existsById(id)) {
            fareRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

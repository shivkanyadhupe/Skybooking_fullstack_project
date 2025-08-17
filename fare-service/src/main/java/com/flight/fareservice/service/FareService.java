package com.flight.fareservice.service;

import com.flight.fareservice.entity.Fare;

import java.util.List;

public interface FareService {
    Fare createFare(Fare fare);
    Fare getFare(String flightNumber, String ticketType);
    List<Fare> getAllFares();
    Fare getFareById(Long id);
	boolean deleteFareById(Long id);
	Fare updateFare(Long id, Fare updatedFare);

   
}

package com.flight.fareservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.flight.fareservice.entity.Fare;

import java.util.Optional;

public interface FareRepository extends JpaRepository<Fare, Long> {
    Optional<Fare> findByFlightNumberAndTicketType(String flightNumber, String ticketType);
}

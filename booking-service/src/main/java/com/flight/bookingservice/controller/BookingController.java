package com.flight.bookingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.flight.bookingservice.dto.BookingDTO;
import com.flight.bookingservice.entity.Booking;
import com.flight.bookingservice.service.BookingService;
import com.flight.bookingservice.service.RabbitMqBooking;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RabbitMqBooking rabbitMqBooking;  // Inject RabbitMqBooking

    @PostMapping("/create")
    public Booking createBooking(@RequestBody BookingDTO dto) {
        // Create the booking
        Booking savedBooking = bookingService.createBooking(dto);

        // Send confirmation message to RabbitMQ
        rabbitMqBooking.sendBookingConfirmation(savedBooking, dto.getUserEmail());

        return savedBooking;  // Return the saved booking
    }

    @GetMapping("/{id}")
    public Booking getBooking(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/all")
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @PutMapping("/{id}")
    public Booking updateBooking(@PathVariable Long id, @RequestBody BookingDTO dto) {
        return bookingService.updateBooking(id, dto);
    }

    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return "Booking deleted successfully";
    }
}

package com.flight.bookingservice.service;

import com.flight.bookingservice.dto.BookingDTO;
import com.flight.bookingservice.entity.Booking;
import com.flight.bookingservice.exception.ResourceNotFoundException;
import com.flight.bookingservice.rabbitmq.BookingMessageSender;
import com.flight.bookingservice.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMessageSender bookingMessageSender;

    @Override
    public Booking createBooking(BookingDTO dto) {
        Booking booking = new Booking();
        booking.setPassengerName(dto.getPassengerName());
        booking.setFlightNumber(dto.getFlightNumber());
        booking.setNumOfAdults(dto.getNumOfAdults());
        booking.setNumOfChildren(dto.getNumOfChildren());
        booking.setSeatNumber(dto.getSeatNumber());
        booking.setTicketType(dto.getTicketType());

        double fare = (dto.getNumOfAdults() * 1000) + (dto.getNumOfChildren() * 500);
        booking.setTotalFare(fare);

        Booking saved = bookingRepository.save(booking);

        String message = String.format("Booking confirmed for %s on flight %s. Total fare: ₹%.2f",
                saved.getPassengerName(), saved.getFlightNumber(), saved.getTotalFare());
        bookingMessageSender.sendMessage(message);

        return saved;
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking updateBooking(Long id, BookingDTO dto) {
        Booking booking = getBookingById(id);
        booking.setPassengerName(dto.getPassengerName());
        booking.setFlightNumber(dto.getFlightNumber());
        booking.setNumOfAdults(dto.getNumOfAdults());
        booking.setNumOfChildren(dto.getNumOfChildren());
        booking.setSeatNumber(dto.getSeatNumber());
        booking.setTicketType(dto.getTicketType());

        double fare = (dto.getNumOfAdults() * 1000) + (dto.getNumOfChildren() * 500);
        booking.setTotalFare(fare);

        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}

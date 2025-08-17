package com.flight.bookingservice.service;


import java.util.List;

import com.flight.bookingservice.dto.BookingDTO;
import com.flight.bookingservice.entity.Booking;

public interface BookingService {
    Booking createBooking(BookingDTO bookingDTO);
    Booking getBookingById(Long id);
    List<Booking> getAllBookings();
    Booking updateBooking(Long id, BookingDTO dto);
    void deleteBooking(Long id);
}

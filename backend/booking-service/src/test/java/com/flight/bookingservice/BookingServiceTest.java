package com.flight.bookingservice;

import com.flight.bookingservice.dto.BookingDTO;
import com.flight.bookingservice.entity.Booking;
import com.flight.bookingservice.repository.BookingRepository;
import com.flight.bookingservice.service.BookingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    private Booking booking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initializing a test booking
        booking = new Booking();
        booking.setId(1L);
        booking.setPassengerName("Shivkanya Dhupe");
        booking.setFlightNumber("FL123");
        booking.setNumOfAdults(2);
        booking.setNumOfChildren(1);
        booking.setSeatNumber("12A");
        booking.setTicketType("Economy");
    }

    @Test
    void testCreateBooking() {
        // Mock the repository save method
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Create BookingDTO
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setPassengerName("Shivkanya Dhupe");
        bookingDTO.setFlightNumber("FL123");
        bookingDTO.setNumOfAdults(2);
        bookingDTO.setNumOfChildren(1);
        bookingDTO.setSeatNumber("12A");
        bookingDTO.setTicketType("Economy");

        // Call the service method
        Booking createdBooking = bookingService.createBooking(bookingDTO);

        // Verify the result
        assertNotNull(createdBooking);
        assertEquals("Shivkanya Dhupe", createdBooking.getPassengerName());
        assertEquals("FL123", createdBooking.getFlightNumber());
    }

    @Test
    void testGetBookingById() {
        // Mock the repository findById method
        when(bookingRepository.findById(anyLong())).thenReturn(java.util.Optional.of(booking));

        // Call the service method
        Booking fetchedBooking = bookingService.getBookingById(1L);

        // Verify the result
        assertNotNull(fetchedBooking);
        assertEquals("Shivkanya Dhupe", fetchedBooking.getPassengerName());
        assertEquals("FL123", fetchedBooking.getFlightNumber());
    }

    @Test
    void testUpdateBooking() {
        // Mock the repository save method
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        // Mock the repository findById method
        when(bookingRepository.findById(anyLong())).thenReturn(java.util.Optional.of(booking));

        // Create an updated BookingDTO
        BookingDTO updatedBookingDTO = new BookingDTO();
        updatedBookingDTO.setPassengerName("Shivkanya Dhupe");
        updatedBookingDTO.setFlightNumber("FL123");
        updatedBookingDTO.setNumOfAdults(2);
        updatedBookingDTO.setNumOfChildren(1);
        updatedBookingDTO.setSeatNumber("15A"); // Updated seat number
        updatedBookingDTO.setTicketType("Business"); // Updated ticket type

        // Call the service method to update the booking
        Booking updatedBooking = bookingService.updateBooking(1L, updatedBookingDTO);

        // Verify the result
        assertNotNull(updatedBooking);
        assertEquals("15A", updatedBooking.getSeatNumber());
        assertEquals("Business", updatedBooking.getTicketType());
    }

    @Test
    void testDeleteBooking() {
        // Mock the repository findById method
        when(bookingRepository.findById(anyLong())).thenReturn(java.util.Optional.of(booking));

        // Call the service method to delete the booking
        bookingService.deleteBooking(1L);

        // Verify that the delete method was called once
        verify(bookingRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testGetAllBookings() {
        // Mock the repository findAll method
        when(bookingRepository.findAll()).thenReturn(java.util.List.of(booking));

        // Call the service method to get all bookings
        java.util.List<Booking> allBookings = bookingService.getAllBookings();

        // Verify the result
        assertNotNull(allBookings);
        assertEquals(1, allBookings.size());
        assertEquals("Shivkanya Dhupe", allBookings.get(0).getPassengerName());
    }
}

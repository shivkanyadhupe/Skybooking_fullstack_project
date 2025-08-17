package com.flight.bookingservice.dto;

public class BookingMessage {
    private String bookingId;
    private String userEmail;
    private String flightNumber;
    private String message;

    // Constructors
    public BookingMessage() {}

    public BookingMessage(String bookingId, String userEmail, String flightNumber, String message) {
        this.bookingId = bookingId;
        this.userEmail = userEmail;
        this.flightNumber = flightNumber;
        this.message = message;
    }

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public String getUserEmail(){
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

    
}

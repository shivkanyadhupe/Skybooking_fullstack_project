package com.flight.bookingservice.service;

import com.flight.bookingservice.dto.BookingMessage;
import com.flight.bookingservice.entity.Booking;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqBooking {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routingKey}")
    private String routingKey;

    public RabbitMqBooking(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendBookingConfirmation(Booking booking, String userEmail) {
        BookingMessage message = new BookingMessage(
            booking.getId().toString(),
            userEmail,
            booking.getFlightNumber(),
            "Your booking is confirmed!"
        );

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}

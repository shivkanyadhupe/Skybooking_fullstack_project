package com.flight.fareservice.controller;


import org.springframework.web.bind.annotation.*;

import com.flight.fareservice.service.PaymentService;
import com.mysql.cj.x.protobuf.MysqlxCrud.Order;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public String createPaymentOrder(@RequestParam int amount) throws Exception {
        com.razorpay.Order order = paymentService.createOrder(amount);
        return order.toString();
    }
}

package com.flight.fareservice.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final RazorpayClient razorpay;

    // ✅ Constructor injects Razorpay keys from application.properties
    public PaymentService(
        @Value("${razorpay.key_id}") String keyId,
        @Value("${razorpay.key_secret}") String keySecret
    ) throws Exception {
        this.razorpay = new RazorpayClient(keyId, keySecret);
    }

    public Order createOrder(int amountInRupees) throws Exception {
        JSONObject options = new JSONObject();
        options.put("amount", amountInRupees * 100); // Razorpay expects paise
        options.put("currency", "INR");
        options.put("receipt", "txn_" + System.currentTimeMillis());
        options.put("payment_capture", true); // Automatically capture payment

        return razorpay.orders.create(options);
    }
}

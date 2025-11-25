package com.example.saradha.model;

public class PaymentRequest {
    private String userId;
    private String productId;
    private String productName;
    private String totalAmount;
    private String status;
    private String referenceId;
    private String paymentDate;
    private String bookingDate;
    private String bookingTime;

    public PaymentRequest( String productId, String productName,
                          String totalAmount, String status, String referenceId,
                          String paymentDate, String bookingDate, String bookingTime) {
        this.productId = productId;
        this.productName = productName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.referenceId = referenceId;
        this.paymentDate = paymentDate;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
    }

    // Optional: getters and setters
}

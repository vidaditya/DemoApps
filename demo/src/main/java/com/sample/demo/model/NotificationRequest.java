package com.sample.demo.model;


public class NotificationRequest {

    private String phoneNumber;

    private String message;

    public NotificationRequest(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }
}

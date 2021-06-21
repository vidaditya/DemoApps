package com.sample.demo.exception;


public class NotificationRequestFailureException extends RuntimeException {

    public NotificationRequestFailureException(String id) {
        super("Notification request failed for the team : " + id);
    }
}

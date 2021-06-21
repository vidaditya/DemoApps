package com.sample.demo.exception;


public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException(String id) {
        super("Could not find the team for the id : " + id);
    }
}

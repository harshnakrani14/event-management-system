package com.example.ems.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String entity, String id) {
        super(entity + " not found with ID: " + id);
    }

    public NotFoundException(String msg) {
        super(msg);
    }

}
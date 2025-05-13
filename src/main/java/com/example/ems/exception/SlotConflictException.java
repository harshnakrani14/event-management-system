package com.example.ems.exception;

public class SlotConflictException extends RuntimeException {

    public SlotConflictException(String msg) {
        super(msg);
    }

}
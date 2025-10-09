package com.example.ems.config;

import com.example.ems.dto.response.ErrorResponse;
import com.example.ems.exception.*;
import com.mongodb.MongoWriteException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        return ErrorResponse.builder().status(HttpStatus.NOT_FOUND.value()).message(ex.getMessage()).build();
    }

    @ExceptionHandler(SlotConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleSlotConflictException(SlotConflictException ex) {
        return ErrorResponse.builder().status(HttpStatus.CONFLICT.value()).message(ex.getMessage()).build();
    }

    @ExceptionHandler(DateAndTimeOverLapException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDateAndTimeOverLapException(DateAndTimeOverLapException ex) {
        return ErrorResponse.builder().status(HttpStatus.CONFLICT.value()).message(ex.getMessage()).build();
    }

    @ExceptionHandler(CustomAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleCustomAccessDenied(CustomAccessDeniedException ex) {
        return ErrorResponse.builder().status(HttpStatus.FORBIDDEN.value()).message(ex.getMessage()).build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
        return ErrorResponse.builder().status(HttpStatus.FORBIDDEN.value())
                .message("You do not have permission to access this resource.").build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentialException(BadCredentialsException ex) {
        return ErrorResponse.builder().status(HttpStatus.UNAUTHORIZED.value()).message(ex.getMessage()).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return ErrorResponse.builder().status(HttpStatus.BAD_REQUEST.value()).message(ex.getMessage()).build();
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        return ErrorResponse.builder().status(HttpStatus.UNAUTHORIZED.value()).message(ex.getMessage()).build();
    }

    @ExceptionHandler(MongoWriteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMongoDuplicateKeyException(MongoWriteException ex) {
        return ErrorResponse.builder().status(HttpStatus.BAD_REQUEST.value()).message(extractMongoError(ex.getMessage())).build();
    }

    private String extractMongoError(String message) {
        Pattern pattern = Pattern.compile("email:\\s*\\\\?\"([^\"]+)\\\\?\"");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String email = matcher.group(1); // Extracts only the email without quotes
            return "email: " + email + " already exists.";
        }
        return "Duplicate key error";
    }

}
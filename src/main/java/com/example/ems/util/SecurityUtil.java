package com.example.ems.util;

import com.example.ems.exception.UnauthorizedAccessException;
import com.example.ems.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static User getCurrentUser() {
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Failed to retrieve current user.");
        }
    }

}
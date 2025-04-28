package com.example.ems.util;

import com.example.ems.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static User getCurrentUserEmail() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}



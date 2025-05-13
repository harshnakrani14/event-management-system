package com.example.ems.util;

import com.example.ems.model.User;
import com.example.ems.util.enums.Role;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {

        if (auth == null || targetDomainObject == null || !(permission instanceof String)) {
            return false;
        }

        // If the permission is "ORGANIZER", check if the logged-in user is the same as the target
        if (Role.ORGANIZER.equals(Role.valueOf(permission.toString()))) {

            User user = (User) auth.getPrincipal(); // Get logged-in user
            String currentUserName = user.getName(); // Get user's name (not email)
            String targetName = (String) targetDomainObject; // Target name

            // Allow access only if the user is an ORGANIZER and the names match
            if (user.getRole() == Role.ORGANIZER) {
                return currentUserName.equals(targetName); // true if names match
            }

            return false; // Deny if user is not an ORGANIZER
        }

        // If the permission is "USER", check if user has the role of USER (for general access)
        if (Role.USER.equals(Role.valueOf(permission.toString()))) {

            User user = (User) auth.getPrincipal(); // Get logged-in user

            // Allow access if the user is a USER (or any other business rule for USER access)
            if (user.getRole() == Role.USER) {
                return true; // Grant access for USER role (can be refined)
            }

            return false; // Deny if the user is not a USER
        }

        return false; // Deny for any other cases if the permission is not recognized
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {

        if (auth == null || targetType == null || !(permission instanceof String)) {
            return false;
        }

        return hasPrivilege(auth, targetType.toUpperCase(), permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(Authentication auth, String targetType, String permission) {

        for (GrantedAuthority grantedAuth : auth.getAuthorities()) {

            if (grantedAuth.getAuthority().startsWith(targetType) &&
                    grantedAuth.getAuthority().contains(permission)) {
                return true;
            }
        }

        return false;
    }

}
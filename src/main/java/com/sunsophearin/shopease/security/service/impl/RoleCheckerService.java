package com.sunsophearin.shopease.security.service.impl;

import com.sunsophearin.shopease.security.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleCheckerService {

    private final JwtUtil jwtUtil;

    public RoleCheckerService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // ពិនិត្យថាមានតួនាទីជាក់លាក់មួយ
    public boolean hasRole(HttpServletRequest request, String role) {
        List<String> roles = jwtUtil.extractRoles(request);
        return roles.contains(role);
    }

    // ពិនិត្យថាមានតួនាទីណាមួយក្នុងចំណោមតួនាទីដែលបានផ្ដល់
    public boolean hasAnyRole(HttpServletRequest request, String... rolesToCheck) {
        List<String> userRoles = jwtUtil.extractRoles(request);
        Set<String> userRoleSet = new HashSet<>(userRoles);
        for (String role : rolesToCheck) {
            if (userRoleSet.contains(role)) {
                return true;
            }
        }
        return false;
    }

    // ពិនិត្យថាមានគ្រប់តួនាទីទាំងអស់ដែលបានផ្ដល់
    public boolean hasAllRoles(HttpServletRequest request, String... rolesToCheck) {
        List<String> userRoles = jwtUtil.extractRoles(request);
        Set<String> userRoleSet = new HashSet<>(userRoles);
        return userRoleSet.containsAll(Arrays.asList(rolesToCheck));
    }

    // ឧទាហរណ៍៖ ពិនិត្យ ROLE_ADMIN
    public boolean hasAdminRole(HttpServletRequest request) {
        return hasRole(request, "ROLE_ADMIN");
    }
}

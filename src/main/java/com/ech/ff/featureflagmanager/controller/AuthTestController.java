package com.ech.ff.featureflagmanager.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth-test")
public class AuthTestController {

    @GetMapping("/public")
    public Map<String, String> publicEndpoint() {
        return Map.of("message", "This is a public endpoint that doesn't require authentication");
    }

    @GetMapping("/protected")
    public Map<String, Object> protectedEndpoint(@AuthenticationPrincipal OAuth2User principal) {
        return Map.of(
            "message", "This is a protected endpoint that requires authentication",
            "user", principal.getName(),
            "authorities", principal.getAuthorities()
        );
    }
}
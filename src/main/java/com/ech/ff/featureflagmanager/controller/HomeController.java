package com.ech.ff.featureflagmanager.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home(@AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            return Map.of(
                "authenticated", true,
                "name", principal.getAttribute("name"),
                "email", principal.getAttribute("email"),
                "attributes", principal.getAttributes()
            );
        } else {
            return Collections.singletonMap("authenticated", false);
        }
    }

    @GetMapping("/user-info")
    public Map<String, Object> userInfo(@AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            return principal.getAttributes();
        } else {
            return Collections.singletonMap("error", "Not authenticated");
        }
    }
}
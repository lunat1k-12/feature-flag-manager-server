package com.ech.ff.featureflagmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String login() {
        // Spring Security will handle the redirect to the OAuth2 provider
        return "redirect:/oauth2/authorization/cognito";
    }
}
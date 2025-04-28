package com.ech.ff.featureflagmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/callback")
@Slf4j
public class OAuth2CallbackController {

    @GetMapping
    @ResponseBody
    public String handleCallback(@RequestParam(required = false) String code,
                                @RequestParam(required = false) String state,
                                @RequestParam(required = false) String error) {
        if (error != null) {
            log.error("OAuth2 callback error: {}", error);
            return "Authentication failed: " + error;
        }
        
        log.info("OAuth2 callback successful with code: {}, state: {}", code, state);
        return "Authentication successful! You can close this window and return to the application.";
    }
}
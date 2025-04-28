package com.ech.ff.featureflagmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login-page")
public class LoginInstructionsController {

    @GetMapping
    public String loginInstructions() {
        return "redirect:/login.html";
    }
}
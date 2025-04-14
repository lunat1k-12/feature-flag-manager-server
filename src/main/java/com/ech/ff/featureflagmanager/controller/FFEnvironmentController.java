package com.ech.ff.featureflagmanager.controller;

import com.ech.ff.featureflagmanager.dynamodb.entity.Environment;
import com.ech.ff.featureflagmanager.dynamodb.repository.EnvironmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/env")
@RequiredArgsConstructor
public class FFEnvironmentController {

    private final EnvironmentRepository environmentRepository;

    @GetMapping
    public List<Environment> getEnvironment() {
        return environmentRepository.getAllEnvironments();
    }

    @PostMapping
    public Environment saveEnvironment(@RequestBody Environment environment) {
        environmentRepository.saveEnvironment(environment);
        return environment;
    }
}

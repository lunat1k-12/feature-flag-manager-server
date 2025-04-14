package com.ech.ff.featureflagmanager.controller;

import com.ech.ff.featureflagmanager.controller.dto.GenerateApiKeyRequest;
import com.ech.ff.featureflagmanager.dynamodb.entity.ApiKey;
import com.ech.ff.featureflagmanager.dynamodb.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/env/key")
@RequiredArgsConstructor
@Slf4j
public class ApiKeysController {

    private final ApiKeyRepository apiKeyRepository;

    @PostMapping
    public ApiKey generateKey(@RequestBody GenerateApiKeyRequest request) {
        log.info("Generate key request: {}", request);
        ApiKey key = ApiKey.builder()
                .key(UUID.randomUUID().toString())
                .envName(request.getEnv())
                .active(true)
                .build();
        apiKeyRepository.save(key);
        return key;
    }

    @GetMapping
    @RequestMapping("/{envName}")
    public List<ApiKey> getEnvKeys(@PathVariable("envName") String envName) {
        return apiKeyRepository.getEnvKeys(envName);
    }
}

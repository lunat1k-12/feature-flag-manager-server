package com.ech.ff.featureflagmanager.controller;

import com.ech.ff.featureflagmanager.controller.dto.GenerateApiKeyRequest;
import com.ech.ff.featureflagmanager.dynamodb.entity.ApiKey;
import com.ech.ff.featureflagmanager.dynamodb.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @GetMapping("/{envName}")
    public List<ApiKey> getEnvKeys(@PathVariable("envName") String envName) {
        log.info("Get env keys request: {}", envName);
        return apiKeyRepository.getEnvKeys(envName);
    }

    @DeleteMapping("/{env}/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteKey(@PathVariable("key") String key,
                          @PathVariable("env") String envName) {
        log.info("Delete key key: {}, env: {}", key, envName);
        apiKeyRepository.deleteKey(key, envName);
    }
}

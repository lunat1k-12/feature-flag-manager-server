package com.ech.ff.featureflagmanager.controller;

import com.ech.ff.featureflagmanager.controller.dto.GenerateApiKeyRequest;
import com.ech.ff.featureflagmanager.dynamodb.entity.ApiKey;
import com.ech.ff.featureflagmanager.dynamodb.repository.ApiKeyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

/**
 * REST controller for managing API keys.
 */
@RestController
@RequestMapping("/env/key")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "API Keys", description = "API for managing API keys for environments")
public class ApiKeysController {

    private final ApiKeyRepository apiKeyRepository;

    @PostMapping
    @Operation(summary = "Generate a new API key", 
               description = "Generates a new API key for the specified environment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "API key generated successfully",
                     content = @Content(schema = @Schema(implementation = ApiKey.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ApiKey generateKey(
            @RequestBody 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Environment for which to generate the API key", 
                required = true,
                content = @Content(schema = @Schema(implementation = GenerateApiKeyRequest.class)))
            GenerateApiKeyRequest request) {
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
    @Operation(summary = "Get API keys for an environment", 
               description = "Retrieves all API keys for the specified environment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "API keys retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Environment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<ApiKey> getEnvKeys(
            @PathVariable("envName") 
            @Parameter(description = "Name of the environment", example = "production", required = true)
            String envName) {
        log.info("Get env keys request: {}", envName);
        return apiKeyRepository.getEnvKeys(envName);
    }

    @DeleteMapping("/{env}/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an API key", 
               description = "Deletes the specified API key for the specified environment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "API key deleted successfully"),
        @ApiResponse(responseCode = "404", description = "API key or environment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void deleteKey(
            @PathVariable("key") 
            @Parameter(description = "API key to delete", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
            String key,
            @PathVariable("env") 
            @Parameter(description = "Name of the environment", example = "production", required = true)
            String envName) {
        log.info("Delete key key: {}, env: {}", key, envName);
        apiKeyRepository.deleteKey(key, envName);
    }
}

package com.ech.ff.featureflagmanager.controller;

import com.ech.ff.featureflagmanager.controller.dto.FeatureFlagRequest;
import com.ech.ff.featureflagmanager.dynamodb.entity.FeatureFlag;
import com.ech.ff.featureflagmanager.dynamodb.repository.FeatureFlagRepository;
import com.ech.ff.featureflagmanager.security.dto.CognitoUser;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing feature flags.
 */
@RestController
@RequestMapping("/env/ff")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Feature Flags", description = "API for managing feature flags")
public class FeatureFlagController {

    private final FeatureFlagRepository featureFlagRepository;

    @PostMapping
    @Operation(summary = "Create a new feature flag", 
               description = "Creates a new feature flag with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feature flag created successfully",
                     content = @Content(schema = @Schema(implementation = FeatureFlag.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public FeatureFlag createFeatureFlag(
            @RequestBody 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Feature flag details", 
                                                                 required = true,
                                                                 content = @Content(schema = @Schema(implementation = FeatureFlagRequest.class)))
            FeatureFlagRequest featureFlag,
            @AuthenticationPrincipal Jwt authentication) {
        log.info("Create FeatureFlag: {}", featureFlag);
        CognitoUser user = CognitoUser.fromJwt(authentication);
        FeatureFlag ff = FeatureFlag.builder()
                .envName(featureFlag.getEnvName())
                .featureName(featureFlag.getFeatureName())
                .type(featureFlag.getType())
                .config(featureFlag.getConfig())
                .userId(user.getId())
                .build();
        featureFlagRepository.save(ff);
        return ff;
    }

    @GetMapping
    @Operation(summary = "Get feature flags by environment", 
               description = "Retrieves all feature flags for a specific environment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feature flags retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Environment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<FeatureFlag> getFeatureFlags(
            @RequestParam 
            @Parameter(description = "Name of the environment", example = "production", required = true) 
            String envName,
            @AuthenticationPrincipal Jwt authentication) {
        return featureFlagRepository.getEnvFF(envName, CognitoUser.fromJwt(authentication));
    }

    @DeleteMapping("/{envName}/{featureName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a feature flag", 
               description = "Deletes the specified feature flag from the specified environment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Feature flag deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Feature flag or environment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void deleteFeatureFlag(
            @PathVariable("envName") 
            @Parameter(description = "Name of the environment", example = "production", required = true)
            String envName,
            @PathVariable("featureName") 
            @Parameter(description = "Name of the feature flag", example = "dark-mode", required = true)
            String featureName) {
        log.info("Deleting feature flag: {}, env: {}", featureName, envName);
        featureFlagRepository.deleteFF(featureName, envName);
    }
}

package com.ech.ff.featureflagmanager.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating a feature flag.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request object for creating or updating a feature flag")
public class FeatureFlagRequest {

    @Schema(description = "Name of the environment", example = "production", required = true)
    private String envName;

    @Schema(description = "Type of the feature flag", example = "BOOLEAN", required = true)
    private String type;

    @Schema(description = "Name of the feature", example = "dark-mode", required = true)
    private String featureName;

    @Schema(description = "Configuration for the feature flag in JSON format", 
           example = "{\"enabled\": true, \"rolloutPercentage\": 100}")
    private String config;
}

package com.ech.ff.featureflagmanager.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for generating a new API key.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request object for generating a new API key")
public class GenerateApiKeyRequest {

    @Schema(description = "Name of the environment for which to generate the API key", 
            example = "production", 
            required = true)
    private String env;
}

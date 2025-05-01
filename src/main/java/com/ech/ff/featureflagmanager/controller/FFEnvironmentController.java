package com.ech.ff.featureflagmanager.controller;

import com.ech.ff.featureflagmanager.dynamodb.entity.Environment;
import com.ech.ff.featureflagmanager.dynamodb.repository.EnvironmentRepository;
import com.ech.ff.featureflagmanager.security.dto.CognitoUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing environments.
 */
@RestController
@RequestMapping("/env")
@RequiredArgsConstructor
@Tag(name = "Environments", description = "API for managing environments")
@Slf4j
public class FFEnvironmentController {

    private final EnvironmentRepository environmentRepository;

    @GetMapping
    @Operation(summary = "Get all environments", 
               description = "Retrieves a list of all available environments")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Environments retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<Environment> getEnvironment(@AuthenticationPrincipal Jwt authentication) {
        CognitoUser user = CognitoUser.fromJwt(authentication);
        return environmentRepository.getUserEnvironments(user);
    }

    @PostMapping
    @Operation(summary = "Create a new environment", 
               description = "Creates a new environment with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Environment created successfully",
                     content = @Content(schema = @Schema(implementation = Environment.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Environment saveEnvironment(
            @RequestBody 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Environment details", 
                required = true,
                content = @Content(schema = @Schema(implementation = Environment.class)))
            Environment environment,
            @AuthenticationPrincipal Jwt authentication) {
        environmentRepository.saveEnvironment(environment);
        return environment;
    }
}

package com.ech.ff.featureflagmanager.controller;

import com.ech.ff.featureflagmanager.controller.dto.FeatureFlagRequest;
import com.ech.ff.featureflagmanager.dynamodb.entity.FeatureFlag;
import com.ech.ff.featureflagmanager.dynamodb.repository.FeatureFlagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/env/ff")
@RequiredArgsConstructor
@Slf4j
public class FeatureFlagController {

    private final FeatureFlagRepository featureFlagRepository;

    @PostMapping
    public FeatureFlag createFeatureFlag(@RequestBody FeatureFlagRequest featureFlag) {
        log.info("Create FeatureFlag: {}", featureFlag);
        FeatureFlag ff = FeatureFlag.builder()
                .envName(featureFlag.getEnvName())
                .featureName(featureFlag.getFeatureName())
                .type(featureFlag.getType())
                .config(featureFlag.getConfig())
                .build();
        featureFlagRepository.save(ff);
        return ff;
    }

    @GetMapping
    public List<FeatureFlag> getFeatureFlags(String envName) {
        return featureFlagRepository.getEnvFF(envName);
    }
}

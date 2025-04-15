package com.ech.ff.featureflagmanager.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureFlagRequest {
    private String envName;
    private String type;
    private String featureName;
    private String config;
}

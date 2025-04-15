package com.ech.ff.featureflagmanager.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@DynamoDbBean
public class FeatureFlag {

    private String envName;
    private String type;
    private String featureName;
    private String config;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("EnvName")
    public String getEnvName() {
        return envName;
    }

    @DynamoDbAttribute("type")
    public String getType() {
        return type;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("FeatureName")
    public String getFeatureName() {
        return featureName;
    }

    @DynamoDbAttribute("config")
    public String getConfig() {
        return config;
    }
}

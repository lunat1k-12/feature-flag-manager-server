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
public class ApiKey {

    private String key;
    private String envName;
    private Boolean active;
    private String userId;

    @DynamoDbSortKey
    @DynamoDbAttribute("key")
    public String getKey() {
        return key;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("EnvName")
    public String getEnvName() {
        return envName;
    }

    @DynamoDbAttribute("active")
    public Boolean getActive() {
        return active;
    }

    @DynamoDbAttribute("userId")
    public String getUserId() {
        return userId;
    }
}

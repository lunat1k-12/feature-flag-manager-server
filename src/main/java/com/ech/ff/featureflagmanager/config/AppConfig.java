package com.ech.ff.featureflagmanager.config;

import com.ech.ff.featureflagmanager.dynamodb.entity.ApiKey;
import com.ech.ff.featureflagmanager.dynamodb.entity.Environment;
import com.ech.ff.featureflagmanager.dynamodb.entity.FeatureFlag;
import com.ech.ff.featureflagmanager.dynamodb.repository.ApiKeyRepository;
import com.ech.ff.featureflagmanager.dynamodb.repository.EnvironmentRepository;
import com.ech.ff.featureflagmanager.dynamodb.repository.FeatureFlagRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class AppConfig {

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient buildDynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public EnvironmentRepository buildEnvironmentRepository(DynamoDbEnhancedClient client) {
        return new EnvironmentRepository(client.table("Environment", TableSchema.fromBean(Environment.class)));
    }

    @Bean
    public ApiKeyRepository buildApiKeyRepository(DynamoDbEnhancedClient client) {
        return new ApiKeyRepository(client.table("EnvApiKey", TableSchema.fromBean(ApiKey.class)));
    }

    @Bean
    public FeatureFlagRepository buildFeatureFlagRepository(DynamoDbEnhancedClient client) {
        return new FeatureFlagRepository(client.table("FeatureFlag", TableSchema.fromBean(FeatureFlag.class)));
    }
}

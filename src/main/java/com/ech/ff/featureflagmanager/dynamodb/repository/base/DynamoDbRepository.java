package com.ech.ff.featureflagmanager.dynamodb.repository.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Base repository class for DynamoDB operations.
 * Provides common CRUD operations and utility methods for DynamoDB tables.
 *
 * @param <T> The entity type
 */
@Slf4j
@RequiredArgsConstructor
public abstract class DynamoDbRepository<T> {

    protected final DynamoDbTable<T> dynamoDbTable;

    /**
     * Save an entity to DynamoDB.
     *
     * @param entity The entity to save
     * @return The saved entity
     */
    public T save(T entity) {
        log.info("Saving entity: {}", entity);
        try {
            dynamoDbTable.putItem(entity);
            return entity;
        } catch (Exception e) {
            log.error("Error saving entity: {}", entity, e);
            throw new RuntimeException("Failed to save entity", e);
        }
    }

    /**
     * Get an entity by its key.
     *
     * @param entity An entity with the key fields populated
     * @return The entity if found, otherwise empty
     */
    public Optional<T> getItem(T entity) {
        log.info("Getting entity: {}", entity);
        try {
            T result = dynamoDbTable.getItem(entity);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.error("Error getting entity: {}", entity, e);
            throw new RuntimeException("Failed to get entity", e);
        }
    }

    /**
     * Get an entity by its partition key and sort key.
     *
     * @param partitionKey The partition key
     * @param sortKey The sort key (can be null if no sort key)
     * @return The entity if found, otherwise empty
     */
    public Optional<T> getItem(String partitionKey, String sortKey) {
        Key.Builder keyBuilder = Key.builder().partitionValue(partitionKey);
        if (sortKey != null) {
            keyBuilder.sortValue(sortKey);
        }

        log.info("Getting entity by key: partition={}, sort={}", partitionKey, sortKey);
        try {
            T result = dynamoDbTable.getItem(keyBuilder.build());
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.error("Error getting entity by key: partition={}, sort={}", partitionKey, sortKey, e);
            throw new RuntimeException("Failed to get entity by key", e);
        }
    }

    /**
     * Delete an entity from DynamoDB.
     *
     * @param entity An entity with the key fields populated
     */
    public void deleteItem(T entity) {
        log.info("Deleting entity: {}", entity);
        try {
            dynamoDbTable.deleteItem(entity);
        } catch (Exception e) {
            log.error("Error deleting entity: {}", entity, e);
            throw new RuntimeException("Failed to delete entity", e);
        }
    }

    /**
     * Delete an entity by its partition key and sort key.
     *
     * @param partitionKey The partition key
     * @param sortKey The sort key (can be null if no sort key)
     */
    public void deleteItem(String partitionKey, String sortKey) {
        Key.Builder keyBuilder = Key.builder().partitionValue(partitionKey);
        if (sortKey != null) {
            keyBuilder.sortValue(sortKey);
        }

        log.info("Deleting entity by key: partition={}, sort={}", partitionKey, sortKey);
        try {
            dynamoDbTable.deleteItem(keyBuilder.build());
        } catch (Exception e) {
            log.error("Error deleting entity by key: partition={}, sort={}", partitionKey, sortKey, e);
            throw new RuntimeException("Failed to delete entity by key", e);
        }
    }

    /**
     * Query items by partition key.
     *
     * @param partitionKey The partition key
     * @return List of entities matching the partition key
     */
    public List<T> queryByPartitionKey(String partitionKey) {
        log.info("Querying entities by partition key: {}", partitionKey);
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(
                    Key.builder().partitionValue(partitionKey).build()
            );

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            return dynamoDbTable.query(request).items().stream().collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error querying entities by partition key: {}", partitionKey, e);
            throw new RuntimeException("Failed to query entities by partition key", e);
        }
    }

    /**
     * Query items by partition key with pagination.
     *
     * @param partitionKey The partition key
     * @param pageSize The number of items per page
     * @param lastEvaluatedKey The last evaluated key for pagination (can be null for first page)
     * @return A page of entities matching the partition key
     */
    public Page<T> queryByPartitionKeyPaginated(String partitionKey, int pageSize, Map<String, AttributeValue> lastEvaluatedKey) {
        log.info("Querying entities by partition key with pagination: key={}, pageSize={}, lastKey={}", 
                partitionKey, pageSize, lastEvaluatedKey);
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(
                    Key.builder().partitionValue(partitionKey).build()
            );

            QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .limit(pageSize);

            if (lastEvaluatedKey != null) {
                requestBuilder.exclusiveStartKey(lastEvaluatedKey);
            }

            PageIterable<T> pages = dynamoDbTable.query(requestBuilder.build());
            return pages.stream().findFirst().orElse(null);
        } catch (Exception e) {
            log.error("Error querying entities by partition key with pagination: key={}, pageSize={}", 
                    partitionKey, pageSize, e);
            throw new RuntimeException("Failed to query entities by partition key with pagination", e);
        }
    }

    /**
     * Scan all items in the table.
     *
     * @return List of all entities in the table
     */
    public List<T> scanAll() {
        log.info("Scanning all entities");
        try {
            return dynamoDbTable.scan().items().stream().collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error scanning all entities", e);
            throw new RuntimeException("Failed to scan all entities", e);
        }
    }

    /**
     * Scan items in the table with pagination.
     *
     * @param pageSize The number of items per page
     * @param lastEvaluatedKey The last evaluated key for pagination (can be null for first page)
     * @return A page of entities
     */
    public Page<T> scanPaginated(int pageSize, Map<String, AttributeValue> lastEvaluatedKey) {
        log.info("Scanning entities with pagination: pageSize={}, lastKey={}", pageSize, lastEvaluatedKey);
        try {
            ScanEnhancedRequest.Builder requestBuilder = ScanEnhancedRequest.builder()
                    .limit(pageSize);

            if (lastEvaluatedKey != null) {
                requestBuilder.exclusiveStartKey(lastEvaluatedKey);
            }

            PageIterable<T> pages = dynamoDbTable.scan(requestBuilder.build());
            return pages.stream().findFirst().orElse(null);
        } catch (Exception e) {
            log.error("Error scanning entities with pagination: pageSize={}", pageSize, e);
            throw new RuntimeException("Failed to scan entities with pagination", e);
        }
    }

    /**
     * Execute a function with exception handling.
     *
     * @param function The function to execute
     * @param errorMessage The error message to log if an exception occurs
     * @param <R> The return type of the function
     * @return The result of the function
     */
    protected <R> R executeWithExceptionHandling(Function<DynamoDbTable<T>, R> function, String errorMessage) {
        try {
            return function.apply(dynamoDbTable);
        } catch (Exception e) {
            log.error("{}: {}", errorMessage, e.getMessage(), e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    /**
     * Query items by global index using only the partition key.
     *
     * @param indexName The name of the global secondary index
     * @param partitionKey The partition key
     * @return List of entities matching the query
     */
    public List<T> queryByIndex(String indexName, String partitionKey) {
        return queryByIndex(indexName, partitionKey, (String) null);
    }

    public List<T> queryByIndex(String indexName, String partitionKey, String sortKey) {
        return queryByIndex(indexName, partitionKey, QueryConditional.keyEqualTo(Key.builder()
                        .partitionValue(partitionKey)
                        .sortValue(sortKey)
                .build()));
    }

    /**
     * Query items by global index.
     *
     * @param indexName The name of the global secondary index
     * @param partitionKey The partition key
     * @param sortKeyCondition The query conditional for the sort key
     * @return List of entities matching the query
     */
    public List<T> queryByIndex(String indexName, String partitionKey, QueryConditional sortKeyCondition) {
        log.info("Querying entities by global index: index={}, partitionKey={}", indexName, partitionKey);
        try {
            DynamoDbIndex<T> index = dynamoDbTable.index(indexName);

            QueryConditional partitionKeyCondition = QueryConditional.keyEqualTo(
                    Key.builder().partitionValue(partitionKey).build()
            );

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(sortKeyCondition != null ? sortKeyCondition : partitionKeyCondition)
                    .build();

            List<T> results = new java.util.ArrayList<>();
            index.query(request).forEach(page -> results.addAll(page.items()));
            return results;
        } catch (Exception e) {
            log.error("Error querying entities by global index: index={}, partitionKey={}", 
                    indexName, partitionKey, e);
            throw new RuntimeException("Failed to query entities by global index", e);
        }
    }

    /**
     * Query items by global index with pagination using only the partition key.
     *
     * @param indexName The name of the global secondary index
     * @param partitionKey The partition key
     * @param pageSize The number of items per page
     * @param lastEvaluatedKey The last evaluated key for pagination (can be null for first page)
     * @return A page of entities matching the query
     */
    public Page<T> queryByGlobalIndexPaginated(String indexName, String partitionKey, 
                                              int pageSize, Map<String, AttributeValue> lastEvaluatedKey) {
        return queryByGlobalIndexPaginated(indexName, partitionKey, null, pageSize, lastEvaluatedKey);
    }

    /**
     * Query items by global index with pagination.
     *
     * @param indexName The name of the global secondary index
     * @param partitionKey The partition key
     * @param sortKeyCondition The query conditional for the sort key
     * @param pageSize The number of items per page
     * @param lastEvaluatedKey The last evaluated key for pagination (can be null for first page)
     * @return A page of entities matching the query
     */
    public Page<T> queryByGlobalIndexPaginated(String indexName, String partitionKey, 
                                              QueryConditional sortKeyCondition, int pageSize, 
                                              Map<String, AttributeValue> lastEvaluatedKey) {
        log.info("Querying entities by global index with pagination: index={}, partitionKey={}, pageSize={}, lastKey={}", 
                indexName, partitionKey, pageSize, lastEvaluatedKey);
        try {
            DynamoDbIndex<T> index = dynamoDbTable.index(indexName);

            QueryConditional partitionKeyCondition = QueryConditional.keyEqualTo(
                    Key.builder().partitionValue(partitionKey).build()
            );

            QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                    .queryConditional(sortKeyCondition != null ? sortKeyCondition : partitionKeyCondition)
                    .limit(pageSize);

            if (lastEvaluatedKey != null) {
                requestBuilder.exclusiveStartKey(lastEvaluatedKey);
            }

            return index.query(requestBuilder.build()).stream().findFirst().orElse(null);
        } catch (Exception e) {
            log.error("Error querying entities by global index with pagination: index={}, partitionKey={}, pageSize={}", 
                    indexName, partitionKey, pageSize, e);
            throw new RuntimeException("Failed to query entities by global index with pagination", e);
        }
    }
}

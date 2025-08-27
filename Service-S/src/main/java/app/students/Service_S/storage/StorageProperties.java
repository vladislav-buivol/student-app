package app.students.Service_S.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage.minio")
public record StorageProperties(
        String endpoint,
        String admin,
        String password,
        String bucket,
        Integer presignTtlSeconds) {
}

package app.students.Service_S.configuration.minio;

import app.students.Service_S.storage.StorageProperties;
import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class MinioClientConfiguration {

    @Bean
    public MinioClient minioClient(StorageProperties storageProperties) {
        return MinioClient.builder()
                .endpoint(storageProperties.endpoint())
                .credentials(storageProperties.admin(), storageProperties.password())
                .build();
    }
}

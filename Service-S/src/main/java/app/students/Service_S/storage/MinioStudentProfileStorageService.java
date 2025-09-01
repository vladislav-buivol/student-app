package app.students.Service_S.storage;

import app.students.Service_S.properties.StorageProperties;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class MinioStudentProfileStorageService implements StorageService {
    private final MinioClient minioClient;
    private final StorageProperties properties;
    private final Logger log = LoggerFactory.getLogger(MinioStudentProfileStorageService.class);

    public MinioStudentProfileStorageService(MinioClient minioClient, StorageProperties storageProperties) {
        this.minioClient = minioClient;
        this.properties = storageProperties;
    }

    @Override
    public URL presignGet(String objectKet, Duration ttl) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Minio.presignGet called objectKey={} ttlSeconds={}", objectKet, ttl != null ? ttl.getSeconds() : null);
        String url = minioClient
                .getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs
                                .builder()
                                .bucket(properties.bucket())
                                .object(objectKet)
                                .method(Method.GET)
                                .expiry(ttl == null ? 0 : Math.toIntExact(ttl.getSeconds()), TimeUnit.SECONDS)
                                .build());
        URL presigned = URI.create(url).toURL();
        log.info("Minio.presignGet success url={}", presigned);
        return presigned;
    }

    @Override
    public String upload(String objectKey, InputStream in, long size, String contentType) {
        log.info("Minio.upload called objectKey={} size={} contentType={}", objectKey, size, contentType);
        try {
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(properties.bucket())
                            .object(objectKey)
                            .contentType(contentType)
                            .stream(in, size, -1)
                            .build()
            );
            log.info("Minio.upload success objectKey={}", objectKey);
            return objectKey;
        } catch (Exception e) {
            log.error("Minio.upload failed objectKey={} msg={}", objectKey, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean isUrlAvailable(String url) {
        log.info("Minio.isUrlAvailable called url={}", url);
        try {
            HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            int responseCode = connection.getResponseCode();
            boolean ok = responseCode == HttpURLConnection.HTTP_OK;
            log.info("Minio.isUrlAvailable responseCode={} available={}", responseCode, ok);
            return ok;
        } catch (Exception e) {
            log.error("Check if url available failed");
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean objectExists(String objectKet) {
        log.info("Minio.objectExists called objectKey={}", objectKet);
        try {
            minioClient.statObject(
                    StatObjectArgs
                            .builder()
                            .bucket(properties.bucket())
                            .object(objectKet)
                            .build()
            );
            log.info("Minio.objectExists true objectKey={}", objectKet);
            return true;
        } catch (Exception e) {
            log.info("Minio.objectExists false objectKey={}", objectKet);
            return false;
        }
    }

    @Override
    public boolean bucketExists() {
        try {
            minioClient.bucketExists(
                    BucketExistsArgs
                            .builder()
                            .bucket(properties.bucket())
                            .build()
            );
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

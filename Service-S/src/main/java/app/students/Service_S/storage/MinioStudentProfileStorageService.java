package app.students.Service_S.storage;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.stereotype.Component;

import java.io.IOException;
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

    public MinioStudentProfileStorageService(MinioClient minioClient, StorageProperties storageProperties) {
        this.minioClient = minioClient;
        this.properties = storageProperties;
    }

    @Override
    public URL presignGet(String objectKet, Duration ttl) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String url = minioClient
                .getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs
                                .builder()
                                .bucket(properties.bucket())
                                .object(objectKet)
                                .method(Method.GET)
                                .expiry(Math.toIntExact(ttl.getSeconds()), TimeUnit.SECONDS)
                                .build());
        return URI.create(url).toURL();
    }
}

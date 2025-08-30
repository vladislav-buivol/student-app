package app.students.Service_S.storage;

import io.minio.errors.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

public interface StorageService {

    URL presignGet(String objectKet, Duration ttl) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    String upload(String objectKey, InputStream in, long size, String contentType);

    boolean isUrlAvailable(String url);

    boolean objectExists(String objectKet);

    boolean bucketExists();
}

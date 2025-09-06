package app.students.Service_S.configuration.minio.initializer;

import app.students.Service_S.properties.StorageProperties;
import app.students.Service_S.service.StudentService;
import app.students.Service_S.storage.MinioStudentProfileStorageService;
import com.example.students.StudentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Component
public class StudentImageInitializer implements CommandLineRunner {
    private final MinioStudentProfileStorageService minioStorageService;
    private final ResourceLoader resourceLoader;
    private final StudentService studentService;
    private final StorageProperties properties;
    private final Logger log = LoggerFactory.getLogger(StudentImageInitializer.class);

    public StudentImageInitializer(MinioStudentProfileStorageService minioStorageService, ResourceLoader resourceLoader, StudentService studentService, StorageProperties properties) {
        this.minioStorageService = minioStorageService;
        this.resourceLoader = resourceLoader;
        this.studentService = studentService;
        this.properties = properties;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("StudentImageInitializer started");
        if (!minioStorageService.bucketExists()) {
            log.warn("Bucket '{}' does not exist, creating it...", properties.bucket());
            minioStorageService.createBucket(); // create the bucket
            log.info("Bucket '{}' created successfully", properties.bucket());
        }

        List<StudentData> studentData = studentService.getAllStudents();
        Resource imageResource = resourceLoader.getResource("classpath:images/profile/init-images/default_profile_image.png");
        String image = imageResource.getFilename();
        if (imageResource.exists() && image != null) {
            for (StudentData student : studentData) {
                String profileImageUrl = student.getProfileImageUrl();
                if (profileImageUrl == null || profileImageUrl.isEmpty() || !isValidUrl(student, profileImageUrl)) {
                    String recordBook = student.getRecordBook();
                    String objectKey = String.format("%s/%s_%s", recordBook, UUID.randomUUID(), imageResource.contentLength());
                    log.info("Uploading default image for recordBook={} objectKey={}", recordBook, objectKey);
                    try (InputStream in = imageResource.getInputStream()) {
                        minioStorageService.upload(objectKey, in, imageResource.contentLength(), "image/png");
                    }
                    student.setProfileImageUrl(objectKey);
                    studentService.update(student.getRecordBook(), student);
                }
            }
            log.info("StudentImageInitializer completed for {} students", studentData.size());
        } else {
            log.error("Default image not found");
            throw new Exception("Image not found");
        }
    }

    private boolean isValidUrl(StudentData student, String profileImageUrl) {
        if (!minioStorageService.isUrlAvailable(profileImageUrl)) {
            log.error("Could not get profile image for recordBook: {},  url: {}", student.getRecordBook(), profileImageUrl);
            return false;
        }
        return true;
    }

}

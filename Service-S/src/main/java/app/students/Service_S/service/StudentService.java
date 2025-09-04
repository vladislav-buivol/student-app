package app.students.Service_S.service;

import app.students.Service_S.data.Student;
import app.students.Service_S.mapper.StudentMapper;
import app.students.Service_S.properties.StorageProperties;
import app.students.Service_S.respository.StudentRepository;
import app.students.Service_S.storage.StorageService;
import com.example.students.StudentData;
import jakarta.persistence.EntityNotFoundException;
import org.bouncycastle.util.StoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StorageService minioStorageService;
    private final StorageProperties storageProperties;
    private final StudentMapper studentMapper;
    private final Logger log = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository, StorageService minioStorageService, StorageProperties storageProperties, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.minioStorageService = minioStorageService;
        this.storageProperties = storageProperties;
        this.studentMapper = studentMapper;
    }

    public List<StudentData> getAllStudents() {
        log.info("StudentService.getAllStudents called");
        List<StudentData> list = studentRepository
                .findAll()
                .stream().map(studentMapper::entityToDto)
                .map(this::enrichWithProfileUrl)
                .toList();
        log.info("StudentService.getAllStudents returning count={}", list.size());
        return list;
    }

    public StudentData getStudentByRecordBook(String recordBook) {
        log.info("StudentService.getStudentByRecordBook called recordBook={}", recordBook);
        StudentData data = studentRepository
                .findByRecordBook(recordBook)
                .map(studentMapper::entityToDto)
                .map(this::enrichWithProfileUrl)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Student with recordBook " + recordBook + " not found"));
        log.info("StudentService.getStudentByRecordBook found={}", data);
        return data;
    }

    public void update(String recordId, StudentData studentData) {
        log.info("StudentService.update called recordId={}", recordId);
        if (studentData == null) {
            throw new IllegalArgumentException("StudentData is null");
        }
        Student student = studentRepository.findByRecordBook(recordId)
                .orElseThrow(EntityNotFoundException::new);
        studentMapper.updateEntityFromDto(student, studentData);
        studentRepository.save(student);
        log.info("StudentService.update saved recordId={}", recordId);
    }

    private StudentData enrichWithProfileUrl(StudentData dto) {
        log.info("StudentService.mapToStudentData called recordBook={}", dto.getRecordBook());
        try {
            if (dto.getProfileImageUrl() != null
                    && minioStorageService.objectExists(dto.getProfileImageUrl())) {
                dto.setProfileImageUrl(minioStorageService
                        .presignGet(
                                dto.getProfileImageUrl(),
                                Duration.ofSeconds(storageProperties.presignTtlSeconds()))
                        .toString());
            }
            return dto;
        } catch (Exception e) {
            log.error("Error accessing profile image for {}", dto.getRecordBook(), e);
            throw new StoreException("Could not access profile image", e);
        }
    }
}

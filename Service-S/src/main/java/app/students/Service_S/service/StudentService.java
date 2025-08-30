package app.students.Service_S.service;

import app.students.Service_S.data.Student;
import app.students.Service_S.respository.StudentRepository;
import app.students.Service_S.storage.StorageProperties;
import app.students.Service_S.storage.StorageService;
import com.example.students.StudentData;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StorageService minioStorageService;
    private final StorageProperties storageProperties;
    private final Logger log = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository, StorageService minioStorageService, StorageProperties storageProperties) {
        this.studentRepository = studentRepository;
        this.minioStorageService = minioStorageService;
        this.storageProperties = storageProperties;
    }

    public List<StudentData> getAllStudents() {
        log.info("StudentService.getAllStudents called");
        List<StudentData> list = studentRepository
                .findAll()
                .stream().map(this::mapToStudentData)
                .toList();
        log.info("StudentService.getAllStudents returning count={}", list.size());
        return list;
    }

    public StudentData getStudentByRecordBook(String recordBook) {
        log.info("StudentService.getStudentByRecordBook called recordBook={}", recordBook);
        StudentData data = studentRepository
                .findByRecordBook(recordBook)
                .map(this::mapToStudentData)
                .orElse(null);
        log.info("StudentService.getStudentByRecordBook found={}", data != null);
        return data;
    }

    public void update(String recordId, StudentData studentData) {
        log.info("StudentService.update called recordId={}", recordId);
        Student student = studentRepository.findByRecordBook(recordId)
                .orElseThrow(EntityNotFoundException::new);
        studentRepository.save(mapToStudent(student, studentData));
        log.info("StudentService.update saved recordId={}", recordId);
    }

    private StudentData mapToStudentData(Student student) {
        if (student == null) {
            throw new EntityNotFoundException("Student is null");
        }
        log.info("StudentService.mapToStudentData called recordBook={}", student.getRecordBook());
        String url = "";
        try {
            if (minioStorageService.objectExists(student.getProfileImageUrl())) {
                url = minioStorageService.presignGet(student.getProfileImageUrl(), Duration.ofSeconds(storageProperties.presignTtlSeconds())).toString();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        StudentData sd = new StudentData();
        sd.setFirstName(student.getFirstName());
        sd.setLastName(student.getLastName());
        sd.setFaculty(student.getFaculty());
        sd.setRecordBook(student.getRecordBook());
        sd.setCreatedAt(convertToXmlCalendar(student));
        sd.setProfileImageUrl(url);
        log.info("StudentService.mapToStudentData created dto for recordBook={}", sd.getRecordBook());
        return sd;
    }

    public Student mapToStudent(Student student, StudentData dto) {
        if (dto == null) {
            throw new EntityNotFoundException("StudentData is null");
        }
        if (student == null) {
            throw new EntityNotFoundException("Student is null");
        }
        log.info("StudentService.mapToStudent called recordBook={} -> {}", student.getRecordBook(), dto.getRecordBook());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setFaculty(dto.getFaculty());
        student.setRecordBook(dto.getRecordBook());
        student.setProfileImageUrl(dto.getProfileImageUrl());
        if (dto.getCreatedAt() != null) {
            student.setCreatedAt(toLocalDateTime(dto.getCreatedAt()));
        }
        return student;
    }

    private LocalDateTime toLocalDateTime(javax.xml.datatype.XMLGregorianCalendar xmlCal) {
        log.info("StudentService.toLocalDateTime called");
        return xmlCal.toGregorianCalendar()
                .toZonedDateTime()
                .toLocalDateTime();
    }


    private XMLGregorianCalendar convertToXmlCalendar(Student students) {
        if (students == null) {
            throw new EntityNotFoundException("Student is null");
        }
        log.info("StudentService.convertToXmlCalendar called recordBook={}", students.getRecordBook());
        LocalDateTime currentUTCTime = students.getCreatedAt();
        String iso = students.getCreatedAt().toString();
        if (currentUTCTime.getSecond() == 0 && currentUTCTime.getNano() == 0) {
            iso += ":00"; // necessary hack because the second part is not optional in XML
        }
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(iso);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}

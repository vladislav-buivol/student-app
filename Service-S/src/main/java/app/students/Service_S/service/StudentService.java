package app.students.Service_S.service;

import app.students.Service_S.data.Student;
import app.students.Service_S.respository.StudentRepository;
import app.students.Service_S.storage.StorageService;
import com.example.students.StudentData;
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
    private final StorageService studentProfileStorageService;

    public StudentService(StudentRepository studentRepository, StorageService studentProfileStorageService) {
        this.studentRepository = studentRepository;
        this.studentProfileStorageService = studentProfileStorageService;
    }

    public List<StudentData> getAllStudents() {
        return studentRepository
                .findAll()
                .stream().map(this::mapStudent)
                .toList();
    }

    public StudentData getStudentByRecordBook(String recordBook) {
        return studentRepository
                .findByRecordBook(recordBook)
                .map(this::mapStudent)
                .orElse(null);
    }

    private StudentData mapStudent(Student student) {
        String url;
        try {
            url = studentProfileStorageService
                    .presignGet(student.getRecordBook() + "/img.png", Duration.ofSeconds(900)).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        StudentData sd = new StudentData();
        sd.setFirstName(student.getFirstName());
        sd.setLastName(student.getLastName());
        sd.setFaculty(student.getFaculty());
        sd.setRecordBook(student.getRecordBook());
        sd.setProfileImageUrl(student.getProfileImageUrl());
        sd.setCreatedAt(convertToXmlCalendar(student));
        if (url != null) {
            sd.setProfileImageUrl(url);
        }
        return sd;
    }

    private XMLGregorianCalendar convertToXmlCalendar(Student students) {
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

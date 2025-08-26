package app.students.Service_S.service;

import app.students.Service_S.data.Student;
import app.students.Service_S.respository.StudentRepository;
import com.example.students.StudentData;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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

    private StudentData mapStudent(Student students) {
        StudentData sd = new StudentData();
        sd.setFirstName(students.getFirstName());
        sd.setLastName(students.getLastName());
        sd.setFaculty(students.getFaculty());
        sd.setRecordBook(students.getRecordBook());
        sd.setProfileImageUrl(students.getProfileImageUrl());
        sd.setCreatedAt(convertToXmlCalendar(students));
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

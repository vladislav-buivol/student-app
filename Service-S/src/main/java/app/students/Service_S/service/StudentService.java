package app.students.Service_S.service;

import app.students.Service_S.data.Student;
import app.students.Service_S.respository.StudentRepository;
import com.example.students.StudentData;
import org.springframework.stereotype.Service;

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
        return sd;
    }
}

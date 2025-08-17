package app.students.Service_S.service;

import app.students.Service_S.data.Student;
import app.students.Service_S.respository.StudentRepository;
import com.example.students.StudentData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentData> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return mapStudents(students);
    }

    private StudentData mapStudent(Student students) {
        StudentData sd = new StudentData();
        sd.setFirstName(students.getFirstName());
        sd.setLastName(students.getLastName());
        sd.setFaculty(students.getFaculty());
        sd.setRecordBook(students.getRecordBook());
        return sd;
    }

    private List<StudentData> mapStudents(List<Student> students) {
        List<StudentData> sds = new ArrayList<>();
        for (Student student : students) {
            sds.add(mapStudent(student));
        }
        return sds;
    }
}

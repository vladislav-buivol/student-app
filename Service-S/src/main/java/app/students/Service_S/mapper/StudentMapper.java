package app.students.Service_S.mapper;

import app.students.Service_S.data.Student;
import com.example.students.StudentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

@Component
public class StudentMapper implements EntityDtoMapper<Student, StudentData> {

    private final Logger log = LoggerFactory.getLogger(StudentMapper.class);

    @Override
    public StudentData entityToDto(Student student) {
        if (student == null) {
            return null;
        }
        StudentData dto = new StudentData();
        updateDtoFromEntity(dto, student);
        return dto;
    }

    @Override
    public Student dtoToEntity(StudentData dto) {
        if (dto == null) {
            return null;
        }
        Student student = new Student();
        updateEntityFromDto(student, dto);
        return student;
    }

    @Override
    public void updateEntityFromDto(Student entity, StudentData dto) {
        if (entity == null || dto == null) {
            return;
        }
        log.info("StudentMapper.updateEntityFromDto called recordBook={} -> {}", entity.getRecordBook(), dto.getRecordBook());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setFaculty(dto.getFaculty());
        entity.setRecordBook(dto.getRecordBook());
        entity.setProfileImageUrl(dto.getProfileImageUrl());
        if (dto.getCreatedAt() != null) {
            entity.setCreatedAt(toZonedDateTime(dto.getCreatedAt()));
        }
    }

    @Override
    public void updateDtoFromEntity(StudentData dto, Student entity) {
        if (dto == null || entity == null) {
            return;
        }
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setFaculty(entity.getFaculty());
        dto.setRecordBook(entity.getRecordBook());
        dto.setCreatedAt(toXmlCalendar(entity.getCreatedAt()));
        dto.setProfileImageUrl(entity.getProfileImageUrl()); // Service can enrich later
    }

    private ZonedDateTime toZonedDateTime(XMLGregorianCalendar xmlCal) {
        return xmlCal == null ? null : xmlCal.toGregorianCalendar().toZonedDateTime();
    }

    private XMLGregorianCalendar toXmlCalendar(ZonedDateTime dateTime) {
        if (dateTime == null) return null;
        try {
            GregorianCalendar gcal = GregorianCalendar.from(dateTime.withZoneSameInstant(ZoneOffset.UTC));
            XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
            xmlCal.setTimezone(0);
            return xmlCal;
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("Error converting date", e);
        }
    }
}

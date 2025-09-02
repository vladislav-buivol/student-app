package app.students.Service_R.validator;

import com.example.students.GetStudentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GetStudentRequestValidator {
    private final Logger log = LoggerFactory.getLogger(GetStudentRequestValidator.class);

    public GetStudentRequestValidator() {
    }

    public void validate(GetStudentRequest request) {
        if (request.getRecordBook() == null || request.getRecordBook().isBlank()) {
            log.error("Record Book is null or empty {}", request);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Record Book is required");
        }
    }
}

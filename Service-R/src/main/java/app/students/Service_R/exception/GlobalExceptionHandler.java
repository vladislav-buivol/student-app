package app.students.Service_R.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionDetails> validationException(ResponseStatusException ex, HttpServletRequest request) {
        log.error("Request {} {} failed: {}", request.getMethod(), request.getRequestURI(), ex.getReason(), ex);

        ExceptionDetails details = new ExceptionDetails(ex, request);
        return ResponseEntity.status(ex.getStatusCode()).body(details);
    }
}

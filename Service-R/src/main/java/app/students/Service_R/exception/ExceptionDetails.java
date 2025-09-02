package app.students.Service_R.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

public class ExceptionDetails {
    private String statusCode;
    private String reason;
    private String uri;
    private String method;

    public ExceptionDetails(ResponseStatusException ex, HttpServletRequest request) {
        this.statusCode = ex.getStatusCode().toString();
        this.reason = ex.getReason();
        this.uri = request.getRequestURI();
        this.method = request.getMethod();
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}

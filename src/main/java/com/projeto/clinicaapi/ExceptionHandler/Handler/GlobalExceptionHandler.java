package com.projeto.clinicaapi.ExceptionHandler.Handler;
import com.projeto.clinicaapi.ExceptionHandler.Exception.BusinessLogicException;
import com.projeto.clinicaapi.ExceptionHandler.Exception.InvalidEntryException;
import com.projeto.clinicaapi.ExceptionHandler.Exception.ResourceAlreadyExistsException;
import com.projeto.clinicaapi.ExceptionHandler.Exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.Instant;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException exception){
        return buildErrorResponseEntity(HttpStatus.NOT_FOUND, "Resource Not Found", exception.getMessage());
    }
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception){
        return buildErrorResponseEntity(HttpStatus.CONFLICT, "Resource Already Exists", exception.getMessage());
    }
    @ExceptionHandler(InvalidEntryException.class)
    public ResponseEntity<ProblemDetail> handleInvalidEntryException(InvalidEntryException exception){
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, "Invalid Entry", exception.getMessage());
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentialsException(BadCredentialsException exception){
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, "Bad Credentials", exception.getMessage());
    }
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ProblemDetail> handleBusinessLogicException(BusinessLogicException exception){
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, "Business Logic", exception.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUncaught(Exception exception){
        log.error("Unexpected error", exception);
        return buildErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected Error", "Internal Server Error");
    }

    private ResponseEntity<ProblemDetail> buildErrorResponseEntity(HttpStatus status, String title, String detail){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("traceId", UUID.randomUUID().toString());
        return ResponseEntity.status(status).body(problemDetail);
    }

}

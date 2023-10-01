package isthatkirill.tasklist.error;

import isthatkirill.tasklist.error.exception.auth.AccessDeniedException;
import isthatkirill.tasklist.error.exception.entity.EntityNotFoundException;
import isthatkirill.tasklist.error.exception.entity.NotUniqueException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kirill Emelyanov
 */

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse notUniqueHandle(final NotUniqueException e) {
        log.error("Error: {} Description: {}", HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotFoundHandle(final EntityNotFoundException e) {
        log.error("Error: {} Description: {}", HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse notValidArgumentHandle(final MethodArgumentNotValidException e) {
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        String errorMessage = errors.stream()
                .map(error -> String.format("Field: %s, error: %s, value: %s",
                        error.getField(), error.getDefaultMessage(), error.getRejectedValue()))
                .collect(Collectors.joining("\n"));
        log.error("Error: {} Description: {}", HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse constraintViolationHandle(final ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMessage = violations.stream()
                .map(violation -> String.format("Field: %s, error: %s, value: %s",
                        violation.getPropertyPath().toString(), violation.getMessage(), violation.getInvalidValue()))
                .collect(Collectors.joining("\n"));
        log.error("Error: {} Description: {}", HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
    }

    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse accessDeniedHandle(final Exception e) {
        log.error("Error: {} Description: {}", HttpStatus.FORBIDDEN.getReasonPhrase(), e.getMessage());
        return new ErrorResponse(HttpStatus.FORBIDDEN.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse httpMessageNotReadableHandle(final HttpMessageNotReadableException e) {
        String errorMessage = String.format("Request body is not readable. Check the request body format. Cause: %s", e.getCause().getMessage());
        log.error("Error: {} Description: {}", HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidArgumentHandle(final MethodArgumentTypeMismatchException e) {
        String errorMessage = String.format("Invalid argument type. Check the request parameters. Required Type: %s, " +
                "invalid value: %s", e.getRequiredType().getName(), e.getValue());;
        log.error("Error: {} Description: {}", HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse unexpectedErrorHandle(final Exception e) {
        log.error("Error: {} Description: {}", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage(), e);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
    }

}

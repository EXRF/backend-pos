package exrf.pos.exception;

import exrf.pos.dto.ValidationErrorDto;
import exrf.pos.dto.responses.MessageResponseDto;
import exrf.pos.dto.responses.ValidationErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        List<ValidationErrorDto> errors = fieldErrors.stream()
                .map(fieldError -> new ValidationErrorDto(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        ValidationErrorResponseDto response = new ValidationErrorResponseDto(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorResponseDto> handleConstraintViolation(ConstraintViolationException ex) {
        List<ValidationErrorDto> errors = ex.getConstraintViolations().stream()
                .map(violation -> new ValidationErrorDto(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());

        ValidationErrorResponseDto response = new ValidationErrorResponseDto(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(new MessageResponseDto(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

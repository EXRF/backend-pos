package exrf.pos.exception;

import exrf.pos.util.ResponseUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.responseError(GlobalExceptionHandler.class, errorMessages));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errorMessages = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.responseError(GlobalExceptionHandler.class, errorMessages));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleWrongUsernameAndPasswordException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtil.responseError(GlobalExceptionHandler.class, "Error: Username or password is wrong"));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleUsernameNotFound(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.responseError(GlobalExceptionHandler.class, "Error: Username not found"));
    }

    @ExceptionHandler(RefreshTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleRefreshTokenException(RefreshTokenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtil.responseError(GlobalExceptionHandler.class, "Error " + e.getMessage()));
    }

    @ExceptionHandler(InvalidRoleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleRoleException(InvalidRoleException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.responseError(GlobalExceptionHandler.class, "Error: " + e.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.responseError(GlobalExceptionHandler.class, "Error: Resource not found"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtil.responseError(GlobalExceptionHandler.class, "Error: Something went wrong to our service"));
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleInvalidPageRequestException(InvalidRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.responseError(GlobalExceptionHandler.class, "Error: " + e.getMessage()));
    }
}

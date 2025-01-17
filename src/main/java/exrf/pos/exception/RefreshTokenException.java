package exrf.pos.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException(String message) {
        super(message);
    }
}

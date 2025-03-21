package com.nails.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NailsException extends RuntimeException {
    private final HttpStatus status;
    private final ErrorCode code;

    public enum ErrorCode {
        RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND),
        INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED),
        EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT),
        VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
        UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
        FORBIDDEN(HttpStatus.FORBIDDEN),
        BAD_REQUEST(HttpStatus.BAD_REQUEST),
        INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

        @Getter
        private final HttpStatus status;

        ErrorCode(HttpStatus status) {
            this.status = status;
        }
    }

    public NailsException(String message, ErrorCode code) {
        super(message);
        this.code = code;
        this.status = code.getStatus();
    }

    public static NailsException resourceNotFound(String resource) {
        return new NailsException(resource + " not found", ErrorCode.RESOURCE_NOT_FOUND);
    }

    public static NailsException invalidCredentials() {
        return new NailsException("Invalid email or password", ErrorCode.INVALID_CREDENTIALS);
    }

    public static NailsException emailAlreadyExists(String email) {
        return new NailsException("Email already exists: " + email, ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    public static NailsException validationError(String message) {
        return new NailsException(message, ErrorCode.VALIDATION_ERROR);
    }

    public static NailsException unauthorized(String message) {
        return new NailsException(message, ErrorCode.UNAUTHORIZED);
    }

    public static NailsException forbidden(String message) {
        return new NailsException(message, ErrorCode.FORBIDDEN);
    }

    public static NailsException badRequest(String message) {
        return new NailsException(message, ErrorCode.BAD_REQUEST);
    }

    public static NailsException internalError(String message) {
        return new NailsException(message, ErrorCode.INTERNAL_ERROR);
    }
}

package com.nailservices.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NailServicesException extends RuntimeException {
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

    public NailServicesException(String message, ErrorCode code) {
        super(message);
        this.code = code;
        this.status = code.getStatus();
    }

    public static NailServicesException resourceNotFound(String resource) {
        return new NailServicesException(resource + " not found", ErrorCode.RESOURCE_NOT_FOUND);
    }

    public static NailServicesException invalidCredentials() {
        return new NailServicesException("Invalid email or password", ErrorCode.INVALID_CREDENTIALS);
    }

    public static NailServicesException emailAlreadyExists(String email) {
        return new NailServicesException("Email already exists: " + email, ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    public static NailServicesException validationError(String message) {
        return new NailServicesException(message, ErrorCode.VALIDATION_ERROR);
    }

    public static NailServicesException unauthorized(String message) {
        return new NailServicesException(message, ErrorCode.UNAUTHORIZED);
    }

    public static NailServicesException forbidden(String message) {
        return new NailServicesException(message, ErrorCode.FORBIDDEN);
    }

    public static NailServicesException badRequest(String message) {
        return new NailServicesException(message, ErrorCode.BAD_REQUEST);
    }

    public static NailServicesException internalError(String message) {
        return new NailServicesException(message, ErrorCode.INTERNAL_ERROR);
    }
}

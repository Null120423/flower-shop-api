package book.core.support.error;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum ErrorType {

    // ==== AUTH ====
    UNAUTHORIZED(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.UNAUTHORIZED.getCode(),
            "Unauthorized: Invalid or missing token",
            LogLevel.WARN
    ),

    FORBIDDEN(
            HttpStatus.FORBIDDEN,
            ErrorCode.FORBIDDEN.getCode(),
            "Forbidden: You do not have permission",
            LogLevel.WARN
    ),

    INVALID_TOKEN(
            HttpStatus.UNAUTHORIZED,
            ErrorCode.INVALID_TOKEN.getCode(),
            "Invalid token",
            LogLevel.WARN
    ),

    // ==== USER ====
    USER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            ErrorCode.NOT_FOUND.getCode(),
            "User not found",
            LogLevel.INFO
    ),

    // ==== SYSTEM ====
    DEFAULT_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorCode.E500.getCode(),
            "Internal server error",
            LogLevel.ERROR
    );

    private final HttpStatus status;
    private final int code;
    private final String message;
    private final LogLevel logLevel;

    ErrorType(HttpStatus status, int code, String message, LogLevel logLevel) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    ErrorType(HttpStatus status, int code, String message) {
        this(status, code, message, LogLevel.INFO);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }
}

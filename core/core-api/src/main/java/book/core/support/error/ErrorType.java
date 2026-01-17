package book.core.support.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum ErrorType {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "User not found"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, ErrorCode.INVALID_TOKEN, "Invalid token"),
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR ,ErrorCode.E500 , "Internal server error" );

    private final HttpStatus status;
    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    // Constructor đầy đủ
    ErrorType(HttpStatus status, ErrorCode code, String message, LogLevel logLevel) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    // Constructor với logLevel mặc định (ví dụ: INFO)
    ErrorType(HttpStatus status, ErrorCode code, String message) {
        this(status, code, message, LogLevel.INFO);
    }

    // getter
    public HttpStatus getStatus() { return status; }
    public ErrorCode getCode() { return code; }
    public String getMessage() { return message; }
    public LogLevel getLogLevel() { return logLevel; }
}

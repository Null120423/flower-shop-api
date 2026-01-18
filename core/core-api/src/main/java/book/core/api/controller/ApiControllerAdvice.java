package book.core.api.controller;

import book.core.support.error.CoreException;
import book.core.support.error.ErrorType;
import book.core.support.response.ApiResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class ApiControllerAdvice {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Environment environment;

    public ApiControllerAdvice(Environment environment) {
        this.environment = environment;
    }

    /* =========================
       HANDLE BUSINESS EXCEPTION
       ========================= */
    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ApiResponse<?>> handleCoreException(CoreException e) {

        switch (e.getErrorType().getLogLevel()) {
            case ERROR -> log.error("CoreException : {}", e.getMessage(), e);
            case WARN -> log.warn("CoreException : {}", e.getMessage(), e);
            default -> log.info("CoreException : {}", e.getMessage(), e);
        }

        return ResponseEntity
                .status(e.getErrorType().getStatus())
                .body(ApiResponse.fail(e.getErrorType(), e.getMessage()));
    }

    /* =========================
       HANDLE UNKNOWN EXCEPTION
       ========================= */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {

        log.error("Unhandled exception", e);

        ErrorType errorType = ErrorType.DEFAULT_ERROR;

        String message = isLocalOrDev()
                ? buildDevMessage(e)
                : buildProdMessage(e);

        return ResponseEntity
                .status(errorType.getStatus())
                .body(ApiResponse.fail(errorType, message));
    }

    /* =========================
       HELPER METHODS
       ========================= */
    private boolean isLocalOrDev() {
        return Arrays.stream(environment.getActiveProfiles())
                .anyMatch(p ->
                        p.equalsIgnoreCase("loca-dev")
                                || p.equalsIgnoreCase("local") || p.equalsIgnoreCase("dev")
                );
    }

    private String buildDevMessage(Exception e) {
        return e.getMessage() != null ? e.getMessage() : "Unexpected error";
    }

    private String buildProdMessage(Exception e) {
        return e.getMessage().split(":").length> 0 ? e.getMessage().split(":")[0] : "Internal server error";
    }
}

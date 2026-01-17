package book.core.support.response;

import book.core.support.error.ErrorType;

public class ApiResponse<T> {

    private final ResultType result;
    private final T data;
    private final Integer status;
    private final String message;

    private ApiResponse(ResultType result, T data, Integer status, String message) {
        this.result = result;
        this.data = data;
        this.status = status;
        this.message = message;
    }

    /* =====================
       SUCCESS
       ===================== */

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(
                ResultType.SUCCESS,
                data,
                null,
                null
        );
    }

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(
                ResultType.SUCCESS,
                null,
                null,
                null
        );
    }

    /* =====================
       ERROR (ONLY status + message)
       ===================== */

    public static <T> ApiResponse<T> fail(ErrorType errorType) {
        return new ApiResponse<>(
                ResultType.ERROR,
                null,
                errorType.getStatus().value(),
                errorType.getMessage()
        );
    }

    public static <T> ApiResponse<T> fail(ErrorType errorType, String message) {
        return new ApiResponse<>(
                ResultType.ERROR,
                null,
                errorType.getStatus().value(),
                message
        );
    }

    /* =====================
       GETTERS
       ===================== */

    public ResultType getResult() {
        return result;
    }

    public T getData() {
        return data;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

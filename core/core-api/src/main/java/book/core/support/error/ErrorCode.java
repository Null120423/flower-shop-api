package book.core.support.error;

public enum ErrorCode {
    // Define constants with their associated codes
    E500(500),
    NOT_FOUND(404),
    INVALID_TOKEN(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403);

    // Field to store the numeric code
    private final int code;

    // Constructor (must be private or package-private)
    ErrorCode(int code) {
        this.code = code;
    }

    // Getter to retrieve the code
    public int getCode() {
        return code;
    }
}
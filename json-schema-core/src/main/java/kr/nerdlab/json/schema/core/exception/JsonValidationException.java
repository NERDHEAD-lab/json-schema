package kr.nerdlab.json.schema.core.exception;

public class JsonValidationException extends Exception {
    public JsonValidationException(String message) {
        super(message);
    }

    public JsonValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

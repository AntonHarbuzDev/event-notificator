package school.sorokin.event_notificator.exception;

public class ConvertToJsonException extends EventBusinessException {
    public ConvertToJsonException(String message) {
        super(message);
    }

    public ConvertToJsonException(String message, Throwable cause) {
        super(message, cause);
    }
}

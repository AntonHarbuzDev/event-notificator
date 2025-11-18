package school.sorokin.event_notificator.exception;

public abstract class EventBusinessException extends RuntimeException {
    EventBusinessException(String message) {
        super(message);
    }

    EventBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

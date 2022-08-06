package wordle.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class IllegalAttemptToShowAnswerException extends RuntimeException {
    public IllegalAttemptToShowAnswerException(String message) {
        super(message);
    }
}

package wordle.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class IllegalPlayException extends RuntimeException {
    public IllegalPlayException(String message) {
        super(message);
    }
}

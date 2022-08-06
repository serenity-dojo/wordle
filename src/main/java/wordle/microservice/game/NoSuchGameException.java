package wordle.microservice.game;

public class NoSuchGameException extends RuntimeException {
    public NoSuchGameException(String message) {
        super(message);
    }
}

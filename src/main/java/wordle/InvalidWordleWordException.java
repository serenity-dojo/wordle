package wordle;

public class InvalidWordleWordException extends RuntimeException {
    public InvalidWordleWordException(String message) {
        super(message);
    }
}

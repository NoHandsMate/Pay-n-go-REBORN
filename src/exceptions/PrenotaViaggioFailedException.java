package exceptions;

public class PrenotaViaggioFailedException extends Exception {
    public PrenotaViaggioFailedException(String errorMessage) {
        super(errorMessage);
    }
}

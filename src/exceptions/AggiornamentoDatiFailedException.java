package exceptions;

public class AggiornamentoDatiFailedException extends Exception {
    public AggiornamentoDatiFailedException(String errorMessage) {
        super(errorMessage);
    }
}

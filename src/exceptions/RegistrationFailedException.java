package exceptions;

public class RegistrationFailedException extends Exception {
    public RegistrationFailedException(String errorMessage) {
        super(errorMessage);
    }
}

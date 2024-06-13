package exceptions;

public class LoginFailedException extends Exception {
    public LoginFailedException(String erroMessage) {
        super(erroMessage);
    }
}

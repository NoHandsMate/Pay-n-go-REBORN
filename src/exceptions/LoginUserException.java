package exceptions;

public class LoginUserException extends Exception {
    public LoginUserException(String erroMessage) {
        super(erroMessage);
    }
}

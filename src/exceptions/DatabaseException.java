package exceptions;

public class DatabaseException extends Exception{
    public DatabaseException(String errorMessage) {
        super(errorMessage);
    }
}

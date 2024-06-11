package exceptions;

public class DatabaseException extends Exception{
    private final boolean visible;

    public DatabaseException(String errorMessage, boolean visible) {
        super(errorMessage);
        this.visible = visible;
    }

    public boolean isVisible() { return visible; }
}

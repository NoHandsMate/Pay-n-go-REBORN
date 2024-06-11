package exceptions;

public class ReportIncassiFailedException extends Exception{
    public ReportIncassiFailedException(String errorMessage) {
        super(errorMessage);
    }
}

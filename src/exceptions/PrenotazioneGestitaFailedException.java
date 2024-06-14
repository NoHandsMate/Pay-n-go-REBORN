package exceptions;

public class PrenotazioneGestitaFailedException extends Exception {
    public PrenotazioneGestitaFailedException(String errorMessage) {
        super(errorMessage);
    }
}

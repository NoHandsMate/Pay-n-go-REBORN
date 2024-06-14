package exceptions;

public class ValutazioneFailedException extends Exception {
    public ValutazioneFailedException(String errorMessage) {
        super(errorMessage);
    }
}

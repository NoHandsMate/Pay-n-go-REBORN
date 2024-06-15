package exceptions;

public class DatabaseException extends Exception{
    private final boolean visible;

    /**
     * DatabaseException è un'eccezione lanciata quando fallisce un'operazione sul database. Tutte le DAO possono
     * lanciare solamente tale eccezione.
     * @param errorMessage un messaggio esplicativo del motivo del sollevamento dell'eccezione.
     * @param visible indica se il messaggio di errore deve essere propagato fino a venir visualizzato all'utente.
     */
    public DatabaseException(String errorMessage, boolean visible) {
        super(errorMessage);
        this.visible = visible;
    }

    /**
     * Indica se il messaggio di errore della DatabaseException deve essere visibile all'utente.
     * @return true se il messaggio deve essere visibile, false altrimenti.
     */
    public boolean isVisible() { return visible; }
}

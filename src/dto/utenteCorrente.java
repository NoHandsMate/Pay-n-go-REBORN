package dto;


public class utenteCorrente {

    private static utenteCorrente uniqueInstance;
    private long idUtenteCorrente;

    private utenteCorrente() {}

    public static utenteCorrente getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new utenteCorrente();
        }
        return uniqueInstance;
    }

    public long getIdUtenteCorrente() {
        return idUtenteCorrente;
    }

    public void setIdUtenteCorrente(long idUtenteCorrente) {
        this.idUtenteCorrente = idUtenteCorrente;
    }
}





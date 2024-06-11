package dto;


public class UtenteCorrente {

    private static UtenteCorrente uniqueInstance;
    private long idUtenteCorrente;
    private String nome;
    private String cognome;


    private UtenteCorrente() {}

    public static UtenteCorrente getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new UtenteCorrente();
        }
        return uniqueInstance;
    }

    public long getIdUtenteCorrente() {
        return idUtenteCorrente;
    }

    public void setIdUtenteCorrente(long idUtenteCorrente) {
        this.idUtenteCorrente = idUtenteCorrente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
}





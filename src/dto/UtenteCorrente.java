package dto;


public class UtenteCorrente {

    private static UtenteCorrente uniqueInstance;
    private long idUtenteCorrente;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String auto;
    private String postiDisp;
    private String contattoTelefonico;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public String getPostiDisp() {
        return postiDisp;
    }

    public void setPostiDisp(String postiDisp) {
        this.postiDisp = postiDisp;
    }

    public String getContattoTelefonico() {
        return contattoTelefonico;
    }

    public void setContattoTelefonico(String contattoTelefonico) {
        this.contattoTelefonico = contattoTelefonico;
    }
}





package entity;

public class EntityUtenteRegistrato {
    private final String nome;
    private final String cognome;
    private final String contattoTelefonico;
    private final String email;
    private final String automobile;
    private final int numPostiDisponibili;

    public EntityUtenteRegistrato() {
        this.nome = "";
        this.cognome = "";
        this.contattoTelefonico = "";
        this.email = "";
        this.automobile = "";
        this.numPostiDisponibili = 0;
    }

    public EntityUtenteRegistrato(String nome, String cognome, String contattoTelefonico, String email, String automobile, int numPostiDisponibili) {
        this.nome = nome;
        this.cognome = cognome;
        this.contattoTelefonico = contattoTelefonico;
        this.email = email;
        this.automobile = automobile;
        this.numPostiDisponibili = numPostiDisponibili;
    }
}

package entity;

import database.UtenteRegistratoDAO;

import java.util.ArrayList;

public class EntityUtenteRegistrato {
    private long id;
    private String nome;
    private String cognome;
    private String contattoTelefonico;
    private String email;
    private String password;
    private String automobile;
    private int postiDisponibili;
    private ArrayList<EntityViaggio> viaggiCondivisi;
    private ArrayList<EntityPrenotazione> prenotazioni;
    private ArrayList<EntityValutazione> valutazioni;


    public EntityUtenteRegistrato() {}

    public EntityUtenteRegistrato(String nome, String cognome,
                                  String contattoTelefonico, String email, String password,
                                  String automobile, int postiDisponibili) {

        this.nome = nome;
        this.cognome = cognome;
        this.contattoTelefonico = contattoTelefonico;
        this.email = email;
        this.automobile = automobile;
        this.postiDisponibili = postiDisponibili;
        /* TODO: per me questo metodo non ha senso di esistere se non va ad effettuare la registazione
            e quindi viene utilizzato in registraUtente */
    }

    /**
     * Costruttore di EntityUtenteRegistrato che ne inizializza l'istanza con gli attributi memorizzati in una DAO.
     * @param utenteRegistratoDAO la UtenteRegistratoDAO da cui inizializzare l'oggetto.
     */
    public EntityUtenteRegistrato(UtenteRegistratoDAO utenteRegistratoDAO) {
        this.id = utenteRegistratoDAO.getIdUtenteRegistrato();
        this.nome = utenteRegistratoDAO.getNome();
        this.cognome = utenteRegistratoDAO.getCognome();
        this.email = utenteRegistratoDAO.getEmail();
        this.password = utenteRegistratoDAO.getPassword();
        this.contattoTelefonico = utenteRegistratoDAO.getContattoTelefonico();
        this.automobile = utenteRegistratoDAO.getAutomobile();
        this.postiDisponibili = utenteRegistratoDAO.getPostiDisponibili();
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getContattoTelefonico() {
        return contattoTelefonico;
    }

    public void setContattoTelefonico(String contattoTelefonico) {
        this.contattoTelefonico = contattoTelefonico;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    protected String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    public String getAutomobile() {
        return automobile;
    }

    public void setAutomobile(String automobile) {
        this.automobile = automobile;
    }

    public int getPostiDisponibili() {
        return postiDisponibili;
    }

    public void setPostiDisponibili(int postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }

    public ArrayList<EntityValutazione> getValutazioni() {
        return valutazioni;
    }

    public void setValutazioni(ArrayList<EntityValutazione> valutazioni) {
        this.valutazioni = valutazioni;
    }

    public ArrayList<EntityPrenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(ArrayList<EntityPrenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    public ArrayList<EntityViaggio> getViaggiCondivisi() {
        return viaggiCondivisi;
    }

    public void setViaggiCondivisi(ArrayList<EntityViaggio> viaggiCondivisi) {
        this.viaggiCondivisi = viaggiCondivisi;
    }
}

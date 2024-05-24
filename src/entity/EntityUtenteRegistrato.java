package entity;

import database.ClienteRegistratoDAO;

import java.util.List;

public class EntityUtenteRegistrato {
    private String nome;
    private String cognome;
    private String contattoTelefonico;
    private String email;
    private String automobile;
    private int numPostiDisponibili;
    private List<EntityViaggio> viaggiCondivisi;
    private List<EntityPrenotazione> prenotazioni;
    private List<EntityValutazione> valutazioni;


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

    public EntityUtenteRegistrato(ClienteRegistratoDAO clienteRegistratoDAO) {
        /* TODO */
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

    public String getAutomobile() {
        return automobile;
    }

    public void setAutomobile(String automobile) {
        this.automobile = automobile;
    }

    public int getNumPostiDisponibili() {
        return numPostiDisponibili;
    }

    public void setNumPostiDisponibili(int numPostiDisponibili) {
        this.numPostiDisponibili = numPostiDisponibili;
    }

    public List<EntityValutazione> getValutazioni() {
        return valutazioni;
    }

    public void setValutazioni(List<EntityValutazione> valutazioni) {
        this.valutazioni = valutazioni;
    }

    public List<EntityPrenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(List<EntityPrenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    public List<EntityViaggio> getViaggiCondivisi() {
        return viaggiCondivisi;
    }

    public void setViaggiCondivisi(List<EntityViaggio> viaggiCondivisi) {
        this.viaggiCondivisi = viaggiCondivisi;
    }
}

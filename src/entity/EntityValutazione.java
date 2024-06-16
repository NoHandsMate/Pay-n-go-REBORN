package entity;

import database.UtenteRegistratoDAO;
import database.ValutazioneDAO;
import exceptions.DatabaseException;

/**
 * Classe del package entity nel modello BCED, essa implementa l'entità valutazione.
 */
public class EntityValutazione {

    /**
     * L'identificativo della valutazione.
     */
    private long id;

    /**
     * Il numero di stelle della valutazione.
     */
    private int numeroStelle;

    /**
     * La descrizione della valutazione.
     */
    private String descrizione;

    /**
     * L'utente a cui si riferisce la valutazione.
     */
    private EntityUtenteRegistrato utenteValutato;

    /**
     * Costruttore di default di EntityValutazione, crea un'entità vuota.
     */
    public EntityValutazione() {}

    /**
     * Costruttore di EntityValutazione che popola l'istanza a partire da ValutazioneDAO.
     * @param valutazioneDAO che permette di popolare l'istanza corrente di EntityValutazione.
     * @throws DatabaseException se si verifica un errore nel caricamento dell'utente valutato.
     */
    public EntityValutazione(ValutazioneDAO valutazioneDAO) throws DatabaseException {
        UtenteRegistratoDAO utenteRegistratoDAO = new UtenteRegistratoDAO(valutazioneDAO.getIdUtente());
        this.id = valutazioneDAO.getIdValutazione();
        this.numeroStelle = valutazioneDAO.getNumeroStelle();
        this.descrizione = valutazioneDAO.getDescrizione();
        this.utenteValutato = new EntityUtenteRegistrato(utenteRegistratoDAO);
    }

    /**
     * Funzione che permette di salvare una valutazione nel database.
     * @throws DatabaseException se si verifica un errore nel salvataggio della valutazione.
     */
    public void salvaInDB() throws DatabaseException {
        ValutazioneDAO valutazioneDAO = new ValutazioneDAO();
        valutazioneDAO.createValutazione(this.numeroStelle, this.descrizione, this.utenteValutato.getId());
    }

    /**
     * Getter dell'identificativo della valutazione.
     * @return l'identificativo della valutazione.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter dell'identificativo della valutazione.
     * @param id il nuovo identificativo della valutazione.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter del numero di stelle della valutazione.
     * @return il numero di stelle della valutazione.
     */
    public int getNumeroStelle() {
        return numeroStelle;
    }

    /**
     * Setter del numero di stelle della valutazione.
     * @param numeroStelle il nuovo numero di stelle della valutazione.
     */
    public void setNumeroStelle(int numeroStelle) {
        this.numeroStelle = numeroStelle;
    }

    /**
     * Getter della descrizione della valutazione.
     * @return la descrizione della valutazione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Setter della descrizione della valutazione.
     * @param descrizione la nuova descrizione della valutazione.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Getter dell'utente a cui si riferisce la valutazione.
     * @return l'utente a cui si riferisce la valutazione.
     */
    public EntityUtenteRegistrato getUtenteValutato() {
        return utenteValutato;
    }

    /**
     * Setter dell'utente a cui si riferisce la valutazione.
     * @param utenteValutato il nuovo utente a cui si riferisce la valutazione.
     */
    public void setUtenteValutato(EntityUtenteRegistrato utenteValutato) {
        this.utenteValutato = utenteValutato;
    }
}

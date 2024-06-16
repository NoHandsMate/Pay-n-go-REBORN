package entity;

import database.*;
import exceptions.*;

/**
 * Classe del package entity nel modello BCED, essa implementa l'entità prenotazione.
 */
public class EntityValutazione {

    /**
     * L'identificativo della prenotazione.
     */
    private long id;

    /**
     * Il numero di stelle della prenotazione.
     */
    private int numeroStelle;

    /**
     * La descrizione della prenotazione.
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumeroStelle() {
        return numeroStelle;
    }

    public void setNumeroStelle(int numeroStelle) {
        this.numeroStelle = numeroStelle;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public EntityUtenteRegistrato getIdUtenteValutato() {
        return utenteValutato;
    }

    public void setIdUtenteValutato(EntityUtenteRegistrato utenteValutato) {
        this.utenteValutato = utenteValutato;
    }
}

package entity;

import database.*;
import exceptions.DatabaseException;

import java.util.ArrayList;

public class EntityPrenotazione {


    private long id;
    private boolean accettata;
    private EntityUtenteRegistrato passeggero;
    private EntityViaggio viaggioPrenotato;


    public EntityPrenotazione() {}

    public EntityPrenotazione(PrenotazioneDAO prenotazioneDAO) {
        this.id = prenotazioneDAO.getIdPrenotazione();
        this.accettata = prenotazioneDAO.isAccettata();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EntityUtenteRegistrato getPasseggero() {
        return passeggero;
    }

    public void setPasseggero(EntityUtenteRegistrato passeggero) {
        this.passeggero = passeggero;
    }


    public boolean isAccettata() {
        return accettata;
    }

    public void setAccettata(boolean accettata) {
        this.accettata = accettata;
    }

    public EntityViaggio getViaggioPrenotato() {
        return viaggioPrenotato;
    }

    public void setViaggioPrenotato(EntityViaggio viaggioPrenotato) {
        this.viaggioPrenotato = viaggioPrenotato;
    }

    public void salvaInDB() throws DatabaseException {
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        prenotazioneDAO.createPrenotazione(this.passeggero.getId(), this.viaggioPrenotato.getId());
    }
}

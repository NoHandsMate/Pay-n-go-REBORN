package entity;

import database.*;

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

    public void creaPrenotazione() {
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
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
}

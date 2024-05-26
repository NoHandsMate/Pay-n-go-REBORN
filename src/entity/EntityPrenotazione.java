package entity;

import database.PrenotazioneDAO;

public class EntityPrenotazione {

    private long id;
    private EntityUtenteRegistrato passeggero;
    private EntityViaggio viaggioPrenotato;
    /* TODO
    *   Aggiustare i costruttori dell'entità per includere l'EntityViaggio viaggioPrenotato */

    public EntityPrenotazione() {}

    public EntityPrenotazione(long id, EntityUtenteRegistrato passeggero) {
        this.id = id;
        this.passeggero = passeggero;
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


}

package entity;

import database.PrenotazioneDAO;
import database.UtenteRegistratoDAO;
import database.ViaggioDAO;
import exceptions.DatabaseException;

/**
 * Classe del package entity nel modello BCED, essa implementa l'entità prenotazione.
 */
public class EntityPrenotazione {

    /**
     * L'identificativo della prenotazione.
     */
    private long id;

    /**
     * Flag che indica se la prenotazione è "In attesa" o "Accettata" (<code>false</code> o <code>true</code>
     * rispettivamente).
     */
    private boolean accettata;

    /**
     * Il passeggero della prenotazione.
     */
    private EntityUtenteRegistrato passeggero;

    /**
     * Il viaggio prenotato.
     */
    private EntityViaggio viaggioPrenotato;

    /**
     * Costruttore di default di EntityPrenotazione, crea un'entità vuota.
     */
    public EntityPrenotazione() {}

    /**
     * Costruttore di EntityPrenotazione che popola l'istanza a partire da PrenotazioneDAO.
     * @param prenotazioneDAO che permette di popolare l'istanza corrente di EntityPrenotazione.
     * @throws DatabaseException se si verifica un errore nel caricamento del passeggero o del viaggio.
     */
    public EntityPrenotazione(PrenotazioneDAO prenotazioneDAO) throws DatabaseException {
        UtenteRegistratoDAO utenteRegistratoDAO = new UtenteRegistratoDAO(prenotazioneDAO.getIdPasseggero());
        ViaggioDAO viaggioDAO = new ViaggioDAO(prenotazioneDAO.getIdViaggioPrenotato());
        this.id = prenotazioneDAO.getIdPrenotazione();
        this.accettata = prenotazioneDAO.isAccettata();
        this.passeggero = new EntityUtenteRegistrato(utenteRegistratoDAO);
        this.viaggioPrenotato = new EntityViaggio(viaggioDAO);
    }

    /**
     * Funzione che permette di salvare una prenotazione nel database.
     * @throws DatabaseException se si verifica un errore nel salvataggio della prenotazione.
     */
    public void salvaInDB() throws DatabaseException {
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        prenotazioneDAO.createPrenotazione(this.passeggero.getId(), this.viaggioPrenotato.getId());
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

}

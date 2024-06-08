package entity;

import database.PrenotazioneDAO;

import java.util.ArrayList;

public class EntityPrenotazione {

    /*
    private long id;
    private EntityUtenteRegistrato passeggero;
    private EntityViaggio viaggioPrenotato;
    TODO: Aggiustare i costruttori dell'entità per includere l'EntityViaggio viaggioPrenotato

    public EntityPrenotazione() {}

    public EntityPrenotazione(long id, EntityUtenteRegistrato passeggero) {
        this.id = id;
        this.passeggero = passeggero;
    }

    public void creaPrenotazione() {
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
    }

    public long getId() {

        TODO: fai esplodere (ma utilizza perché sensato)
        // EntityPrenotazione ha una lista ArrayList<EntityPrenotazione> prenotazioni;
        ArrayList<PrenotazioneDAO> prenotazioniList = new ArrayList<>();
        int idViaggio = 1;
        PrenotazioneDAO.popolaPrenotazioni(prenotazioniList, idViaggio);
        for (PrenotazioneDAO prenotazioneDAO : prenotazioniList) {
            EntityPrenotazione entityPrenotazione = new EntityPrenotazione(prenotazioneDAO);
            if (entityPrenotazione.viaggioPrenotato.getId() == idViaggio) {
                prenotazioni.append(entityPrenotazione);
            }
        }

        this.idDPrenotazioni = prenotazioniList;
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

     */
}

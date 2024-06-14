package entity;

import database.*;
import exceptions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EntityViaggio {
    private long id;
    private String luogoPartenza;
    private String luogoDestinazione;
    private LocalDateTime dataPartenza;
    private LocalDateTime dataArrivo;
    private float contributoSpese;
    private EntityUtenteRegistrato autista;
    private ArrayList<EntityPrenotazione> prenotazioni;

    public EntityViaggio() {}


    public EntityViaggio(ViaggioDAO viaggioDAO) throws DatabaseException {

        UtenteRegistratoDAO utenteRegistratoDAO = new UtenteRegistratoDAO(viaggioDAO.getIdAutista());
        this.id = viaggioDAO.getIdViaggio();
        this.luogoPartenza = viaggioDAO.getLuogoPartenza();
        this.luogoDestinazione = viaggioDAO.getLuogoDestinazione();
        this.dataPartenza = viaggioDAO.getDataPartenza();
        this.dataArrivo = viaggioDAO.getDataArrivo();
        this.contributoSpese = viaggioDAO.getContributoSpese();
        this.autista = new EntityUtenteRegistrato(utenteRegistratoDAO);
    }

    /**
     * Funzione che permette di popolare un viaggio con le prenotazioni associate
     * @throws DatabaseException se il caricamento delle prenotazioni dal database fallisce
     */
    public void popolaPrenotazioni() throws DatabaseException {
        ArrayList<PrenotazioneDAO> listaPrenotazioni = PrenotazioneDAO.getPrenotazioni();
        this.prenotazioni = new ArrayList<>();
        for (PrenotazioneDAO prenotazioneDAO : listaPrenotazioni) {
            if(prenotazioneDAO.getIdPrenotazione() == this.id) {
                EntityPrenotazione prenotazione = new EntityPrenotazione(prenotazioneDAO);
                this.prenotazioni.add(prenotazione);
            }
        }
    }

    public void salvaInDB() throws DatabaseException {
        ViaggioDAO viaggioDAO = new ViaggioDAO();
        viaggioDAO.createViaggio(this.luogoPartenza, this.luogoDestinazione, this.dataPartenza,
                                 this.dataArrivo, this.contributoSpese, this.autista.getId());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLuogoPartenza() {
        return luogoPartenza;
    }

    public void setLuogoPartenza(String luogoPartenza) {
        this.luogoPartenza = luogoPartenza;
    }

    public String getLuogoDestinazione() {
        return luogoDestinazione;
    }

    public void setLuogoDestinazione(String luogoDestinazione) {
        this.luogoDestinazione = luogoDestinazione;
    }

    public LocalDateTime getDataPartenza() {
        return dataPartenza;
    }

    public void setDataPartenza(LocalDateTime dataPartenza) {
        this.dataPartenza = dataPartenza;
    }

    public LocalDateTime getDataArrivo() {
        return dataArrivo;
    }

    public void setDataArrivo(LocalDateTime dataArrivo) {
        this.dataArrivo = dataArrivo;
    }

    public float getContributoSpese() {
        return contributoSpese;
    }

    public void setContributoSpese(float contributoSpese) {
        this.contributoSpese = contributoSpese;
    }

    public EntityUtenteRegistrato getAutista() {
        return autista;
    }

    public void setAutista(EntityUtenteRegistrato autista) {
        this.autista = autista;
    }

    public ArrayList<EntityPrenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(ArrayList<EntityPrenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

}

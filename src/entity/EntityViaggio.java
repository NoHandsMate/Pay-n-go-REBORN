package entity;

import database.*;
import exceptions.DatabaseException;
import exceptions.ReportIncassiFailedException;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Date;
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

    public EntityViaggio(ViaggioDAO viaggioDAO) {

        this.id = viaggioDAO.getIdViaggio();
        this.luogoPartenza = viaggioDAO.getLuogoPartenza();
        this.luogoDestinazione = viaggioDAO.getLuogoDestinazione();
        this.dataPartenza = viaggioDAO.getDataPartenza();
        this.dataArrivo = viaggioDAO.getDataArrivo();
        this.contributoSpese = viaggioDAO.getContributoSpese();
    }

    public void popolaPrenotazioni() throws DatabaseException {
        ArrayList<PrenotazioneDAO> prenotazioni = PrenotazioneDAO.getPrenotazioni();
        this.prenotazioni = new ArrayList<>();
        for (PrenotazioneDAO prenotazioneDAO : prenotazioni) {
            if(prenotazioneDAO.getIdPrenotazione() == this.id) {
                EntityPrenotazione prenotazione = new EntityPrenotazione(prenotazioneDAO);
                this.prenotazioni.add(prenotazione);
            }
        }
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

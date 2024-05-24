package entity;

import java.util.Date;
import java.util.ArrayList;

public class EntityViaggio {

    private String luogoPartenza;
    private String luogoDestinazione;
    private Date dataPartenza;
    private Date oraPartenza;
    private Date dataArrivo;
    private Date oraArrivo;
    private float contributoSpese;
    private EntityUtenteRegistrato autista;
    private ArrayList<EntityPrenotazione> prenotazioni;

    public EntityViaggio() {}

    public EntityViaggio(String luogoPartenza, String luogoDestinazione,
                         Date dataPartenza, Date oraPartenza,
                         Date dataArrivo, Date oraArrivo,
                         float contributoSpese,
                         EntityUtenteRegistrato autista, ArrayList<EntityPrenotazione> prenotazioni) {

        this.luogoPartenza = luogoPartenza;
        this.luogoDestinazione = luogoDestinazione;
        this.dataPartenza = dataPartenza;
        this.oraPartenza = oraPartenza;
        this.dataArrivo = dataArrivo;
        this.oraArrivo = oraArrivo;
        this.contributoSpese = contributoSpese;
        this.autista = autista;
        this.prenotazioni = prenotazioni;
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

    public Date getDataPartenza() {
        return dataPartenza;
    }

    public void setDataPartenza(Date dataPartenza) {
        this.dataPartenza = dataPartenza;
    }

    public Date getOraPartenza() {
        return oraPartenza;
    }

    public void setOraPartenza(Date oraPartenza) {
        this.oraPartenza = oraPartenza;
    }

    public Date getDataArrivo() {
        return dataArrivo;
    }

    public void setDataArrivo(Date dataArrivo) {
        this.dataArrivo = dataArrivo;
    }

    public Date getOraArrivo() {
        return oraArrivo;
    }

    public void setOraArrivo(Date oraArrivo) {
        this.oraArrivo = oraArrivo;
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

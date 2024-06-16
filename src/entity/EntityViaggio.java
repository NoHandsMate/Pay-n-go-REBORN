package entity;

import database.PrenotazioneDAO;
import database.UtenteRegistratoDAO;
import database.ViaggioDAO;
import exceptions.DatabaseException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe del package entity nel modello BCED, essa implementa l'entità viaggio.
 */
public class EntityViaggio {

    /**
     * L'identificativo del viaggio.
     */
    private long id;

    /**
     * Il luogo di partenza del viaggio.
     */
    private String luogoPartenza;

    /**
     * Il luogo di destinazione del viaggio.
     */
    private String luogoDestinazione;

    /**
     * La data e ora di partenza del viaggio.
     */
    private LocalDateTime dataPartenza;

    /**
     * La data e ora di arrivo del viaggio.
     */
    private LocalDateTime dataArrivo;

    /**
     * Il contributo spese del viaggio.
     */
    private float contributoSpese;

    /**
     * L'autista del viaggio.
     */
    private EntityUtenteRegistrato autista;

    /**
     * La lista di prenotazioni del viaggio.
     */
    private ArrayList<EntityPrenotazione> prenotazioni;

    /**
     * Costruttore di default di EntityViaggio, crea un'entità vuota.
     */
    public EntityViaggio() {}

    /**
     * Costruttore di EntityViaggio che popola l'istanza a partire da ViaggioDAO.
     * @param viaggioDAO che permette di popolare l'istanza corrente di EntityViaggio.
     * @throws DatabaseException se si verifica un errore nel caricamento dell'autista del viaggio.
     */
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
     * Funzione che permette di popolare un viaggio con le prenotazioni associate.
     * @throws DatabaseException se si verifica un errore nel caricamento delle prenotazioni del viaggio.
     */
    public void popolaPrenotazioni() throws DatabaseException {
        ArrayList<PrenotazioneDAO> listaPrenotazioni = PrenotazioneDAO.getPrenotazioni();
        this.prenotazioni = new ArrayList<>();
        for (PrenotazioneDAO prenotazioneDAO : listaPrenotazioni) {
            if(prenotazioneDAO.getIdViaggioPrenotato() == this.id) {
                EntityPrenotazione prenotazione = new EntityPrenotazione(prenotazioneDAO);

                if (this.dataPartenza.isBefore(LocalDateTime.now()) && !prenotazione.isAccettata()) {
                    prenotazioneDAO.deletePrenotazione();
                } else {
                    this.prenotazioni.add(prenotazione);
                }
            }
        }
    }

    /**
     * Funzione che permette di salvare un viaggio nel database.
     * @throws DatabaseException se si verifica un errore nel salvataggio del viaggio.
     */
    public void salvaInDB() throws DatabaseException {
        ViaggioDAO viaggioDAO = new ViaggioDAO();
        viaggioDAO.createViaggio(this.luogoPartenza, this.luogoDestinazione, this.dataPartenza,
                                 this.dataArrivo, this.contributoSpese, this.autista.getId());
    }

    /**
     * Getter dell'identificativo del viaggio.
     * @return l'identificativo del viaggio.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter dell'identificativo del viaggio.
     * @param id il nuovo identificativo del viaggio.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter del luogo di partenza del viaggio.
     * @return il luogo di partenza del viaggio.
     */
    public String getLuogoPartenza() {
        return luogoPartenza;
    }

    /**
     * Setter del luogo di partenza del viaggio.
     * @param luogoPartenza il nuovo luogo di partenza del viaggio.
     */
    public void setLuogoPartenza(String luogoPartenza) {
        this.luogoPartenza = luogoPartenza;
    }

    /**
     * Getter del luogo di destinazione del viaggio.
     * @return il luogo di destinazione del viaggio.
     */
    public String getLuogoDestinazione() {
        return luogoDestinazione;
    }

    /**
     * Setter del luogo di destinazione del viaggio.
     * @param luogoDestinazione il nuovo luogo di destinazione del viaggio.
     */
    public void setLuogoDestinazione(String luogoDestinazione) {
        this.luogoDestinazione = luogoDestinazione;
    }

    /**
     * Getter della data e ora di partenza del viaggio.
     * @return la data e ora di partenza del viaggio.
     */
    public LocalDateTime getDataPartenza() {
        return dataPartenza;
    }

    /**
     * Setter della data e ora di partenza del viaggio.
     * @param dataPartenza la nuova data e ora di partenza del viaggio.
     */
    public void setDataPartenza(LocalDateTime dataPartenza) {
        this.dataPartenza = dataPartenza;
    }

    /**
     * Getter della data e ora di arrivo del viaggio.
     * @return la data e ora di arrivo del viaggio.
     */
    public LocalDateTime getDataArrivo() {
        return dataArrivo;
    }

    /**
     * Setter della data e ora di arrivo del viaggio.
     * @param dataArrivo la nuova data e ora di arrivo del viaggio.
     */
    public void setDataArrivo(LocalDateTime dataArrivo) {
        this.dataArrivo = dataArrivo;
    }

    /**
     * Getter del contributo spese del viaggio.
     * @return il contributo spese del viaggio.
     */
    public float getContributoSpese() {
        return contributoSpese;
    }

    /**
     * Setter del contributo spese del viaggio.
     * @param contributoSpese il nuovo contributo spese del viaggio.
     */
    public void setContributoSpese(float contributoSpese) {
        this.contributoSpese = contributoSpese;
    }

    /**
     * Getter dell'autista del viaggio.
     * @return l'autista del viaggio.
     */
    public EntityUtenteRegistrato getAutista() {
        return autista;
    }

    /**
     * Setter dell'autista del viaggio.
     * @param autista il nuovo autista del viaggio.
     */
    public void setAutista(EntityUtenteRegistrato autista) {
        this.autista = autista;
    }

    /**
     * Getter della lista di prenotazioni del viaggio.
     * @return la lista di prenotazioni del viaggio.
     */
    public List<EntityPrenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    /**
     * Setter della lista di prenotazioni del viaggio.
     * @param prenotazioni la nuova lista di prenotazioni del viaggio.
     */
    public void setPrenotazioni(List<EntityPrenotazione> prenotazioni) {
        this.prenotazioni = (ArrayList<EntityPrenotazione>) prenotazioni;
    }

}

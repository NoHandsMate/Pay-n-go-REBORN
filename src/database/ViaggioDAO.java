package database;

import exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Classe del package database nel modello BCED, essa implementa la DAO dei viaggi.
 */
public class ViaggioDAO {

    /**
     * L'identificativo del viaggio.
     */
    private long idViaggio;

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
     * L'identificativo dell'autista del viaggio.
     */
    private long idAutista;

    /**
     * Costante utilizzata da DateTimeFormatter per la conversione da stringa a data simbolica.
     */
    private static final String DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Logger che stampa tutte le query eseguite sul database e gli eventuali messaggi di errore.
     */
    private static final Logger logger = Logger.getLogger("loggerViaggioDAO");

    /**
     * Costruttore di default di ViaggioDAO, crea un'istanza vuota da popolare successivamente.
     */
    public ViaggioDAO() { }

    /**
     * Costruttore di ViaggioDAO che popola l'istanza in base all'id fornito con i dati già memorizzati nel database.
     * @param idViaggio l'identificativo della prenotazione.
     * @throws DatabaseException se non è stato possibile creare un'istanza di ViaggioDAO.
     */
    public ViaggioDAO(long idViaggio) throws DatabaseException {
        if (!caricaDaDB(idViaggio))
            throw new DatabaseException("Errore nella creazione di ViaggioDAO.",false);
        this.idViaggio = idViaggio;
    }

    /**
     * Funzione per impostare tutti i parametri dell'oggetto ViaggioDAO dato e salvare tale istanza nel database.
     * @param luogoPartenza il luogo di partenza del viaggio.
     * @param luogoDestinazione il luogo di destinazione del viaggio.
     * @param dataPartenza la data di partenza del viaggio.
     * @param dataArrivo la data di arrivo del viaggio.
     * @param contributoSpese il contributo spese per la prenotazione del viaggio.
     * @param idAutista l'identificativo dell'autista.
     * @throws DatabaseException se si è verificato un errore nella creazione dell'oggetto ViaggioDAO.
     */
    public void createViaggio(String luogoPartenza,
                                 String luogoDestinazione,
                                 LocalDateTime dataPartenza,
                                 LocalDateTime dataArrivo,
                                 float contributoSpese,
                                 long idAutista) throws DatabaseException {
        if (cercaInDB(luogoPartenza, luogoDestinazione, dataPartenza, dataArrivo, contributoSpese, idAutista) != 0)
            throw new DatabaseException("Esiste già un viaggio identico.",true);
        if (salvaInDB(luogoPartenza, luogoDestinazione, dataPartenza, dataArrivo, contributoSpese, idAutista) == 0)
            throw new DatabaseException("Non è stato aggiunto alcun viaggio al database.",false);

        this.idViaggio = cercaInDB(luogoPartenza, luogoDestinazione, dataPartenza, dataArrivo, contributoSpese, idAutista);
        this.luogoPartenza = luogoPartenza;
        this.luogoDestinazione = luogoDestinazione;
        this.dataPartenza = dataPartenza;
        this.dataArrivo = dataArrivo;
        this.idAutista = idAutista;
    }

    /**
     * Funzione per eliminare un viaggio dal database.
     * @throws DatabaseException se non è stato possibile eliminare il viaggio dal database.
     */
    public void deleteViaggio() throws DatabaseException {
        if (this.eliminaDaDB() == 0)
            throw new DatabaseException("Non è stato trovato un viaggio corrispondente.",true);
    }

    /**
     * Funzione per caricare dal database la lista dei viaggi memorizzati
     * @return la lista di viaggiDAO
     * @throws DatabaseException se si è verificato un errore nel caricamento dal database dei viaggi
     */
    public static List<ViaggioDAO> getViaggi() throws DatabaseException {
        String query = "SELECT * from viaggi";
        logger.info(query);
        ArrayList<ViaggioDAO> viaggiSalvati = new ArrayList<>();
        try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
           while(rs.next()) {
               ViaggioDAO v = new ViaggioDAO();
               v.idViaggio = rs.getLong("idViaggio");
               v.luogoPartenza = rs.getString("luogoPartenza");
               v.luogoDestinazione = rs.getString("luogoDestinazione");
               v.dataPartenza = rs.getObject("dataPartenza", LocalDateTime.class);
               v.dataArrivo = rs.getObject("dataArrivo", LocalDateTime.class);
               v.contributoSpese = rs.getFloat("contributoSpese");
               v.idAutista = rs.getLong("autista");
               viaggiSalvati.add(v);
           }
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante il caricamento dal database: %s", e.getMessage()));
            throw new DatabaseException("Errore nel caricamento di un viaggio dal database.",false);
        }

        return viaggiSalvati;
    }

    /**
     * Funzione privata per caricare i dati di un viaggio dal database.
     * @param idViaggio l'identificativo del viaggio.
     * @return true in caso di successo, false altrimenti.
     * @throws DatabaseException se si è verificato un errore nel caricamento del viaggio dal database.
     */
    private boolean caricaDaDB(long idViaggio) throws DatabaseException{
        String query = String.format("SELECT * from viaggi WHERE (idViaggio = %d);", idViaggio);
        logger.info(query);
        try (ResultSet rs = DBManager.getInstance().selectQuery(query)){
            while (rs.next()) {
                this.luogoPartenza = rs.getString("luogoPartenza");
                this.luogoDestinazione = rs.getString("luogoDestinazione");
                this.dataPartenza = rs.getObject("dataPartenza", LocalDateTime.class);
                this.dataArrivo = rs.getObject("dataArrivo", LocalDateTime.class);
                this.contributoSpese = rs.getFloat("contributoSpese");
                this.idAutista = rs.getLong("autista");
            }
            if (this.luogoPartenza == null && this.luogoDestinazione == null && this.dataPartenza == null &&
                    this.dataArrivo == null && this.contributoSpese == 0f && this.idAutista == 0) {
                return false;
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante il caricamento dal database di un viaggio con id %d.%n%s",
                    idViaggio, e.getMessage()));
            throw new DatabaseException("Errore nel caricamento di un viaggio dal database.",false);
        }
        return true;
    }

    /**
     * Funzione privata per cercare uno specifico viaggio nel database.
     * @param luogoPartenza il luogo di partenza del viaggio.
     * @param luogoDestinazione il luogo di destinazione del viaggio.
     * @param dataPartenza la data di partenza del viaggio.
     * @param dataArrivo la data di arrivo del viaggio.
     * @param contributoSpese il contributo spese per la prenotazione del viaggio.
     * @param idAutista l'identificativo dell'autista.
     * @return l'id del viaggio (positivo) in caso di viaggio trovato, 0 in caso di viaggio non trovato.
     * @throws DatabaseException se si è verificato un errore nella ricerca del viaggio nel database.
     */
    private long cercaInDB(String luogoPartenza,
                           String luogoDestinazione,
                           LocalDateTime dataPartenza,
                           LocalDateTime dataArrivo,
                           float contributoSpese,
                           long idAutista) throws DatabaseException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIMEFORMAT);
        String luogoPartenzaFormatted = luogoPartenza.replace("'", "\\'");
        String luogoDestinazioneFormatted = luogoDestinazione.replace("'", "\\'");
        String dataPartenzaS = dataPartenza.format(dateTimeFormatter);
        String dataArrivoS = dataArrivo.format(dateTimeFormatter);
        String contributoSpeseS = String.format("%.2f", contributoSpese).replace(",", ".");
        String query = String.format("SELECT * FROM viaggi WHERE (luogoPartenza = '%s' AND luogoDestinazione = '%s' " +
                "AND dataPartenza = '%s' AND dataArrivo = '%s' AND contributoSpese LIKE %s AND autista = %d);",
                luogoPartenzaFormatted, luogoDestinazioneFormatted, dataPartenzaS, dataArrivoS, contributoSpeseS,
                idAutista);
        logger.info(query);
        long newIdViaggio;
        try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
            if (!rs.next())
                return 0;
            newIdViaggio = rs.getLong("idViaggio");
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore nella ricerca del viaggio ['%s', '%s', '%s', '%s', %s, %d] nel " +
                            "database.%n%s", luogoDestinazione, luogoDestinazione, dataPartenzaS, dataArrivoS,
                            contributoSpeseS, idAutista, e.getMessage()));
            throw new DatabaseException("Errore nella ricerca di un viaggio nel database.",false);
        }
        return newIdViaggio;
    }

    /**
     * Funzione privata per salvare i dati di un viaggio nel database.
     * @param luogoPartenza il luogo di partenza del viaggio.
     * @param luogoDestinazione il luogo di destinazione del viaggio.
     * @param dataPartenza la data di partenza del viaggio.
     * @param dataArrivo la data di arrivo del viaggio.
     * @param contributoSpese il contributo spese per la prenotazione del viaggio.
     * @param idAutista l'identificativo dell'autista.
     * @return il numero di righe inserite nel database.
     * @throws DatabaseException se si è verificato un errore nel salvataggio del viaggio nel database.
     */
    private int salvaInDB(String luogoPartenza,
                          String luogoDestinazione,
                          LocalDateTime dataPartenza,
                          LocalDateTime dataArrivo,
                          float contributoSpese,
                          long idAutista) throws DatabaseException{
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIMEFORMAT);
        String luogoPartenzaFormatted = luogoPartenza.replace("'", "\\'");
        String luogoDestinazioneFormatted = luogoDestinazione.replace("'", "\\'");
        String dataPartenzaS = dataPartenza.format(dateTimeFormatter);
        String dataArrivoS = dataArrivo.format(dateTimeFormatter);
        String contributoSpeseS = String.format("%.2f", contributoSpese).replace(",", ".");
        String query = String.format("INSERT INTO viaggi (luogoPartenza, luogoDestinazione, dataPartenza, " +
                "dataArrivo, contributoSpese, autista) VALUES ('%s', '%s', '%s', '%s', %s, %d);", luogoPartenzaFormatted,
                luogoDestinazioneFormatted, dataPartenzaS, dataArrivoS, contributoSpeseS, idAutista);
        logger.info(query);
        int rs;
        try {
            rs = DBManager.getInstance().executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante l'inserimento del viaggio ['%s', '%s', '%s', '%s', %s, %d] " +
                            "nel database.%n%s", luogoPartenza, luogoDestinazione, dataPartenzaS, dataArrivoS,
                            contributoSpeseS, idAutista, e.getMessage()));
            throw new DatabaseException("Errore nel salvataggio del viaggio nel database.",false);
        }
        return rs;
    }

    /**
     * Funzione privata per eliminare un viaggio dal database.
     * @return il numero di righe eliminate dal database.
     * @throws DatabaseException se non è stato possibile eliminare il viaggio dal database.
     */
    private int eliminaDaDB() throws  DatabaseException {
        String query = String.format("DELETE FROM viaggi WHERE (idViaggio = %d);", this.idViaggio);
        logger.info(query);
        int rs;
        try {
            rs = DBManager.getInstance().executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIMEFORMAT);
            String dataPartenzaS = this.dataPartenza.format(dateTimeFormatter);
            String dataArrivoS = this.dataArrivo.format(dateTimeFormatter);
            String contributoSpeseS = String.format("%.2f", this.contributoSpese).replace(",", ".");
            logger.warning(String.format("Errore durante l'eliminazione del viaggio [%d, '%s', '%s', '%s', '%s', %s, %d] " +
                            "dal database.%n%s", this.idViaggio, this.luogoPartenza, this.luogoDestinazione, dataPartenzaS,
                            dataArrivoS, contributoSpeseS, this.idAutista, e.getMessage()));
            throw new DatabaseException("Errore nell'eliminazione del viaggio dal database.",false);
        }
        return rs;
    }

    /**
     * Getter dell'identificativo del viaggio.
     * @return l'identificativo del viaggio.
     */
    public long getIdViaggio() {
        return idViaggio;
    }

    /**
     * Getter del luogo di partenza del viaggio.
     * @return il luogo di partenza del viaggio.
     */
    public String getLuogoPartenza() {
        return luogoPartenza;
    }

    /**
     * Getter del luogo di destinazione del viaggio.
     * @return il luogo di destinazione del viaggio.
     */
    public String getLuogoDestinazione() {
        return luogoDestinazione;
    }

    /**
     * Getter della data e ora di partenza del viaggio.
     * @return la data e ora di partenza del viaggio.
     */
    public LocalDateTime getDataPartenza() {
        return dataPartenza;
    }

    /**
     * Getter della data e ora di arrivo del viaggio.
     * @return la data e ora di arrivo del viaggio.
     */
    public LocalDateTime getDataArrivo() {
        return dataArrivo;
    }

    /**
     * Getter del contributo spese del viaggio.
     * @return il contributo spese del viaggio.
     */
    public float getContributoSpese() {
        return contributoSpese;
    }

    /**
     * Getter dell'identificativo dell'autista del viaggio.
     * @return l'identificativo dell'autista del viaggio.
     */
    public long getIdAutista() {
        return idAutista;
    }
}
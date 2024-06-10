package database;

import exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class ViaggioDAO {

    private long idViaggio;
    private String luogoPartenza;
    private String luogoDestinazione;
    private LocalDateTime dataPartenza;
    private LocalDateTime dataArrivo;
    private float contributoSpese;
    private long idAutista;

    private static final String DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

    private final Logger logger = Logger.getLogger("loggerViaggioDAO");

    /**
     * Costruttore di default di ViaggioDAO, crea un'istanza vuota da popolare successivamente.
     */
    public ViaggioDAO() { }

    /**
     * Costruttore di ViaggioDAO che popola l'istanza in base all'id fornito con i dati già memorizzati nel database.
     * @param id l'identificativo della prenotazione.
     * @throws DatabaseException se non è stato possibile creare un'istanza di ViaggioDAO.
     */
    public ViaggioDAO(int id) throws DatabaseException {
        if (!caricaDaDB(id))
            throw new DatabaseException("Errore nella creazione di ViaggioDAO.");
        this.idViaggio = id;
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
                                 int idAutista) throws DatabaseException {
        if (cercaInDB(luogoPartenza, luogoDestinazione, dataPartenza, dataArrivo, contributoSpese, idAutista) != 0)
            throw new DatabaseException("Esiste già un viaggio identico nel database.");
        if (salvaInDB(luogoPartenza, luogoDestinazione, dataPartenza, dataArrivo, contributoSpese, idAutista) == 0)
            throw new DatabaseException("Non è stato aggiunto alcun viaggio al database.");

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
            throw new DatabaseException("Non è stato trovato un viaggio corrispondente nel database.");
    }

    /**
     * Funzione privata per caricare i dati di un viaggio dal database.
     * @param id l'identificativo del viaggio.
     * @return true in caso di successo, false altrimenti.
     * @throws DatabaseException se si è verificato un errore nel caricamento del viaggio dal database.
     */
    private boolean caricaDaDB(long id) throws DatabaseException{
        String query = String.format("SELECT * from viaggi WHERE (idViaggio = %d);", id);
        logger.info(query);
        try (ResultSet rs = DBManager.selectQuery(query)){
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
                    id, e.getMessage()));
            throw new DatabaseException("Errore nel caricamento di un viaggio dal database.");
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
                           int idAutista) throws DatabaseException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIMEFORMAT);
        String dataPartenzaS = dataPartenza.format(dateTimeFormatter);
        String dataArrivoS = dataArrivo.format(dateTimeFormatter);
        String contributoSpeseS = String.format("%.2f", contributoSpese).replace(",", ".");
        String query = String.format("SELECT * FROM viaggi WHERE (luogoPartenza = '%s' AND luogoDestinazione = '%s' " +
                "AND dataPartenza = '%s' AND dataArrivo = '%s' AND contributoSpese LIKE %s AND autista = %d);",
                luogoPartenza, luogoDestinazione, dataPartenzaS, dataArrivoS, contributoSpeseS, idAutista);
        logger.info(query);
        long idViaggio;
        try (ResultSet rs = DBManager.selectQuery(query)) {
            if (!rs.next())
                return 0;
            idViaggio = rs.getLong("idViaggio");
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore nella ricerca del viaggio ['%s', '%s', '%s', '%s', %s, %d] nel " +
                            "database.%n%s", luogoDestinazione, luogoDestinazione, dataPartenzaS, dataArrivoS,
                            contributoSpeseS, idAutista, e.getMessage()));
            throw new DatabaseException("Errore nella ricerca di un viaggio nel database.");
        }
        return idViaggio;
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
     * @throws DatabaseException se non è stato possibile salvare il viaggio nel database.
     */
    private int salvaInDB(String luogoPartenza,
                          String luogoDestinazione,
                          LocalDateTime dataPartenza,
                          LocalDateTime dataArrivo,
                          float contributoSpese,
                          long idAutista) throws DatabaseException{
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIMEFORMAT);
        String dataPartenzaS = dataPartenza.format(dateTimeFormatter);
        String dataArrivoS = dataArrivo.format(dateTimeFormatter);
        String contributoSpeseS = String.format("%.2f", contributoSpese).replace(",", ".");
        String query = String.format("INSERT INTO viaggi (luogoPartenza, luogoDestinazione, dataPartenza, " +
                "dataArrivo, contributoSpese, autista) VALUES ('%s', '%s', '%s', '%s', %s, %d);", luogoPartenza,
                luogoDestinazione, dataPartenzaS, dataArrivoS, contributoSpeseS, idAutista);
        logger.info(query);
        int rs;
        try {
            rs = DBManager.executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante l'inserimento del viaggio ['%s', '%s', '%s', '%s', %s, %d] " +
                            "nel database.%n%s", luogoPartenza, luogoDestinazione, dataPartenzaS, dataArrivoS,
                            contributoSpeseS, idAutista, e.getMessage()));
            throw new DatabaseException("Errore nel salvataggio del viaggio nel database.");
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
            rs = DBManager.executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIMEFORMAT);
            String dataPartenzaS = this.dataPartenza.format(dateTimeFormatter);
            String dataArrivoS = this.dataArrivo.format(dateTimeFormatter);
            String contributoSpeseS = String.format("%.2f", this.contributoSpese).replace(",", ".");
            logger.warning(String.format("Errore durante l'eliminazione del viaggio [%d, '%s', '%s', '%s', '%s', %s, %d] " +
                            "dal database.%n%s", this.idViaggio, this.luogoPartenza, this.luogoDestinazione, dataPartenzaS,
                            dataArrivoS, contributoSpeseS, this.idAutista, e.getMessage()));
            throw new DatabaseException("Errore nell'eliminazione del viaggio dal database.");
        }
        return rs;
    }

}
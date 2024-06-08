package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class ViaggioDAO {

    private long id;
    private String luogoPartenza;
    private String luogoDestinazione;
    private LocalDateTime dataPartenza;
    private LocalDateTime dataArrivo;
    private float contributoSpese;
    private long idAutista;

    /* TODO: rimuovi logging delle query eseguite dopo il testing. */
    private final Logger logger = Logger.getLogger("loggerViaggioDAO");

    /**
     * Costruttore di default di ViaggioDAO, crea un'istanza vuota da popolare successivamente.
     */
    public ViaggioDAO() { }

    /**
     * Costruttore di ViaggioDAO che popola l'istanza in base all'id fornito con i dati già memorizzati nel database.
     * @param id l'identificativo della prenotazione.
     */

    /* TODO: E se non troviamo la prenotazione? Va creata l'eccezione custom apposita oppure sfruttiamo sempre il
        paradigma costruttore vuoto -> caricaDaDB(id) */
    public ViaggioDAO(int id) {
        boolean res = caricaDaDB(id);
        this.id = id;
    }

    /**
     * Funzione per impostare tutti i parametri dell'oggetto ViaggioDAO dato e salvare tale istanza nel database.
     * @param id l'identificativo del viaggio.
     * @param luogoPartenza il luogo di partenza del viaggio.
     * @param luogoDestinazione il luogo di destinazione del viaggio.
     * @param dataPartenza la data di partenza del viaggio.
     * @param dataArrivo la data di arrivo del viaggio.
     * @param contributoSpese il contributo spese per la prenotazione del viaggio.
     * @param idAutista l'identificativo dell'autista.
     * @return true in vaso di successo (in tal caso l'oggetto sarà stato valorizzato con i parametri dati), false
     * altrimenti (l'oggetto non sarà valorizzato).
     */
    public boolean createViaggio(long id, String luogoPartenza, String luogoDestinazione, LocalDateTime dataPartenza,
                                 LocalDateTime dataArrivo, float contributoSpese, int idAutista) {
        boolean res = salvaInDB(id, luogoPartenza, luogoDestinazione, dataPartenza, dataArrivo, contributoSpese,
                idAutista);

        if (!res)
            return false;

        this.id = id;
        this.luogoPartenza = luogoPartenza;
        this.luogoDestinazione = luogoDestinazione;
        this.dataPartenza = dataPartenza;
        this.dataArrivo = dataArrivo;
        this.idAutista = idAutista;
        return true;
    }

    /**
     * Funzione per eliminare un viaggio dal database.
     * @return true in caso di successo, false altrimenti.
     */
    public boolean deleteViaggio() {
        return this.eliminaDaDB();
    }

    /**
     * Funzione privata per caricare i dati di un viaggio dal database.
     * @param id l'identificativo del viaggio.
     * @return true in caso di successo, false altrimenti.
     */
    private boolean caricaDaDB(long id) {
        String query = String.format("SELECT * from viaggi WHERE (id = %d);", id);
        logger.info(query);
        try {
            /* TODO: debug di ResultSet */
            ResultSet rs = DBManager.selectQuery(query);
            while (rs.next()) {
                this.luogoPartenza = rs.getString("luogoPartenza");
                this.luogoDestinazione = rs.getString("luogoDestinazione");
                this.dataPartenza = rs.getObject("dataPartenza", LocalDateTime.class);
                this.dataArrivo = rs.getObject("dataArrivo", LocalDateTime.class);
                this.contributoSpese = rs.getFloat("contributoSpese");
                this.idAutista = rs.getLong("autista");
            }
            if (this.luogoPartenza == null && this.luogoDestinazione == null && this.dataPartenza == null &&
                    this.dataArrivo == null && this.contributoSpese == 0f & this.idAutista == 0) {
                return false;
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.info(String.format("Errore durante il caricamento dal database di un viaggio con id %d.%n%s",
                    id, e.getMessage()));
            return false;
        }
        return true;
    }

    /**
     * Funzione privata per salvare i dati di un viaggio nel database.
     * @param id l'identificativo del viaggio.
     * @param luogoPartenza il luogo di partenza del viaggio.
     * @param luogoDestinazione il luogo di destinazione del viaggio.
     * @param dataPartenza la data di partenza del viaggio.
     * @param dataArrivo la data di arrivo del viaggio.
     * @param contributoSpese il contributo spese per la prenotazione del viaggio.
     * @param idAutista l'identificativo dell'autista.
     * @return true in caso di successo, false altrimenti.
     */
    private boolean salvaInDB(long id, String luogoPartenza, String luogoDestinazione, LocalDateTime dataPartenza,
                              LocalDateTime dataArrivo, float contributoSpese, long idAutista) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dataPartenzaS = dataPartenza.format(dateTimeFormatter);
        String dataArrivoS = dataArrivo.format(dateTimeFormatter);
        String query = String.format("INSERT INTO viaggi (idViaggio, luogoPartenza, luogoDestinazione, dataPartenza, " +
                "dataArrivo, contributoSpese, autista ) VALUES (%d, '%s', '%s', '%s', '%s', %f, %d);",
                id, luogoPartenza, luogoDestinazione, dataPartenzaS, dataArrivoS, contributoSpese, idAutista);
        logger.info(query);
        try {
            /* TODO: questo int rs, dato che non lo usiamo è inutile (?) Ci sono modi per usarli. */
            int rs = DBManager.executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.info(String.format("Errore durante l'inserimento del viaggio [%d, '%s', '%s', '%s', '%s', %f, %d] " +
                            "nel database.\n%s", id, luogoPartenza, luogoDestinazione, dataPartenzaS, dataArrivoS,
                            contributoSpese, idAutista, e.getMessage()));
            return false;
        }
        return true;
    }

    /**
     * Funzione privata per eliminare un viaggio dal database.
     * @return true in caso di successo, false altrimenti.
     */
    private boolean eliminaDaDB() {
        String query = String.format("DELETE FROM prenotazioni WHERE (idPrenotazione = %d);", this.id);
        logger.info(query);
        try {
            /* TODO: questo int rs, dato che non lo usiamo è inutile (?) Ci sono modi per usarli. */
            int rs = DBManager.executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dataPartenzaS = this.dataPartenza.format(dateTimeFormatter);
            String dataArrivoS = this.dataArrivo.format(dateTimeFormatter);
            logger.info(String.format("Errore durante l'eliminazione del viaggio [%d, '%s', '%s', '%s', '%s', %f, %d] " +
                            "dal database.\n%s", this.id, this.luogoPartenza, this.luogoDestinazione, dataPartenzaS,
                            dataArrivoS, this.contributoSpese, this.idAutista, e.getMessage()));
            return false;
        }
        return true;
    }

}
package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class PrenotazioneDAO {

    private long id;
    private long idPasseggero;
    private long idViaggioPrenotato;

    /* TODO: rimuovi logging delle query eseguite dopo il testing. */
    private final Logger logger = Logger.getLogger("loggerPrenotazioneDAO");

    /**
     * Costruttore di default di PrenotazioneDAO, crea un'istanza vuota da popolare successivamente.
      */
    public PrenotazioneDAO() {
        super();
    }

    /**
     * Costruttore di PrenotazioneDAO che popola l'istanza in base all'id fornito con i dati già memorizzati nel
     * database.
     * @param id l'identificativo della prenotazione.
      */

    /* TODO: E se non troviamo la prenotazione? Va creata l'eccezione custom apposita oppure sfruttiamo sempre il
        paradigma costruttore vuoto -> caricaDaDB(id) */
    public PrenotazioneDAO(int id) {
        boolean res = caricaDaDB(id);
        this.id = id;
    }

    /**
     * Funzione per impostare tutti i parametri dell'oggetto prenotazione dato e salvare tale istanza nel database.
     * @param id l'identificativo della prenotazione.
     * @param idPasseggero l'identificativo dell'utente passeggero.
     * @param idViaggioPrenotato l'identificativo del viaggio prenotato.
     * @return true in vaso di successo (in tal caso l'oggetto sarà stato valorizzato con i parametri dati), false
     * altrimenti (l'oggetto non sarà valorizzato).
     */
    public boolean createPrenotazione(long id, long idPasseggero, long idViaggioPrenotato) {
        boolean res = salvaInDB(id, idPasseggero, idViaggioPrenotato);

        if (!res)
            return false;

        this.id = id;
        this.idPasseggero = idPasseggero;
        this.idViaggioPrenotato = idViaggioPrenotato;
        return true;
    }

    /**
     * Funzione per eliminare una prenotazione dal database.
     * @return true in caso di successo, false altrimenti.
     */
    public boolean deletePrenotazione() {
        return this.eliminaDaDB();
    }

    /**
     * Funzione privata per caricare i dati di una prenotazione dal database.
     * @param id l'identificativo della prenotazione.
     * @return true in caso di successo, false altrimenti.
     */
    private boolean caricaDaDB(long id) {
        String query = String.format("SELECT * from prenotazioni WHERE (id = %d);", id);
        logger.info(query);
        try {
            /* TODO: debug di ResultSet */
            ResultSet rs = DBManager.selectQuery(query);
            while (rs.next()) {
                this.idPasseggero = rs.getLong("passeggero");
                this.idViaggioPrenotato = rs.getLong("viaggioPrenotato");
            }
            if (this.idPasseggero == 0 && this.idViaggioPrenotato == 0) {
                return false;
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.info(String.format("Errore durante il caricamento dal database di una prenotazione con id %d.", id));
            return false;
        }
        return true;
    }

    /**
     * Funzione privata per salvare i dati di una prenotazione nel database.
     * @param id identificativo della prenotazione.
     * @param idPasseggero identificativo del passeggero.
     * @param idViaggioPrenotato identificativo del viaggio.
     * @return true in caso di successo, false altrimenti.
     */
    private boolean salvaInDB(long id, long idPasseggero, long idViaggioPrenotato) {
        String query = String.format("INSERT INTO prenotazioni (idPrenotazione, passeggero, viaggioPrenotato) VALUES " +
                "(%d, %d, %d);", id, idPasseggero, idViaggioPrenotato);
        logger.info(query);
        try {
            /* TODO: questo int rs, dato che non lo usiamo è inutile (?) Ci sono modi per usarli. */
            int rs = DBManager.executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.info(String.format("Errore durante l'inserimento di [%d, %d, %d] nel database.",
                    id, idPasseggero, idViaggioPrenotato));
            return false;
        }
        return true;
    }

    /**
     * Funzione privata per eliminare una prenotazione dal database.
     * @return true in caso di successo, false altrimenti.
     */
    private boolean eliminaDaDB() {
        String query = String.format("DELETE FROM prenotazioni WHERE (idPrenotazione = %d);", this.id);
        logger.info(query);
        try {
            /* TODO: questo int rs, dato che non lo usiamo è inutile (?) Ci sono modi per usarli. */
            int rs = DBManager.executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.info(String.format("Errore durante l'eliminazione di [%d, %d, %d] dal database.",
                    this.id, this.idPasseggero, this.idViaggioPrenotato));
            return false;
        }
        return true;
    }

}
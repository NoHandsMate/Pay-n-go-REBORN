package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ValutazioneDAO {

    private long id;
    private int numeroStelle;
    private String descrizione;
    private long idUtente;

    /* TODO: rimuovi logging delle query eseguite dopo il testing. */
    private final Logger logger = Logger.getLogger("loggerValutazioneDAO");

    /**
     * Costruttore di default di ValutazioneDAO, crea un'istanza vuota da popolare successivamente.
     */
    public ValutazioneDAO() { }

    /**
     * Costruttore di ValutazioneDAO che popola l'istanza in base all'id fornito con i dati già memorizzati nel
     * database.
     * @param id l'identificativo della prenotazione.
     */

    /* TODO: E se non troviamo la prenotazione? Va creata l'eccezione custom apposita oppure sfruttiamo sempre il
        paradigma costruttore vuoto -> caricaDaDB(id) */
    public ValutazioneDAO(int id) {
        boolean res = caricaDaDB(id);
        this.id = id;
    }

    /**
     * Funzione per impostare tutti i parametri dell'oggetto ValutazioneDAO dato e salvare tale istanza nel database.
     * @param id l'identificativo della valutazione.
     * @param numeroStelle il numero di stelle della valutazione.
     * @param descrizione la descrizione della valutazione.
     * @param idUtente l'identificativo dell'utente valutato.
     * @return true in vaso di successo (in tal caso l'oggetto sarà stato valorizzato con i parametri dati), false
     * altrimenti (l'oggetto non sarà valorizzato).
     */
    public boolean createValutazione(long id, int numeroStelle, String descrizione, long idUtente) {
        boolean res = salvaInDB(id, numeroStelle, descrizione, idUtente);

        if (!res)
            return false;

        this.id = id;
        this.numeroStelle = numeroStelle;
        this.descrizione = descrizione;
        this.idUtente = idUtente;
        return true;
    }

    /**
     * Funzione per eliminare una valutazione dal database.
     * @return true in caso di successo, false altrimenti.
     */
    public boolean deleteValutazione() {
        return this.eliminaDaDB();
    }

    /**
     * Funzione privata per caricare i dati di una valutazione dal database.
     * @param id l'identificativo della valutazione.
     * @return true in caso di successo, false altrimenti.
     */
    private boolean caricaDaDB(long id) {
        String query = String.format("SELECT * from valutazioni WHERE (id = %d);", id);
        logger.info(query);
        try {
            /* TODO: debug di ResultSet */
            ResultSet rs = DBManager.selectQuery(query);
            while (rs.next()) {
                this.numeroStelle = rs.getInt("numeroStelle");
                this.descrizione = rs.getString("descrizione");
                this.idUtente = rs.getLong("utente");
            }
            if (this.numeroStelle == 0 && this.descrizione == null && this.idUtente == 0) {
                return false;
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.info(String.format("Errore durante il caricamento dal database di una valutazione con id %d.\n%s",
                    id, e.getMessage()));
            return false;
        }
        return true;
    }

    /**
     * Funzione privata per salvare i dati di una valutazione nel database.
     * @param id l'identificativo della valutazione.
     * @param numeroStelle il numero di stelle della valutazione.
     * @param descrizione la descrizione della valutazione.
     * @param idUtente l'identificativo dell'utente valutato.
     * @return true in caso di successo, false altrimenti.
     */
    private boolean salvaInDB(long id, int numeroStelle, String descrizione, long idUtente) {
        String query = String.format("INSERT INTO valutazioni (idValutazioni, passeggero, viaggioPrenotato) VALUES " +
                "(%d, %d, '%s', %d);", id, numeroStelle, descrizione, idUtente);
        logger.info(query);
        try {
            /* TODO: questo int rs, dato che non lo usiamo è inutile (?) Ci sono modi per usarli. */
            int rs = DBManager.executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.info(String.format("Errore durante l'inserimento della valutazione [%d, %d, '%s', %d] nel " +
                            "database.\n%s", id, numeroStelle, descrizione, idUtente, e.getMessage()));
            return false;
        }
        return true;
    }

    /**
     * Funzione privata per eliminare una valutazione dal database.
     * @return true in caso di successo, false altrimenti.
     */
    private boolean eliminaDaDB() {
        String query = String.format("DELETE FROM valutazioni WHERE (idValutazioni = %d);", this.id);
        logger.info(query);
        try {
            /* TODO: questo int rs, dato che non lo usiamo è inutile (?) Ci sono modi per usarli. */
            int rs = DBManager.executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.info(String.format("Errore durante l'eliminazione della valutazione [%d, %d, '%s', %d] " +
                    "dal database.\n%s", this.id, this.numeroStelle, this.descrizione, this.idUtente, e.getMessage()));
            return false;
        }
        return true;
    }
}

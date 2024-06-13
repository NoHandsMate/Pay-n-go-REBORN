package database;

import exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ValutazioneDAO {

    private long idValutazione;
    private int numeroStelle;
    private String descrizione;
    private long idUtente;

    /* TODO: rimuovi logging delle query eseguite dopo il testing. */
    private static final Logger logger = Logger.getLogger("loggerValutazioneDAO");

    /**
     * Costruttore di default di ValutazioneDAO, crea un'istanza vuota da popolare successivamente.
     */
    public ValutazioneDAO() { }

    /**
     * Costruttore di ValutazioneDAO che popola l'istanza in base all'id fornito con i dati già memorizzati nel
     * database.
     * @param idValutazione l'identificativo della valutazione.
     */
    public ValutazioneDAO(long idValutazione) throws DatabaseException {
        if (!caricaDaDB(idValutazione))
            throw new DatabaseException("Errore nella creazione di PrenotazioneDAO.",false);
        this.idValutazione = idValutazione;
    }

    /**
     * Funzione per impostare tutti i parametri dell'oggetto ValutazioneDAO dato e salvare tale istanza nel database.
     * @param numeroStelle il numero di stelle della valutazione.
     * @param descrizione la descrizione della valutazione.
     * @param idUtente l'identificativo dell'utente valutato.
     * @return true in vaso di successo (in tal caso l'oggetto sarà stato valorizzato con i parametri dati), false
     * altrimenti (l'oggetto non sarà valorizzato).
     * @throws DatabaseException se si è verificato un errore nella creazione dell'oggetto ValutazioneDAO.
     */
    public boolean createValutazione(int numeroStelle, String descrizione, long idUtente) throws DatabaseException{

        if (cercaInDB(numeroStelle, descrizione,idUtente) != 0)
            throw new DatabaseException("Esiste già una valutazione identica.",true);
        if (salvaInDB(numeroStelle, descrizione,idUtente) == 0)
            throw new DatabaseException("Non è stata aggiunta alcuna valutazione al database.",false);

        this.idValutazione = cercaInDB(numeroStelle,descrizione,idUtente);
        this.numeroStelle = numeroStelle;
        this.descrizione = descrizione;
        this.idUtente = idUtente;
        return true;
    }

    /**
     * Funzione per eliminare una valutazione dal database.
     * @throws DatabaseException se non è stato possibile eliminare la prenotazione dal database.
     */
    public void deletePrenotazione() throws DatabaseException{
        if (this.eliminaDaDB() == 0)
            throw new DatabaseException("Non è stata trovata una valutazione corrispondente.",false);
    }

    /**
     * Funzione per prelevare tutte le valutazioni dal database.
     * @throws DatabaseException se non è stato possibile selezionare tutte le valutazioni dal database.
     */
    public static ArrayList<ValutazioneDAO> getValutazioni() throws DatabaseException {
        String query = "SELECT * from valutazioni";
        ArrayList<ValutazioneDAO> valutazioni = new ArrayList<>();
        try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
            while (rs.next()) {
                ValutazioneDAO v = new ValutazioneDAO();
                v.numeroStelle = rs.getInt("numeroStelle");
                v.descrizione = rs.getString("descrizione");
                v.idUtente = rs.getLong("utente");
                valutazioni.add(v);
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning("Errore durante il caricamento dal database delle valutazioni");
            throw new DatabaseException("Errore nel caricamento delle valutazioni dal database.", false);
        }
        return valutazioni;
    }
    /**
     * Funzione privata per caricare i dati di una valutazione dal database.
     * @param id l'identificativo della valutazione.
     * @return true in caso di successo, false altrimenti.
     */
    private boolean caricaDaDB(long id) throws DatabaseException {
        String query = String.format("SELECT * from valutazioni WHERE (id = %d);", id);
        logger.info(query);
            /* TODO: debug di ResultSet */
            try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
                while (rs.next()) {
                    this.numeroStelle = rs.getInt("numeroStelle");
                    this.descrizione = rs.getString("descrizione");
                    this.idUtente = rs.getLong("utente");
                }
            if (this.numeroStelle == 0 && this.descrizione == null && this.idUtente == 0) {
                return false;
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante il caricamento dal database di una valutazione con " +
                    "idValutazione %d.%n%s", idValutazione, e.getMessage()));
            throw new DatabaseException("Errore nel caricamento di una valutazione dal database.", false);
        }
        return true;
    }
    /**
     * Funzione privata per cercare una specifico valutazione nel database.
     * @param numeroStelle il numero di stelle relative alla valutazione
     * @param descrizione il contenuto della valutazione
     * @return l'id della valutazione (positivo) in caso di valutazione trovata, 0 in caso di valutazione non trovato.
     * @throws DatabaseException se si è verificato un errore nella ricerca della valutazione nel database.
     */

    private long cercaInDB(int numeroStelle, String descrizione, long idUtente)
            throws DatabaseException {
        String query = String.format("SELECT * FROM valutazioni WHERE (idValutazione = %d AND numeroStelle = %d " +
                        "AND descrizione = '%s' AND idUtente = %d);",this.idValutazione,numeroStelle, descrizione, idUtente);
        logger.info(query);
        long newIdValutazione;
        try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
            if (!rs.next())
                return 0;
            newIdValutazione = rs.getLong("idValutazione");
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore nella ricerca della valutazione [%d,'%s',%d] nel database.%n%s",
                    numeroStelle, descrizione, idUtente, e.getMessage()));
            throw new DatabaseException("Errore nella ricerca di una prenotazione nel database.",false);
        }
        return newIdValutazione;
    }

    /**
     * Funzione privata per salvare i dati di una valutazione nel database.
     * @param numeroStelle il numero di stelle della valutazione.
     * @param descrizione la descrizione della valutazione.
     * @param idUtente l'identificativo dell'utente valutato.
     * @return true in caso di successo, false altrimenti.
     */
    private int salvaInDB(int numeroStelle, String descrizione, long idUtente) throws DatabaseException{
        String query = String.format("INSERT INTO valutazioni (numeroStelle, descrizione, idUtente) VALUES " +
                "(%d, '%s', %d);",numeroStelle, descrizione, idUtente);
        logger.info(query);
        int rs;
        try {
            rs = DBManager.getInstance().executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante l'inserimento della valutazione [%d, '%s' ,%d] nel database.%n%s",
                    numeroStelle, descrizione, idUtente, e.getMessage()));
            throw new DatabaseException("Errore nel salvataggio della prenotazione nel database.",false);
        }
        return rs;
    }

    /**
     * Funzione privata per eliminare una valutazione dal database.
     * @return true in caso di successo, false altrimenti.
     */
    private int eliminaDaDB() throws DatabaseException {
        String query = String.format("DELETE FROM valutazioni WHERE (idValutazioni = %d);", this.idValutazione);
        logger.info(query);
        int rs;
        try {
            rs = DBManager.getInstance().executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante l'eliminazione della valutazione [%d, '%s', %d] dal database.%n%s",
                    this.numeroStelle, this.descrizione, this.idUtente, e.getMessage()));
            throw new DatabaseException("Errore nell'eliminazione della valutazione dal database.",false);
        }
        return rs;
    }

    public long getIdValutazione() {
        return idValutazione;
    }

    public int getNumeroStelle() {
        return numeroStelle;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public long getIdUtente() {
        return idUtente;
    }
}

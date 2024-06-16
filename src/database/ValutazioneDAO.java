package database;

import exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Classe del package database nel modello BCED, essa implementa la DAO delle valutazioni.
 */
public class ValutazioneDAO {

    /**
     * L'identificativo della valutazione.
     */
    private long idValutazione;

    /**
     * Il numero di stelle della valutazione.
     */
    private int numeroStelle;

    /**
     * La descrizione della valutazione.
     */
    private String descrizione;

    /**
     * L'identificativo dell'utente a cui si riferisce la valutazione.
     */
    private long idUtente;

    /**
     * Logger che stampa tutte le query eseguite sul database e gli eventuali messaggi di errore.
     */
    private static final Logger logger = Logger.getLogger("loggerValutazioneDAO");

    /**
     * Costruttore di default di ValutazioneDAO, crea un'istanza vuota da popolare successivamente.
     */
    public ValutazioneDAO() { }

    /**
     * Costruttore di ValutazioneDAO che popola l'istanza in base all'id fornito con i dati già memorizzati nel
     * database.
     * @param idValutazione l'identificativo della valutazione.
     * @throws DatabaseException se non è stato possibile creare un'istanza di ValutazioneDAO.
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
     * @throws DatabaseException se si è verificato un errore nella creazione dell'oggetto ValutazioneDAO.
     */
    public void createValutazione(int numeroStelle,
                                  String descrizione,
                                  long idUtente) throws DatabaseException{
        if (cercaInDB(numeroStelle, descrizione, idUtente) != 0)
            throw new DatabaseException("Esiste già una valutazione identica.",true);
        if (salvaInDB(numeroStelle, descrizione, idUtente) == 0)
            throw new DatabaseException("Non è stata aggiunta alcuna valutazione al database.",false);

        this.idValutazione = cercaInDB(numeroStelle,descrizione,idUtente);
        this.numeroStelle = numeroStelle;
        this.descrizione = descrizione;
        this.idUtente = idUtente;
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
     * @return la lista di tutte le valutazioni del database.
     * @throws DatabaseException se non è stato possibile selezionare tutte le valutazioni dal database.
     */
    public static List<ValutazioneDAO> getValutazioni() throws DatabaseException {
        String query = "SELECT * from valutazioni";
        ArrayList<ValutazioneDAO> valutazioni = new ArrayList<>();
        try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
            while (rs.next()) {
                ValutazioneDAO v = new ValutazioneDAO();
                v.idValutazione = rs.getLong("idValutazione");
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
     * @throws DatabaseException se si è verificato un errore nel caricamento della valutazione dal database.
     */
    private boolean caricaDaDB(long id) throws DatabaseException {
        String query = String.format("SELECT * from valutazioni WHERE (id = %d);", id);
        logger.info(query);
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
        String descrizioneFormatted = descrizione.replace("'", "\\'");
        String query = String.format("SELECT * FROM valutazioni WHERE (idValutazione = %d AND numeroStelle = %d " +
                        "AND descrizione = '%s' AND utente = %d);", this.idValutazione, numeroStelle,
                descrizioneFormatted, idUtente);
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
     * @return il numero di righe inserite nel database.
     * @throws DatabaseException se si è verificato un errore nel salvataggio della valutazione nel database.
     */
    private int salvaInDB(int numeroStelle,
                          String descrizione,
                          long idUtente) throws DatabaseException{
        String descrizioneFormatted = descrizione.replace("'", "\\'");
        String query = String.format("INSERT INTO valutazioni (numeroStelle, descrizione, utente) VALUES " +
                "(%d, '%s', %d);",numeroStelle, descrizioneFormatted, idUtente);
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
     * @return il numero di righe eliminate nel database.
     * @throws DatabaseException se non è stato possibile eliminare la valutazione dal database.
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

    /**
     * Getter dell'identificativo della valutazione.
     * @return l'identificativo della valutazione.
     */
    public long getIdValutazione() {
        return idValutazione;
    }

    /**
     * Getter del numero di stelle della valutazione.
     * @return il numero di stelle della valutazione.
     */
    public int getNumeroStelle() {
        return numeroStelle;
    }

    /**
     * Getter della descrizione della valutazione.
     * @return la descrizione della valutazione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Getter dell'identificativo dell'utente a cui si riferisce la valutazione.
     * @return l'identificativo dell'utente a cui si riferisce la valutazione.
     */
    public long getIdUtente() {
        return idUtente;
    }
}

package database;

import exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class PrenotazioneDAO {

    private long idPrenotazione;
    private long idPasseggero;
    private long idViaggioPrenotato;
    private boolean accettata;

    private static final Logger logger = Logger.getLogger("loggerPrenotazioneDAO");

    /**
     * Costruttore di default di PrenotazioneDAO, crea un'istanza vuota da popolare successivamente.
      */
    public PrenotazioneDAO() { }

    /**
     * Costruttore di PrenotazioneDAO che popola l'istanza in base all'id fornito con i dati già memorizzati nel
     *      * database.
     * @param idPrenotazione l'identificativo della prenotazione.
     * @throws DatabaseException se non è stato possibile creare un'istanza di PrenotazioneDAO.
     */
    public PrenotazioneDAO(long idPrenotazione) throws DatabaseException {
        if (!caricaDaDB(idPrenotazione))
            throw new DatabaseException("Errore nella creazione di PrenotazioneDAO.",false);
        this.idPrenotazione = idPrenotazione;
    }

    /**
     * Funzione per impostare tutti i parametri dell'oggetto PrenotazioneDAO dato e salvare tale istanza nel database.
     * @param idPasseggero l'identificativo dell'utente passeggero.
     * @param idViaggioPrenotato l'identificativo del viaggio prenotato.
     * @return true in vaso di successo (in tal caso l'oggetto sarà stato valorizzato con i parametri dati), false
     * altrimenti (l'oggetto non sarà valorizzato).
     * @throws DatabaseException se si è verificato un errore nella creazione dell'oggetto PrenotazioneDAO.
     */
    public void createPrenotazione(long idPasseggero,
                                      long idViaggioPrenotato) throws DatabaseException {
        if (cercaInDB(idPasseggero, idViaggioPrenotato) != 0)
            throw new DatabaseException("Esiste già una prenotazione identica.",true);
        if (salvaInDB(idPasseggero, idViaggioPrenotato) == 0)
            throw new DatabaseException("Non è stata aggiunta alcuna prenotazione al database.",false);

        this.idPrenotazione = cercaInDB(idPasseggero, idViaggioPrenotato);
        this.idPasseggero = idPasseggero;
        this.idViaggioPrenotato = idViaggioPrenotato;
        this.accettata = false;
    }

    /**
     * Funzione per aggiornare i dati relativi a una prenotazione nel database.
     * @param accettata lo stato della prenotazione
     * @throws DatabaseException se non è stato possibile aggiornare la prenotazione nel database.
     */
    public void updatePrenotazione(boolean accettata) throws DatabaseException {
        if (aggiornaInDB(accettata)==0)
            throw new DatabaseException("Non è stata aggiornata alcuna prenotazione.", false);

        this.accettata = accettata;
    }

    /**
     * Funzione per eliminare una prenotazione dal database.
     * @throws DatabaseException se non è stato possibile eliminare la prenotazione dal database.
     */
    public void deletePrenotazione() throws DatabaseException{
        if (this.eliminaDaDB() == 0)
            throw new DatabaseException("Non è stata trovata una prenotazione corrispondente.",true);
    }

    /**
     * Funzione per prelevare tutte le prenotazioni dal database.
     * @throws DatabaseException se non è stato possibile selezionare tutte le prenotazioni dal database.
     */
    public static ArrayList<PrenotazioneDAO> getPrenotazioni() throws DatabaseException {
        String query = "SELECT * from prenotazioni";
        ArrayList<PrenotazioneDAO> prenotazioni = new ArrayList<>();
        try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
            while (rs.next()) {
                PrenotazioneDAO p = new PrenotazioneDAO();
                p.idPrenotazione = rs.getLong("idPrenotazione");
                p.accettata = rs.getBoolean("accettata");
                p.idViaggioPrenotato = rs.getLong("viaggioPrenotato");
                p.idPasseggero = rs.getLong("passeggero");
                prenotazioni.add(p);
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning("Errore durante il caricamento dal database delle prenotazioni");
            throw new DatabaseException("Errore nel caricamento delle prenotazioni dal database.", false);
        }
        return prenotazioni;
    }

    /**
     * Funzione privata per caricare i dati di una prenotazione dal database.
     * @param id l'identificativo della prenotazione.
     * @return true in caso di successo, false altrimenti.
     * @throws DatabaseException se si è verificato un errore nel caricamento della prenotazione dal database.
     */
    private boolean caricaDaDB(long id) throws DatabaseException {
        String query = String.format("SELECT * from prenotazioni WHERE (idPrenotazione = %d);", id);
        logger.info(query);
        try (ResultSet rs = DBManager.getInstance().selectQuery(query)){
            while (rs.next()) {
                this.idPasseggero = rs.getLong("passeggero");
                this.idViaggioPrenotato = rs.getLong("viaggioPrenotato");
                this.accettata = rs.getBoolean("accettata");
            }
            if (this.idPasseggero == 0 && this.idViaggioPrenotato == 0) {
                return false;
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante il caricamento dal database di una prenotazione con id %d.%n%s",
                    id, e.getMessage()));
            throw new DatabaseException("Errore nel caricamento di una prenotazione dal database.",false);
        }
        return true;
    }

    /**
     * Funzione privata per cercare una specifica prenotazione nel database.
     * @param idPasseggero l'identificativo dell'utente passeggero.
     * @param idViaggioPrenotato l'identificativo del viaggio prenotato.
     * @return l'id della prenotazione (positivo) in caso di viaggio trovato, 0 in caso di viaggio non trovato.
     * @throws DatabaseException se si è verificato un errore nella ricerca della prenotazione nel database.
     */
    private long cercaInDB(long idPasseggero,
                           long idViaggioPrenotato) throws DatabaseException {
        String query = String.format("SELECT * FROM prenotazioni WHERE (passeggero = %d AND viaggioPrenotato = %d);",
                idPasseggero, idViaggioPrenotato);
        logger.info(query);
        long newIdPrenotazione;
        try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
            if (!rs.next())
                return 0;
            newIdPrenotazione = rs.getLong("idPrenotazione");
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore nella ricerca della prenotazione [%d, %d] nel database.%n%s",
                    idPasseggero, idViaggioPrenotato, e.getMessage()));
            throw new DatabaseException("Errore nella ricerca di una prenotazione nel database.",false);
        }
        return newIdPrenotazione;
    }

    /**
     * Funzione privata per salvare i dati di una prenotazione nel database.
     * @param idPasseggero l'identificativo del passeggero.
     * @param idViaggioPrenotato l'identificativo del viaggio.
     * @return il numero di righe inserite nel database.
     * @throws DatabaseException se non è stato possibile salvare la prenotazione nel database.
     */
    private int salvaInDB(long idPasseggero, long idViaggioPrenotato) throws DatabaseException {
        String query = String.format("INSERT INTO prenotazioni (passeggero, viaggioPrenotato) VALUES (%d, %d);",
                idPasseggero, idViaggioPrenotato);
        logger.info(query);
        int rs;
        try {
            rs = DBManager.getInstance().executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante l'inserimento della prenotazione [%d, %d] nel database.%n%s",
                    idPasseggero, idViaggioPrenotato, e.getMessage()));
            throw new DatabaseException("Errore nel salvataggio della prenotazione nel database.",false);
        }
        return rs;
    }

    /**
     *
     * @param accettata lo stato della prenotazione
     * @throws DatabaseException se non è stato possibile aggiornare la prenotazione nel database.
     */
    private int aggiornaInDB(boolean accettata) throws DatabaseException {

        String query = String.format("UPDATE prenotazioni SET (accettata = %b WHERE (idPrenotazione = %d);",
                accettata,this.idPrenotazione);
        logger.info(query);
        int rs;
        try {
            rs = DBManager.getInstance().executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante l'aggiornamento dello stato della prenotazione " +
                            "%d nel database.%n%s",this.idPrenotazione, e.getMessage()));
            throw new DatabaseException("Errore nell'aggiornamento dello stato della prenotazione nel database.", false);
        }
        return rs;
    }

    /**
     * Funzione privata per eliminare una prenotazione dal database.
     * @return il numero di righe eliminate dal database.
     * @throws DatabaseException se non è stato possibile eliminare la prenotazione dal database.
     */
    private int eliminaDaDB() throws DatabaseException {
        String query = String.format("DELETE FROM prenotazioni WHERE (idPrenotazione = %d);", this.idPrenotazione);
        logger.info(query);
        int rs;
        try {
            rs = DBManager.getInstance().executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante l'eliminazione della prenotazione [%d, %d, %d] dal database.%n%s",
                    this.idPrenotazione, this.idPasseggero, this.idViaggioPrenotato, e.getMessage()));
            throw new DatabaseException("Errore nell'eliminazione della prenotazione dal database.",false);
        }
        return rs;
    }

    public long getIdPrenotazione() {
        return idPrenotazione;
    }

    public long getIdPasseggero() {
        return idPasseggero;
    }

    public long getIdViaggioPrenotato() {
        return idViaggioPrenotato;
    }

    public boolean isAccettata() {
        return accettata;
    }
}
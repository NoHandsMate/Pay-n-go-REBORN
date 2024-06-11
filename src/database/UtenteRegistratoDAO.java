package database;


import exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class UtenteRegistratoDAO {

    private long idUtenteRegistrato;
    private String nome;
    private String cognome;
    private String contattoTelefonico;
    private String email;
    private String automobile;
    private int postiDisponibili;
    private String password;

    private final Logger logger = Logger.getLogger("loggerUtenteRegistratoDAO");

    /**
     * Costruttore di default di UtenteRegistratoDAO, crea un'istanza vuota da popolare successivamente.
     */
    public UtenteRegistratoDAO() { }

    /**
     * Costruttore di UtenteRegistratoDAO che popola l'istanza in base all'id fornito con i dati già memorizzati nel
     * database.
     * @param idUtenteRegistrato l'identificativo dell'utente registrato.
     * @throws DatabaseException se non è stato possibile creare un'istanza di UtenteRegistratoDAO.
     */
    public UtenteRegistratoDAO(int idUtenteRegistrato) throws DatabaseException {
        if (!caricaDaDB(idUtenteRegistrato))
            throw new DatabaseException("Errore nella creazione di UtenteRegistratoDAO.");
        this.idUtenteRegistrato = idUtenteRegistrato;
    }

    /**
     * Funzione per impostare tutti i parametri dell'oggetto UtenteRegistratoDAO dato e salvare tale istanza nel
     * database.
     * @param nome il nome dell'utente registrato.
     * @param cognome il cognome dell'utente registrato.
     * @param contattoTelefonico il contatto telefonico dell'utente registrato.
     * @param email l'email dell'utente registrato.
     * @param automobile l'automobile dell'utente registrato.
     * @param postiDisponibili il numero di posti disponibili nell'automobile dell'utente registrato.
     * @param password la password (bcrypt) dell'utente registrato.
     * @throws DatabaseException se si è verificato un errore nella creazione dell'oggetto UtenteRegistratoDAO.
     */
    public void createUtenteRegistrato(String nome,
                                       String cognome,
                                       String contattoTelefonico,
                                       String email,
                                       String automobile,
                                       int postiDisponibili,
                                       String password) throws DatabaseException {
        if (cercaInDB(email) != 0)
            throw new DatabaseException("Esiste già un utente associato alla mail data nel database.");
        if (salvaInDB(nome, cognome, contattoTelefonico, email, automobile, postiDisponibili, password) == 0)
            throw new DatabaseException("Non è stato aggiunto alcun utente registrato al database.");

        this.idUtenteRegistrato = cercaInDB(email);
        this.nome = nome;
        this.cognome = cognome;
        this.contattoTelefonico = contattoTelefonico;
        this.email = email;
        this.automobile = automobile;
        this.postiDisponibili = postiDisponibili;
        this.password = password;
    }

    /**
     * Funzione per aggiornare i dati relativi a un utente registrato dal database.
     * @param nome il nome dell'utente registrato.
     * @param cognome il cognome dell'utente registrato.
     * @param contattoTelefonico il contatto telefonico dell'utente registrato.
     * @param email l'email dell'utente registrato.
     * @param automobile l'automobile dell'utente registrato.
     * @param postiDisponibili il numero di posti disponibili nell'automobile dell'utente registrato.
     * @param password la password (bcrypt) dell'utente registrato.
     * @return il numero di righe modificate nel database.
     * @throws DatabaseException se non è stato possibile aggiornare l'utente registrato dal database.
     */

    public int updateUtenteRegistrato(String nome,
                                       String cognome,
                                       String contattoTelefonico,
                                       String email,
                                       String automobile,
                                       int postiDisponibili,
                                       String password) throws DatabaseException {
        String query = String.format("UPDATE utentiregistrati SET (nome = '%s', cognome = '%s', " +
                        "contattoTelefonico = '%s', email = '%s', automobile = '%s', postiDisponibili = %d, " +
                        "password = '%s') WHERE (idUtenteRegistrato = %d);", nome, cognome, contattoTelefonico, email,
                        automobile, postiDisponibili, password,this.idUtenteRegistrato);
        logger.info(query);
        int rs;
        try {
            rs = DBManager.getInstance().executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante l'aggiornamento dell'utente registrato " +
                            "['%s', '%s', '%s', '%s', '%s', %d, '%s'] nel database.%n%s", nome, cognome,
                    contattoTelefonico, email, automobile, postiDisponibili, password, e.getMessage()));
            throw new DatabaseException("Errore nell'aggiornamento dell'utente registrato nel database.");
        }
        return rs;

    }

    /**
     * Funzione per eliminare un utente registrato dal database.
     * @throws DatabaseException se non è stato possibile eliminare l'utente registrato dal database.
     */
    public void deleteUtenteRegistrato() throws DatabaseException {
        if (this.eliminaDaDB() == 0)
            throw new DatabaseException("Non è stato trovato un utente registrato corrispondente nel database.");
    }

    /**
     * Funzione per cercare un utente registrato data una mail e una password
     * @param email l'email da utilizzare per la ricerca
     * @param password la password da utilizzare per la ricerca
     * @throws DatabaseException se si è verificato un errore nella ricerca
     */
    public void readUtenteRegistrato(String email, String password) throws DatabaseException {
        String query = String.format("SELECT * from utentiregistrati WHERE (email = '%s' AND password = '%s')", email, password);
        logger.info(query);

        try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
            while (rs.next()) {
                this.idUtenteRegistrato = rs.getInt("idUtenteRegistrato");
                this.nome = rs.getString("nome");
                this.cognome = rs.getString("cognome");
                this.contattoTelefonico = rs.getString("contattoTelefonico");
                this.email = email;
                this.automobile = rs.getString("automobile");
                this.postiDisponibili = rs.getInt("postiDisponibili");
                this.password = password;
            }
            if (this.nome == null && this.cognome == null && this.contattoTelefonico == null && this.email == null &&
                    this.automobile == null && this.postiDisponibili == 0 && this.password == null) {
                throw new DatabaseException("Non è stato possibile trovare l'utente nel database.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante il caricamento dal database di un utente registrato con " +
                    "idUtenteRegistrato %d.%n%s", idUtenteRegistrato, e.getMessage()));
            throw new DatabaseException("Errore nel caricamento di un utente registrato dal database.");
        }
    }

    /**
     * Funzione privata per caricare i dati di un utente registrato dal database.
     * @param idUtenteRegistrato l'identificativo dell'utente registrato.
     * @return true in caso di successo, false altrimenti.
     * @throws DatabaseException se si è verificato un errore nel caricamento dell'utente registrato dal database.
     */
    private boolean caricaDaDB(long idUtenteRegistrato) throws DatabaseException {
        String query = String.format("SELECT * from utentiregistrati WHERE (idUtenteRegistrato = %d);",
                idUtenteRegistrato);
        logger.info(query);
        try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
            while (rs.next()) {
                this.nome = rs.getString("nome");
                this.cognome = rs.getString("cognome");
                this.contattoTelefonico = rs.getString("contattoTelefonico");
                this.email = rs.getString("email");
                this.automobile = rs.getString("automobile");
                this.postiDisponibili = rs.getInt("postiDisponibili");
                this.password = rs.getString("password");
            }
            if (this.nome == null && this.cognome == null && this.contattoTelefonico == null && this.email == null &&
                    this.automobile == null && this.postiDisponibili == 0 && this.password == null) {
                return false;
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante il caricamento dal database di un utente registrato con " +
                    "idUtenteRegistrato %d.%n%s", idUtenteRegistrato, e.getMessage()));
            throw new DatabaseException("Errore nel caricamento di un utente registrato dal database.");
        }
        return true;
    }

    /**
     * Funzione privata per cercare uno specifico utente registrato nel database.
     * @param email l'email associata all'utente registrato.
     * @return l'id del viaggio (positivo) in caso di viaggio trovato, 0 in caso di viaggio non trovato.
     * @throws DatabaseException se si è verificato un errore nella ricerca del viaggio nel database.
     */
    private long cercaInDB(String email) throws DatabaseException {
        String query = String.format("SELECT * FROM utentiregistrati WHERE (email = '%s');", email);
        logger.info(query);
        long newIdUtenteRegistrato;
        try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
            if (!rs.next())
                return 0;
            newIdUtenteRegistrato = rs.getLong("idUtenteRegistrato");
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore nella ricerca dell'utente associato alla mail '%s' nel database.%n%s",
                    email, e.getMessage()));
            throw new DatabaseException("Errore nella ricerca di un utente registrato nel database.");
        }
        return newIdUtenteRegistrato;
    }

    /**
     * Funzione privata per salvare i dati di un utente registrato nel database.
     * @param nome il nome dell'utente registrato.
     * @param cognome il cognome dell'utente registrato.
     * @param contattoTelefonico il contatto telefonico dell'utente registrato.
     * @param email l'email dell'utente registrato.
     * @param automobile l'automobile dell'utente registrato.
     * @param postiDisponibili il numero di posti disponibili nell'automobile dell'utente registrato.
     * @param password la password (bcrypt) dell'utente registrato.
     * @return il numero di righe inserite nel database.
     * @throws DatabaseException se non è stato possibile salvare l'utente registrato nel database.
     */
    private int salvaInDB(String nome,
                              String cognome,
                              String contattoTelefonico,
                              String email,
                              String automobile,
                              int postiDisponibili,
                              String password) throws DatabaseException {
        String query;
        query = String.format("INSERT INTO utentiregistrati (nome, cognome, contattoTelefonico, " +
                        "email, automobile, postiDisponibili, password) VALUES " +
                        "('%s', '%s', '%s', '%s', '%s', %d, '%s');", nome, cognome, contattoTelefonico, email,
                        automobile, postiDisponibili, password);

        logger.info(query);
        int rs;
        try {
            rs = DBManager.getInstance().executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante l'inserimento dell'utente registrato " +
                            "['%s', '%s', '%s', '%s', '%s', %d, '%s'] nel database.%n%s", nome, cognome,
                            contattoTelefonico, email, automobile, postiDisponibili, password, e.getMessage()));
            throw new DatabaseException("Errore nel salvataggio dell'utente registrato nel database.");
        }
        return rs;
    }

    /**
     * Funzione privata per eliminare un utente registrato dal database.
     * @return il numero di righe eliminate dal database.
     * @throws DatabaseException se non è stato possibile eliminare l'utente registrato dal database.
     */
    private int eliminaDaDB() throws DatabaseException {
        String query = String.format("DELETE FROM utentiregistrati WHERE (idUtenteRegistrato = %d);", this.idUtenteRegistrato);
        logger.info(query);
        int rs;
        try {
            rs = DBManager.getInstance().executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            if (this.automobile != null || this.postiDisponibili != 0) {
                logger.info(String.format("Errore durante l'eliminazione dell'utente registrato " +
                                "[%d, '%s', '%s', '%s', '%s', '%s', %d, '%s'] dal database.%n%s", this.idUtenteRegistrato, this.nome,
                                this.cognome, this.contattoTelefonico, this.email, this.automobile,
                                this.postiDisponibili, this.password, e.getMessage()));
            } else {
                logger.info(String.format("Errore durante l'eliminazione dell'utente registrato " +
                                "[%d, '%s', '%s', '%s', '%s', '%s'] dal database.%n%s", this.idUtenteRegistrato, this.nome,
                                this.cognome, this.contattoTelefonico, this.email, this.password, e.getMessage()));
            }
            throw new DatabaseException("Errore nell'eliminazione dell'utente registrato dal database.");
        }
        return rs;
    }

}





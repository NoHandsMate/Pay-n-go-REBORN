package database;

import exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Classe del package database nel modello BCED, essa implementa la DAO degli utenti registrati.
 */
public class UtenteRegistratoDAO {

    /**
     * L'identificativo del'utente registrato.
     */
    private long idUtenteRegistrato;

    /**
     * Il nome dell'utente registrato.
     */
    private String nome;

    /**
     * Il cognome dell'utente registrato.
     */
    private String cognome;

    /**
     * Il contatto telefonico dell'utente registrato.
     */
    private String contattoTelefonico;

    /**
     * L'indirizzo email dell'utente registrato.
     */
    private String email;

    /**
     * L'automobile dell'utente registrato.
     */
    private String automobile;

    /**
     * Il numero di posti disponibili nell'automobile dell'utente registrato.
     */
    private int postiDisponibili;

    /**
     * La password dell'utente registrato.
     */
    private String password;

    /**
     * Logger che stampa tutte le query eseguite sul database e gli eventuali messaggi di errore.
     */
    private static final Logger logger = Logger.getLogger("loggerUtenteRegistratoDAO");

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
    public UtenteRegistratoDAO(long idUtenteRegistrato) throws DatabaseException {
        if (!caricaDaDB(idUtenteRegistrato))
            throw new DatabaseException("Errore nella creazione di UtenteRegistratoDAO.", false);
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
            throw new DatabaseException("Esiste già un utente associato alla mail data.", true);
        if (salvaInDB(nome, cognome, contattoTelefonico, email, automobile, postiDisponibili, password) == 0)
            throw new DatabaseException("Non è stato aggiunto alcun utente registrato al database.", false);

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
     * Funzione per aggiornare i dati relativi a un utente registrato nel database.
     * @param nome il nome dell'utente registrato.
     * @param cognome il cognome dell'utente registrato.
     * @param contattoTelefonico il contatto telefonico dell'utente registrato.
     * @param email l'email dell'utente registrato.
     * @param automobile l'automobile dell'utente registrato.
     * @param postiDisponibili il numero di posti disponibili nell'automobile dell'utente registrato.
     * @param password la password (bcrypt) dell'utente registrato.
     * @throws DatabaseException se non è stato possibile aggiornare l'utente registrato nel database.
     */
    public void updateUtenteRegistrato(String nome,
                                       String cognome,
                                       String contattoTelefonico,
                                       String email,
                                       String automobile,
                                       int postiDisponibili,
                                       String password) throws DatabaseException {
        if (aggiornaInDB(nome, cognome, contattoTelefonico, email, automobile, postiDisponibili, password) == 0)
            throw new DatabaseException("Non è stato aggiornato alcun utente registrato.", false);

        this.nome = nome;
        this.cognome = cognome;
        this.contattoTelefonico = contattoTelefonico;
        this.email = email;
        this.automobile = automobile;
        this.postiDisponibili = postiDisponibili;
        this.password = password;
    }

    /**
     * Funzione per eliminare un utente registrato dal database.
     * @throws DatabaseException se non è stato possibile eliminare l'utente registrato dal database.
     */
    public void deleteUtenteRegistrato() throws DatabaseException {
        if (this.eliminaDaDB() == 0)
            throw new DatabaseException("Non è stato trovato un utente registrato corrispondente.", true);
    }

    /**
     * Funzione per prelevare tutti gli utenti registrati dal database.
     * @return la lista di tutti gli utenti del database.
     * @throws DatabaseException se non è stato possibile selezionare tutti gli utenti registrati dal database.
     */
    public static List<UtenteRegistratoDAO> getUtentiRegistrati() throws DatabaseException {
        String query = "SELECT * from utentiregistrati";
        ArrayList<UtenteRegistratoDAO> utentiRegistrati = new ArrayList<>();
        try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
            while (rs.next()) {
                UtenteRegistratoDAO u = new UtenteRegistratoDAO();
                u.idUtenteRegistrato = rs.getLong("idUtenteRegistrato");
                u.nome = rs.getString("nome");
                u.cognome = rs.getString("cognome");
                u.contattoTelefonico = rs.getString("contattoTelefonico");
                u.email = rs.getString("email");
                u.automobile = rs.getString("automobile");
                u.postiDisponibili = rs.getInt("postiDisponibili");
                u.password = rs.getString("password");
                utentiRegistrati.add(u);
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning("Errore durante il caricamento dal database degli utenti registrati.");
            throw new DatabaseException("Errore nel caricamento degli utenti registrati dal database.", false);
        }
        return utentiRegistrati;
    }

    /**
     * Funzione per cercare un utente registrato data una mail e una password
     * @param email l'email da utilizzare per la ricerca
     * @param password la password da utilizzare per la ricerca
     * @throws DatabaseException se si è verificato un errore nella ricerca
     */
    public void readUtenteRegistrato(String email,
                                     String password) throws DatabaseException {
        String passwordFormatted = password.replace("'", "\\'");
        String query = String.format("SELECT * from utentiregistrati WHERE (email = '%s' AND password = '%s')", email,
                passwordFormatted);
        logger.info(query);

        try (ResultSet rs = DBManager.getInstance().selectQuery(query)) {
            while (rs.next()) {
                this.idUtenteRegistrato = rs.getLong("idUtenteRegistrato");
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
                throw new DatabaseException("Non è stato possibile trovare l'utente nel database.", false);
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante il caricamento dal database di un utente registrato con " +
                    "idUtenteRegistrato %d.%n%s", idUtenteRegistrato, e.getMessage()));
            throw new DatabaseException("Errore nel caricamento di un utente registrato dal database.", false);
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
            throw new DatabaseException("Errore nel caricamento di un utente registrato dal database.", false);
        }
        return true;
    }

    /**
     * Funzione privata per cercare uno specifico utente registrato nel database.
     * @param email l'email associata all'utente registrato.
     * @return l'id dell'utente registrato (positivo) in caso di utente registrato trovato, 0 in caso di utente
     * registrato non trovato.
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
            throw new DatabaseException("Errore nella ricerca di un utente registrato nel database.", false);
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
        String nomeFormatted = nome.replace("'", "\\'");
        String cognomeFormatted = cognome.replace("'", "\\'");
        String automobileFormatted = automobile.replace("'", "\\'");
        String passwordFormatted = password.replace("'", "\\'");
        String query = String.format("INSERT INTO utentiregistrati (nome, cognome, contattoTelefonico, " +
                        "email, automobile, postiDisponibili, password) VALUES " +
                        "('%s', '%s', '%s', '%s', '%s', %d, '%s');", nomeFormatted, cognomeFormatted,
                        contattoTelefonico, email, automobileFormatted, postiDisponibili, passwordFormatted);

        logger.info(query);
        int rs;
        try {
            rs = DBManager.getInstance().executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante l'inserimento dell'utente registrato " +
                            "['%s', '%s', '%s', '%s', '%s', %d, '%s'] nel database.%n%s", nome, cognome,
                            contattoTelefonico, email, automobile, postiDisponibili, password, e.getMessage()));
            throw new DatabaseException("Errore nel salvataggio dell'utente registrato nel database.", false);
        }
        return rs;
    }

    /**
     *
     * @param nome il nome dell'utente registrato.
     * @param cognome il cognome dell'utente registrato.
     * @param contattoTelefonico il contatto telefonico dell'utente registrato.
     * @param email l'email dell'utente registrato.
     * @param automobile l'automobile dell'utente registrato.
     * @param postiDisponibili il numero di posti disponibili nell'automobile dell'utente registrato.
     * @param password la password (bcrypt) dell'utente registrato.
     * @return il numero di righe modificate nel database.
     * @throws DatabaseException se non è stato possibile aggiornare l'utente registrato nel database.
     */
    private int aggiornaInDB(String nome,
                             String cognome,
                             String contattoTelefonico,
                             String email,
                             String automobile,
                             int postiDisponibili,
                             String password) throws DatabaseException {
        String nomeFormatted = nome.replace("'", "\\'");
        String cognomeFormatted = cognome.replace("'", "\\'");
        String automobileFormatted = automobile.replace("'", "\\'");
        String passwordFormatted = password.replace("'", "\\'");
        String query = String.format("UPDATE utentiregistrati SET nome = '%s', cognome = '%s', " +
                        "contattoTelefonico = '%s', email = '%s', automobile = '%s', postiDisponibili = %d, " +
                        "password = '%s' WHERE (idUtenteRegistrato = %d);", nomeFormatted, cognomeFormatted,
                        contattoTelefonico, email, automobileFormatted, postiDisponibili, passwordFormatted,
                        this.idUtenteRegistrato);
        logger.info(query);
        int rs;
        try {
            rs = DBManager.getInstance().executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            logger.warning(String.format("Errore durante l'aggiornamento dell'utente registrato " +
                            "['%s', '%s', '%s', '%s', '%s', %d, '%s'] nel database.%n%s", nome, cognome,
                    contattoTelefonico, email, automobile, postiDisponibili, password, e.getMessage()));
            if (e.getMessage().contains("Duplicate entry"))
                throw new DatabaseException("Esiste già un utente associato alla mail data.", true);
            throw new DatabaseException("Errore nell'aggiornamento dell'utente registrato nel database.", false);
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
            throw new DatabaseException("Errore nell'eliminazione dell'utente registrato dal database.", false);
        }
        return rs;
    }

    /**
     * Getter dell'identificativo dell'utente registrato.
     * @return l'identificativo dell'utente registrato.
     */
    public long getIdUtenteRegistrato() {
        return idUtenteRegistrato;
    }

    /**
     * Getter del nome dell'utente registrato.
     * @return il nome dell'utente registrato.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Getter del cognome dell'utente registrato.
     * @return il cognome dell'utente registrato.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Getter del contatto telefonico dell'utente registrato.
     * @return il contatto telefonico dell'utente registrato.
     */
    public String getContattoTelefonico() {
        return contattoTelefonico;
    }

    /**
     * Getter dell'email dell'utente registrato.
     * @return l'email dell'utente registrato.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter della password dell'utente registrato.
     * @return la password dell'utente registrato.
     */
    public String getAutomobile() {
        return automobile;
    }

    /**
     * Getter dell'automobile dell'utente registrato.
     * @return l'automobile dell'utente registrato.
     */
    public int getPostiDisponibili() {
        return postiDisponibili;
    }

    /**
     * Getter del numero di posti disponibili nell'automobile dell'utente registrato.
     * @return il numero di posti disponibili nell'automobile dell'utente registrato.
     */
    public String getPassword() {
        return password;
    }
}





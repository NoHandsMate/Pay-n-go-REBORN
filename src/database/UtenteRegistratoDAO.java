package database;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class UtenteRegistratoDAO {

    private long id;
    private String nome;
    private String cognome;
    private String contattoTelefonico;
    private String email;
    private String automobile;
    private int postiDisponibili;
    private String password;

    /* TODO: rimuovi logging delle query eseguite dopo il testing. */
    private final Logger logger = Logger.getLogger("loggerUtenteRegistratoDAO");

    /**
     * Costruttore di default di UtenteRegistratoDAO, crea un'istanza vuota da popolare successivamente.
     */
    public UtenteRegistratoDAO() { }

    /**
     * Costruttore di UtenteRegistratoDAO che popola l'istanza in base all'id fornito con i dati già memorizzati nel
     * database.
     * @param id l'identificativo dell'utente registrato.
     */

    /* TODO: E se non troviamo la prenotazione? Va creata l'eccezione custom apposita oppure sfruttiamo sempre il
        paradigma costruttore vuoto -> caricaDaDB(id) */
    public UtenteRegistratoDAO(int id) {
        boolean res = caricaDaDB(id);
        this.id = id;
    }

    /**
     * Funzione per impostare tutti i parametri dell'oggetto UtenteRegistratoDAO dato e salvare tale istanza nel
     * database.
     * @param id l'identificativo dell'utente registrato.
     * @param nome il nome dell'utente registrato.
     * @param cognome il cognome dell'utente registrato.
     * @param contattoTelefonico il contatto telefonico dell'utente registrato.
     * @param email l'email dell'utente registrato.
     * @param automobile l'automobile dell'utente registrato.
     * @param postiDisponibili il numero di posti disponibili nell'automobile dell'utente registrato.
     * @param password la password (bcrypt) dell'utente registrato.
     * @return true in vaso di successo (in tal caso l'oggetto sarà stato valorizzato con i parametri dati), false
     * altrimenti (l'oggetto non sarà valorizzato).
     */
    public boolean createUtenteRegistrato(long id, String nome, String cognome, String contattoTelefonico, String email,
                                      String automobile, int postiDisponibili, String password) {
        boolean res = salvaInDB(id, nome, cognome, contattoTelefonico, email, automobile, postiDisponibili, password);

        if (!res)
            return false;

        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.contattoTelefonico = contattoTelefonico;
        this.email = email;
        this.automobile = automobile;
        this.postiDisponibili = postiDisponibili;
        this.password = password;
        return true;
    }

    /**
     * Funzione per impostare tutti i parametri dell'oggetto UtenteRegistratoDAO dato e salvare tale istanza nel
     * database.
     * @param id l'identificativo dell'utente registrato.
     * @param nome il nome dell'utente registrato.
     * @param cognome il cognome dell'utente registrato.
     * @param contattoTelefonico il contatto telefonico dell'utente registrato.
     * @param email l'email dell'utente registrato.
     * @param password la password (bcrypt) dell'utente registrato.
     * @return true in vaso di successo (in tal caso l'oggetto sarà stato valorizzato con i parametri dati), false
     * altrimenti (l'oggetto non sarà valorizzato).
     */
    public boolean createUtenteRegistrato(long id, String nome, String cognome, String contattoTelefonico, String email,
                                      String password) {
        boolean res = salvaInDB(id, nome, cognome, contattoTelefonico, email, null, 0, password);

        if (!res)
            return false;

        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.contattoTelefonico = contattoTelefonico;
        this.email = email;
        this.automobile = null;
        this.postiDisponibili = 0;
        this.password = password;
        return true;
    }

    /**
     * Funzione per eliminare un utente registrato dal database.
     * @return true in caso di successo, false altrimenti.
     */
    public boolean deleteUtenteRegistrato() {
        return this.eliminaDaDB();
    }

    /**
     * Funzione privata per caricare i dati di un utente registrato dal database.
     * @param id l'identificativo dell'utente registrato.
     * @return true in caso di successo, false altrimenti.
     */
    private boolean caricaDaDB(long id) {
        String query = String.format("SELECT * from utentiregistrati WHERE (id = %d);", id);
        logger.info(query);
        try {
            /* TODO: debug di ResultSet */
            ResultSet rs = DBManager.selectQuery(query);
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
            logger.info(String.format("Errore durante il caricamento dal database di un utente registrato con id %d." +
                            "%n%s", id, e.getMessage()));
            return false;
        }
        return true;
    }

    /**
     * Funzione privata per salvare i dati di un utente registrato nel database.
     * @param id l'identificativo dell'utente registrato.
     * @param nome il nome dell'utente registrato.
     * @param cognome il cognome dell'utente registrato.
     * @param contattoTelefonico il contatto telefonico dell'utente registrato.
     * @param email l'email dell'utente registrato.
     * @param automobile l'automobile dell'utente registrato.
     * @param postiDisponibili il numero di posti disponibili nell'automobile dell'utente registrato.
     * @param password la password (bcrypt) dell'utente registrato.
     * @return true in caso di successo, false altrimenti.
     */
    private boolean salvaInDB(long id, String nome, String cognome, String contattoTelefonico, String email,
                              String automobile, int postiDisponibili, String password) {
        String query;
        if (automobile != null || postiDisponibili != 0) {
            query = String.format("INSERT INTO utentiregistrati (idUtenteRegistrato, nome, cognome, " +
                            "contattoTelefonico, email, automobile, postiDisponibili, password) VALUES " +
                            "(%d, '%s', '%s', '%s', '%s', '%s', %d, '%s');", id, nome, cognome, contattoTelefonico,
                            email, automobile, postiDisponibili, password);
        } else {
            query = String.format("INSERT INTO utentiregistrati (idUtenteRegistrato, nome, cognome, " +
                            "contattoTelefonico, email, password) VALUES (%d, '%s', '%s', '%s', '%s', '%s');",
                            id, nome, cognome, contattoTelefonico, email, password);
        }

        logger.info(query);
        try {
            /* TODO: questo int rs, dato che non lo usiamo è inutile (?) Ci sono modi per usarli. */
            int rs = DBManager.executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            if (automobile != null || postiDisponibili != 0) {
                logger.info(String.format("Errore durante l'inserimento dell'utente registrato " +
                                "[%d, '%s', '%s', '%s', '%s', '%s', %d, '%s'] nel database.%n%s", id, nome, cognome,
                                contattoTelefonico, email, automobile, postiDisponibili, password, e.getMessage()));
            } else {
                logger.info(String.format("Errore durante l'inserimento dell'utente registrato " +
                                "[%d, '%s', '%s', '%s', '%s', '%s'] nel database.%n%s", id, nome, cognome,
                                contattoTelefonico, email, password, e.getMessage()));
            }
            return false;
        }
        return true;
    }

    /**
     * Funzione privata per eliminare un utente registrato dal database.
     * @return true in caso di successo, false altrimenti.
     */
    private boolean eliminaDaDB() {
        String query = String.format("DELETE FROM utentiregistrati WHERE (idUtenteRegistrato = %d);", this.id);
        logger.info(query);
        try {
            /* TODO: questo int rs, dato che non lo usiamo è inutile (?) Ci sono modi per usarli. */
            int rs = DBManager.executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            if (this.automobile != null || this.postiDisponibili != 0) {
                logger.info(String.format("Errore durante l'eliminazione dell'utente registrato " +
                                "[%d, '%s', '%s', '%s', '%s', '%s', %d, '%s'] dal database.%n%s", this.id, this.nome,
                                this.cognome, this.contattoTelefonico, this.email, this.automobile,
                                this.postiDisponibili, this.password, e.getMessage()));
            } else {
                logger.info(String.format("Errore durante l'eliminazione dell'utente registrato " +
                                "[%d, '%s', '%s', '%s', '%s', '%s'] dal database.%n%s", this.id, this.nome,
                                this.cognome, this.contattoTelefonico, this.email, this.password, e.getMessage()));
            }
            return false;
        }
        return true;
    }

}





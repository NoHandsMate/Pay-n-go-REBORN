package database;


import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteRegistratoDAO {

    private long id;
    private long keyPrenotazioni;
    private long keyViaggi;
    private long keyValutazioni;

    private String nome;
    private String cognome;
    private String contattoTelefonico;
    private String email;
    private String password;
    private String automobile;
    private int numPostiDisponibili;

    public UtenteRegistratoDAO() {}


    // Costruttore di UtenteRegistratoDAO che popola l'istanza in base all'id fornito
    /*TODO: e se non lo trovo? */
    public UtenteRegistratoDAO(long id) {
        caricaDaDB(id);
        this.id = id;
    }

    public boolean createUtenteRegistrato() {
        return false;
    }

    public boolean readUtenteRegistrato() {
        return false;
    }

    public boolean updateUtenteRegistrato() {
        return false;
    }

    public boolean deleteUtenteRegistrato() {
        return false;
    }

    private boolean caricaDaDB(long id) {
        String query = "";
        try {
            ResultSet rs = DBManager.selectQuery(query);

            if (rs.next()) {
                /*TODO: Devo prelevare accedendo al DB tramite il nome dell'attributo-colonna */
                this.automobile = rs.getString("automobile");
                this.email = rs.getString("email");
                this.nome = rs.getString("nome");
                this.cognome = rs.getString("cognome");
                this.contattoTelefonico = rs.getString("contattoTelefonico");
                this.keyPrenotazioni = rs.getLong("keyPrenotazioni");
                this.keyViaggi = rs.getLong("keyViaggi");
                this.keyValutazioni = rs.getLong("keyValutazioni");
                this.numPostiDisponibili = rs.getInt("numPostiDisponibili");
            }
        } catch (ClassNotFoundException | SQLException e) {
            /* TODO: Logging più robusto */
            e.printStackTrace();
            return false;
        }

        return true;
    }

}





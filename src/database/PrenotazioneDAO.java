package database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrenotazioneDAO {

    private long id;
    private long keyPasseggero;
    private long keyViaggioPrenotato;

    // Costruttore di default di PrenotazioneDAO, crea un'istanza vuota da popolare successivamente
    public PrenotazioneDAO() { }

    // Costruttore di PrenotazioneDAO che popola l'istanza in base all'id fornito
    /*TODO: E se non troviamo la prenotazione?*/
    public PrenotazioneDAO(int id) {
        boolean res = caricaDaDB(id);

        this.id = id;
    }

    public boolean createPrenotazione(long id, long keyPasseggero, long keyViaggioPrenotato) {
        boolean res = salvaInDB(id, keyPasseggero, keyViaggioPrenotato);

        if (!res)
            return false;

        this.id = id;
        this.keyPasseggero = keyPasseggero;
        this.keyViaggioPrenotato = keyViaggioPrenotato;
        return true;
    }

    /*TODO: Che senso ha? */
    public PrenotazioneDAO readPrenotazione() {
        return this;
    }

    /*TODO: Probabilmente superfluo*/
    public boolean updatePrenotazione() {
        return false;
    }

    public boolean deletePrenotazione(long id) {
        boolean res = eliminaDaDB(id);

        return res;
    }

    private boolean caricaDaDB(long id) {
        String query = "";
        try {
            ResultSet rs = DBManager.selectQuery(query);

            if (rs.next()) {
                /*TODO: Devo prelevare accedendo al DB tramite il nome dell'attributo-colonna */
                this.keyPasseggero = rs.getLong("keyPasseggero");
                this.keyViaggioPrenotato = rs.getLong("keyViaggioPrenotato");
            }
        } catch (ClassNotFoundException | SQLException e) {
            /* TODO: Logging più robusto */
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean salvaInDB(long id, long keyPasseggero, long keyViaggioPrenotato) {
        String query = "INSERT INTO ...";
        try {
            int rs = DBManager.executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            /* TODO: Logging più robusto */
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean eliminaDaDB(long id) {
        String query = "DELETE FROM ...";
        try {
            int rs = DBManager.executeQuery(query);
        } catch (ClassNotFoundException | SQLException e) {
            /* TODO: Logging più robusto */
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
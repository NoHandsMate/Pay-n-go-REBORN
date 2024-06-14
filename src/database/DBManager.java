package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    // Credenziali di accesso al Database.
    public static final String url = "jdbc:mysql://localhost:3306/";
    public static final String dbName = "payngo";
    public static final String driver = "com.mysql.cj.jdbc.Driver";
    public static final String userName = "admin";
    public static final String password = "payngo";

    private static DBManager uniqueInstance;

    /**
     * Costruttore privato per non permettere la creazione erronea di un'istanza dall'esterno.
      */
    private DBManager() {}

    public static DBManager getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new DBManager();
        }
        return uniqueInstance;
    }

    /**
     * Metodo per ottenere una nuova connessione al Database.
     * @return la connessione instaurata.
     * @throws ClassNotFoundException se la classe non viene trovata.
     * @throws SQLException in caso di errore nella chiusura della connessione.
     */


    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName(driver);
        conn = DriverManager.getConnection(url+dbName,userName,password);

        return conn;
    }

    /**
     * Metodo per chiudere una connessione esistente al Database.
      * @param c connessione al database.
     * @throws SQLException in caso di errore nella chiusura della connessione.
     */
    public void closeConnection(Connection c) throws SQLException {
        c.close();
    }

    /**
     * Metodo per eseguire una query CREATE, UPDATE o DELETE sul Database.
     * @param query la query da eseguire sul database.
     * @return il numero di righe della tabella affette dalla query.
     * @throws ClassNotFoundException se la classe non viene trovata.
     * @throws SQLException in caso di errore nell'esecuzione della query.
     */
    public int executeQuery(String query) throws ClassNotFoundException, SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        int ret = statement.executeUpdate(query);
        conn.close();

        return ret;
    }

    /**
     * Metodo per eseguire una query SELECT sul Database.
     * @param query la query da eseguire sul database.
     * @return il ResultSet contenente il risultato della query.
     * @throws ClassNotFoundException se la classe non viene trovata.
     * @throws SQLException in caso di errore nell'esecuzione della query.
     */
    public ResultSet selectQuery(String query) throws ClassNotFoundException, SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(query);

        return result;
    }
}

package database;

import entity.EntityPrenotazione;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PrenotazioneDAO {

    private long id;
    private String passeggero;
    private String viaggioPrenotato;

    // Costruttore di default di PrenotazioneDAO, crea un'istanza vuota da popolare successivamente
    public PrenotazioneDAO() { super(); }

    // Costruttore di PrenotazioneDAO che popola l'istanza in base all'id fornito
    public PrenotazioneDAO(int id) {
        this.id = id;
        this.passeggero = new UtenteRegistratoDAO();
        this.viaggioPrenotato = new ViaggioDAO();
    }

    public boolean createPrenotazione() {

        return false;
    }

    public void readPrenotazione() {
        return this;
    }

    public boolean updatePrenotazione() {
        return false;
    }

    public boolean deletePrenotazione() {
        return false;
    }



}
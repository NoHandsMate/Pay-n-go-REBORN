package entity;

import exceptions.DatabaseException;
import exceptions.RegistrationFailedException;
import database.UtenteRegistratoDAO;

import java.util.Arrays;

public class GestoreUtenti {

    private static GestoreUtenti uniqueInstance;

    private GestoreUtenti() {
    }

    public static GestoreUtenti getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new GestoreUtenti();
        }
        return uniqueInstance;
    }

    public void registraUtente(String nome, String cognome, String email,
                               String auto, char[] password, Integer postiDisp,
                               String telefono) throws RegistrationFailedException {

        UtenteRegistratoDAO utenteDAO = new UtenteRegistratoDAO();
        try {
            utenteDAO.createUtenteRegistrato(nome, cognome, telefono, email, auto,
                    postiDisp, Arrays.toString(password));
        } catch (DatabaseException e) {
            throw new RegistrationFailedException("Registrazione Utente fallita");
        }
    }
}
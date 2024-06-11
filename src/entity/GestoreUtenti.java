package entity;

import exceptions.DatabaseException;
import exceptions.LoginUserException;
import exceptions.RegistrationFailedException;
import database.UtenteRegistratoDAO;
import dto.UtenteCorrente;

public class GestoreUtenti {

    private static GestoreUtenti uniqueInstance;

    private GestoreUtenti() {}

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
            utenteDAO.createUtenteRegistrato(nome, cognome, telefono, email, auto, postiDisp, new String(password));
            UtenteCorrente.getInstance().setIdUtenteCorrente(utenteDAO.getIdUtenteRegistrato());
            UtenteCorrente.getInstance().setNome(utenteDAO.getNome());
            UtenteCorrente.getInstance().setCognome(utenteDAO.getCognome());
        } catch (DatabaseException e) {
            if (e.isVisible())
            {
                throw new RegistrationFailedException(String.format("Registrazione Utente fallita: %s", e.getMessage()));
            }
            throw new RegistrationFailedException("Registrazione Utente fallita.");
        }
    }

    public void loginUtente(String email, char[] password) throws LoginUserException {
        UtenteRegistratoDAO utenteDAO = new UtenteRegistratoDAO();
        try {
            utenteDAO.readUtenteRegistrato(email, new String(password));
            UtenteCorrente.getInstance().setIdUtenteCorrente(utenteDAO.getIdUtenteRegistrato());
            UtenteCorrente.getInstance().setNome(utenteDAO.getNome());
            UtenteCorrente.getInstance().setCognome(utenteDAO.getCognome());
        } catch (DatabaseException e) {
            throw new LoginUserException("Login Utente fallito: " + e.getMessage());
        }
    }

    public void generaReportUtenti(){
        /* TODO: Da implementare */
    }
}
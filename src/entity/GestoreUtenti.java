package entity;

import exceptions.RegistrationFailedException;
import database.UtenteRegistratoDAO;

public class GestoreUtenti {

    private GestoreUtenti uniqueIstance;

    private GestoreUtenti() {}

    public GestoreUtenti getIstance() {
        if (uniqueIstance == null) {
            uniqueIstance = new GestoreUtenti();
        }
        return uniqueIstance;
    }

    public static void registraUtente(String nome, String cognome, String email,
                               String auto, char[] password, Integer postiDisp,
                               String telefono) throws RegistrationFailedException {

        UtenteRegistratoDAO utenteDAO = new UtenteRegistratoDAO();
        boolean res = utenteDAO.createUtenteRegistrato(nome, cognome, telefono, email, auto,
                                                       postiDisp, password.toString());

        if (!res)
            throw new RegistrationFailedException("Registrazione Utente fallita");
    }
}

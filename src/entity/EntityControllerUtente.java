package entity;

import database.UtenteRegistratoDAO;

public class EntityControllerUtente {

    public boolean registraUtente(String nome, String cognome, String contattoTelefonico, String email,
                                  String automobile, int numPostiDisponibili, String password) {

        UtenteRegistratoDAO utenteDao = new UtenteRegistratoDAO();
        EntityUtenteRegistrato user = new EntityUtenteRegistrato(nome, cognome, contattoTelefonico, email,
                                                                 password, automobile, numPostiDisponibili);
        boolean result = utenteDao.createUtenteRegistrato(user);

        return result;
    }

    public boolean loginUtente(String email, String password) {

        UtenteRegistratoDAO utenteDao = new UtenteRegistratoDAO();
        EntityUtenteRegistrato queryResult = utenteDao.readUtenteRegistrato(email, password);

        return queryResult != null;
    }

    public boolean convidiViaggio() {
        return false;
    }

    public boolean aggiornaDatiPersonali() {
        return false;
    }

    public boolean visualizzaPrenotazioni() {
        return false;
    }

    public boolean gestisciPrentonazioni() {
        return false;
    }

    public boolean ricercaViaggio() {
        return false;
    }

    public boolean valutaUtente() {
        return false;
    }
}

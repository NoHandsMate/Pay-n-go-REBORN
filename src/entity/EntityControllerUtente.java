package entity;

import database.UtenteRegistratoDAO;
import database.ViaggioDAO;
import database.ValutazioneDAO;
import database.PrenotazioneDAO;

import java.util.Date;

public class EntityControllerUtente {

    public boolean registraUtente(String nome, String cognome, String contattoTelefonico, String email,
                                  String automobile, int numPostiDisponibili, String password) {

        UtenteRegistratoDAO utenteDao = new UtenteRegistratoDAO();
        EntityUtenteRegistrato utente = new EntityUtenteRegistrato(nome, cognome, contattoTelefonico, email,
                                                                 password, automobile, numPostiDisponibili);

        boolean result = utenteDao.createUtenteRegistrato(utente);

        return result;
    }

    public boolean loginUtente(String email, String password) {

        UtenteRegistratoDAO utenteDao = new UtenteRegistratoDAO();
        EntityUtenteRegistrato queryResult = utenteDao.readUtenteRegistrato(email, password);

        return queryResult != null; //ritorna un booleano: vero se il risultato è diverso da null, falso se viceversa
    }

    public boolean condividiViaggio(String luogoPartenza, String luogoDestinazione,
                                  Date dataPartenza, Date oraPartenza,
                                  Date dataArrivo, Date oraArrivo,
                                  float contributoSpese,
                                  EntityUtenteRegistrato autista) {

        ViaggioDAO viaggioDao = new ViaggioDAO();
        boolean result = viaggioDao.createViaggio(luogoPartenza, luogoDestinazione,dataPartenza,oraPartenza,
                dataArrivo,oraArrivo,contributoSpese,autista);

        return result;
    }

    public boolean aggiornaDatiPersonali(String nome, String cognome, String contattoTelefonico, String email,
                                         String automobile, int numPostiDisponibili,String password) {

        UtenteRegistratoDAO utenteDao = new UtenteRegistratoDAO();
        EntityUtenteRegistrato newUtenteRegistrato = new EntityUtenteRegistrato(nome, cognome, contattoTelefonico, email,
                  password, automobile, numPostiDisponibili);
        boolean result = utenteDao.updateUtenteRegistrato(newUtenteRegistrato);

        return result;
    }

    public EntityPrenotazione visualizzaPrenotazioni() {


        EntityPrenotazione mammt = new EntityPrenotazione();

        return mammt;
    }

    public boolean gestisciPrenotazione() {



        return false;
    }

    public boolean ricercaViaggio() {
        return false;
    }

    public boolean valutaUtente() {
        return false;
    }
}

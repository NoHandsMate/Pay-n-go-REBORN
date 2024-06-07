//Classe Facade per l'Utente

package entity;

import database.UtenteRegistratoDAO;
import database.ViaggioDAO;
import database.ValutazioneDAO;
import database.PrenotazioneDAO;

import java.util.ArrayList;
import java.util.Date;



/*
* Creare classi information expert -(per ora solo per Utente) per contenere la lista di istanze della classe e relativi metodi
* per aumentare il disaccoppiamento. La classe info expert deve contenere il currentUser così da slegare la facade
* le classi DAO possono comunicare tra loro ma noi ce ne freghiamo e ci manteniamo i riferimenti alle chiavi esterne.
*
*
* */

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

        //TODO se esiste l'utente, caricare tutte le informazioni dal database

        UtenteRegistratoDAO utenteDao = new UtenteRegistratoDAO();
        UtenteRegistratoDAO queryResult = utenteDao.readUtenteRegistrato(email, password);

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

    public ArrayList<EntityPrenotazione> visualizzaPrenotazioni(long id_utente) {


        ArrayList<EntityPrenotazione> prenotazioniVisualizzate = new ArrayList<>();
        prenotazioniVisualizzate =

        /*PrenotazioneDAO queryResult = prenotazioneDao.readPrenotazione();
        long id = queryResult.getId();
        EntityUtenteRegistrato passeggero = queryResult.getPasseggero();
        EntityViaggio viaggio = queryResult.getViaggio();
        EntityPrenotazione prenotazioneVisualizzata = new EntityPrenotazione(id,passeggero,viaggio);*/


        return prenotazioniVisualizzate;
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

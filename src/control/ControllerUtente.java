package control;
import entity.EntityPrenotazione;
import entity.EntityUtenteRegistrato;
import entity.EntityViaggio;
import entity.EntityControllerUtente;

import java.util.ArrayList;
import java.util.Date;

public class ControllerUtente {

    //Variabile statica univoca per implementare il pattern Singleton
    private static ControllerUtente uniqueInstance;

    private ControllerUtente() {}

    public static ControllerUtente getInstance(){
        if(uniqueInstance == null) {
            uniqueInstance = new ControllerUtente();
        }
        return uniqueInstance;
    }


    public boolean registraUtente(String nome, String cognome, String contattoTelefonico, String email, String automobile,
                               int numPostiDisponibili,String password){

        //richiamo il metodo registraUtente di EntityControllerUtente (Facade)

        EntityControllerUtente facadeUtenteRegistrato = new EntityControllerUtente();

        boolean result = facadeUtenteRegistrato.registraUtente(nome,cognome,contattoTelefonico,email,automobile,
                numPostiDisponibili,password);

        return result;
    }

    public boolean loginUtente(String email, String password){

        //richiamo il metodo loginUtente di EntityControllerUtente (Facade)
        EntityControllerUtente facadeUtenteRegistrato = new EntityControllerUtente();

        boolean result = facadeUtenteRegistrato.loginUtente(email,password);

        return result;
    }

    public boolean condividiViaggio(String luogoPartenza, String luogoDestinazione,
                                 Date dataPartenza, Date oraPartenza,
                                 Date dataArrivo, Date oraArrivo,
                                 float contributoSpese,
                                 EntityUtenteRegistrato autista){

        //Non viene passato l'arraylist relativo alle prenotazioni, poiché sarà nel package Entity che esso verrà creato
        //richiamo il metodo condividiViaggio di EntityControllerUtente (Facade)
        EntityControllerUtente facadeUtenteRegistrato = new EntityControllerUtente();
        boolean result = facadeUtenteRegistrato.condividiViaggio(luogoPartenza, luogoDestinazione,dataPartenza,oraPartenza,
                dataArrivo,oraArrivo,contributoSpese,autista);

        return result;

    }

    public boolean aggiornaDatiPersonali(String nome, String cognome, String contattoTelefonico, String email,
                                      String automobile, int numPostiDisponibili, String password){

        //ATTENZIONE! Alcuni di questi parametri possono anche essere una stringa vuota (l'utente può voler modificare
        //solo parte dei suoi dati personali)
        //richiamo il metodo aggiornaDatiPersonali di EntityControllerUtente (Facade)
        EntityControllerUtente facadeUtenteRegistrato = new EntityControllerUtente();
        boolean result = facadeUtenteRegistrato.aggiornaDatiPersonali(nome,cognome,contattoTelefonico,email,automobile,
                numPostiDisponibili,password);

        return result;
    }

    //Si tratta di una prima bozza, c'è incertezza sul tipo di ritorno di questa funzione, a causa dell'interfacciamento
    //con la GUI
    public ArrayList<EntityPrenotazione> visualizzaPrenotazioni(long id_utente){

        //richiamao il metodo visualizzaPrenotazioni di EntityControllerUtente (Facade), con opportuno parametro di
        //ritorno
        EntityControllerUtente facadeUtenteRegistrato = new EntityControllerUtente();
        ArrayList<EntityPrenotazione> prenotazioniVisualizzate = facadeUtenteRegistrato.visualizzaPrenotazioni(id_utente);

        return prenotazioniVisualizzate;
    }

    public boolean gestisciPrenotazione(){

        //richiamo il metodo gestisciPrenotazione di EntityControllerUtente (Facade)
        EntityControllerUtente facadeUtenteRegistrato = new EntityControllerUtente();
        boolean result = facadeUtenteRegistrato.gestisciPrenotazione();

        return result;
    }

    public EntityViaggio ricercaViaggio(){

        //richiamo il metodo visualizzaPrenotazioni di EntityControllerUtente (Facade), con opportuno parametro di
        //ritorno
        EntityControllerUtente facadeUtenteRegistrato = new EntityControllerUtente();
        EntityViaggio = facadeUtenteRegistrato.ricercaViaggio()

        return null; //NON VA BENE QUESTO RETURN, ritornare ciò che ritorna il metodo richiamato
    }

    public void ValutaUtente(){

        //richiamare il metodo valutaUtente di EntityControllerUtente (Facade)

    }
}

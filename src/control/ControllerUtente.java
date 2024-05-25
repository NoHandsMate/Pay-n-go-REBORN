package control;
import entity.EntityPrenotazione;
import entity.EntityUtenteRegistrato;
import entity.EntityViaggio;

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


    public void registraUtente(String nome, String cognome, String contattoTelefonico, String email, String Automobile,
                               int numPostiDisponibili,String password){

        //richiamare il metodo registraUtente di EntityControllerUtente (Facade)

    }

    public void loginUtente(String email, String password){

        //richiamare il metodo loginUtente di EntityControllerUtente (Facade)

    }

    public void condividiViaggio(String luogoPartenza, String luogoDestinazione,
                                 Date dataPartenza, Date oraPartenza,
                                 Date dataArrivo, Date oraArrivo,
                                 float contributoSpese,
                                 EntityUtenteRegistrato autista){

        //Non viene passato l'arraylist relativo alle prenotazioni, poiché sarà nel package Entity che esso verrà creato
        //richiamare il metodo condividiViaggio di EntityControllerUtente (Facade)

    }

    public void aggiornaDatiPersonali(String nome, String cognome, String contattoTelefonico, String email,
                                      String Automobile, int numPostiDisponibili){

        //ATTENZIONE! Alcuni di questi parametri possono anche essere una stringa vuota (l'utente può voler modificare
        //solo parte dei suoi dati personali)
        //richiamare il metodo aggiornaDatiPersonali di EntityControllerUtente (Facade)
    }

    //Si tratta di una prima bozza, c'è incertezza sul tipo di ritorno di questa funzione, a causa dell'interfacciamento
    //con la GUI
    public EntityPrenotazione visualizzaPrenotazioni(){

        //richiamare il metodo visualizzaPrenotazioni di EntityControllerUtente (Facade), con opportuno parametro di
        //ritorno

        return null; //NON VA BENE QUESTO RETURN, ritornare ciò che ritorna il metodo richiamato
    }

    public void gestisciPrenotazione(){

        //richiamare il metodo gestisciPrenotazione di EntityControllerUtente (Facade)

    }

    public EntityViaggio ricercaViaggio(){

        //richiamare il metodo visualizzaPrenotazioni di EntityControllerUtente (Facade), con opportuno parametro di
        //ritorno

        return null; //NON VA BENE QUESTO RETURN, ritornare ciò che ritorna il metodo richiamato
    }

    public void ValutaUtente(){

        //richiamare il metodo valutaUtente di EntityControllerUtente (Facade)

    }
}

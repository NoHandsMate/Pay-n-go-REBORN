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


}

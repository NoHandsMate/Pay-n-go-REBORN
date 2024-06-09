package control;


import java.util.AbstractMap;
import entity.FacadeEntityUtente;
import exceptions.RegistrationFailedException;

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


    public static AbstractMap.SimpleEntry<Boolean, String> registraUtente(String nome, String cognome, String email,
                                                                          String auto, char[] password, Integer postiDisp,
                                                                          String telefono) {

        try {
            FacadeEntityUtente.registraUtente(nome, cognome, email, auto, password, postiDisp, telefono);
        } catch (RegistrationFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Registrazione effettuata con successo");

    }
}

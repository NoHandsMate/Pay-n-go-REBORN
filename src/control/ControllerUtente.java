package control;


import java.util.AbstractMap;
import entity.FacadeEntityUtente;
import exceptions.LoginUserException;
import exceptions.RegistrationFailedException;
import dto.*;

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


    public AbstractMap.SimpleEntry<Boolean, String> registraUtente(String nome, String cognome, String email,
                                                                          String auto, char[] password, Integer postiDisp,
                                                                          String telefono) {

        try {
            FacadeEntityUtente.getInstance().registraUtente(nome, cognome, email, auto, password, postiDisp, telefono);
        } catch (RegistrationFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Registrazione effettuata con successo");
    }

    public AbstractMap.SimpleEntry<Boolean, String> loginUtente(String email, char[] password) {

        try {
            FacadeEntityUtente.getInstance().loginUtente(email, password);
        } catch (LoginUserException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Login effettuato con successo");
    }

}

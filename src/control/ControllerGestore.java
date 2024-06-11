package control;


import java.util.AbstractMap;
import entity.FacadeEntityCarpooling;
import exceptions.RegistrationFailedException;
import dto.*;


public class ControllerGestore {

    //Variabile statica univoca per implementare il pattern Singleton
    private static ControllerGestore uniqueInstance;

    private ControllerGestore() {}

    public static ControllerGestore getInstance(){
        if(uniqueInstance == null) {
            uniqueInstance = new ControllerGestore();
        }
        return uniqueInstance;
    }


    public AbstractMap.SimpleEntry<Boolean, String> generaReportIncassi(){

        try {

            FacadeEntityCarpooling.getInstance().GeneraReportIncassi();
        } catch (RegistrationFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Report incassi generato con successo");
    }

    public AbstractMap.SimpleEntry<Boolean, String> generaReportUtenti(){

        try {

            FacadeEntityCarpooling.getInstance().GeneraReportIncassi();
        } catch (RegistrationFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Report utenti generato con successo");
    }

}

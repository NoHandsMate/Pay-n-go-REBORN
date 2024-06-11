package control;


import java.util.AbstractMap;
import entity.FacadeEntityGestore;
import exceptions.ReportIncassiFailedException;
import exceptions.ReportUtentiFailedException;


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


    public AbstractMap.SimpleEntry<Boolean, Object> generaReportIncassi(){

        Float reportIncassi;

        try {

             reportIncassi = FacadeEntityGestore.getInstance().GeneraReportIncassi();
        } catch (ReportIncassiFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, reportIncassi);
    }

    public AbstractMap.SimpleEntry<Boolean, String> generaReportUtenti(){

        try {

            FacadeEntityGestore.getInstance().GeneraReportUtenti();
        } catch (ReportUtentiFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Report utenti generato con successo");
    }

}

package control;


import java.util.AbstractMap;
import java.util.ArrayList;

import entity.FacadeEntityGestore;
import exceptions.ReportIncassiFailedException;
import exceptions.ReportUtentiFailedException;

import dto.*;


public class ControllerGestore {

    private static ControllerGestore uniqueInstance;

    private ControllerGestore() {}

    public static ControllerGestore getInstance(){
        if(uniqueInstance == null) {
            uniqueInstance = new ControllerGestore();
        }
        return uniqueInstance;
    }


    /**
     * Funzione che permette al gestore dell'applicazione di generare un report degli incassi di tutti i viaggi nel
     * sistema
     * @return reportIncass float che rappresenta l'incasso complessivo
     */
    public AbstractMap.SimpleEntry<Boolean, Object> generaReportIncassi(){

        Float reportIncassi;

        try {

             reportIncassi = FacadeEntityGestore.getInstance().generaReportIncassi();
        } catch (ReportIncassiFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, reportIncassi);
    }

    /**
     * Funzione che permette al gestore dell'applicazione di generare un report di valutazioni per tutti gli utenti del
     * sistema
     * @return reportUtenti un ArrayList di ArrayList di DTO (matrice) che rappresenta il report
     */
    public AbstractMap.SimpleEntry<Boolean, Object> generaReportUtenti(){

        ArrayList<ArrayList<MyDto>> reportUtenti;

        try {

           reportUtenti = FacadeEntityGestore.getInstance().generaReportUtenti();
        } catch (ReportUtentiFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true,reportUtenti);
    }

}

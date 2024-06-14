package entity;

import exceptions.ReportIncassiFailedException;
import exceptions.ReportUtentiFailedException;
import dto.*;
import java.util.ArrayList;

public class FacadeEntityGestore {

    private static FacadeEntityGestore uniqueInstance;

    private FacadeEntityGestore() {}

    public static FacadeEntityGestore getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new FacadeEntityGestore();
        }
        return uniqueInstance;
    }

    /**
     * Funzione che permette al gestore dell'applicazione di generare un report degli incassi di tutti i viaggi nel
     * sistema
     * @return reportIncassi float che rappresenta l'incasso complessivo
     */
    public Float generaReportIncassi() throws ReportIncassiFailedException {
        return GestoreViaggi.getInstance().generaReportIncassi();
    }

    /**
     * Funzione che permette al gestore dell'applicazione di generare un report di valutazioni per tutti gli utenti del
     * sistema
     * @return reportUtenti un ArrayList di ArrayList di DTO (matrice) che rappresenta il report
     */
    public ArrayList<ArrayList<MyDto>> generaReportUtenti() throws ReportUtentiFailedException {
        return GestoreUtenti.getInstance().generaReportUtenti();
    }
}

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


    public Float GeneraReportIncassi() throws ReportIncassiFailedException {
        return GestoreViaggi.getInstance().GeneraReportIncassi();
    }

    public ArrayList<ArrayList<MyDto>> GeneraReportUtenti() throws ReportUtentiFailedException {
        return GestoreUtenti.getInstance().generaReportUtenti();
    }
}

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


    public Float generaReportIncassi() throws ReportIncassiFailedException {
        return GestoreViaggi.getInstance().generaReportIncassi();
    }

    public ArrayList<ArrayList<MyDto>> generaReportUtenti() throws ReportUtentiFailedException {
        return GestoreUtenti.getInstance().generaReportUtenti();
    }
}

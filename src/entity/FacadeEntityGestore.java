package entity;

import exceptions.ReportIncassiFailedException;
import exceptions.ReportUtentiFailedException;

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

    public void GeneraReportUtenti() throws ReportUtentiFailedException {
        GestoreUtenti.getInstance().generaReportUtenti();
    }
}

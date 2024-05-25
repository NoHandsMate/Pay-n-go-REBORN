package control;


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


    public float generaReportIncassi(){

        //richiamare il metodo generaReportIncassi di EntityControllerCarpooling (Facade)
        float ritorno_per_non_dare_errore = 1;

        return ritorno_per_non_dare_errore; //NON VA BENE QUESTO RETURN, ritornare ciò che ritorna il metodo richiamato
    }

    public float generaReportUtenti(){

        //richiamare il metodo generaReportUtenti di EntityControllerCarpooling (Facade)
        float ritorno_per_non_dare_errore = 1;

        return ritorno_per_non_dare_errore; //NON VA BENE QUESTO RETURN, ritornare ciò che ritorna il metodo richiamato
    }

}

package entity;

public class Sessione {

    private static Sessione uniqueInstance;
    private EntityUtenteRegistrato utenteCorrente;

    private Sessione() {}

    public static Sessione getInstance() {
        if(uniqueInstance == null) {
            uniqueInstance = new Sessione();
        }
        return uniqueInstance;
    }

    public EntityUtenteRegistrato getUtenteCorrente() {
        return utenteCorrente;
    }

    public void setUtenteCorrente(EntityUtenteRegistrato utenteCorrente) {
        this.utenteCorrente = utenteCorrente;
    }
}



package entity;

import exceptions.ReportIncassiFailedException;
import exceptions.ReportUtentiFailedException;
import dto.*;
import exceptions.VisualizzaValutazioniFailedException;

import java.util.List;

/**
 * Classe del package entity nel modello BCED, essa implementa la façade che espone a ControllerGestore tutte le
 * funzionalità utilizzabili dal gestore dell'applicazione.
 */
public class FacadeEntityGestore {

    /**
     * L'unica istanza di FacadeEntityGestore che implementa il pattern Singleton.
     */
    private static FacadeEntityGestore uniqueInstance;

    /**
     * Costruttore privato per impedire la creazione di istanze multiple.
     */
    private FacadeEntityGestore() {}

    /**
     * Funzione statica per richiamare l'unica istanza di FacadeEntityGestore o crearne una se non esiste già.
     * @return l'istanza singleton di FacadeEntityGestore.
     */
    public static FacadeEntityGestore getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new FacadeEntityGestore();
        }
        return uniqueInstance;
    }

    /**
     * Funzione che permette al gestore dell'applicazione di generare un report degli incassi di tutti i viaggi nel
     * sistema.
     * @return l'incasso complessivo del sistema.
     * @throws ReportIncassiFailedException ne non è stato possibile generare il report di incassi.
     */
    public Float generaReportIncassi() throws ReportIncassiFailedException {
        return GestoreViaggi.getInstance().generaReportIncassi();
    }

    /**
     * Funzione che permette al gestore dell'applicazione di generare un report di valutazioni sommario per tutti gli
     * utenti del sistema.
     * @return il report di valutazione sommario per tutti gli utenti del sistema.
     */
    public List<MyDto> generaReportUtenti() throws ReportUtentiFailedException {
        return GestoreUtenti.getInstance().generaReportUtenti();
    }

    /**
     * Funzione che, dopo aver visualizzato il report di valutazione sommario di
     * {@link #visualizzaValutazioniUtente(long) visualizzaValutazioniUtente}, permette di visualizzare i dettagli di
     * tutte le valutazioni associate a un determinato utente.
     * @param idUtente l'identificativo dell'utente del quale si vogliono visualizzare le valutazioni.
     * @return l'elenco valutazioni dell'utente.
     */
    public List<MyDto> visualizzaValutazioniUtente(long idUtente) throws VisualizzaValutazioniFailedException {
        return GestoreUtenti.getInstance().visualizzaValutazioniUtente(idUtente);
    }
}

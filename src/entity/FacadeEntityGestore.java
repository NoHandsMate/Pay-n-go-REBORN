package entity;

import exceptions.ReportIncassiFailedException;
import exceptions.ReportUtentiFailedException;
import dto.*;
import exceptions.ValutazioneFailedException;
import exceptions.VisualizzaValutazioniFailedException;

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
    public ArrayList<MyDto> generaReportUtenti() throws ReportUtentiFailedException {
        return GestoreUtenti.getInstance().generaReportUtenti();
    }

    /**
     * Funzione d'ausilio a <close> generaReportUtenti </close> che permette di visualizzare tutte le valutazioni di
     * un utente
     * @param idUtente l'utente del quale si vogliono visualizzare le valutazioni
     * @return un ArrayList di DTO con le valutazioni dell'utente
     */
    public ArrayList<MyDto> visualizzaValutazioniUtente(long idUtente) throws VisualizzaValutazioniFailedException {
        return GestoreUtenti.getInstance().visualizzaValutazioniUtente(idUtente);
    }
}

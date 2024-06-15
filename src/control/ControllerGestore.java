package control;


import java.util.AbstractMap;
import java.util.List;

import entity.FacadeEntityGestore;
import exceptions.ReportIncassiFailedException;
import exceptions.ReportUtentiFailedException;

import dto.*;
import exceptions.VisualizzaValutazioniFailedException;

/**
 * Classe del package controller nel modello BCED, essa implementa tutte le funzionalità utilizzabili dal gestore
 * dell'applicazione.
 */
public class ControllerGestore {

    /**
     * L'unica istanza di ControllerGestore che implementa il pattern Singleton.
     */
    private static ControllerGestore uniqueInstance;

    /**
     * Costruttore privato per impedire la creazione di istanze multiple.
     */
    private ControllerGestore() {}

    /**
     * Funzione statica per richiamare l'unica istanza di ControllerGestore o crearne una se non esiste già.
     * @return l'istanza singleton di ControllerGestore.
     */
    public static ControllerGestore getInstance(){
        if(uniqueInstance == null) {
            uniqueInstance = new ControllerGestore();
        }
        return uniqueInstance;
    }


    /**
     * Funzione che permette al gestore dell'applicazione di generare un report degli incassi di tutti i viaggi nel
     * sistema.
     * @return una tupla <code>(Boolean, Object)</code>: nel caso di boolean <code>true</code> l'oggetto conterrà
     * l'incasso complessivo del sistema, nel caso di boolean <code>false</code> esso conterrà il messaggio di errore.
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
     * Funzione che permette al gestore dell'applicazione di generare un report di valutazioni sommario per tutti gli
     * utenti del sistema.
     * @return una tupla <code>(Boolean, Object)</code>: nel caso di boolean <code>true</code> l'oggetto conterrà il
     * report di valutazione sommario per tutti gli utenti del sistema, nel caso di boolean <code>false</code> esso
     * conterrà il messaggio di errore.
     */
    public AbstractMap.SimpleEntry<Boolean, Object> generaReportUtenti(){
        List<MyDto> reportUtenti;
        try {
           reportUtenti = FacadeEntityGestore.getInstance().generaReportUtenti();
        } catch (ReportUtentiFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, reportUtenti);
    }

    /**
     * Funzione che, dopo aver visualizzato il report di valutazione sommario di
     * {@link #visualizzaValutazioniUtente(long) visualizzaValutazioniUtente}, permette di visualizzare i dettagli di
     * tutte le valutazioni associate a un determinato utente.
     * @param idUtente l'identificativo dell'utente del quale si vogliono visualizzare le valutazioni.
     * @return una tupla <code>(Boolean, Object)</code>: nel caso di boolean <code>true</code> l'oggetto conterrà
     * l'elenco di valutazioni dell'utente, nel caso di boolean <code>false</code> esso conterrà il messaggio di errore.
     */
    public AbstractMap.SimpleEntry<Boolean, Object> visualizzaValutazioniUtente(long idUtente) {
        List<MyDto> valutazioniUtente;
        try {
            valutazioniUtente = FacadeEntityGestore.getInstance().visualizzaValutazioniUtente(idUtente);
        } catch (VisualizzaValutazioniFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, valutazioniUtente);
    }
}

package testing;

import control.ControllerGestore;
import control.ControllerUtente;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;

import static org.junit.Assert.*;

/**
 * Casi di test di ControllerGestore
 */
public class ControllerGestoreTest {

    /**
     * Setup del testing di ControllerGestore
     */
    @Before
    public void setUp() {
        char [] password = {'p', 'a', 'y', 'n', 'g', 'o'};
        ControllerUtente.getInstance().loginUtente("administrator@payngo.com", password);
    }

    /**
     * Si prova a ottenere l'istanza di ControllerGestore. Esito atteso: istanza non nulla.
     */
    @Test
    public void getInstance() {
        assertNotNull("Non è possibile richiamare ControllerGestore", ControllerUtente.getInstance());
    }

    /**
     * Si prova a generare il report di incassi. Esito atteso: report generato.
     */
    @Test
    public void generaReportIncassi() {
        AbstractMap.SimpleEntry<Boolean, Object> result;
        result = ControllerGestore.getInstance().generaReportIncassi();
        assertTrue("Report incassi fallito.", result.getKey());
    }

    /**
     * Si prova a generare il report utenti. Esito atteso: report generato.
     */
    @Test
    public void generaReportUtenti() {
        AbstractMap.SimpleEntry<Boolean, Object> result;
        result = ControllerGestore.getInstance().generaReportUtenti();
        assertTrue("Report utenti fallito.", result.getKey());
    }

    /**
     * Si prova a ottenere i dettagli delle valutazioni di un utente. Esito atteso: report generato.
     */
    @Test
    public void visualizzaValutazioniUtente() {
        AbstractMap.SimpleEntry<Boolean, Object> result;
        result = ControllerGestore.getInstance().visualizzaValutazioniUtente(2);
        assertTrue("Report incassi fallito.", result.getKey());
    }
}
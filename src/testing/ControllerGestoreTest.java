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

    @Before
    public void setUp() throws Exception {
        char [] password = {'p', 'a', 'y', 'n', 'g', 'o'};
        ControllerUtente.getInstance().loginUtente("administrator@payngo.com", password);
    }

    @Test
    public void getInstance() {
        assertNotNull("Non è possibile richiamare ControllerGestore", ControllerUtente.getInstance());
    }

    @Test
    public void generaReportIncassi() {
        AbstractMap.SimpleEntry<Boolean, Object> result;
        result = ControllerGestore.getInstance().generaReportIncassi();
        assertTrue("Report incassi fallito.", result.getKey());
    }

    @Test
    public void generaReportUtenti() {
        AbstractMap.SimpleEntry<Boolean, Object> result;
        result = ControllerGestore.getInstance().generaReportUtenti();
        assertTrue("Report utenti fallito.", result.getKey());
    }

    @Test
    public void visualizzaValutazioniUtente() {
        AbstractMap.SimpleEntry<Boolean, Object> result;
        result = ControllerGestore.getInstance().visualizzaValutazioniUtente(1);
        assertTrue("Report incassi fallito.", result.getKey());
    }
}
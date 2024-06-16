package testing;

import control.ControllerUtente;
import dto.MyDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ControllerUtenteTest {

    @org.junit.Before
    public void setUp() throws Exception {
        char [] password = {'a', 'b', 'c', 'd'};
        ControllerUtente.getInstance().registraUtente("Mario",
                "Rossi",
                "r.mario@gmail.com",
                "Fiat",
                password,
                3,
                "3326789922");
        ControllerUtente.getInstance().loginUtente("r.mario@gmail.com", password);
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void getInstance() {
        assertNotNull("Non è possibile richiamare ControllerUtente", ControllerUtente.getInstance());
    }

    @org.junit.Test
    public void getSessione() {
        MyDto sessione = ControllerUtente.getInstance().getSessione();
        String nome = sessione.getCampo2();
        assertEquals("Non è stato possibile ricavare la sessione", nome, "Mario");
    }

    @org.junit.Test
    public void registraUtente() {
        char [] password = {'a', 'b', 'c', 'd'};
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().registraUtente("Mario",
                "Rossi",
                "r.mario2@gmail.com",
                "Fiat",
                password,
                3,
                "3326789922");

        assertTrue("Registrazione fallita", result.getKey());
    }

    @org.junit.Test
    public void loginUtente() {
        char [] password = {'a', 'b', 'c', 'd'};
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().loginUtente("r.mario@gmail.com", password);

        assertTrue("Login fallito", result.getKey());
    }

    @org.junit.Test
    public void condividiViaggio() {
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().condividiViaggio("Milano",
                "Roma",
                LocalDateTime.of(2024, 4, 25, 18, 35, 0),
                LocalDateTime.of(2024, 4, 25, 20, 54, 2),
                3);

        assertTrue("Condividi viaggio fallita", result.getKey());
    }

    @org.junit.Test
    public void aggiornaDatiPersonali() {
        char [] password = {'a', 'b', 'c', 'd'};
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().aggiornaDatiPersonali("Mario",
                "Rossi",
                "r.mario@gmail.com",
                password,
                "3326789922",
                "Lancia",
                3);

        assertTrue("Aggiornamento dati personali fallito", result.getKey());
    }

    @org.junit.Test
    public void ricercaViaggio() {
        AbstractMap.SimpleEntry<Boolean, Object> result;
        result = ControllerUtente.getInstance().ricercaViaggio("Milano",
                "Roma",
                LocalDate.of(2024, 4, 25));

        assertTrue("Ricerca viaggio fallita", result.getKey());
    }

    @org.junit.Test
    public void visualizzaPrenotazioniEffettuate() {
        AbstractMap.SimpleEntry<Boolean, Object> result;
        result = ControllerUtente.getInstance().visualizzaPrenotazioniEffettuate();

        assertTrue("Visualizza prenotazioni effettuate fallita", result.getKey());
    }

    @org.junit.Test
    public void prenotaViaggio() {
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().prenotaViaggio(18);

        assertTrue("Prenotazione viaggio fallita", result.getKey());
    }

    @org.junit.Test
    public void gestisciPrenotazione() {
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().gestisciPrenotazione(1, true);

        assertTrue("Prenotazione gestita con successo", result.getKey());
    }

    @org.junit.Test
    public void valutaUtente() {
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().valutaUtente(1,4,"....");

        assertTrue("Valutazione utente fallita", result.getKey());
    }

    @org.junit.Test
    public void visualizzaViaggiCondivisi() {
        ControllerUtente.getInstance().condividiViaggio("Milano",
                "Roma",
                LocalDateTime.of(2024, 4, 25, 18, 35, 0),
                LocalDateTime.of(2024, 4, 25, 20, 54, 2),
                3);
        ArrayList<MyDto> viaggiCondivisi;
        viaggiCondivisi = ControllerUtente.getInstance().visualizzaViaggiCondivisi();
        assertNotNull(viaggiCondivisi.getFirst().getCampo2());
    }

    @org.junit.Test
    public void visualizzaPrenotazioni() {
        AbstractMap.SimpleEntry<Boolean, Object> result;
        result = ControllerUtente.getInstance().visualizzaPrenotazioni(1);
        assertTrue("Visualizza prenotazioni fallita", result.getKey());
    }
}
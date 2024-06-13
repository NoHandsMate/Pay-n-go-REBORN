package test;

import control.ControllerUtente;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;

public class ControllerUtenteTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getInstance() {
    }

    @Test
    public void registraUtente() {
        char [] password = {'a', 'b', 'c', 'd'};
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().registraUtente("Mario",
                "Rossi",
                "r.mario@gmail.com",
                "Fiat",
                password,
                3,
                "3326789922");

        assertTrue("Registrazione effettuata con successo", result.getKey()); // è una tupla, dove la key è booelana e la stringa è stringa
                // la getKey è il valore booleano
    }

    @Test
    public void loginUtente() {
        char [] password = {'a', 'b', 'c', 'd'};
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().loginUtente("r.mario@gmail.com", password);

        assertTrue("Login effettuato con successo", result.getKey());
    }

    @Test
    public void condividiViaggio() {
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().condividiViaggio("Milano",
                "Roma", LocalDateTime.of(2024, 4, 25, 18, 35, 0), LocalDateTime.of(), 3);

        assertTrue("Viaggio condiviso con successo", result.getKey());
    }

    @Test
    public void aggiornaDatiPersonali() {
        char [] password = {'a', 'b', 'c', 'd'};
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().aggiornaDatiPersonali("Mario",
                "Rossi",
                "r.mario@gmail.com",
                password,
                "3326789922",
                "Fiat",
                3);

        assertTrue("Aggiornamento effettuato con successo", result.getKey());
    }

    @Test
    public void ricercaViaggio() {
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().ricercaViaggio("Milano",
                "Roma",
                LocalDate.of());

        assertTrue("Ricerca effettuata con successo", result.getKey());
    }
}
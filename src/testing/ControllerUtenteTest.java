package testing;

import control.ControllerUtente;
import dto.MyDto;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Casi di test di ControllerUtente
 * Potrebbe essere necessario aumentare il limite di connessioni contemporanee per eseguire tutti i test
 * contemporaneamente. Noi abbiamo impostato il limite a 5000 (scelta decisamente conservativa).
 */
public class ControllerUtenteTest {

    /**
     * Setup del testing di ControllerUtente
     */
    @Before
    public void setUp() {
        // Caricare il database: Pay-n-Go-REBORN-database.sql
        char [] password = {'a', 'b', 'c', 'd'};
        ControllerUtente.getInstance().loginUtente("r.mario@gmail.com", password);
    }

    /**
     * Si prova a ottenere l'istanza di ControllerUtente. Esito atteso: istanza non nulla.
     */
    @Test
    public void getInstance() {
        assertNotNull("Non è possibile richiamare ControllerUtente", ControllerUtente.getInstance());
    }

    /**
     * Si prova a ottenere la sessione corrente. L'esito atteso è: sessione valida, nome utente: Mario
     */
    @Test
    public void getSessione() {
        MyDto sessione = ControllerUtente.getInstance().getSessione();
        String nome = sessione.getCampo2();
        assertEquals("Non è stato possibile ricavare la sessione", nome, "Mario");
    }

    /**
     * Si prova a registrare un utente non presente nel database. L'esito atteso è: registrazione riuscita.
     */
    @Test
    public void registraUtente() {
        char [] password = {'d', 'e', 'f', 'g'};
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().registraUtente("Bianca",
                "Aversano",
                "b.aversano@libero.it",
                "Dacia Duster",
                password,
                4,
                "+393664045465");

        assertTrue("Registrazione valida fallita", result.getKey());
    }

    /**
     * Si prova a registrare un utente con una email già presente nel database, L'esito atteso è: registrazione fallita.
     */
    @Test
    public void registraUtenteEsistente() {
        char [] password = {'a', 'b', 'c', 'd'};
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().registraUtente("Mario",
                "Rossi",
                "r.mario@gmail.com",
                "",
                password,
                0,
                "+393664045465");
        assertFalse("Registrazione invalida riuscita", result.getKey());
    }

    /**
     * Si prova a effettuare il login con email e password valide. L'esito atteso è: login effettuato.
     */
    @Test
    public void loginUtente() {
        char [] password = {'a', 'b', 'c', 'd'};
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().loginUtente("r.mario@gmail.com", password);

        assertTrue("Login valido fallito", result.getKey());
    }

    /**
     * Si prova a effettuare il login con email e password non valide. L'esito atteso è: login fallito.
     */
    @Test
    public void loginUtenteInvalido() {
        char [] password = {'a', 'b', 'c', 'e'};
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().loginUtente("r.mario@gmail.com", password);

        assertFalse("Login invalido riuscito", result.getKey());
    }

    /**
     * Si prova a inserire un viaggio con parametri corretti. L'esito atteso è: viaggio condiviso.
     * Da notare che non verrò effettuato il test con parametri non corretti in quanto essi verranno preventivamente
     * filtrati attraverso i metodi dell'interfaccia grafica.
     */
    @Test
    public void condividiViaggio() {
        AbstractMap.SimpleEntry<Boolean, String> result;
        // Per non influenzare i test successivi.
        char [] password = {'p', 'u', 'l', 'l', 'r', 'e', 'q', 'u', 'e', 's', 't'};
        ControllerUtente.getInstance().loginUtente("matteo.arnese@github.com", password);

        result = ControllerUtente.getInstance().condividiViaggio("Stazione Milano Centrale",
                "Stazione Roma Termini",
                LocalDateTime.of(2024, 8, 25, 13, 35, 0),
                LocalDateTime.of(2024, 8, 25, 20, 54, 0),
                3.49f);
        assertTrue("Condividi viaggio fallita", result.getKey());
    }

    /**
     * Si prova ad aggiornare i propri dati personali, senza modificare automobile o numero di posti disponibili.
     * L'esito atteso è: dati aggiornati correttamente.
     */
    @Test
    public void aggiornaDatiPersonali() {
        char [] password = {'a', 'b', 'c', 'd'};
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().aggiornaDatiPersonali("Mario",
                "Rossi",
                "r.mario@gmail.com",
                password,
                "+390278246538",
                "",
                0);

        assertTrue("Aggiornamento dati senza auto personali fallito", result.getKey());
    }

    /**
     * Si prova ad aggiornare i propri dati personali, modificando automobile e numero di posti disponibili, ma senza
     * viaggi futuri con prenotazioni accettate.
     * L'esito atteso è: dati aggiornati correttamente.
     */
    @Test
    public void aggiornaDatiPersonaliAutoSenzaPrenotazioni() {
        AbstractMap.SimpleEntry<Boolean, String> result;
        // Per non influenzare i test successivi.
        char [] password = {'1', '2', '3', '4'};
        ControllerUtente.getInstance().loginUtente("afileni@hotmail.com", password);
        result = ControllerUtente.getInstance().aggiornaDatiPersonali("Andrea",
                "Fileni",
                "afileni@hotmail.com",
                password,
                "+390784528167",
                "Renault C1",
                3);

        assertTrue("Aggiornamento dati personali con auto senza prenotazioni fallito", result.getKey());
    }

    /**
     * Si prova ad aggiornare i propri dati personali, modificando automobile e numero di posti disponibili, ma avendo
     * viaggi futuri con prenotazioni accettate.
     * L'esito atteso è: dati non aggiornati.
     */
    @Test
    public void aggiornaDatiPersonaliAutoConPrenotazioni() {
        // Per non influenzare i test successivi
        char [] password = {'p', 'u', 'l', 'l', 'r', 'e', 'q', 'u', 'e', 's', 't'};
        ControllerUtente.getInstance().loginUtente("matteo.arnese@github.com", password);
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().aggiornaDatiPersonali("Matteo",
                "Arnese",
                "matteo.arnese@github.com",
                password,
                "+390815551234",
                "Fiat Punto",
                4);

        assertFalse("Aggiornamento dati personali con auto con prenotazioni future accettate riuscito",
                result.getKey());
    }

    /**
     * Si prova a ricercare un viaggio con parametri corretti. L'esito atteso è: ricerca riuscita.
     * Da notare che non verrò effettuato il test con parametri non corretti in quanto essi verranno preventivamente
     * filtrati attraverso i metodi dell'interfaccia grafica.
     */
    @Test
    public void ricercaViaggio() {
        AbstractMap.SimpleEntry<Boolean, Object> result;
        result = ControllerUtente.getInstance().ricercaViaggio("Aeroporto Roma Fiumicino",
                "Fontana di trevi",
                LocalDate.of(2024, 8, 15));

        assertTrue("Ricerca viaggio fallita", result.getKey());
    }

    /**
     * Si prova a visualizzare le prenotazioni effettuate, data una sessione utente valida. L'esito atteso è:
     * visualizzazione riuscita, ritornata lista anche vuota, nessuna eccezione generata.
     */
    @Test
    public void visualizzaPrenotazioniEffettuate() {
        List<MyDto> result;
        result = ControllerUtente.getInstance().visualizzaPrenotazioniEffettuate();
        assertNotNull("Visualizza prenotazioni effettuate fallita", result);
    }

    /**
     * Si prova a prenotare un viaggio non già iniziato, non già prenotato, di cui non di è l'autista e con posti
     * disponibili. Esito atteso: prenotazione riuscita.
     */
    @Test
    public void prenotaViaggio() {
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().prenotaViaggio(2);

        assertTrue("Prenotazione viaggio valida fallita", result.getKey());
    }

    /**
     * Si prova a prenotare un viaggio già iniziato. Esito atteso: prenotazione fallita.
     */
    @Test
    public void prenotaViaggioIniziato() {
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().prenotaViaggio(3);

        assertFalse("Prenotazione viaggio già iniziato riuscita", result.getKey());
    }

    /**
     * Si prova a prenotare un viaggio già prenotato. Esito atteso: prenotazione fallita.
     */
    @Test
    public void prenotaViaggioGiaPrenotato() {
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().prenotaViaggio(1);

        assertFalse("Prenotazione viaggio già prenotato riuscita", result.getKey());
    }

    /**
     * Si prova a prenotare un viaggio di cui si è l'autista. Esito atteso: prenotazione fallita.
     */
    @Test
    public void prenotaViaggioAutista() {
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().prenotaViaggio(4);

        assertFalse("Prenotazione viaggio di cui si è l'autista riuscita", result.getKey());
    }

    /**
     * Si prova a prenotare un viaggio che non ha altri posti disponibili. Esito atteso: prenotazione fallita.
     */
    @Test
    public void prenotaViaggioNoPosti() {
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().prenotaViaggio(5);

        assertFalse("Prenotazione viaggio senza posti disponibili riuscita.", result.getKey());
    }

    /**
     * Si prova a gestire una prenotazione "in attesa" su un proprio viaggio futuro, accettandola. Esito atteso:
     * gestione prenotazione riuscita.
     * Il caso di prenotazione "in attesa" su un viaggio passato viene gestito di volta in visualizzaPrenotazioni e
     * visualizzaPrenotazioniEffettuate.
     */
    @Test
    public void gestisciPrenotazione() {
        AbstractMap.SimpleEntry<Boolean, String> result;
        result = ControllerUtente.getInstance().gestisciPrenotazione(5, true);

        assertTrue("Prenotazione valida non gestita", result.getKey());
    }

    /**
     * Si prova a valutare un utente relativamente a una prenotazione di un viaggio già cominciato. Esito atteso:
     * valutazione riuscita
     */
    @Test
    public void valutaUtente() {
        AbstractMap.SimpleEntry<Boolean, String> result;

        // L'utente 6, passeggero, valuta l'autista, utente 2, con la prenotazione 6 relativa al viaggio 6, passato.
        result = ControllerUtente.getInstance().valutaUtente(6,4,"");

        assertTrue("Valutazione utente valida fallita", result.getKey());
    }

    /**
     * Si prova a valutare un utente relativamente a una prenotazione di un viaggio futuro. Esito atteso: valutazione
     * fallita
     */
    @Test
    public void valutaUtenteFuturo() {
        AbstractMap.SimpleEntry<Boolean, String> result;

        // L'utente 6, passeggero, valuta l'autista, utente 2, con la prenotazione 1 relativa al viaggio 1, futuro.
        result = ControllerUtente.getInstance().valutaUtente(1,4,"");

        assertFalse("Valutazione utente invalida riuscita", result.getKey());
    }

    /**
     * Si prova a ottenere i viaggi condivisi. Esito atteso: visualizzazione riuscita.
     * Essendo l'utente corrente l'utente 6, avente come unico viaggio condiviso il viaggio 4, si testa la corretta
     * popolazione della lista ricevuta.
     */
    @Test
    public void visualizzaViaggiCondivisi() {
        List<MyDto> viaggiCondivisi = ControllerUtente.getInstance().visualizzaViaggiCondivisi();
        assertEquals("Visualizza viaggi condivisi fallita",
                Long.parseLong(viaggiCondivisi.getFirst().getCampo1()), 4);
    }

    /**
     * Si prova a ottenere le prenotazioni ricevute su un viaggio condiviso. Esito atteso: visualizzazione riuscita.
     */
    @Test
    public void visualizzaPrenotazioni() {
        AbstractMap.SimpleEntry<Boolean, Object> result;
        result = ControllerUtente.getInstance().visualizzaPrenotazioni(4);
        assertTrue("Visualizza prenotazioni fallita", result.getKey());
    }
}
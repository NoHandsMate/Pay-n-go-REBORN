package control;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;

import entity.FacadeEntityUtente;
import exceptions.*;
import dto.MyDto;

/**
 * Classe del package controller nel modello BCED, essa implementa tutte le funzionalità utilizzabili da un utente
 * dell'applicazione.
 */
public class ControllerUtente {

    /**
     * L'unica istanza di ControllerUtente che implementa il pattern Singleton.
     */
    private static ControllerUtente uniqueInstance;

    /**
     * Costruttore privato per impedire la creazione di istanze multiple.
     */
    private ControllerUtente() {}

    /**
     * Funzione statica per richiamare l'unica istanza di ControllerUtente o crearne una se non esiste già.
     * @return l'istanza singleton di ControllerUtente.
     */
    public static ControllerUtente getInstance(){
        if(uniqueInstance == null) {
            uniqueInstance = new ControllerUtente();
        }
        return uniqueInstance;
    }

    /**
     * Funzione che permette alle form di accedere ai dati relativi all'utente corrente.
     * @return il DTO popolato con i dati relativi all'utente corrente.
     */
    public MyDto getSessione() {
        return FacadeEntityUtente.getInstance().getSessione();
    }

    /**
     * Funzione che permette di registrare un utente nel sistema.
     * @param nome il nome dell'utente da registrare.
     * @param cognome il cognome dell'utente da registrare.
     * @param email l'indirizzo email dell'utente da registrare.
     * @param auto l'automobile dell'utente da registrare.
     * @param password la password dell'utente da registrare.
     * @param postiDisponibili il numero di posti disponibili dell'utente da registrare.
     * @param contattoTelefonico il contatto telefonico dell'utente da registrare.
     * @return una tupla <code>(Boolean, String)</code>, rappresentanti l'esito e il relativo messaggio.
     */
    public AbstractMap.SimpleEntry<Boolean, String> registraUtente(String nome,
                                                                   String cognome,
                                                                   String email,
                                                                   String auto,
                                                                   char[] password,
                                                                   Integer postiDisponibili,
                                                                   String contattoTelefonico) {

        try {
            FacadeEntityUtente.getInstance().registraUtente(nome, cognome, email, auto, password,
                    postiDisponibili, contattoTelefonico);

        } catch (RegistrationFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Registrazione effettuata con successo");
    }



    /**
     * Funzione che permette a un utente registrato di effettuare il login nel sistema.
     * @param email l'email dell'utente che deve effettuare il login.
     * @param password la password dell'utente che deve effettuare il login.
     * @return una tupla <code>(Boolean, String)</code>, rappresentanti l'esito e il relativo messaggio.
     */
    public AbstractMap.SimpleEntry<Boolean, String> loginUtente(String email,
                                                                char[] password) {

        try {
            FacadeEntityUtente.getInstance().loginUtente(email, password);
        } catch (LoginFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Login effettuato con successo.");
    }

    /**
     * Funzione che permette all'utente corrente di condividere un nuovo viaggio.
     * @param luogoPartenza il luogo di partenza del viaggio da condividere.
     * @param luogoDestinazione il luogo di destinazione del viaggio da condividere.
     * @param dataPartenza la data di partenza del viaggio da condividere.
     * @param dataArrivo la data di arrivo del viaggio da condividere.
     * @param contributoSpese il contributo spese per ogni passeggero del viaggio da condividere.
     * @return una tupla <code>(Boolean, String)</code>, rappresentanti l'esito e il relativo messaggio.
     */
    public AbstractMap.SimpleEntry<Boolean, String> condividiViaggio(String luogoPartenza,
                                                                     String luogoDestinazione,
                                                                     LocalDateTime dataPartenza,
                                                                     LocalDateTime dataArrivo,
                                                                     float contributoSpese){

        try {
            FacadeEntityUtente.getInstance().condividiViaggio(luogoPartenza, luogoDestinazione, dataPartenza, dataArrivo,
                    contributoSpese);
        } catch (CondivisioneViaggioFailedException e){
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Viaggio condiviso con successo.");

    }

    /**
     * Funzione che permette all'utente corrente di aggiornare i propri dati personali.
     * @param nome il nuovo nome dell'utente corrente.
     * @param cognome il nuovo cognome dell'utente corrente.
     * @param email il nuovo indirizzo email dell'utente corrente.
     * @param password la nuova password dell'utente corrente.
     * @param contattoTelefonico il nuovo numero di telefono dell'utente corrente.
     * @param auto la nuova automobile dell'utente corrente.
     * @param postiDisponibili il nuovo numero di posti disponibili dell'utente corrente.
     * @return una tupla <code>(Boolean, String)</code>, rappresentanti l'esito e il relativo messaggio.
     */
    public AbstractMap.SimpleEntry<Boolean, String> aggiornaDatiPersonali(String nome,
                                                                          String cognome,
                                                                          String email,
                                                                          char[] password,
                                                                          String contattoTelefonico,
                                                                          String auto,
                                                                          Integer postiDisponibili) {

        try {
            FacadeEntityUtente.getInstance().aggiornaDatiPersonali(nome, cognome, email, password, contattoTelefonico,
                    auto, postiDisponibili);

        } catch (AggiornamentoDatiFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Aggiornamento effettuato con successo.");

    }

    /**
     * Funzione che permette all'utente corrente di visualizzare le prenotazioni da lui effettuate sui viaggi degli
     * altri autisti.
     * @return una tupla <code>(Boolean, Object)</code>: nel caso di boolean <code>true</code> l'oggetto conterrà la
     * lista di prenotazioni effettuate, nel caso di boolean <code>false</code> esso conterrà il messaggio di errore.
     */
    public List<MyDto> visualizzaPrenotazioniEffettuate() {
        return FacadeEntityUtente.getInstance().visualizzaPrenotazioniEffettuate();
    }

    /**
     * Funzione che permette la ricerca di un viaggio all'interno del sistema sulla base di determinati filtri.
     * @param luogoPartenza il luogo di partenza del viaggio da ricercare.
     * @param luogoDestinazione il luogo di destinazione del viaggio da ricercare.
     * @param dataPartenza la data di partenza del viaggio da ricercare.
     * @return una tupla <code>(Boolean, Object)</code>: nel caso di boolean <code>true</code> l'oggetto conterrà la
     * lista dei viaggi trovati, nel caso di boolean <code>false</code> esso conterrà il messaggio di errore.
     */
    public AbstractMap.SimpleEntry<Boolean, Object> ricercaViaggio(String luogoPartenza,
                                                                   String luogoDestinazione,
                                                                   LocalDate dataPartenza) {

        List<MyDto> viaggiTrovatiDTO;

        try {
            viaggiTrovatiDTO = FacadeEntityUtente.getInstance().ricercaViaggio(luogoPartenza, luogoDestinazione, dataPartenza);
        } catch (RicercaViaggioFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, viaggiTrovatiDTO);
    }

    /**
     * Funzione che permette all'utente registrato di prenotare un viaggio.
     * @param idViaggio l'identificativo del viaggio che si intende prenotare.
     * @return una tupla <code>(Boolean, String)</code>, rappresentanti l'esito e il relativo messaggio.
     */
    public AbstractMap.SimpleEntry<Boolean, String> prenotaViaggio(long idViaggio) {

        try {
             FacadeEntityUtente.getInstance().prenotaViaggio(idViaggio);
         } catch (PrenotaViaggioFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
         }

         return new AbstractMap.SimpleEntry<>(true, "Prenotazione effettuata con successo");
    }

    /**
     * Funzione che permette all'utente registrato in quanto autista, di gestire una prenotazione ricevuta, accettandola
     * o rifiutandola.
     * @param idPrenotazione l'identificativo della prenotazione da gestire.
     * @param accettata <code>true</code> per accettarla, <code>false</code> per rifiutarla.
     * @return una tupla <code>(Boolean, String)</code>, rappresentanti l'esito e il relativo messaggio.
     */
    public AbstractMap.SimpleEntry<Boolean, String> gestisciPrenotazione(long idPrenotazione,boolean accettata){

        try {
            FacadeEntityUtente.getInstance().gestisciPrenotazione(idPrenotazione,accettata);
        }catch (PrenotazioneGestitaFailedException e){
            return new AbstractMap.SimpleEntry<>(false,e.getMessage());
        }
        return new AbstractMap.SimpleEntry<>(true, "Prenotazione gestita con successo.");
    }

    /**
     * Funzione che permette all'utente corrente, data una prenotazione, di valutare l'altro utente riferito dalla
     * stessa. Di conseguenza, se l'utente corrente è l'autista del viaggio associato alla prenotazione si valuterà il
     * passeggero; se l'utente corrente è il passeggero associato alla prenotazione si valuterà l'autista del viaggio
     * associato alla prenotazione.
     * @param idPrenotazione l'identificativo della prenotazione a cui si fa riferimento.
     * @param numeroStelle il numero di stelle da assegnare.
     * @param text la descrizione della valutazione.
     * @return una tupla <code>(Boolean, String)</code>, rappresentanti l'esito e il relativo messaggio.
     */
    public AbstractMap.SimpleEntry<Boolean, String> valutaUtente(long idPrenotazione,
                                                                 int numeroStelle,
                                                                 String text) {

        try {
            FacadeEntityUtente.getInstance().valutaUtente(idPrenotazione, numeroStelle, text);
        } catch (ValutazioneFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Valutazione effettuata con successo.");
    }

    /**
     * Funzione che permette all'utente corrente di visualizzare i propri viaggi condivisi.
     * @return ArrayList di DTO che racchiude le informazioni sui viaggi condivisi.
     */
    public List<MyDto> visualizzaViaggiCondivisi() {
        return FacadeEntityUtente.getInstance().visualizzaViaggiCondivisi();
    }

    /**
     * Funzione che permette all'utente corrente di visualizzare le prenotazioni effettuate su uno dei suoi viaggi.
     * @param idViaggio l'identificativo del viaggio di cui visualizzare la prenotazione.
     * @return una tupla <code>(Boolean, Object)</code>: nel caso di boolean <code>true</code> l'oggetto conterrà la
     * lista delle prenotazioni associate al viaggio, nel caso di boolean <code>false</code> esso conterrà il messaggio
     * di errore.
     */
    public AbstractMap.SimpleEntry<Boolean,Object> visualizzaPrenotazioni(long idViaggio) {
        List<MyDto> prenotazioni;
        try {
            prenotazioni = FacadeEntityUtente.getInstance().visualizzaPrenotazioni(idViaggio);
        } catch (VisualizzaPrenotazioniFailedException e) {
            return new AbstractMap.SimpleEntry<>(false,e.getMessage());
        }
        return new AbstractMap.SimpleEntry<>(true, prenotazioni);
    }
}

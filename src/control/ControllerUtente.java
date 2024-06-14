package control;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;

import entity.FacadeEntityUtente;
import exceptions.*;
import dto.*;

public class ControllerUtente {

    //Variabile statica univoca per implementare il pattern Singleton
    private static ControllerUtente uniqueInstance;

    private ControllerUtente() {}

    public static ControllerUtente getInstance(){
        if(uniqueInstance == null) {
            uniqueInstance = new ControllerUtente();
        }
        return uniqueInstance;
    }

    /**
     * Funzione che permette alle form di accedere ai dati relativi all'utente corrente
     * @return MyDto il DTO popolato con i dati relativi all'utente corrente
     */
    public MyDto getSessione() {
        return FacadeEntityUtente.getInstance().getSessione();
    }

    /**
     * Funzione che permette di registrare un utente nel sistema
     * @param nome il nome dell'utente da registrare
     * @param cognome il cognome dell'utente  da registrare
     * @param email l'indirizzo email dell'utente da registrare
     * @param auto l'automobile dell'utente da registrare
     * @param password la password dell'utente da registrare
     * @param postiDisponibili il numero di posti disponibili dell'utente da registrare
     * @param contattoTelefonico il contatto telefonico dell'utente da registrare
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
     * Funzione che permette a un utente registrato di effettuare il login nel sistema
     * @param email l'email dell'utente che deve effettuare il login
     * @param password la password dell'utente che deve effettuare il login
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
     * Funzione che permette all'utente corrente di condividere un nuovo viaggio
     * @param luogoPartenza il luogo di partenza del viaggio da condividere
     * @param luogoDestinazione il luogo di destinazione del viaggio da condividere
     * @param dataPartenza la data di partenza del viaggio da condividere
     * @param dataArrivo la data di arrivo del viaggio da condividere
     * @param contributoSpese il contributo spese per ogni passegero del viaggio da condividere
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
     * Funzione che permette all'utente corrente di aggiornare i propri dati personali
     * @param nome il nuovo nome dell'utente corrente
     * @param cognome il nuovo cognome dell'utente corrente
     * @param email il nuovo indirizzo email dell'utente corrente
     * @param auto la nuova automobile dell'utente corrente
     * @param password la nuova password dell'utente corrente
     * @param postiDisponibili il nuovo numero di posti disponibili dell'utente corrente
     * @param contattoTelefonico il nuovo numero di telefono dell'utente corrente
     */
    public AbstractMap.SimpleEntry<Boolean, String> aggiornaDatiPersonali(String nome,
                                                                          String cognome,
                                                                          String email,
                                                                          char[] password,
                                                                          String contattoTelefonico,
                                                                          String auto,
                                                                          Integer postiDisponibili) {

        try {
            FacadeEntityUtente.getInstance().aggiornaDatiPersonali(nome, cognome, email, auto, password,
                    postiDisponibili, contattoTelefonico);

        } catch (AggiornamentoDatiFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Aggiornamento effettuato con successo.");

    }

    /**
     * Funzione che permette all'utente corrente di visualizzare le prenotazioni effettuate sui suoi viaggi
     * @return prenotazioni ArrayList di DTO prenotazioni
     */
    public AbstractMap.SimpleEntry<Boolean,Object> visualizzaPrenotazioni() {
        ArrayList<MyDto> prenotazioni;
        try {
            prenotazioni = FacadeEntityUtente.getInstance().visualizzaPrenotazioni();
        } catch (VisualizzaPrenotazioniFailedException e) {
            return new AbstractMap.SimpleEntry<>(false,e.getMessage());
        }
        return new AbstractMap.SimpleEntry<>(true, prenotazioni);
    }

    /**
     * Funzione che permette all'utente corrente di visualizzare le prenotazioni effettuate ad altri viaggi
     * @return prenotazioni ArrayList di DTO prenotazioni
     */
    public ArrayList<MyDto> visualizzaPrenotazioniEffettuate() {
        return FacadeEntityUtente.getInstance().visualizzaPrenotazioniEffettuate();
    }

    /**
     * Funzione che permette la ricerca di un viaggio all'interno del sistema sulla base di determinati filtri
     * @param luogoPartenza il luogo di partenza del viaggio da ricercare
     * @param luogoDestinazione il luogo di destinazione del viaggio da ricercare
     * @param dataPartenza la data di partenza del viaggio da ricercare
     * @return ArrayList di DTO che rappresentano l'insieme di viaggi che rispettano i filtri
     */
    public AbstractMap.SimpleEntry<Boolean, Object> ricercaViaggio(String luogoPartenza, String luogoDestinazione,
                                                                   LocalDate dataPartenza) {

        ArrayList<MyDto> viaggiTrovatiDTO;

        try {
            viaggiTrovatiDTO = FacadeEntityUtente.getInstance().ricercaViaggio(luogoPartenza, luogoDestinazione, dataPartenza);
        } catch (RicercaViaggioFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, viaggiTrovatiDTO);
    }

    public AbstractMap.SimpleEntry<Boolean, String> prenotaViaggio(long idViaggio) {
         try {
             FacadeEntityUtente.getInstance().prenotaViaggio(idViaggio);
         } catch (PrenotaViaggioFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
         }

         return new AbstractMap.SimpleEntry<>(true, "Prenotazione effettuata con successo");
    }

    /**
     * Funzione che permette all'utente registrato in quanto autista, di gestire una prenotazione
     * ricevuta (accettarla o rifiutarla)
     * @param idPrenotazione l'id della prenotazione da gestire
     * @param accettata lo stato attuale della prenotazione
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
     * Funzione che permette all'utente registrato di valutare un altro utente
     * @param idPrenotazione l'id dell'utente che si intende valutare
     * @param numeroStelle il numero di stelle da assegnare
     * @param text la descrizione della valutazione
     */
    public AbstractMap.SimpleEntry<Boolean, String> valutaUtente(long idPrenotazione, int numeroStelle, String text) {
        try {
            FacadeEntityUtente.getInstance().valutaUtente(idPrenotazione, numeroStelle, text);
        } catch (ValutazioneFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Valutazione effettuata con successo.");
    }
}

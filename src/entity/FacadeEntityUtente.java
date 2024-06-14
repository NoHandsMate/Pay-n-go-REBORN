package entity;

import dto.MyDto;
import exceptions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FacadeEntityUtente {

    private static FacadeEntityUtente uniqueInstance;

    private FacadeEntityUtente() {}

    public static FacadeEntityUtente getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new FacadeEntityUtente();
        }
        return uniqueInstance;
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
    public void registraUtente(String nome, String cognome, String email, String auto, char[] password,
                               Integer postiDisponibili, String contattoTelefonico) throws RegistrationFailedException {

        GestoreUtenti.getInstance().registraUtente(nome, cognome, email, auto, password,
                postiDisponibili, contattoTelefonico);
    }

    /**
     * Funzione che permette a un utente registrato di effettuare il login nel sistema
     * @param email l'email dell'utente che deve effettuare il login
     * @param password la password dell'utente che deve effettuare il login
     */
    public void loginUtente(String email, char[] password) throws LoginFailedException {
        GestoreUtenti.getInstance().loginUtente(email, password);
    }

    /**
     * Funzione che permette all'utente corrente di condividere un nuovo viaggio
     * @param luogoPartenza il luogo di partenza del viaggio da condividere
     * @param luogoDestinazione il luogo di destinazione del viaggio da condividere
     * @param dataPartenza la data di partenza del viaggio da condividere
     * @param dataArrivo la data di arrivo del viaggio da condividere
     * @param contributoSpese il contributo spese per ogni passegero del viaggio da condividere
     * @throws CondivisioneViaggioFailedException se la condivisione del viaggio fallisce
     */
    public void condividiViaggio(String luogoPartenza,
                                 String luogoDestinazione,
                                 LocalDateTime dataPartenza,
                                 LocalDateTime dataArrivo,
                                 float contributoSpese) throws CondivisioneViaggioFailedException {
        GestoreUtenti.getInstance().condividiViaggio(luogoPartenza, luogoDestinazione, dataPartenza, dataArrivo, contributoSpese);
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
     * @throws AggiornamentoDatiFailedException se l'aggiornamento dei dati personali fallisce
     */
    public void aggiornaDatiPersonali(String nome, String cognome, String email,
                                      String auto, char[] password, Integer postiDisponibili,
                                      String contattoTelefonico) throws AggiornamentoDatiFailedException {

        GestoreUtenti.getInstance().aggiornaDatiPersonali(nome, cognome, email, auto, password,
                postiDisponibili, contattoTelefonico);

    }

    /**
     * Funzione che permette all'utente corrente di visualizzare le prenotazioni effettuate sui suoi viaggi
     * @return prenotazioni ArrayList di DTO prenotazioni
     */
    public ArrayList<MyDto> visualizzaPrenotazioni() throws VisualizzaPrenotazioniFailedException {
        return GestoreUtenti.getInstance().visualizzaPrenotazioni();
    }

    /**
     * Funzione che permette all'utente corrente di visualizzare le prenotazioni effettuate ad altri viaggi
     * @return prenotazioni ArrayList di DTO prenotazioni
     */
    public ArrayList<MyDto> visualizzaPrenotazioniEffettuate() {
        return GestoreUtenti.getInstance().visualizzaPrenotazioniEffettuate();
    }

    /**
     * Funzione che permette la ricerca di un viaggio all'interno del sistema sulla base di determinati filtri
     * @param luogoPartenza il luogo di partenza del viaggio da ricercare
     * @param luogoDestinazione il luogo di destinazione del viaggio da ricercare
     * @param dataPartenza la data di partenza del viaggio da ricercare
     * @return ArrayList di DTO che rappresentano l'insieme di viaggi che rispettano i filtri
     * @throws RicercaViaggioFailedException se la ricerca dei viaggi fallisce
     */
    public ArrayList<MyDto> ricercaViaggio(String luogoPartenza, String luogoDestinazione,
                                           LocalDate dataPartenza) throws RicercaViaggioFailedException {

        return GestoreViaggi.getInstance().ricercaViaggio(luogoPartenza, luogoDestinazione, dataPartenza);
    }

    public void prenotaViaggio(long idViaggio) throws PrenotaViaggioFailedException {
        GestoreUtenti.getInstance().prenotaViaggio(idViaggio);
    }


    /**
     * Funzione che permette all'utente registrato in quanto autista, di gestire una prenotazione
     * ricevuta (accettarla o rifiutarla)
     * @param idPrenotazione l'id della prenotazione da gestire
     * @param accettata lo stato attuale della prenotazione
     * @throws PrenotazioneGestitaFailedException se la gestione della prenotazione fallisce
     */
    public void gestisciPrenotazione(long idPrenotazione, boolean accettata) throws PrenotazioneGestitaFailedException {
            GestoreUtenti.getInstance().gestisciPrenotazione(idPrenotazione,accettata);

    }

    /**
     * Funzione che permette all'utente corrente di valutare un altro utente
     * @param idPrenotazione l'id della prenotazione della cui si vuole valutare l'utente
     * @param numeroStelle il numero di stelle da assegnare
     * @param text la descrizione della valutazione
     * @throws ValutazioneFailedException se la valutazione fallisce
     */
    public void valutaUtente(long idPrenotazione, int numeroStelle, String text) throws ValutazioneFailedException {
        GestoreUtenti.getInstance().valutaUtente(idPrenotazione, numeroStelle, text);
    }
    /**
     * Funzione che permette alle form (mediante i livelli superiori) di accedere ai dati relativi all'utente corrente
     * @return MyDto il DTO popolato con i dati relativi all'utente corrente
     */
    public MyDto getSessione() {
        return GestoreUtenti.getInstance().getSessione();
    }

}

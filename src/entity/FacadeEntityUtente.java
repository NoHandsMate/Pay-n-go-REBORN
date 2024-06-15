package entity;

import dto.MyDto;
import exceptions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe del package entity nel modello BCED, essa implementa la façade che espone a ControllerUtente tutte le
 * funzionalità utilizzabili da un utente dell'applicazione.
 */
public class FacadeEntityUtente {

    /**
     * L'unica istanza di FacadeEntityUtente che implementa il pattern Singleton.
     */
    private static FacadeEntityUtente uniqueInstance;

    /**
     * Costruttore privato per impedire la creazione di istanze multiple.
     */
    private FacadeEntityUtente() {}

    /**
     * Funzione statica per richiamare l'unica istanza di FacadeEntityUtente o crearne una se non esiste già.
     * @return l'istanza singleton di FacadeEntityUtente.
     */
    public static FacadeEntityUtente getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new FacadeEntityUtente();
        }
        return uniqueInstance;
    }

    /**
     * Funzione che permette alle form (mediante i livelli superiori) di accedere ai dati relativi all'utente corrente.
     * @return MyDto il DTO popolato con i dati relativi all'utente corrente.
     */
    public MyDto getSessione() {
        return GestoreUtenti.getInstance().getSessione();
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
     * @throws RegistrationFailedException se non è stato possibile effettuare la registrazione.
     */
    public void registraUtente(String nome,
                               String cognome,
                               String email,
                               String auto,
                               char[] password,
                               Integer postiDisponibili,
                               String contattoTelefonico) throws RegistrationFailedException {

        GestoreUtenti.getInstance().registraUtente(nome, cognome, email, auto, password,
                postiDisponibili, contattoTelefonico);
    }

    /**
     * Funzione che permette a un utente registrato di effettuare il login nel sistema.
     * @param email l'email dell'utente che deve effettuare il login.
     * @param password la password dell'utente che deve effettuare il login.
     * @throws LoginFailedException se non è stato possibile effettuare il login.
     */
    public void loginUtente(String email,
                            char[] password) throws LoginFailedException {
        GestoreUtenti.getInstance().loginUtente(email, password);
    }

    /**
     * Funzione che permette all'utente corrente di condividere un nuovo viaggio.
     * @param luogoPartenza il luogo di partenza del viaggio da condividere.
     * @param luogoDestinazione il luogo di destinazione del viaggio da condividere.
     * @param dataPartenza la data di partenza del viaggio da condividere.
     * @param dataArrivo la data di arrivo del viaggio da condividere.
     * @param contributoSpese il contributo spese per ogni passeggero del viaggio da condividere
     * @throws CondivisioneViaggioFailedException se non è stato possibile condividere il viaggio.
     */
    public void condividiViaggio(String luogoPartenza,
                                 String luogoDestinazione,
                                 LocalDateTime dataPartenza,
                                 LocalDateTime dataArrivo,
                                 float contributoSpese) throws CondivisioneViaggioFailedException {
        GestoreUtenti.getInstance().condividiViaggio(luogoPartenza, luogoDestinazione, dataPartenza, dataArrivo, contributoSpese);
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
     * @throws AggiornamentoDatiFailedException se non è stato possibile aggiornare i dati personali.
     */
    public void aggiornaDatiPersonali(String nome,
                                      String cognome,
                                      String email,
                                      char[] password,
                                      String contattoTelefonico,
                                      String auto,
                                      Integer postiDisponibili) throws AggiornamentoDatiFailedException {

        GestoreUtenti.getInstance().aggiornaDatiPersonali(nome, cognome, email, password, contattoTelefonico,
                auto, postiDisponibili);
    }

    /**
     * Funzione che permette all'utente corrente di visualizzare le prenotazioni da lui effettuate sui viaggi degli
     * altri autisti.
     * @return la lista di prenotazioni effettuate.
     * @throws  VisualizzaPrenotazioniEffettuateFailedException se non è stato possibile ottenere la lista di
     * prenotazioni effettuate.
     */
    public List<MyDto> visualizzaPrenotazioniEffettuate() throws VisualizzaPrenotazioniEffettuateFailedException {
        return GestoreUtenti.getInstance().visualizzaPrenotazioniEffettuate();
    }

    /**
     * Funzione che permette la ricerca di un viaggio all'interno del sistema sulla base di determinati filtri.
     * @param luogoPartenza il luogo di partenza del viaggio da ricercare.
     * @param luogoDestinazione il luogo di destinazione del viaggio da ricercare.
     * @param dataPartenza la data di partenza del viaggio da ricercare.
     * @return ArrayList di DTO che rappresentano l'insieme di viaggi che rispettano i filtri.
     * @throws RicercaViaggioFailedException se non è stato possibile effettuare la ricerca del viaggio.
     */
    public List<MyDto> ricercaViaggio(String luogoPartenza,
                                      String luogoDestinazione,
                                      LocalDate dataPartenza) throws RicercaViaggioFailedException {

        return GestoreViaggi.getInstance().ricercaViaggio(luogoPartenza, luogoDestinazione, dataPartenza);
    }

    /**
     * Funzione che permette all'utente registrato di prenotare un viaggio.
     * @param idViaggio l'identificativo del viaggio che si intende prenotare.
     * @throws PrenotaViaggioFailedException se non è stato possibile prenotare il viaggio.
     */
    public void prenotaViaggio(long idViaggio) throws PrenotaViaggioFailedException {
        GestoreUtenti.getInstance().prenotaViaggio(idViaggio);
    }


    /**
     * Funzione che permette all'utente registrato in quanto autista, di gestire una prenotazione ricevuta, accettandola
     * o rifiutandola.
     * @param idPrenotazione l'identificativo della prenotazione da gestire.
     * @param accettata <code>true</code> per accettarla, <code>false</code> per rifiutarla.
     * @throws PrenotazioneGestitaFailedException se non è stato possibile gestire la prenotazione.
     */
    public void gestisciPrenotazione(long idPrenotazione, boolean accettata) throws PrenotazioneGestitaFailedException {
            GestoreUtenti.getInstance().gestisciPrenotazione(idPrenotazione,accettata);
    }

    /**
     * Funzione che permette all'utente registrato, data una prenotazione, di valutare l'altro utente riferito dalla
     * stessa. Di conseguenza, se si è l'autista del viaggio associato alla prenotazione si valuterà il passeggero; se
     * si è il passeggero associato alla prenotazione si valuterà l'autista del viaggio associato alla prenotazione.
     * @param idPrenotazione l'identificativo della prenotazione a cui si fa riferimento.
     * @param numeroStelle il numero di stelle da assegnare.
     * @param text la descrizione della valutazione.
     * @throws ValutazioneFailedException se non è stato possibile valutare l'utente.
     */
    public void valutaUtente(long idPrenotazione,
                             int numeroStelle,
                             String text) throws ValutazioneFailedException {
        GestoreUtenti.getInstance().valutaUtente(idPrenotazione, numeroStelle, text);
    }

    /**
     * Funzione che permette all'utente corrente di visualizzare i propri viaggi condivisi.
     * @return ArrayList di DTO che racchiude le informazioni sui viaggi condivisi.
     */
    public List<MyDto> visualizzaViaggiCondivisi(){
        return GestoreUtenti.getInstance().visualizzaViaggiCondivisi();
    }

    /**
     * Funzione che permette all'utente corrente di visualizzare le prenotazioni effettuate su uno dei suoi viaggi.
     * @param idViaggio l'identificativo del viaggio di cui visualizzare la prenotazione.
     * @return la lista di prenotazioni.
     * @throws VisualizzaPrenotazioniFailedException se non è stato possibile ottenere la lista di prenotazioni.
     */
    public List<MyDto> visualizzaPrenotazioni(long idViaggio) throws VisualizzaPrenotazioniFailedException {
        return GestoreUtenti.getInstance().visualizzaPrenotazioni(idViaggio);
    }

}

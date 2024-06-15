package entity;

import database.ValutazioneDAO;
import database.ViaggioDAO;
import exceptions.*;
import database.UtenteRegistratoDAO;
import dto.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GestoreUtenti {

    private static GestoreUtenti uniqueInstance;

    private static final String NATURALDATEFORMAT = "dd/MM/yyyy HH:mm:ss";

    private GestoreUtenti() {}

    public static GestoreUtenti getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new GestoreUtenti();
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
     * @throws RegistrationFailedException se la registrazione non è avvenuta correttamente
     */
    public void registraUtente(String nome, String cognome, String email,
                               String auto, char[] password, Integer postiDisponibili,
                               String contattoTelefonico) throws RegistrationFailedException {
        UtenteRegistratoDAO utenteDAO = new UtenteRegistratoDAO();
        try {
            utenteDAO.createUtenteRegistrato(nome, cognome, contattoTelefonico, email, auto, postiDisponibili,
                    new String(password));

            EntityUtenteRegistrato utenteRegistrato = new EntityUtenteRegistrato(utenteDAO);
            aggiornaUtenteCorrente(utenteRegistrato);
        } catch (DatabaseException e) {
            if (e.isVisible())
                throw new RegistrationFailedException("Registrazione Utente fallita: " + e.getMessage());
            throw new RegistrationFailedException("Registrazione Utente fallita.");
        }
    }


    /**
     * Funzione che permette a un utente registrato di effettuare il login nel sistema
     * @param email l'email dell'utente che deve effettuare il login
     * @param password la password dell'utente che deve effettuare il login
     * @throws LoginFailedException se il login dell'utente fallisce
     */
    public void loginUtente(String email, char[] password) throws LoginFailedException {
        UtenteRegistratoDAO utenteDAO = new UtenteRegistratoDAO();
        try {
            utenteDAO.readUtenteRegistrato(email, new String(password));
            EntityUtenteRegistrato utenteRegistrato = new EntityUtenteRegistrato(utenteDAO);
            aggiornaUtenteCorrente(utenteRegistrato);
        } catch (DatabaseException e) {
            if (e.isVisible())
                throw new LoginFailedException("Login Utente fallito: %s" + e.getMessage());
            throw new LoginFailedException("Login Utente fallito.");
        }
    }

    /**
     * Funzione che permette al gestore dell'applicazione di generare un report di valutazioni per tutti gli utenti del
     * sistema
     * @return reportUtenti un ArrayList di ArrayList di DTO (matrice) che rappresenta il report
     * @throws ReportUtentiFailedException se la generazione del report utenti fallisce
     */
    public ArrayList<MyDto> generaReportUtenti() throws ReportUtentiFailedException {
        ArrayList<MyDto> reportUtenti = new ArrayList<>();
        try{
            ArrayList<UtenteRegistratoDAO> utentiRegistratiDAO = UtenteRegistratoDAO.getUtentiRegistrati();

            for(UtenteRegistratoDAO utenteRegistratoDAO : utentiRegistratiDAO){
                EntityUtenteRegistrato utenteRegistrato = new EntityUtenteRegistrato(utenteRegistratoDAO);
                utenteRegistrato.popolaValutazioni();
                ArrayList<EntityValutazione> listaValutazioni = utenteRegistrato.getValutazioni();
                MyDto utente = new MyDto();
                float mediaStelle = 0f;
                int numValutazioni = listaValutazioni.size();
                if (numValutazioni > 0) {
                    for (EntityValutazione valutazione : listaValutazioni) {
                        mediaStelle = mediaStelle + valutazione.getNumeroStelle();
                    }
                    mediaStelle = mediaStelle / numValutazioni;
                    utente.setCampo4(String.format("%.2f", mediaStelle).replace(',', '.'));
                }
                else {
                    utente.setCampo4("Nessuna valutazione");
                }
                utente.setCampo1(String.valueOf(utenteRegistrato.getId()));
                utente.setCampo2(String.format("%s %s", utenteRegistrato.getNome(), utenteRegistrato.getCognome()));
                utente.setCampo3(utenteRegistrato.getEmail());
                reportUtenti.add(utente);
            }
        } catch (DatabaseException e) {
            if (e.isVisible())
                throw new ReportUtentiFailedException("Generazione report utenti fallita: " + e.getMessage());
            throw new ReportUtentiFailedException("Generazione report utenti fallita.");
        }
        return reportUtenti;
    }

    /**
     * Funzione d'ausilio a <close> generaReportUtenti </close> che permette di visualizzare tutte le valutazioni di
     * un utente
     * @param idUtente l'utente del quale si vogliono visualizzare le valutazioni
     * @return un ArrayList di DTO con le valutazioni dell'utente in caso di successo
     */
    public ArrayList<MyDto> visualizzaValutazioniUtente(long idUtente) throws VisualizzaValutazioniFailedException {
        ArrayList<MyDto> valutazioniUtente = new ArrayList<>();
        try {
            UtenteRegistratoDAO utenteDAO = new UtenteRegistratoDAO(idUtente);
            EntityUtenteRegistrato utenteRegistrato = new EntityUtenteRegistrato(utenteDAO);
            ArrayList<EntityValutazione> listaValutazioni = utenteRegistrato.visualizzaValutazioni();
            for (EntityValutazione valutazione : listaValutazioni) {
                MyDto valutazioneDTO = new MyDto();
                valutazioneDTO.setCampo1(String.valueOf(valutazione.getId()));
                valutazioneDTO.setCampo2(String.valueOf(valutazione.getNumeroStelle()));
                valutazioneDTO.setCampo3(valutazione.getDescrizione());
                valutazioniUtente.add(valutazioneDTO);
            }
        } catch (DatabaseException e) {
            if (e.isVisible())
                throw new VisualizzaValutazioniFailedException("Generazione report valutazioni fallita: " + e.getMessage());
            throw new VisualizzaValutazioniFailedException("Generazione report valutazioni fallita.");
        }
        return valutazioniUtente;
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

        EntityUtenteRegistrato utenteCorrente = Sessione.getInstance().getUtenteCorrente();
        try {
            utenteCorrente.condividiViaggio(luogoPartenza, luogoDestinazione, dataPartenza, dataArrivo, contributoSpese);
        } catch (DatabaseException e) {
            if(e.isVisible()) {
                throw new CondivisioneViaggioFailedException("Condivisione viaggio fallita: " + e.getMessage());
            }
            throw new CondivisioneViaggioFailedException("Condivisione viaggio fallita");
        }
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
    public void aggiornaDatiPersonali(String nome,
                                      String cognome,
                                      String email,
                                      String auto,
                                      char[] password,
                                      Integer postiDisponibili,
                                      String contattoTelefonico) throws AggiornamentoDatiFailedException {
        try {
            EntityUtenteRegistrato utenteCorrente = Sessione.getInstance().getUtenteCorrente();
            utenteCorrente.aggiornaDatiPersonali(nome, cognome, email, auto, password,
                    postiDisponibili, contattoTelefonico);

            aggiornaUtenteCorrente(utenteCorrente);
        } catch (DatabaseException e) {
            if (e.isVisible())
                throw new AggiornamentoDatiFailedException("Aggiornamento Dati fallito: " + e.getMessage());
            throw new AggiornamentoDatiFailedException("Aggiornamento Dati fallito.");
        }
    }

    /**
     * Funzione che permette all'utente corrente di visualizzare le prenotazioni effettuate ad altri viaggi
     * @return prenotazioni ArrayList di DTO prenotazioni
     */
    public ArrayList<MyDto> visualizzaPrenotazioniEffettuate() throws VisualizzaPrenotazioniEffettuateFailedException {
        EntityUtenteRegistrato utenteCorrente = Sessione.getInstance().getUtenteCorrente();
        ArrayList<EntityPrenotazione> prenotazioniUtente = utenteCorrente.getPrenotazioni();
        ArrayList<MyDto> prenotazioni = new ArrayList<>();
        try {
            for (EntityPrenotazione prenotazione : prenotazioniUtente) {
                prenotazione.popolaViaggio();
                MyDto prenotazioneDto = new MyDto();
                prenotazioneDto.setCampo1(String.valueOf(prenotazione.getId()));
                prenotazioneDto.setCampo2(prenotazione.getViaggioPrenotato().getAutista().getNome() + " " +
                        prenotazione.getViaggioPrenotato().getAutista().getCognome());
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(NATURALDATEFORMAT);
                prenotazioneDto.setCampo3(String.format("%d: %s -> %s - %s", prenotazione.getViaggioPrenotato().getId(),
                        prenotazione.getViaggioPrenotato().getLuogoPartenza(),
                        prenotazione.getViaggioPrenotato().getLuogoDestinazione(),
                        prenotazione.getViaggioPrenotato().getDataPartenza().format(dateTimeFormatter)));
                prenotazioneDto.setCampo4(prenotazione.isAccettata()? "Accettata" : "In attesa");
                prenotazioni.add(prenotazioneDto);
            }
        } catch (DatabaseException e) {
            if(e.isVisible())
                throw new VisualizzaPrenotazioniEffettuateFailedException(
                        "Visualizzazione prenotazioni effettuate fallita: " + e.getMessage());
            throw new VisualizzaPrenotazioniEffettuateFailedException("Visualizzazione prenotazioni effettuate fallita.");
        }
        return prenotazioni;
    }

    /**
     * Funzione che permette all'utente corrente di prenotare un viaggio
     * @param idViaggio l'id del viaggio che si intende prenotare
     * @throws PrenotaViaggioFailedException nel caso in cui la prenotazione non vada a buon fine
     */
    public void prenotaViaggio(long idViaggio) throws PrenotaViaggioFailedException {
        EntityUtenteRegistrato utenteCorrente = Sessione.getInstance().getUtenteCorrente();
        try {
            utenteCorrente.prenotaViaggio(idViaggio);
            aggiornaUtenteCorrente(utenteCorrente);
        } catch (DatabaseException e) {
            if(e.isVisible()) {
                throw new PrenotaViaggioFailedException("Prenotazione viaggio fallita: " + e.getMessage());
            }
            throw new PrenotaViaggioFailedException("Prenotazione viaggio fallita");
        }
    }

    /**
     * Funzione che permette all'utente registrato in quanto autista, di gestire una prenotazione
     * ricevuta (accettarla o rifiutarla)
     * @param idPrenotazione l'id della prenotazione da gestire
     * @param accettata lo stato attuale della prenotazione
     * @throws PrenotazioneGestitaFailedException se la gestione della prenotazione fallisce
     */
    public void gestisciPrenotazione(long idPrenotazione,boolean accettata) throws PrenotazioneGestitaFailedException{
        EntityUtenteRegistrato utenteCorrente = Sessione.getInstance().getUtenteCorrente();
        try{
            utenteCorrente.gestisciPrenotazione(idPrenotazione,accettata);
        } catch (DatabaseException e){
            if(e.isVisible()) {
                throw new PrenotazioneGestitaFailedException("Gestione prenotazione fallita: " + e.getMessage());
            }
            throw new PrenotazioneGestitaFailedException("Gestione prenotazione fallita.");
        }
    }

    /**
     * Funzione che permette all'utente corrente di valutare un altro utente
     * @param idPrenotazione l'id della prenotazione della cui si vuole valutare l'utente
     * @param numeroStelle il numero di stelle da assegnare
     * @param text la descrizione della valutazione
     * @throws ValutazioneFailedException se la valutazione fallisce
     */
    public void valutaUtente(long idPrenotazione, int numeroStelle, String text) throws ValutazioneFailedException {

        EntityUtenteRegistrato utenteCorrente = Sessione.getInstance().getUtenteCorrente();

        try {

            utenteCorrente.valutaUtente(idPrenotazione, numeroStelle, text);
        } catch (DatabaseException e) {
            if (e.isVisible()) {
                throw new ValutazioneFailedException("Valutazione fallita: " + e.getMessage());
            }

            throw new ValutazioneFailedException("Valutazione fallita");
        }
    }

    /**
     * Funzione che permette all'utente corrente di visualizzare i propri viaggi condivisi
     * @return ArrayList di DTO che racchiude le informazioni sui viaggi condivisi
     */
    public ArrayList<MyDto> visualizzaViaggiCondivisi(){
        EntityUtenteRegistrato utenteCorrente = Sessione.getInstance().getUtenteCorrente();
        ArrayList<EntityViaggio> viaggiCondivisi = utenteCorrente.getViaggiCondivisi();
        ArrayList<MyDto> viaggiCondivisiDto = new ArrayList<>();
        for(EntityViaggio viaggio : viaggiCondivisi){
            MyDto viaggioDTO = new MyDto();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(NATURALDATEFORMAT);
            viaggioDTO.setCampo1(String.valueOf(viaggio.getId()));
            viaggioDTO.setCampo2(viaggio.getLuogoPartenza());
            viaggioDTO.setCampo3(viaggio.getLuogoDestinazione());
            viaggioDTO.setCampo4(viaggio.getDataPartenza().format(dateTimeFormatter));
            viaggioDTO.setCampo5(viaggio.getDataArrivo().format(dateTimeFormatter));
            viaggioDTO.setCampo6(String.format("%.2f", viaggio.getContributoSpese()).replace(',', '.'));
            viaggiCondivisiDto.add(viaggioDTO);
        }
        return viaggiCondivisiDto;
    }

    /**
     * Funzione che permette all'utente corrente di visualizzare le prenotazioni effettuate su uno dei suoi viaggi.
     * @param idViaggio l'identificativo del viaggio di cui visualizzare la prenotazione.
     * @return prenotazioni ArrayList di DTO prenotazioni
     */
    public ArrayList<MyDto> visualizzaPrenotazioni(long idViaggio) throws VisualizzaPrenotazioniFailedException{
        ArrayList<EntityPrenotazione> prenotazioni;
        ArrayList<MyDto> prenotazioniDTO = new ArrayList<>();
        try {
            EntityUtenteRegistrato utenteCorrente = Sessione.getInstance().getUtenteCorrente();
            prenotazioni = utenteCorrente.visualizzaPrenotazioni(idViaggio);
            for (EntityPrenotazione prenotazione : prenotazioni) {
                MyDto prenotazioneDTO = new MyDto();
                prenotazioneDTO.setCampo1(String.valueOf(prenotazione.getId()));
                prenotazioneDTO.setCampo2(String.format("%s %s", prenotazione.getPasseggero().getNome(),
                        prenotazione.getPasseggero().getCognome()));
                prenotazioneDTO.setCampo3(prenotazione.isAccettata()? "Accettata" : "In attesa");
                prenotazioniDTO.add(prenotazioneDTO);
            }
        } catch (DatabaseException e) {
            if(e.isVisible()) {
                throw new VisualizzaPrenotazioniFailedException("Visualizza prenotazioni fallita: " + e.getMessage());
            }
            throw new VisualizzaPrenotazioniFailedException("Visualizza prenotazioni fallita.");
        }
        return prenotazioniDTO;
    }

    /**
     * Funzione di supporto alle funzioni registraUtente e loginUtente che permette di evitare la ridondanza del
     * codice relativa all'aggiornamento dell'utente corrente, necessaria in entrambe le funzioni suddette.
     * @param utenteCorrente DAO a partire dalla quale costruire il nuovo utente corrente
     * @throws DatabaseException se si è verificato un errore nel salvataggio nel database del nuovo utente corrente
     */
    private void aggiornaUtenteCorrente(EntityUtenteRegistrato utenteCorrente) throws DatabaseException {
        utenteCorrente.popolaPrenotazioni();
        utenteCorrente.popolaViaggiCondivisi();
        utenteCorrente.popolaValutazioni();
        Sessione.getInstance().setUtenteCorrente(utenteCorrente);
    }

    /**
     * Funzione che permette alle form (mediante i livelli superiori) di accedere ai dati relativi all'utente corrente
     * @return MyDto il DTO popolato con i dati relativi all'utente corrente
     */
    public MyDto getSessione() {
        EntityUtenteRegistrato sessioneCorrente = Sessione.getInstance().getUtenteCorrente();
        return new MyDto(String.valueOf(sessioneCorrente.getId()),
                sessioneCorrente.getNome(),
                sessioneCorrente.getCognome(),
                sessioneCorrente.getEmail(),
                sessioneCorrente.getPassword(),
                sessioneCorrente.getContattoTelefonico(),
                sessioneCorrente.getAutomobile(),
                String.valueOf(sessioneCorrente.getPostiDisponibili()));
    }
}
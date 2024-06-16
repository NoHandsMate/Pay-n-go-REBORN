package entity;

import exceptions.*;
import database.UtenteRegistratoDAO;
import dto.MyDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe del package entity nel modello BCED, essa implementa l'information expert degli utenti.
 */
public class GestoreUtenti {

    /**
     * L'unica istanza di GestoreUtenti che implementa il pattern Singleton.
     */
    private static GestoreUtenti uniqueInstance;

    /**
     * Costante utilizzata da DateTimeFormatter per la conversione da data simbolica a stringa.
     */
    private static final String NATURALDATEFORMAT = "dd/MM/yyyy HH:mm:ss";

    /**
     * Costruttore privato per impedire la creazione di istanze multiple.
     */
    private GestoreUtenti() {}

    /**
     * Funzione statica per richiamare l'unica istanza di GestoreUtenti o crearne una se non esiste già.
     * @return l'istanza singleton di GestoreUtenti.
     */
    public static GestoreUtenti getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new GestoreUtenti();
        }
        return uniqueInstance;
    }

    /**
     * Funzione che permette alle form (mediante i livelli superiori) di accedere ai dati relativi all'utente corrente.
     * @return MyDto il DTO popolato con i dati relativi all'utente corrente.
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
     * Funzione che permette a un utente registrato di effettuare il login nel sistema.
     * @param email l'email dell'utente che deve effettuare il login.
     * @param password la password dell'utente che deve effettuare il login.
     * @throws LoginFailedException se non è stato possibile effettuare il login.
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
     * Funzione che permette al gestore dell'applicazione di generare un report di valutazioni sommario per tutti gli
     * utenti del sistema.
     * @return il report di valutazione sommario per tutti gli utenti del sistema.
     * @throws ReportUtentiFailedException se non è stato possibile generare il report di valutazione sommario.
     */
    public List<MyDto> generaReportUtenti() throws ReportUtentiFailedException {
        List<MyDto> reportUtenti = new ArrayList<>();
        try{
            List<UtenteRegistratoDAO> utentiRegistratiDAO = UtenteRegistratoDAO.getUtentiRegistrati();

            for(UtenteRegistratoDAO utenteRegistratoDAO : utentiRegistratiDAO){
                EntityUtenteRegistrato utenteRegistrato = new EntityUtenteRegistrato(utenteRegistratoDAO);
                utenteRegistrato.popolaValutazioni();
                List<EntityValutazione> listaValutazioni = utenteRegistrato.getValutazioni();
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
     * Funzione che, dopo aver visualizzato il report di valutazione sommario di {@link #generaReportUtenti()
     * generaReportUtenti}, permette di visualizzare i dettagli di tutte le valutazioni associate a un utente.
     * @param idUtente l'identificativo dell'utente del quale si vogliono visualizzare le valutazioni.
     * @return l'elenco valutazioni dell'utente.
     * @throws VisualizzaValutazioniFailedException se non è stato possibile creare l'elenco delle valutazioni
     * dell'utente.
     */
    public List<MyDto> visualizzaValutazioniUtente(long idUtente) throws VisualizzaValutazioniFailedException {
        List<MyDto> valutazioniUtente = new ArrayList<>();
        try {
            UtenteRegistratoDAO utenteDAO = new UtenteRegistratoDAO(idUtente);
            EntityUtenteRegistrato utenteRegistrato = new EntityUtenteRegistrato(utenteDAO);
            List<EntityValutazione> listaValutazioni = utenteRegistrato.visualizzaValutazioni();
            for (EntityValutazione valutazione : listaValutazioni) {
                MyDto valutazioneDTO = new MyDto();
                valutazioneDTO.setCampo1(String.valueOf(valutazione.getId()));
                valutazioneDTO.setCampo2(String.valueOf(valutazione.getNumeroStelle()));
                valutazioneDTO.setCampo3(valutazione.getDescrizione());
                valutazioniUtente.add(valutazioneDTO);
            }
        } catch (DatabaseException e) {
            if (e.isVisible())
                throw new VisualizzaValutazioniFailedException("Generazione report valutazioni fallita: " +
                        e.getMessage());
            throw new VisualizzaValutazioniFailedException("Generazione report valutazioni fallita.");
        }
        return valutazioniUtente;
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

        EntityUtenteRegistrato utenteCorrente = Sessione.getInstance().getUtenteCorrente();
        try {
            utenteCorrente.condividiViaggio(luogoPartenza, luogoDestinazione, dataPartenza, dataArrivo, contributoSpese);
            aggiornaUtenteCorrente(utenteCorrente);
        } catch (DatabaseException e) {
            if(e.isVisible()) {
                throw new CondivisioneViaggioFailedException("Condivisione viaggio fallita: " + e.getMessage());
            }
            throw new CondivisioneViaggioFailedException("Condivisione viaggio fallita");
        }
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
     * Funzione che permette all'utente corrente di visualizzare le prenotazioni da lui effettuate sui viaggi degli
     * altri autisti.
     * @return la lista di prenotazioni effettuate.
     */
    public List<MyDto> visualizzaPrenotazioniEffettuate() {
        EntityUtenteRegistrato utenteCorrente = Sessione.getInstance().getUtenteCorrente();
        List<EntityPrenotazione> prenotazioniUtente = utenteCorrente.getPrenotazioni();
        List<MyDto> prenotazioni = new ArrayList<>();

        for (EntityPrenotazione prenotazione : prenotazioniUtente) {
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
        return prenotazioni;
    }

    /**
     * Funzione che permette all'utente registrato di prenotare un viaggio.
     * @param idViaggio l'identificativo del viaggio che si intende prenotare.
     * @throws PrenotaViaggioFailedException se non è stato possibile prenotare il viaggio.
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
     * Funzione che permette all'utente registrato in quanto autista, di gestire una prenotazione ricevuta, accettandola
     * o rifiutandola.
     * @param idPrenotazione l'identificativo della prenotazione da gestire.
     * @param accettata <code>true</code> per accettarla, <code>false</code> per rifiutarla.
     * @throws PrenotazioneGestitaFailedException se non è stato possibile gestire la prenotazione.
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
     * Funzione che permette all'utente corrente, data una prenotazione, di valutare l'altro utente riferito dalla
     * stessa. Di conseguenza, se l'utente corrente è l'autista del viaggio associato alla prenotazione si valuterà il
     * passeggero; se l'utente corrente è il passeggero associato alla prenotazione si valuterà l'autista del viaggio
     * associato alla prenotazione.
     * @param idPrenotazione l'identificativo della prenotazione a cui si fa riferimento.
     * @param numeroStelle il numero di stelle da assegnare.
     * @param text la descrizione della valutazione.
     * @throws ValutazioneFailedException se non è stato possibile valutare l'utente.
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
     * Funzione che permette all'utente corrente di visualizzare i propri viaggi condivisi.
     * @return ArrayList di DTO che racchiude le informazioni sui viaggi condivisi.
     */
    public List<MyDto> visualizzaViaggiCondivisi(){
        EntityUtenteRegistrato utenteCorrente = Sessione.getInstance().getUtenteCorrente();
        List<EntityViaggio> viaggiCondivisi = utenteCorrente.getViaggiCondivisi();
        List<MyDto> viaggiCondivisiDto = new ArrayList<>();
        for(EntityViaggio viaggio : viaggiCondivisi){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(NATURALDATEFORMAT);
            MyDto viaggioDTO = new MyDto(String.valueOf(viaggio.getId()),
                    viaggio.getLuogoPartenza(),
                    viaggio.getLuogoDestinazione(),
                    viaggio.getDataPartenza().format(dateTimeFormatter),
                    viaggio.getDataArrivo().format(dateTimeFormatter),
                    String.format("%.2f €", viaggio.getContributoSpese()).replace(',', '.'),
                    null,
                    null);
            viaggiCondivisiDto.add(viaggioDTO);
        }
        return viaggiCondivisiDto;
    }

    /**
     * Funzione che permette all'utente corrente di visualizzare le prenotazioni effettuate su uno dei suoi viaggi.
     * @param idViaggio l'identificativo del viaggio di cui visualizzare la prenotazione.
     * @return la lista di prenotazioni.
     * @throws VisualizzaPrenotazioniFailedException se non è stato possibile ottenere la lista di prenotazioni.
     */
    public List<MyDto> visualizzaPrenotazioni(long idViaggio) throws VisualizzaPrenotazioniFailedException{
        List<EntityPrenotazione> prenotazioni;
        List<MyDto> prenotazioniDTO = new ArrayList<>();
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
     * Funzione di supporto alle funzioni {@link #registraUtente(String, String, String, String, char[], Integer, String)
     * registraUtente} e {@link #loginUtente(String, char[]) loginUtente} che permette di evitare la ridondanza del
     * codice relativa all'aggiornamento dell'utente corrente, necessaria in entrambe le funzioni suddette. L'utente
     * corrente viene popolato con le sue prenotazioni, i suoi viaggi condivisi e le sue valutazioni.
     * @param utenteCorrente l'utente corrente da popolare.
     * @throws DatabaseException se si è verificato un errore nel popolamento dei campi dell'utente corrente.
     */
    private void aggiornaUtenteCorrente(EntityUtenteRegistrato utenteCorrente) throws DatabaseException {
        utenteCorrente.popolaPrenotazioni();
        utenteCorrente.popolaViaggiCondivisi();
        utenteCorrente.popolaValutazioni();
        Sessione.getInstance().setUtenteCorrente(utenteCorrente);
    }
}
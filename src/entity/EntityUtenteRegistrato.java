package entity;

import database.*;
import exceptions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EntityUtenteRegistrato {
    private long id;
    private String nome;
    private String cognome;
    private String contattoTelefonico;
    private String email;
    private String password;
    private String automobile;
    private int postiDisponibili;
    private ArrayList<EntityViaggio> viaggiCondivisi;
    private ArrayList<EntityPrenotazione> prenotazioni;
    private ArrayList<EntityValutazione> valutazioni;

    /**
     * Costruttore di default
     */
    public EntityUtenteRegistrato() {}


    /**
     * Costruttore di EntityUtenteRegistrato a partire da una utenteRegistratoDAO popolata
     * @param utenteRegistratoDAO che permette di popolare l'istanza corrente di EntityUtenteRegistrato
     */
    public EntityUtenteRegistrato(UtenteRegistratoDAO utenteRegistratoDAO) {
        this.id = utenteRegistratoDAO.getIdUtenteRegistrato();
        this.nome = utenteRegistratoDAO.getNome();
        this.cognome = utenteRegistratoDAO.getCognome();
        this.contattoTelefonico = utenteRegistratoDAO.getContattoTelefonico();
        this.email = utenteRegistratoDAO.getEmail();
        this.automobile = utenteRegistratoDAO.getAutomobile();
        this.password = utenteRegistratoDAO.getPassword();
        this.postiDisponibili = utenteRegistratoDAO.getPostiDisponibili();
    }


    /**
     * Funzione che carica dal database le prenotazioni effettuate dall'utente
     * @throws DatabaseException nel caso in cui ci sia stato un errore nel DB durante il prelievo delle prenotazioni
     */
    public void popolaPrenotazioni() throws DatabaseException {
        ArrayList<PrenotazioneDAO> prenotazioni = PrenotazioneDAO.getPrenotazioni();
        this.prenotazioni = new ArrayList<>();
        for (PrenotazioneDAO prenotazioneDAO : prenotazioni) {
            if (prenotazioneDAO.getIdPasseggero() == this.id) {
                EntityPrenotazione prenotazione = new EntityPrenotazione(prenotazioneDAO);
                this.prenotazioni.add(prenotazione);
            }
        }
    }


    /**
     * Funzione che carica dal database le valutazioni dell'utente
     * @throws DatabaseException nel caso in cui ci sia stato un errore nel DB durante il prelievo delle valutazioni
     */
    public void popolaValutazioni() throws DatabaseException {
        ArrayList<ValutazioneDAO> valutazioni = ValutazioneDAO.getValutazioni();
        this.valutazioni = new ArrayList<>();
        for (ValutazioneDAO valutazioneDAO : valutazioni) {
            if (valutazioneDAO.getIdUtente() == this.id) {
                EntityValutazione valutazione = new EntityValutazione(valutazioneDAO);
                this.valutazioni.add(valutazione);
            }
        }

    }


    /**
     * Funzione che carica dal database i viaggi condivisi dall'utente
     * @throws DatabaseException nel caso in cui ci sia stato un errore nel DB durante il prelievo delle valutazioni
     */
    public void popolaViaggiCondivisi() throws DatabaseException {
        ArrayList<ViaggioDAO> viaggi = ViaggioDAO.getViaggi();
        this.viaggiCondivisi = new ArrayList<>();
        for (ViaggioDAO viaggioDAO : viaggi) {
            if (viaggioDAO.getIdAutista() == this.id) {
                EntityViaggio viaggio = new EntityViaggio(viaggioDAO);
                this.viaggiCondivisi.add(viaggio);
            }
        }

    }

    /**
     * Funzione che permette all'utente corrente di condividere un nuovo viaggio
     * @param luogoPartenza il luogo di partenza del viaggio da condividere
     * @param luogoDestinazione il luogo di destinazione del viaggio da condividere
     * @param dataPartenza la data di partenza del viaggio da condividere
     * @param dataArrivo la data di arrivo del viaggio da condividere
     * @param contributoSpese il contributo spese per ogni passegero del viaggio da condividere
     * @throws DatabaseException se la condivisione del viaggio fallisce
     */
    public void condividiViaggio(String luogoPartenza, String luogoDestinazione, LocalDateTime dataPartenza,
                                 LocalDateTime dataArrivo, float contributoSpese) throws DatabaseException {

        EntityViaggio viaggioCondiviso = new EntityViaggio();
        viaggioCondiviso.setLuogoPartenza(luogoPartenza);
        viaggioCondiviso.setLuogoDestinazione(luogoDestinazione);
        viaggioCondiviso.setDataPartenza(dataPartenza);
        viaggioCondiviso.setDataArrivo(dataArrivo);
        viaggioCondiviso.setContributoSpese(contributoSpese);
        viaggioCondiviso.setAutista(this);

        viaggioCondiviso.salvaInDB();

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
     * @throws DatabaseException se l'aggiornamento dei dati personali fallisce
     */
    public void aggiornaDatiPersonali(String nome, String cognome, String email,
                                      String auto, char[] password, Integer postiDisponibili,
                                      String contattoTelefonico) throws DatabaseException {

        if (!this.automobile.equals(auto) || this.postiDisponibili != postiDisponibili) {
            this.eliminaViaggiCondivisi();
        }
        UtenteRegistratoDAO utenteDAO = new UtenteRegistratoDAO(this.id);
        utenteDAO.updateUtenteRegistrato(nome, cognome, contattoTelefonico, email, auto, postiDisponibili, new String(password));
        aggiornaDati(utenteDAO);
    }

    /**
     * Funzione che permette all'utente corrente di visualizzare le prenotazioni effettuate sui suoi viaggi
     * @return prenotazioni ArrayList di Entity prenotazioni, vuoto se non presenti
     */
    public ArrayList<EntityPrenotazione> visualizzaPrenotazioni() {
        ArrayList<EntityPrenotazione> prenotazioni = new ArrayList<>();
        for(EntityViaggio viaggio : this.viaggiCondivisi){
            try {
                viaggio.popolaPrenotazioni();
                /*TODO assicurarsi al 100% che funziona perche Lorenzo ha insistito a fare sto addAll*/
                prenotazioni.addAll(viaggio.getPrenotazioni());
            } catch (DatabaseException e) {
                return new ArrayList<EntityPrenotazione>();  //Ritorno un ArrayList di prenotazioni vuoto
            }
        }
        return prenotazioni;
    }

    /**
     * Funzione che permette all'utente corrente di prenotare un viaggio
     * @param idViaggio l'id del viaggio che si intende prenotare
     * @throws DatabaseException nel caso in cui la prenotazione non vada a buon fine
     */
    public void prenotaViaggio(long idViaggio) throws DatabaseException {
        ViaggioDAO viaggioDAO = new ViaggioDAO(idViaggio);
        EntityViaggio entityViaggio = new EntityViaggio(viaggioDAO);

        if (viaggioDAO.getIdAutista() == this.id) {
            throw new DatabaseException("Non puoi prenotare un viaggio di cui sei autista", true);
        }

        if (entityViaggio.getDataPartenza().isBefore(LocalDateTime.now())) {
           throw new DatabaseException("Non puoi prenotare un viaggio già iniziato", true);
        }

        EntityPrenotazione entityPrenotazione = new EntityPrenotazione();
        entityPrenotazione.setPasseggero(this);
        entityPrenotazione.setViaggioPrenotato(entityViaggio);

        entityPrenotazione.salvaInDB();

    }

    /**
     * Funzione che permette all'utente registrato in quanto autista, di gestire una prenotazione
     * ricevuta (accettarla o rifiutarla)
     * @param idPrenotazione l'id della prenotazione da gestire
     * @param accettata lo stato attuale della prenotazione
     * @throws DatabaseException se la gestione della prenotazione nel database fallisce
     */
    public void gestisciPrenotazione(long idPrenotazione,boolean accettata) throws DatabaseException {
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO(idPrenotazione);
        if(accettata) {
            prenotazioneDAO.updatePrenotazione(true);
        }else{
            prenotazioneDAO.deletePrenotazione();
        }
    }

    /**
     * Funzione che permette all'utente corrente di valutare un altro utente
     * @param idPrenotazione l'id della prenotazione della cui si vuole valutare l'utente
     * @param numeroStelle il numero di stelle da assegnare
     * @param text la descrizione della valutazione
     * @throws DatabaseException se il salvataggio della valutazione nel database fallisce
     */
    public void valutaUtente(long idPrenotazione, int numeroStelle, String text) throws DatabaseException {
        EntityValutazione entityValutazione = new EntityValutazione();

        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO(idPrenotazione);
        EntityPrenotazione prenotazione = new EntityPrenotazione(prenotazioneDAO);

        if(prenotazione.getViaggioPrenotato().getDataPartenza().isAfter(LocalDateTime.now())) {
            throw new DatabaseException("Impossibile valutare un utente il cui viaggio non è ancora iniziato",true);
        }

        entityValutazione.setNumeroStelle(numeroStelle);
        entityValutazione.setDescrizione(text);
        entityValutazione.setIdUtenteValutato(prenotazione.getPasseggero().getId());

        entityValutazione.salvaInDB();
    }

    /**
     * Funzione di utilità ad aggiornaDatiPersonali che permette di eliminare i viaggi condvisi fino a quel momento,
     * quando l'utente aggiorna la sua automobile o il numero di posti disponibili
     * @throws DatabaseException se l'eliminazione dei viaggi dal database non va a buon fine
     */
    private void eliminaViaggiCondivisi() throws DatabaseException {
        for (EntityViaggio viaggio : this.viaggiCondivisi) {
            ViaggioDAO viaggioDAO = new ViaggioDAO(viaggio.getId());
            viaggioDAO.deleteViaggio();
        }
        this.viaggiCondivisi = new ArrayList<>();
    }


    /**
     * Funzione di utilità ad aggiornaDatiPersonali che permette di aggiornare la DAO in ingresso con i dati dell'entity
     * @param utenteRegistratoDAO DAO da aggiornare con i dati dell'entity
     */
    private void aggiornaDati(UtenteRegistratoDAO utenteRegistratoDAO) {
        this.nome = utenteRegistratoDAO.getNome();
        this.cognome = utenteRegistratoDAO.getCognome();
        this.email = utenteRegistratoDAO.getEmail();
        this.contattoTelefonico = utenteRegistratoDAO.getContattoTelefonico();
        this.automobile = utenteRegistratoDAO.getAutomobile();
        this.password = utenteRegistratoDAO.getPassword();
        this.postiDisponibili = utenteRegistratoDAO.getPostiDisponibili();
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getContattoTelefonico() {
        return contattoTelefonico;
    }

    public void setContattoTelefonico(String contattoTelefonico) {
        this.contattoTelefonico = contattoTelefonico;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAutomobile() {
        return automobile;
    }

    public void setAutomobile(String automobile) {
        this.automobile = automobile;
    }

    public int getPostiDisponibili() {
        return postiDisponibili;
    }

    public void setPostiDisponibili(int postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }

    public ArrayList<EntityValutazione> getValutazioni() {
        return valutazioni;
    }

    public void setValutazioni(ArrayList<EntityValutazione> valutazioni) {
        this.valutazioni = valutazioni;
    }

    public ArrayList<EntityPrenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(ArrayList<EntityPrenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    public ArrayList<EntityViaggio> getViaggiCondivisi() {
        return viaggiCondivisi;
    }

    public void setViaggiCondivisi(ArrayList<EntityViaggio> viaggiCondivisi) {
        this.viaggiCondivisi = viaggiCondivisi;
    }

}

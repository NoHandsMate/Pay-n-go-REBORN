package entity;

import database.PrenotazioneDAO;
import database.UtenteRegistratoDAO;
import database.ValutazioneDAO;
import database.ViaggioDAO;
import exceptions.DatabaseException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe del package entity nel modello BCED, essa implementa l'entità utente registrato.
 */
public class EntityUtenteRegistrato {

    /**
     * L'identificativo del'utente registrato.
     */
    private long id;

    /**
     * Il nome dell'utente registrato.
     */
    private String nome;

    /**
     * Il cognome dell'utente registrato.
     */
    private String cognome;

    /**
     * Il contatto telefonico dell'utente registrato.
     */
    private String contattoTelefonico;

    /**
     * L'indirizzo email dell'utente registrato.
     */
    private String email;

    /**
     * La password dell'utente registrato.
     */
    private String password;

    /**
     * L'automobile dell'utente registrato.
     */
    private String automobile;

    /**
     * Il numero di posti disponibili nell'automobile dell'utente registrato.
     */
    private int postiDisponibili;

    /**
     * I viaggi condivisi dell'utente registrato.
     */
    private ArrayList<EntityViaggio> viaggiCondivisi;

    /**
     * Le prenotazioni effettuate dall'utente registrato.
     */
    private ArrayList<EntityPrenotazione> prenotazioni;

    /**
     * Le valutazioni relative all'utente registrato.
     */
    private ArrayList<EntityValutazione> valutazioni;

    /**
     * Costruttore di default di EntityUtenteRegistrato, crea un'entità vuota.
     */
    public EntityUtenteRegistrato() {}

    /**
     * Costruttore di EntityUtenteRegistrato che popola l'istanza a partire da UtenteRegistratoDAO.
     * @param utenteRegistratoDAO che permette di popolare l'istanza corrente di EntityUtenteRegistrato.
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
     * Funzione che permette di popolare un utente registrato con le prenotazioni effettuate.
     * @throws DatabaseException se si verifica un errore nel caricamento delle prenotazioni.
     */
    public void popolaPrenotazioni() throws DatabaseException {
        List<PrenotazioneDAO> listaPrenotazioni = PrenotazioneDAO.getPrenotazioni();
        this.prenotazioni = new ArrayList<>();
        for (PrenotazioneDAO prenotazioneDAO : listaPrenotazioni) {
            if (prenotazioneDAO.getIdPasseggero() == this.id) {
                EntityPrenotazione prenotazione = new EntityPrenotazione(prenotazioneDAO);
                if (prenotazione.getViaggioPrenotato().getDataPartenza().isBefore(LocalDateTime.now())
                        && !prenotazione.isAccettata()) {
                    prenotazioneDAO.deletePrenotazione();
                } else
                    this.prenotazioni.add(prenotazione);
            }
        }
    }

    /**
     * Funzione che permette di popolare un utente registrato con le valutazioni associate.
     * @throws DatabaseException se si verifica un errore nel caricamento delle valutazioni.
     */
    public void popolaValutazioni() throws DatabaseException {
        List<ValutazioneDAO> listaValutazioni = ValutazioneDAO.getValutazioni();
        this.valutazioni = new ArrayList<>();
        for (ValutazioneDAO valutazioneDAO : listaValutazioni) {
            if (valutazioneDAO.getIdUtente() == this.id) {
                EntityValutazione valutazione = new EntityValutazione(valutazioneDAO);
                this.valutazioni.add(valutazione);
            }
        }
    }

    /**
     * Funzione che permette di popolare un utente registrato con i viaggi condivisi.
     * @throws DatabaseException se si verifica un errore nel caricamento dei viaggi.
     */
    public void popolaViaggiCondivisi() throws DatabaseException {
        List<ViaggioDAO> viaggi = ViaggioDAO.getViaggi();
        this.viaggiCondivisi = new ArrayList<>();
        for (ViaggioDAO viaggioDAO : viaggi) {
            if (viaggioDAO.getIdAutista() == this.id) {
                EntityViaggio viaggio = new EntityViaggio(viaggioDAO);
                this.viaggiCondivisi.add(viaggio);
            }
        }
    }

    /**
     * Funzione che permette all'utente registrato di condividere un nuovo viaggio.
     * @param luogoPartenza il luogo di partenza del viaggio da condividere.
     * @param luogoDestinazione il luogo di destinazione del viaggio da condividere.
     * @param dataPartenza la data di partenza del viaggio da condividere.
     * @param dataArrivo la data di arrivo del viaggio da condividere.
     * @param contributoSpese il contributo spese per ogni passeggero del viaggio da condividere.
     * @throws DatabaseException se non è stato possibile condividere il viaggio.
     */
    public void condividiViaggio(String luogoPartenza,
                                 String luogoDestinazione,
                                 LocalDateTime dataPartenza,
                                 LocalDateTime dataArrivo,
                                 float contributoSpese) throws DatabaseException {

        if (this.automobile.isBlank() || this.postiDisponibili == 0) {
            throw new DatabaseException("Non puoi condividere un viaggio se non possiedi un automobile", true);
        }

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
     * Funzione che permette all'utente registrato di aggiornare i propri dati personali.
     * @param nome il nuovo nome dell'utente registrato.
     * @param cognome il nuovo cognome dell'utente registrato.
     * @param email il nuovo indirizzo email dell'utente registrato.
     * @param auto la nuova automobile dell'utente registrato.
     * @param password la nuova password dell'utente registrato.
     * @param postiDisponibili il nuovo numero di posti disponibili dell'utente registrato.
     * @param contattoTelefonico il nuovo numero di telefono dell'utente registrato.
     * @throws DatabaseException se non è stato possibile aggiornare i dati personali.
     */
    public void aggiornaDatiPersonali(String nome,
                                      String cognome,
                                      String email,
                                      String auto,
                                      char[] password,
                                      Integer postiDisponibili,
                                      String contattoTelefonico) throws DatabaseException {

        if (!this.automobile.equals(auto) || this.postiDisponibili != postiDisponibili) {

            for (EntityViaggio entityViaggio : this.viaggiCondivisi) {
                entityViaggio.popolaPrenotazioni();
                for(EntityPrenotazione entityPrenotazione : entityViaggio.getPrenotazioni()) {
                    if (entityPrenotazione.isAccettata() && entityViaggio.getDataPartenza().isAfter(LocalDateTime.now())) {
                        throw new DatabaseException("Non puoi modificare l'automobile o i posti disponibili " +
                                "se hai prenotazioni accettate ad almeno uno dei tuoi viaggi condivisi", true);
                    }
                }
            }
            this.eliminaViaggiCondivisi();
        }

        UtenteRegistratoDAO utenteDAO = new UtenteRegistratoDAO(this.id);
        utenteDAO.updateUtenteRegistrato(nome, cognome, contattoTelefonico, email, auto, postiDisponibili, new String(password));
        aggiornaDati(utenteDAO);
    }

    /**
     * Funzione che permette all'utente di visualizzare le prenotazioni effettuate su un suo viaggio.
     * @param idViaggio l'identificativo del viaggio di cui visualizzare le prenotazioni.
     * @return una lista di prenotazioni effettuate sul viaggio dell'utente.
     * @throws DatabaseException se non è stato possibile ottenere la lista di prenotazioni.
     */
    public List<EntityPrenotazione> visualizzaPrenotazioni(long idViaggio) throws DatabaseException{
        List<EntityPrenotazione> listaPrenotazioni = new ArrayList<>();
        for(EntityViaggio viaggio : this.viaggiCondivisi){
            if (viaggio.getId() == idViaggio){
                viaggio.popolaPrenotazioni();
                listaPrenotazioni.addAll(viaggio.getPrenotazioni());
            }
        }
        return listaPrenotazioni;
    }

    /**
     * Funzione che permette all'utente registrato di prenotare un viaggio.
     * @param idViaggio l'identificativo del viaggio che si intende prenotare.
     * @throws DatabaseException se non è stato possibile prenotare il viaggio.
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

        entityViaggio.popolaPrenotazioni();

        int numPrenotazioniConfermate = 0;
        for (EntityPrenotazione prenotazione : entityViaggio.getPrenotazioni()) {
            if (prenotazione.isAccettata())
                numPrenotazioniConfermate++;
        }

        if (numPrenotazioniConfermate >= entityViaggio.getAutista().postiDisponibili) {
            throw new DatabaseException("Tutti i posti disponibili per il viaggio sono stati prenotati", true);
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
    public void gestisciPrenotazione(long idPrenotazione,
                                     boolean accettata) throws DatabaseException {

        int numPrenotazioniAccettate = 0;
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO(idPrenotazione);
        EntityPrenotazione prenotazioneEntity = new EntityPrenotazione(prenotazioneDAO);
        EntityViaggio viaggio = prenotazioneEntity.getViaggioPrenotato();

        for (EntityPrenotazione prenotazione : viaggio.getPrenotazioni()) {
            if (prenotazione.isAccettata()) {
                numPrenotazioniAccettate++;
            }
        }

        if(numPrenotazioniAccettate + 1 > this.postiDisponibili && accettata) {
            for(EntityPrenotazione prenotazione : viaggio.getPrenotazioni()){
                if(!prenotazione.isAccettata()) {
                    PrenotazioneDAO prenotazioneDaEliminare = new PrenotazioneDAO(prenotazione.getId());
                    prenotazioneDaEliminare.deletePrenotazione();
                }
            }
            throw new DatabaseException("Impossibile accettare ulteriori prenotazioni per il viaggio selezionato",true);
        }

        if(accettata) {
            prenotazioneDAO.updatePrenotazione(true);
        }else{
            prenotazioneDAO.deletePrenotazione();
        }
    }

    /**
     * Funzione che permette all'utente registrato, data una prenotazione, di valutare l'altro utente riferito dalla
     * stessa. Di conseguenza, se l'utente registrato è l'autista del viaggio associato alla prenotazione si valuterà il
     * passeggero; se l'utente registrato è il passeggero associato alla prenotazione si valuterà l'autista del viaggio
     * associato alla prenotazione.
     * @param idPrenotazione l'identificativo della prenotazione a cui si fa riferimento.
     * @param numeroStelle il numero di stelle da assegnare.
     * @param text la descrizione della valutazione.
     * @throws DatabaseException se non è stato possibile valutare l'utente.
     */
    public void valutaUtente(long idPrenotazione,
                             int numeroStelle,
                             String text) throws DatabaseException {
        EntityValutazione entityValutazione = new EntityValutazione();

        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO(idPrenotazione);
        EntityPrenotazione prenotazione = new EntityPrenotazione(prenotazioneDAO);

        if (prenotazione.getViaggioPrenotato().getDataPartenza().isAfter(LocalDateTime.now())) {
            throw new DatabaseException("Impossibile valutare un utente il cui viaggio non è ancora iniziato",true);
        }

        long idValutato;
        if (prenotazione.getPasseggero().id == this.id)
            idValutato = prenotazione.getViaggioPrenotato().getAutista().id;
        else
            idValutato = prenotazione.getPasseggero().id;

        UtenteRegistratoDAO utenteValutatoDAO = new UtenteRegistratoDAO(idValutato);
        EntityUtenteRegistrato utenteValutato = new EntityUtenteRegistrato(utenteValutatoDAO);
        entityValutazione.setNumeroStelle(numeroStelle);
        entityValutazione.setDescrizione(text);
        entityValutazione.setUtenteValutato(utenteValutato);

        entityValutazione.salvaInDB();
    }

    /**
     * Funzione che permette di visualizzare i dettagli di tutte le valutazioni associate all'utente registrato.
     * @return l'elenco valutazioni dell'utente.
     * @throws DatabaseException se non è stato possibile creare l'elenco delle valutazioni dell'utente registrato.
     */
    public List<EntityValutazione> visualizzaValutazioni() throws DatabaseException {
        this.popolaValutazioni();
        return this.valutazioni;
    }

    /**
     * Funzione di utilità ad {@link #aggiornaDatiPersonali(String, String, String, String, char[], Integer, String)
     * aggiornaDatiPersonali} che permette di eliminare i viaggi condivisi fino a quel momento, quando l'utente aggiorna
     * la sua automobile o il numero di posti disponibili.
     * @throws DatabaseException se non è stato possibile eliminare i viaggi condivisi.
     */
    private void eliminaViaggiCondivisi() throws DatabaseException {
        for (EntityViaggio viaggio : this.viaggiCondivisi) {
            ViaggioDAO viaggioDAO = new ViaggioDAO(viaggio.getId());
            viaggioDAO.deleteViaggio();
        }
        this.viaggiCondivisi = new ArrayList<>();
    }

    /**
     * Funzione di utilità ad {@link #aggiornaDatiPersonali(String, String, String, String, char[], Integer, String)
     * aggiornaDatiPersonali} che permette di aggiornare l'utente registrato con i dati memorizzati nella DAO.
     * @param utenteRegistratoDAO che permette di aggiornare l'istanza corrente di EntityUtenteRegistrato.
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

    /**
     * Getter dell'identificativo dell'utente registrato.
     * @return l'identificativo dell'utente registrato.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter dell'identificativo dell'utente registrato.
     * @param id il nuovo identificativo dell'utente registrato.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter del nome dell'utente registrato.
     * @return il nome dell'utente registrato.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Setter del nome dell'utente registrato.
     * @param nome il nuovo nome dell'utente registrato.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Getter del cognome dell'utente registrato.
     * @return il cognome dell'utente registrato.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Setter del cognome dell'utente registrato.
     * @param cognome il nuovo cognome dell'utente registrato.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Getter del contatto telefonico dell'utente registrato.
     * @return il contatto telefonico dell'utente registrato.
     */
    public String getContattoTelefonico() {
        return contattoTelefonico;
    }

    /**
     * Setter del contatto telefonico dell'utente registrato.
     * @param contattoTelefonico il nuovo contatto telefonico dell'utente registrato.
     */
    public void setContattoTelefonico(String contattoTelefonico) {
        this.contattoTelefonico = contattoTelefonico;
    }

    /**
     * Getter dell'email dell'utente registrato.
     * @return l'email dell'utente registrato.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter dell'email dell'utente registrato.
     * @param email la nuova email dell'utente registrato.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter della password dell'utente registrato.
     * @return la password dell'utente registrato.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter della password dell'utente registrato.
     * @param password la nuova password dell'utente registrato.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter dell'automobile dell'utente registrato.
     * @return l'automobile dell'utente registrato.
     */
    public String getAutomobile() {
        return automobile;
    }

    /**
     * Setter dell'automobile dell'utente registrato.
     * @param automobile la nuova automobile dell'utente registrato.
     */
    public void setAutomobile(String automobile) {
        this.automobile = automobile;
    }

    /**
     * Getter del numero di posti disponibili nell'automobile dell'utente registrato.
     * @return il numero di posti disponibili nell'automobile dell'utente registrato.
     */
    public int getPostiDisponibili() {
        return postiDisponibili;
    }

    /**
     * Setter del numero di posti disponibili nell'automobile dell'utente registrato.
     * @param postiDisponibili il nuovo numero di posti disponibili nell'automobile dell'utente registrato.
     */
    public void setPostiDisponibili(int postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }

    /**
     * Getter delle valutazioni relative all'utente registrato.
     * @return la lista di valutazioni relative all'utente registrato.
     */
    public List<EntityValutazione> getValutazioni() {
        return valutazioni;
    }

    /**
     * Setter delle valutazioni relative all'utente registrato.
     * @param valutazioni la nuova lista di valutazioni relative all'utente registrato.
     */
    public void setValutazioni(List<EntityValutazione> valutazioni) {
        this.valutazioni = (ArrayList<EntityValutazione>) valutazioni;
    }

    /**
     * Getter delle prenotazioni effettuate dall'utente registrato.
     * @return la lista di prenotazioni effettuate dall'utente registrato.
     */
    public List<EntityPrenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    /**
     * Setter delle prenotazioni effettuate dall'utente registrato.
     * @param prenotazioni la nuova lista di prenotazioni effettuate dall'utente registrato.
     */
    public void setPrenotazioni(List<EntityPrenotazione> prenotazioni) {
        this.prenotazioni = (ArrayList<EntityPrenotazione>) prenotazioni;
    }

    /**
     * Getter dei viaggi condivisi dall'utente registrato.
     * @return la lista dei viaggi condivisi dall'utente registrato.
     */
    public List<EntityViaggio> getViaggiCondivisi() {
        return viaggiCondivisi;
    }

    /**
     * Setter dei viaggi condivisi dall'utente registrato.
     * @param viaggiCondivisi la nuova lista dei viaggi condivisi dall'utente registrato.
     */
    public void setViaggiCondivisi(List<EntityViaggio> viaggiCondivisi) {
        this.viaggiCondivisi = (ArrayList<EntityViaggio>) viaggiCondivisi;
    }
}

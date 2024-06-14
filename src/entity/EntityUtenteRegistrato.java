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


    public EntityUtenteRegistrato() {}

    /**
     * Costruttore che popola l'EntityUtenteRegistrato a partire dall'id specificato. Gli attributi viaggiCondivisi,
     * prenotazioni e valutazioni non vengono popolati.
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

    public void aggiornaDatiPersonali(String nome, String cognome, String email,
                                      String auto, char[] password, Integer postiDisp,
                                      String telefono) throws DatabaseException {

        if (!this.automobile.equals(auto) || this.postiDisponibili != postiDisp) {
            this.eliminaViaggiCondivisi();
        }
        UtenteRegistratoDAO utenteDAO = new UtenteRegistratoDAO(this.id);
        utenteDAO.updateUtenteRegistrato(nome, cognome, telefono, email, auto, postiDisp, new String(password));
        aggiornaDati(utenteDAO);
    }

    /*TODO: gestisciPrenotazione*/

    public void prenotaViaggio(long idViaggio) throws DatabaseException {
        ViaggioDAO viaggioDAO = new ViaggioDAO(this.id);
        EntityViaggio entityViaggio = new EntityViaggio(viaggioDAO);
        EntityPrenotazione entityPrenotazione = new EntityPrenotazione();
        entityPrenotazione.setPasseggero(this);
        entityPrenotazione.creaPrenotazione();
    }

    private void eliminaViaggiCondivisi() throws DatabaseException {
        for (EntityViaggio viaggio : this.viaggiCondivisi) {
            ViaggioDAO viaggioDAO = new ViaggioDAO(viaggio.getId());
            viaggioDAO.deleteViaggio();
        }
        this.viaggiCondivisi = new ArrayList<>();
    }


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

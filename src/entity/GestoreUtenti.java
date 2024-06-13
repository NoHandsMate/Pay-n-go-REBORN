package entity;

import database.ValutazioneDAO;
import exceptions.*;
import database.UtenteRegistratoDAO;
import dto.*;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class GestoreUtenti {

    private static GestoreUtenti uniqueInstance;

    private GestoreUtenti() {}

    public static GestoreUtenti getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new GestoreUtenti();
        }
        return uniqueInstance;
    }

    public void registraUtente(String nome, String cognome, String email,
                               String auto, char[] password, Integer postiDisp,
                               String telefono) throws RegistrationFailedException {

        UtenteRegistratoDAO utenteDAO = new UtenteRegistratoDAO();
        try {
            utenteDAO.createUtenteRegistrato(nome, cognome, telefono, email, auto, postiDisp, new String(password));
            aggiornaUtenteCorrente(utenteDAO);
        } catch (DatabaseException e) {
            if (e.isVisible())
                throw new RegistrationFailedException("Registrazione Utente fallita: " + e.getMessage());
            throw new RegistrationFailedException("Registrazione Utente fallita.");
        }
    }

    public void loginUtente(String email, char[] password) throws LoginFailedException {
        UtenteRegistratoDAO utenteDAO = new UtenteRegistratoDAO();
        try {
            utenteDAO.readUtenteRegistrato(email, new String(password));
            aggiornaUtenteCorrente(utenteDAO);
        } catch (DatabaseException e) {
            if (e.isVisible())
                throw new LoginFailedException("Login Utente fallito: %s" + e.getMessage());
            throw new LoginFailedException("Login Utente fallito.");
        }
    }

    public void aggiornaDatiPersonali(String nome, String cognome, String email,
                                      String auto, char[] password, Integer postiDisp,
                                      String telefono) throws AggiornamentoDatiFailedException {

        EntityUtenteRegistrato utenteCorrente = Sessione.getInstance().getUtenteCorrente();
        utenteCorrente.aggiornaDatiPersonali(nome, cognome, email, auto, password, postiDisp, telefono);
        Sessione.getInstance().setUtenteCorrente(utenteCorrente);
    }


    public ArrayList<ArrayList<MyDto>> generaReportUtenti() throws ReportUtentiFailedException {

        //ArrayList di ArrayList di DTO: ogni utente può avere una serie di valutazioni
        ArrayList<ArrayList<MyDto>> reportUtenti = new ArrayList<>();

        try{
            //Tutti gli utenti del database
            ArrayList<UtenteRegistratoDAO> utentiRegistratiDAO = UtenteRegistratoDAO.getUtentiRegistrati();
            //Tutti le valutazioni del database
            ArrayList<ValutazioneDAO> valutazioniDAO = ValutazioneDAO.getValutazioni();

            //Costruisco un array di Entity o lavoro sulle DAO?
            for(UtenteRegistratoDAO utenteRegistratoDAO : utentiRegistratiDAO){
                ArrayList<MyDto> reportUtente = new ArrayList<>();
                MyDto utente = new MyDto();
                utente.setCampo1(utenteRegistratoDAO.getNome());
                utente.setCampo2(utenteRegistratoDAO.getCognome());
                reportUtente.add(utente);
                for(ValutazioneDAO valutazioneDAO : valutazioniDAO){
                    if(valutazioneDAO.getIdUtente() == utenteRegistratoDAO.getIdUtenteRegistrato()){
                        MyDto valutazione = new MyDto();
                        valutazione.setCampo1(valutazioneDAO.getDescrizione());
                        valutazione.setCampo2(String.valueOf(valutazioneDAO.getNumeroStelle()));
                        reportUtente.add(valutazione);
                    }
                }
                reportUtenti.add(reportUtente);
            }
        } catch (DatabaseException e) {
            if (e.isVisible())
                throw new ReportUtentiFailedException("Generazione report utenti fallita: " + e.getMessage());
            throw new ReportUtentiFailedException("Generazione report utenti fallita.");

        }
        return reportUtenti;
    }

    public void condividiViaggio(String luogoPartenza, String luogoDestinazione, LocalDateTime dataPartenza,
                                 LocalDateTime dataArrivo, float contributoSpese) throws CondivisioneViaggioFailedException {

        EntityUtenteRegistrato utenteCorrente = Sessione.getInstance().getUtenteCorrente();
        utenteCorrente.condividiViaggio(luogoPartenza, luogoDestinazione, dataPartenza, dataArrivo, contributoSpese);
    }

    private void aggiornaUtenteCorrente(UtenteRegistratoDAO utenteRegistratoDAO) throws DatabaseException {
        EntityUtenteRegistrato utenteCorrente = new EntityUtenteRegistrato(utenteRegistratoDAO);
        Sessione.getInstance().setUtenteCorrente(utenteCorrente);
    }

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
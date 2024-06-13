package entity;

import database.ValutazioneDAO;
import exceptions.*;
import database.UtenteRegistratoDAO;
import dto.*;

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
            {
                throw new RegistrationFailedException(String.format("Registrazione Utente fallita: %s", e.getMessage()));
            }
            throw new RegistrationFailedException("Registrazione Utente fallita.");
        }
    }

    public void loginUtente(String email, char[] password) throws LoginUserException {
        UtenteRegistratoDAO utenteDAO = new UtenteRegistratoDAO();
        try {
            utenteDAO.readUtenteRegistrato(email, new String(password));
            aggiornaUtenteCorrente(utenteDAO);
        } catch (DatabaseException e) {
            throw new LoginUserException("Login Utente fallito: " + e.getMessage());
        }
    }

    public void aggiornaDatiPersonali(String nome, String cognome, String email,
                                      String auto, char[] password, Integer postiDisp,
                                      String telefono) throws AggiornamentoDatiFailedException {

        try {
            UtenteRegistratoDAO utenteDAO = new UtenteRegistratoDAO(UtenteCorrente.getInstance().getIdUtenteCorrente());
            utenteDAO.updateUtenteRegistrato(nome, cognome, email, auto, new String(password), postiDisp, telefono);
            aggiornaUtenteCorrente(utenteDAO);
        } catch (DatabaseException e) {
            throw new AggiornamentoDatiFailedException("Aggiornamento Dati fallito: " + e.getMessage());
        }

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

        }catch (DatabaseException e){
            throw new ReportUtentiFailedException("Generazione report utenti fallita: " + e.getMessage());

        }
        return reportUtenti;
    }

    private void aggiornaUtenteCorrente(UtenteRegistratoDAO utenteDAO) {
        UtenteCorrente.getInstance().setIdUtenteCorrente(utenteDAO.getIdUtenteRegistrato());
        UtenteCorrente.getInstance().setNome(utenteDAO.getNome());
        UtenteCorrente.getInstance().setCognome(utenteDAO.getCognome());
        UtenteCorrente.getInstance().setEmail(utenteDAO.getEmail());
        UtenteCorrente.getInstance().setAuto(utenteDAO.getAutomobile());
        UtenteCorrente.getInstance().setPassword(utenteDAO.getPassword());
        UtenteCorrente.getInstance().setPostiDisp(String.valueOf(utenteDAO.getPostiDisponibili()));
        UtenteCorrente.getInstance().setContattoTelefonico(utenteDAO.getContattoTelefonico());
    }
}
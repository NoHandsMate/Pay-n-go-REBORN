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

        try{
            int i = 0;


            //Tutti gli utenti del database
            ArrayList<UtenteRegistratoDAO> utentiRegistrati = UtenteRegistratoDAO.getUtentiRegistrati();
            //Tutti le valutazioni del database
            ArrayList<ValutazioneDAO> valutazioni = ValutazioneDAO.getValutazioni();

            //ArrayList di ArrayList di DTO: ogni utente può avere una serie di valutazioni
            ArrayList<ArrayList<MyDto>> reportUtenti = new ArrayList<>();


            for (UtenteRegistratoDAO utente : utentiRegistrati) {

                //Tutte le valutazioni associate a un utente: ognuna di esse è un Dto, con descrizione e numstelle
                ArrayList<MyDto> valutazioniUtente = new ArrayList<>();
                MyDto i_esimo_utente = new MyDto();
                ArrayList<MyDto> i_esimo_reportUtente = new ArrayList<>();

                for (ValutazioneDAO valutazione : valutazioni) {

                    MyDto i_esima_valutazione_utente = new MyDto();
                    if(valutazione.getIdUtente() == utente.getIdUtenteRegistrato() ){
                        i_esimo_utente.setCampo1(utente.getNome());
                        i_esimo_utente.setCampo2(utente.getCognome());
                        i_esima_valutazione_utente.setCampo3(valutazione.getDescrizione());
                        i_esima_valutazione_utente.setCampo4(String.valueOf(valutazione.getNumeroStelle()));
                        valutazioniUtente.add(i_esima_valutazione_utente);
                        i_esimo_reportUtente.add(i_esimo_utente);
                        i_esimo_reportUtente.add(i_esima_valutazione_utente);
                    }

                }
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
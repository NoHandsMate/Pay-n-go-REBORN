package entity;

import dto.MyDto;
import exceptions.*;
import database.UtenteRegistratoDAO;

public class GestoreUtenti {

    private static GestoreUtenti uniqueInstance;
    private EntityUtenteRegistrato utenteCorrente;

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
            this.utenteCorrente = new EntityUtenteRegistrato(utenteDAO);
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
            this.utenteCorrente = new EntityUtenteRegistrato(utenteDAO);
        } catch (DatabaseException e) {
            throw new LoginUserException("Login Utente fallito: " + e.getMessage());
        }
    }

    public void aggiornaDatiPersonali(String nome, String cognome, String email,
                                      String auto, char[] password, Integer postiDisp,
                                      String telefono) throws AggiornamentoDatiFailedException {

        /* TODO: Bisogna capire come ottenere l'utente corrente. O lo prendiamo dal GestoreUtenti come entità oppure
        *        utilizziamo l'id corrente e costruiamo l'utente corrente con la DAO con il quale poi
        *        aggiornare i dati personali. Nel try dopo l'update va aggiornato l'utenteRegistrato corrente
        *
        */

        EntityUtenteRegistrato entity = new EntityUtenteRegistrato(); /*TODO: Questa va ottenuta in qualche modo come detto prima*/
        UtenteRegistratoDAO utenteDAO = new UtenteRegistratoDAO();
        try {
            utenteDAO.updateUtenteRegistrato(nome, cognome, email, auto, new String(password), postiDisp, telefono);
        } catch (DatabaseException e) {
            throw new AggiornamentoDatiFailedException("Aggiornamento Dati fallito: " + e.getMessage());
        }

    }

    public void generaReportUtenti(){
        /* TODO: Da implementare */
    }

    public MyDto infoUtenteCorrente() throws InfoUtenteCorrenteFailedException {
        if (this.utenteCorrente == null) {
            throw new InfoUtenteCorrenteFailedException("Utente non loggato.");
        }
        return new MyDto(String.valueOf(this.utenteCorrente.getId()),
                this.utenteCorrente.getNome(),
                this.utenteCorrente.getCognome(),
                this.utenteCorrente.getEmail(),
                this.utenteCorrente.getPassword(),
                this.utenteCorrente.getContattoTelefonico(),
                this.utenteCorrente.getAutomobile(),
                String.valueOf(this.utenteCorrente.getPostiDisponibili()));
    }
}
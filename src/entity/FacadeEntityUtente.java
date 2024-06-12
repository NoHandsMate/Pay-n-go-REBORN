package entity;

import dto.MyDto;
import exceptions.*;

import java.time.LocalDateTime;

public class FacadeEntityUtente {

    private static FacadeEntityUtente uniqueInstance;

    private FacadeEntityUtente() {}

    public static FacadeEntityUtente getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new FacadeEntityUtente();
        }
        return uniqueInstance;
    }


    public void registraUtente(String nome, String cognome, String email, String auto, char[] password,
                                      Integer postiDisp, String telefono) throws RegistrationFailedException {

        GestoreUtenti.getInstance().registraUtente(nome, cognome, email, auto, password, postiDisp, telefono);
    }

    public void loginUtente(String email, char[] password) throws LoginUserException {

        GestoreUtenti.getInstance().loginUtente(email, password);
    }

    public void condividiViaggio(String luogoPartenza,
                                 String luogoDestinazione,
                                 LocalDateTime dataPartenza,
                                 LocalDateTime dataArrivo,
                                 float contributoSpese,long idAutista) throws CondivisioneViaggioFailedException {

    }

    public void aggiornaDatiPersonali(String nome, String cognome, String email,
                                      String auto, char[] password, Integer postiDisp,
                                      String telefono) throws AggiornamentoDatiFailedException {

        GestoreUtenti.getInstance().aggiornaDatiPersonali(nome, cognome, email, auto, password, postiDisp, telefono);

    }

    public MyDto infoUtenteCorrente() throws InfoUtenteCorrenteFailedException {
        return GestoreUtenti.getInstance().infoUtenteCorrente();
    }
}

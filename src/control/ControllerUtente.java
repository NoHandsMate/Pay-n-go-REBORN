package control;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;

import entity.FacadeEntityUtente;
import exceptions.*;
import dto.*;

public class ControllerUtente {

    //Variabile statica univoca per implementare il pattern Singleton
    private static ControllerUtente uniqueInstance;

    private ControllerUtente() {}

    public static ControllerUtente getInstance(){
        if(uniqueInstance == null) {
            uniqueInstance = new ControllerUtente();
        }
        return uniqueInstance;
    }

    public AbstractMap.SimpleEntry<Boolean, String> registraUtente(String nome,
                                                                   String cognome,
                                                                   String email,
                                                                   String auto,
                                                                   char[] password,
                                                                   Integer postiDisp,
                                                                   String telefono) {
        try {
            FacadeEntityUtente.getInstance().registraUtente(nome, cognome, email, auto, password, postiDisp, telefono);
        } catch (RegistrationFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Registrazione effettuata con successo");
    }

    public AbstractMap.SimpleEntry<Boolean, String> loginUtente(String email,
                                                                char[] password) {
        try {
            FacadeEntityUtente.getInstance().loginUtente(email, password);
        } catch (LoginFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Login effettuato con successo.");
    }

    public AbstractMap.SimpleEntry<Boolean, String> condividiViaggio(String luogoPartenza,
                                                                     String luogoDestinazione,
                                                                     LocalDateTime dataPartenza,
                                                                     LocalDateTime dataArrivo,
                                                                     float contributoSpese){
        //Prelievo idAutista (chi sta condividendo il viaggio) dal DTO
        long idAutista = UtenteCorrente.getInstance().getIdUtenteCorrente();

        try {
            FacadeEntityUtente.getInstance().condividiViaggio(luogoPartenza, luogoDestinazione, dataPartenza, dataArrivo,
                    contributoSpese, idAutista);
        } catch (CondivisioneViaggioFailedException e){
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Viaggio condiviso con successo.");

    }

    public AbstractMap.SimpleEntry<Boolean, String> aggiornaDatiPersonali(String nome,
                                                                          String cognome,
                                                                          String email,
                                                                          char[] password,
                                                                          String telefono,
                                                                          String auto,
                                                                          Integer postiDisp) {

        try {
            FacadeEntityUtente.getInstance().aggiornaDatiPersonali(nome, cognome, email, auto, password, postiDisp, telefono);
        } catch (AggiornamentoDatiFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, "Aggiornamento effettuato con successo.");

    }

    public AbstractMap.SimpleEntry<Boolean, Object> ricercaViaggio(String luogoPartenza, String luogoDestinazione,
                                                                   LocalDate dataPartenza) {

        ArrayList<MyDto> viaggiTrovatiDTO;

        try {
            viaggiTrovatiDTO = FacadeEntityUtente.getInstance().ricercaViaggio(luogoPartenza, luogoDestinazione, dataPartenza);
        } catch (RicercaViaggioFailedException e) {
            return new AbstractMap.SimpleEntry<>(false, e.getMessage());
        }

        return new AbstractMap.SimpleEntry<>(true, viaggiTrovatiDTO);
    }
}

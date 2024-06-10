//Classe Facade per l'Utente

package entity;

import exceptions.RegistrationFailedException;

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

}
package entity;

import exceptions.RegistrationFailedException;

public class GestoreUtente {

    private GestoreUtente uniqueIstance;

    private GestoreUtente() {}

    public GestoreUtente getIstance() {
        if (uniqueIstance == null) {
            uniqueIstance = new GestoreUtente();
        }
        return uniqueIstance;
    }

    public void registraUtente(String nome, String cognome, String email,
                               String auto, char[] password, Integer postiDisp,
                               String telefono) throws RegistrationFailedException {


    }
}

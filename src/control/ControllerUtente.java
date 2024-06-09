package control;


import java.util.AbstractMap;

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


    public static AbstractMap.SimpleEntry<Boolean, String> registraUtente(String nome, String cognome, String email,
                                                                          String auto, char[] password, Integer postiDisp,
                                                                          String telefono) {




    }
}

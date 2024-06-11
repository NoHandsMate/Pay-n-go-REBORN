package entity;

import exceptions.RegistrationFailedException;

public class FacadeEntityCarpooling {

    private static FacadeEntityCarpooling uniqueInstance;

    private FacadeEntityCarpooling() {}

    public static FacadeEntityCarpooling getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new FacadeEntityCarpooling();
        }
        return uniqueInstance;
    }


    public void GeneraReportIncassi() throws RegistrationFailedException {

    }
    public void GeneraReportUtenti() throws RegistrationFailedException {

    }
}

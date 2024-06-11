package entity;

public class GestoreViaggi {

    private static GestoreViaggi uniqueInstance;

    private GestoreViaggi() {}

    public static GestoreViaggi getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new GestoreViaggi();
        }
        return uniqueInstance;
    }

    public void GeneraReportIncassi(){

    }
}

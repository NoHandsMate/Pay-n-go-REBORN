package entity;

public class EntityValutazione {
    private int numeroStelle;
    private String descrizione;

    public EntityValutazione() {}

    public EntityValutazione(int numeroStelle, String descrizione) {
        this.numeroStelle = numeroStelle;
        this.descrizione = descrizione;
    }

    public int getNumeroStelle() {
        return numeroStelle;
    }

    public void setNumeroStelle(int numeroStelle) {
        this.numeroStelle = numeroStelle;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}

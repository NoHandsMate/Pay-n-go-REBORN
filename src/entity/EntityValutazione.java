package entity;

import database.*;
import exceptions.*;

public class EntityValutazione {
    private long id;
    private int numeroStelle;
    private String descrizione;

    public EntityValutazione() {}

    public EntityValutazione(ValutazioneDAO valutazioneDAO) {
        this.numeroStelle = valutazioneDAO.getNumeroStelle();
        this.descrizione = valutazioneDAO.getDescrizione();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

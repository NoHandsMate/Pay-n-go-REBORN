package entity;

import database.*;
import exceptions.*;

public class EntityValutazione {
    private long id;
    private int numeroStelle;
    private String descrizione;
    private long idUtenteValutato;

    public EntityValutazione() {}

    public EntityValutazione(ValutazioneDAO valutazioneDAO) {
        this.numeroStelle = valutazioneDAO.getNumeroStelle();
        this.descrizione = valutazioneDAO.getDescrizione();
    }

    public void salvaInDB() throws DatabaseException {
        ValutazioneDAO valutazioneDAO = new ValutazioneDAO();
        valutazioneDAO.createValutazione(this.numeroStelle, this.descrizione, this.idUtenteValutato);
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

    public long getIdUtenteValutato() {
        return idUtenteValutato;
    }

    public void setIdUtenteValutato(long idUtenteValutato) {
        this.idUtenteValutato = idUtenteValutato;
    }
}

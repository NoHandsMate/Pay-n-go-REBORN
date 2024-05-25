package entity;

public class EntityPrenotazione {

    private long id;
    private EntityUtenteRegistrato passeggero;


    public EntityPrenotazione() {}

    public EntityPrenotazione(long id, EntityUtenteRegistrato passeggero) {
        this.id = id;
        this.passeggero = passeggero;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EntityUtenteRegistrato getPasseggero() {
        return passeggero;
    }

    public void setPasseggero(EntityUtenteRegistrato passeggero) {
        this.passeggero = passeggero;
    }


}

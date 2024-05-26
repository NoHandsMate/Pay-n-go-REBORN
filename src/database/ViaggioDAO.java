package database;

import entity.EntityUtenteRegistrato;
import entity.EntityViaggio;

import java.util.Date;

public class ViaggioDAO {

    public boolean createViaggio(String luogoPartenza, String luogoDestinazione,
                                 Date dataPartenza, Date oraPartenza,
                                 Date dataArrivo, Date oraArrivo,
                                 float contributoSpese,
                                 EntityUtenteRegistrato autista) {


        return false;
    }

    public EntityViaggio readViaggio() {
        return new EntityViaggio();
    }

    public boolean updateViaggio() {
        return false;
    }

    public boolean deleteViaggio() {
        return false;
    }
}

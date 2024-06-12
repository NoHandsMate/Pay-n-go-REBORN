package entity;

import database.UtenteRegistratoDAO;
import database.ViaggioDAO;
import dto.MyDto;
import exceptions.DatabaseException;
import exceptions.RicercaViaggioFailedException;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class GestoreViaggi {

    private static GestoreViaggi uniqueInstance;

    private static final String NATURALDATEFORMAT = "dd/MM/yyyy HH:mm:ss";

    private GestoreViaggi() {}

    public static GestoreViaggi getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new GestoreViaggi();
        }
        return uniqueInstance;
    }

    public Float GeneraReportIncassi(){
        return 0f;
    }

    public ArrayList<MyDto> ricercaViaggio(String luogoPartenza, String luogoDestinazione,
                                           LocalDate dataPartenza) throws RicercaViaggioFailedException {

        ArrayList<MyDto> viaggiTrovati = new ArrayList<>();
        try {
            ArrayList<ViaggioDAO> listaViaggi = ViaggioDAO.getViaggi();

            for (ViaggioDAO viaggio : listaViaggi) {
                if (viaggio.getLuogoPartenza().equalsIgnoreCase(luogoPartenza) &&
                    viaggio.getLuogoDestinazione().equalsIgnoreCase(luogoDestinazione) &&
                    viaggio.getDataPartenza().toLocalDate().isEqual(dataPartenza)) {

                        UtenteRegistratoDAO utenteRegistrato = new UtenteRegistratoDAO(viaggio.getIdAutista());
                        MyDto viaggioDTO = caricaViaggioDTO(viaggio, utenteRegistrato);
                        viaggiTrovati.add(viaggioDTO);
                }
            }
        } catch (DatabaseException e) {
            throw new RicercaViaggioFailedException("Ricerca viaggio fallito: " + e.getMessage());
        }

        return viaggiTrovati;
    }

    private MyDto caricaViaggioDTO(ViaggioDAO viaggio, UtenteRegistratoDAO utenteRegistrato) {
        MyDto viaggioDTO = new MyDto();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(NATURALDATEFORMAT);
        viaggioDTO.setCampo1(String.valueOf(viaggio.getIdViaggio()));
        viaggioDTO.setCampo2(viaggio.getLuogoPartenza());
        viaggioDTO.setCampo3(viaggio.getLuogoDestinazione());
        viaggioDTO.setCampo4(viaggio.getDataPartenza().format(dateTimeFormatter));
        viaggioDTO.setCampo5(viaggio.getDataArrivo().format(dateTimeFormatter));
        viaggioDTO.setCampo6(String.valueOf(viaggio.getContributoSpese()));
        viaggioDTO.setCampo7(String.format("%s %s", utenteRegistrato.getNome(), utenteRegistrato.getCognome()));
        return viaggioDTO;
    }
}

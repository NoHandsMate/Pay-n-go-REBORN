package entity;

import database.PrenotazioneDAO;
import database.UtenteRegistratoDAO;
import database.ViaggioDAO;
import dto.MyDto;
import exceptions.DatabaseException;
import exceptions.ReportIncassiFailedException;
import exceptions.RicercaViaggioFailedException;

import javax.swing.text.html.parser.Entity;
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


    /**
     * Funzione che permette al gestore dell'applicazione di generare un report degli incassi di tutti i viaggi nel
     * sistema
     * @return reportIncass float che rappresenta l'incasso complessivo
     * @throws ReportIncassiFailedException se la generazione del report incassi fallisce
     */
    public Float generaReportIncassi() throws ReportIncassiFailedException {

        float reportIncassi = 0f;
        ArrayList<ViaggioDAO> listaDAOViaggi;
        try {
            listaDAOViaggi = GestoreViaggi.caricaViaggiDaDB();
            for (ViaggioDAO viaggioDAO : listaDAOViaggi) {
                EntityViaggio viaggio = new EntityViaggio(viaggioDAO);
                viaggio.popolaPrenotazioni();
                int numPrenotazioniAccettate = 0;
                for(EntityPrenotazione prenotazione : viaggio.getPrenotazioni()) {
                    if(prenotazione.isAccettata()) {
                        numPrenotazioniAccettate++;
                    }
                }
                reportIncassi = reportIncassi + viaggio.getContributoSpese() * numPrenotazioniAccettate;
            }
        }catch (DatabaseException e){
            throw new ReportIncassiFailedException(e.getMessage());
        }
        return reportIncassi;
    }

    /**
     * Funzione che permette la ricerca di un viaggio all'interno del sistema sulla base di determinati filtri
     * @param luogoPartenza il luogo di partenza del viaggio da ricercare
     * @param luogoDestinazione il luogo di destinazione del viaggio da ricercare
     * @param dataPartenza la data di partenza del viaggio da ricercare
     * @return ArrayList di DTO che rappresentano l'insieme di viaggi che rispettano i filtri
     * @throws RicercaViaggioFailedException se la ricerca dei viaggi fallisce
     */
    public ArrayList<MyDto> ricercaViaggio(String luogoPartenza, String luogoDestinazione,
                                           LocalDate dataPartenza) throws RicercaViaggioFailedException {

        ArrayList<MyDto> viaggiTrovati = new ArrayList<>();
        try {
            ArrayList<ViaggioDAO> listaViaggi = ViaggioDAO.getViaggi();
            for (ViaggioDAO viaggio : listaViaggi) {

                EntityViaggio entityViaggio = new EntityViaggio(viaggio);
                if (entityViaggio.getLuogoPartenza().equalsIgnoreCase(luogoPartenza) &&
                    entityViaggio.getLuogoDestinazione().equalsIgnoreCase(luogoDestinazione) &&
                    entityViaggio.getDataPartenza().toLocalDate().isEqual(dataPartenza)) {
                        MyDto viaggioDTO = caricaViaggioDTO(entityViaggio, entityViaggio.getAutista());
                        viaggiTrovati.add(viaggioDTO);
                }
            }
        } catch (DatabaseException e) {
            throw new RicercaViaggioFailedException("Ricerca viaggio fallito: " + e.getMessage());
        }

        return viaggiTrovati;
    }

    /**
     * Funzione di utilità per ricercaViaggi che permette il popolamento dei DTO da restituire
     * @param viaggio l'entità viaggio individuata nella ricerca
     * @param utenteRegistrato l'autista del viaggio
     * @return viaggioDTO cioè il DTO contenente il viaggio con tutte le informazioni
     */
    private MyDto caricaViaggioDTO(EntityViaggio viaggio, EntityUtenteRegistrato utenteRegistrato) {
        MyDto viaggioDTO = new MyDto();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(NATURALDATEFORMAT);
        viaggioDTO.setCampo1(String.valueOf(viaggio.getId()));
        viaggioDTO.setCampo2(viaggio.getLuogoPartenza());
        viaggioDTO.setCampo3(viaggio.getLuogoDestinazione());
        viaggioDTO.setCampo4(viaggio.getDataPartenza().format(dateTimeFormatter));
        viaggioDTO.setCampo5(viaggio.getDataArrivo().format(dateTimeFormatter));
        viaggioDTO.setCampo6(String.valueOf(viaggio.getContributoSpese()));
        viaggioDTO.setCampo7(String.format("%s %s", utenteRegistrato.getNome(), utenteRegistrato.getCognome()));
        return viaggioDTO;
    }

    /**
     * Funzione di utilità per GeneraReportIncassi per caricare tutti i viaggi dal database
     * @return listaDAOViaggi cioè tutti i viaggi presenti all'interno del database
     * @throws DatabaseException se si verifica un errore durante il caricamento di tutti i viaggi dal database
     */
    private static ArrayList<ViaggioDAO> caricaViaggiDaDB() throws DatabaseException{
        ArrayList<ViaggioDAO> listaDAOViaggi;
        listaDAOViaggi = ViaggioDAO.getViaggi();
        return listaDAOViaggi;
    }

    private static ArrayList<PrenotazioneDAO> caricaPrenotazioniDaDB() throws DatabaseException {
        ArrayList<PrenotazioneDAO> listaDAOPrenotazioni;
        listaDAOPrenotazioni = PrenotazioneDAO.getPrenotazioni();
        return listaDAOPrenotazioni;
    }
}

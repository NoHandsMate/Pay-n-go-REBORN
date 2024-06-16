package entity;

import database.ViaggioDAO;
import dto.MyDto;
import exceptions.DatabaseException;
import exceptions.ReportIncassiFailedException;
import exceptions.RicercaViaggioFailedException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe del package entity nel modello BCED, essa implementa l'information expert dei viaggi.
 */
public class GestoreViaggi {

    /**
     * L'unica istanza di GestoreViaggi che implementa il pattern Singleton.
     */
    private static GestoreViaggi uniqueInstance;

    /**
     * Costante utilizzata da DateTimeFormatter per la conversione da data simbolica a stringa.
     */
    private static final String NATURALDATEFORMAT = "dd/MM/yyyy HH:mm:ss";

    /**
     * Costruttore privato per impedire la creazione di istanze multiple.
     */
    private GestoreViaggi() {}

    /**
     * Funzione statica per richiamare l'unica istanza di GestoreViaggi o crearne una se non esiste già.
     * @return l'istanza singleton di GestoreViaggi.
     */
    public static GestoreViaggi getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new GestoreViaggi();
        }
        return uniqueInstance;
    }

    /**
     * Funzione che permette al gestore dell'applicazione di generare un report degli incassi di tutti i viaggi nel
     * sistema.
     * @return l'incasso complessivo del sistema.
     * @throws ReportIncassiFailedException ne non è stato possibile generare il report di incassi.
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
     * Funzione che permette la ricerca di un viaggio all'interno del sistema sulla base di determinati filtri.
     * @param luogoPartenza il luogo di partenza del viaggio da ricercare.
     * @param luogoDestinazione il luogo di destinazione del viaggio da ricercare.
     * @param dataPartenza la data di partenza del viaggio da ricercare.
     * @return ArrayList di DTO che rappresentano l'insieme di viaggi che rispettano i filtri.
     * @throws RicercaViaggioFailedException se non è stato possibile effettuare la ricerca del viaggio.
     */
    public List<MyDto> ricercaViaggio(String luogoPartenza, String luogoDestinazione,
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
     * Funzione di utilità per {@link #ricercaViaggio(String, String, LocalDate) ricercaViaggio} che permette il
     * popolamento dei DTO da restituire.
     * @param viaggio l'entità viaggio individuata nella ricerca.
     * @param utenteRegistrato l'autista del viaggio.
     * @return il DTO contenente tutte le informazioni del viaggio.
     */
    private MyDto caricaViaggioDTO(EntityViaggio viaggio, EntityUtenteRegistrato utenteRegistrato) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(NATURALDATEFORMAT);
        return new MyDto(String.valueOf(viaggio.getId()),
                viaggio.getLuogoPartenza(),
                viaggio.getLuogoDestinazione(),
                viaggio.getDataPartenza().format(dateTimeFormatter),
                viaggio.getDataArrivo().format(dateTimeFormatter),
                String.format("%.2f €", viaggio.getContributoSpese()).replace(',', '.'),
                String.format("%s %s", utenteRegistrato.getNome(), utenteRegistrato.getCognome()),
                null);
    }

    /**
     * Funzione di utilità per {@link #generaReportIncassi() generaReportIncassi} per caricare tutti i viaggi dal
     * database.
     * @return lista di DAO rappresentante tutti i viaggi presenti all'interno del database.
     * @throws DatabaseException se si verifica un errore durante il caricamento di tutti i viaggi dal database.
     */
    private static ArrayList<ViaggioDAO> caricaViaggiDaDB() throws DatabaseException{
        ArrayList<ViaggioDAO> listaDAOViaggi;
        listaDAOViaggi = ViaggioDAO.getViaggi();
        return listaDAOViaggi;
    }
}

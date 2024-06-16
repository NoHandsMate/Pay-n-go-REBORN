package boundary;

import control.ControllerGestore;
import control.ControllerUtente;
import dto.MyDto;
import utility.Utilities;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe del package boundary nel modello BCED, essa implementa l'interfaccia utilizzabile dal gestore
 * dell'applicazione per effettuare le operazioni di reportistica.
 */
public class FormGestore extends JFrame {
    private JPanel gestorePanel;
    private JTabbedPane contentTab;
    private JPanel circularLogoPanel;
    private JButton homeTabButton;
    private JButton reportUtentiTabButton;
    private JButton reportIncassiTabButton;
    private JEditorPane homeEditorPane;
    private JTable utentiTable;
    private JTable valutazioniTable;
    private JLabel incassiLabel;

    private final JButton[] tabButtons = {homeTabButton, reportUtentiTabButton, reportIncassiTabButton};
    private static final String NATURALDATEFORMAT = "dd/MM/yyyy - HH:mm:ss";

    /**
     * FormGestore è il pannello di amministratore dedicato al gestore dell'applicazione. È possibile accedervi
     * digitando le credenziali associate all'account di amministrazione.
     */
    public FormGestore() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pay-n-go REBORN - Amministrazione");
        setSize(1000, 800);
        setMinimumSize(new Dimension(1000, 800));
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().add(new JScrollPane(gestorePanel), BorderLayout.CENTER);
        contentTab.setUI(new BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tabPlacement, int runCount, int maxTabHeight) {
                return -1;
            }
        });

        MyDto sessione = ControllerUtente.getInstance().getSessione();

        if (Long.parseLong(sessione.getCampo1()) == 0)
        {
            JOptionPane.showMessageDialog(rootPane, "Non è presente una sessione utente attiva!", "Errore",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } else if (Long.parseLong(sessione.getCampo1()) != 1) {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
            setVisible(false);
        }

        homeTabButton.addActionListener(actionEvent -> setTabActive(0));
        reportUtentiTabButton.addActionListener(actionEvent -> setTabActive(1));
        reportIncassiTabButton.addActionListener(actionEvent -> setTabActive(2));

        contentTab.addChangeListener(e -> {
            if (contentTab.getSelectedIndex() == 1) {
                generaReportUtenti();
            } else if (contentTab.getSelectedIndex() == 2) {
                generaReportIncassi();
            }
        });

        utentiTable.getSelectionModel().addListSelectionListener(selectionEvent -> {
            int selectedRow = utentiTable.getSelectedRow();
            if (selectedRow != -1) {
                visualizzaValutazioniUtente(Long.parseLong((String) utentiTable.getValueAt(selectedRow, 0)));
            } else visualizzaValutazioniUtente(0); // idUtente nullo per svuotare la tabella.
        });
    }

    /**
     * Creazione degli ImagePanel per la visualizzazione delle immagini.
     */
    private void createUIComponents() {
        circularLogoPanel = new ImagePanel("resources/payngo-circlogo.png", 32, 32);
    }

    /**
     * Funzione di utilità che permette di cambiare tab attivo dell'applicazione attraverso JTabbedPane.
     * @param index l'indice del tab da visualizzare.
     */
    private void setTabActive(int index) {
        contentTab.setSelectedIndex(index);
        if (index < 0 || index > 3)
            return;
        for (int i = 0; i < 3; i++) {
            if (i != index) {
                tabButtons[i].setBackground(new Color(240, 155, 50));
                tabButtons[i].setForeground(Color.BLACK);
            } else {
                tabButtons[i].setBackground(new Color(15, 53, 156));
                tabButtons[i].setForeground(Color.WHITE);
            }
        }
    }

    /**
     * Funzione che, insieme con <code>visualizzaValutazioniUtente</code>, implementa il caso d'uso
     * <code>generaReportUtenti</code>. Restituisce a schermo la lista degli utenti iscritti al sistema, con una
     * valutazione sintetica degli stessi.
     */
    private void generaReportUtenti() {
        String[] columnNames = {"Id Utente", "Nome e cognome", "Email", "Valutazione media"};

        AbstractMap.SimpleEntry<Boolean, Object> result = ControllerGestore.getInstance().generaReportUtenti();
        if (Boolean.FALSE.equals(result.getKey())) {
            JOptionPane.showMessageDialog(rootPane, result.getValue(), "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<MyDto> listaUtenti = (List<MyDto>)result.getValue();
        List<String> rows = new ArrayList<>();
        for (MyDto utente : listaUtenti) {
            rows.add(utente.getCampo1());
            rows.add(utente.getCampo2());
            rows.add(utente.getCampo3());
            rows.add(utente.getCampo4());
        }

        String[][] data = new String[rows.size() / columnNames.length][columnNames.length];

        Utilities.rowToMatrix(rows, data, columnNames.length);
        Utilities.populateTable(utentiTable, columnNames, data);
    }

    /**
     * Funzione che, insieme con <code>generaReportUtenti</code>, implementa il caso d'uso
     * <code>generaReportUtenti</code>. Restituisce a schermo la lista delle valutazioni relative a ogni utente.
     */
    private void visualizzaValutazioniUtente(long idUtente) {
        String[] columnNames = {"Id valutazione", "Numero stelle", "Descrizione"};
        String[][] data;
        List<String> rows = new ArrayList<>();
        if (idUtente == 0) {
            data = new String[0][0];
        } else {
            AbstractMap.SimpleEntry<Boolean, Object> result =
                    ControllerGestore.getInstance().visualizzaValutazioniUtente(idUtente);
            if (Boolean.FALSE.equals(result.getKey())) {
                JOptionPane.showMessageDialog(rootPane, result.getValue(), "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<MyDto> listaValutazioni = (List<MyDto>) result.getValue();
            for (MyDto valutazione : listaValutazioni) {
                rows.add(valutazione.getCampo1());
                rows.add(valutazione.getCampo2());
                rows.add(valutazione.getCampo3());
            }

            data = new String[rows.size() / columnNames.length][columnNames.length];
        }

        Utilities.rowToMatrix(rows, data, columnNames.length);
        Utilities.populateTable(valutazioniTable, columnNames, data);
    }

    /**
     * Funzione che implementa il caso d'uso <code>generaReportIncassi</code>. Restituisce a schermo il totale degli
     * incassi del sistema.
     */
    private void generaReportIncassi() {
        AbstractMap.SimpleEntry<Boolean, Object> result = ControllerGestore.getInstance().generaReportIncassi();
        if (Boolean.FALSE.equals(result.getKey())) {
            JOptionPane.showMessageDialog(contentTab, result.getValue(), "Errore", JOptionPane.ERROR_MESSAGE);
            incassiLabel.setText("Non è stato possibile generare il report incassi.");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(NATURALDATEFORMAT);
        incassiLabel.setText(String.format("Gli incassi totali del sistema al giorno %s sono: %.2f €",
                LocalDateTime.now().format(formatter), (float) result.getValue()));
    }
}

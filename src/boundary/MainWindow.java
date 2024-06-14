package boundary;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DateTimePicker;
import control.ControllerUtente;
import dto.*;

public class MainWindow extends JFrame {
    private JPanel mainWindowPanel;
    private JTabbedPane contentTab;
    private JLabel welcomeLabel;
    private JPanel circularLogoPanel;
    private JButton accountTabButton;
    private JButton homeTabButton;
    private JButton valutaTabButton;
    private JButton ricercaTabButton;
    private JButton condividiTabButton;
    private JButton viaggiTabButton;
    private JEditorPane homeEditorPane;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField autoField;
    private JSpinner postiSpinner;
    private JButton aggiornaDatiPersonaliButton;
    private JTextField telefonoField;
    private JTextField luogoPartenzaField;
    private JTextField luogoArrivoField;
    private JButton cercaViaggioButton;
    private JTable viaggiTrovatiTable;
    private JButton prenotaViaggioButton;
    private JTable prenotazioniEffettuateTable;
    private DatePicker dataPartenzaPicker;
    private JButton valutaAutistaButton;
    private JTextField condividiLuogoPartenzaField;
    private JTextField condividiLuogoDestinazioneField;
    private DateTimePicker condividiDataOraPartenzaPicker;
    private DateTimePicker condividiDataOraArrivoPicker;
    private JTextField condividiContributoSpeseField;
    private JButton condividiViaggioButton;
    private JTable viaggiCondivisiTable;
    private JTable prenotazioniTable;
    private JButton gestisciPrenotazioneButton;
    private JButton valutaPasseggeroButton;

    private final JButton[] tabButtons = {homeTabButton, accountTabButton, ricercaTabButton, valutaTabButton,
            condividiTabButton, viaggiTabButton};

    public MainWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pay-n-go REBORN");
        setSize(1000, 800);
        setMinimumSize(new Dimension(1000, 800));
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().add(new JScrollPane(mainWindowPanel), BorderLayout.CENTER);
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
        } else if (Long.parseLong(sessione.getCampo1()) == 1) {
            FormGestore formGestore = new FormGestore();
            formGestore.setVisible(true);
            setVisible(false);
        }

        welcomeLabel.setText(String.format("Ciao %s %s!",
                sessione.getCampo2(), sessione.getCampo3()));

        homeTabButton.addActionListener(actionEvent -> setTabActive(0));
        accountTabButton.addActionListener(actionEvent -> setTabActive(1));
        ricercaTabButton.addActionListener(actionListener -> setTabActive(2));
        valutaTabButton.addActionListener(actionListener -> setTabActive(3));
        condividiTabButton.addActionListener(actionListener -> setTabActive(4));
        viaggiTabButton.addActionListener(actionListener -> setTabActive(5));

        contentTab.addChangeListener(e -> {
            if (contentTab.getSelectedIndex() == 0) {
                MyDto datiSessione = ControllerUtente.getInstance().getSessione();
                welcomeLabel.setText(String.format("Ciao %s %s!", datiSessione.getCampo2(), datiSessione.getCampo3()));
            }
            else if (contentTab.getSelectedIndex() == 1) {
                populateAccountTab();
            } else if (contentTab.getSelectedIndex() == 3) {
                visualizzaPrenotazioniEffettuate();
            } else if (contentTab.getSelectedIndex() == 5) {
                visualizzaViaggiCondivisi();
            }
        });

        aggiornaDatiPersonaliButton.addActionListener(actionEvent -> aggiornaDatiPersonali());

        viaggiTrovatiTable.getSelectionModel().addListSelectionListener(selectionEvent ->
                prenotaViaggioButton.setEnabled(viaggiTrovatiTable.getSelectedRow() != -1));

        cercaViaggioButton.addActionListener(actionEvent -> {
            AbstractMap.SimpleEntry<Boolean, String> result = ricercaViaggiValidateInput();
            if (Boolean.FALSE.equals(result.getKey())) {
                JOptionPane.showMessageDialog(mainWindowPanel, result.getValue(), "Errore",
                        JOptionPane.ERROR_MESSAGE);
            } else
                ricercaViaggi();
        });

        prenotaViaggioButton.addActionListener(actionEvent -> {
            int selectedRow = viaggiTrovatiTable.getSelectedRow();
            if (selectedRow != -1) {
                prenotaViaggio(Long.parseLong((String) viaggiTrovatiTable.getValueAt(selectedRow, 0)));
            }
        });

        prenotazioniEffettuateTable.getSelectionModel().addListSelectionListener(selectionEvent ->
                valutaAutistaButton.setEnabled(prenotazioniEffettuateTable.getSelectedRow() != -1));

        valutaAutistaButton.addActionListener(actionEvent -> {
            int selectedRow = prenotazioniEffettuateTable.getSelectedRow();
            if (selectedRow != -1) {
                valutaAutista(Long.parseLong((String) prenotazioniEffettuateTable.getValueAt(selectedRow, 0)),
                        (String) prenotazioniEffettuateTable.getValueAt(selectedRow, 1));
            }
        });

        condividiViaggioButton.addActionListener(actionEvent -> condividiViaggio());

        viaggiCondivisiTable.getSelectionModel().addListSelectionListener(selectionEvent -> {
            int selectedRow = viaggiCondivisiTable.getSelectedRow();
            if (selectedRow != -1) {
                visualizzaPrenotazioni(Long.parseLong(
                        (String) viaggiCondivisiTable.getValueAt(selectedRow, 0)));
            } else visualizzaPrenotazioni(0); // idViaggio nullo per svuotare la tabella.
        });
    }

    private void createUIComponents() {
        circularLogoPanel = new ImagePanel("resources/payngo-circlogo.png", 42, 42);
    }

    private void setTabActive(int index) {
        contentTab.setSelectedIndex(index);
        if (index < 0 || index > 5)
            return;
        for (int i = 0; i < 6; i++) {
            if (i != index) {
                tabButtons[i].setBackground(new Color(240, 155, 50));
                tabButtons[i].setForeground(Color.BLACK);
            } else {
                tabButtons[i].setBackground(new Color(15, 53, 156));
                tabButtons[i].setForeground(Color.WHITE);
            }
        }
    }

    private void populateTable(JTable table, Object[] columnNames, Object[][] data) {
        TableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 16));
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(48, 48, 48));
        headerRenderer.setOpaque(true);
        table.getTableHeader().setDefaultRenderer(headerRenderer);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
    }

    private void populateAccountTab() {
        MyDto datiSessione = ControllerUtente.getInstance().getSessione();
        nomeField.setText(datiSessione.getCampo2());
        cognomeField.setText(datiSessione.getCampo3());
        emailField.setText(datiSessione.getCampo4());
        passwordField.setText(datiSessione.getCampo5());
        telefonoField.setText(datiSessione.getCampo6());
        autoField.setText(datiSessione.getCampo7());
        postiSpinner.setValue(Integer.parseInt(datiSessione.getCampo8()));
    }

    private void aggiornaDatiPersonali() {
        AbstractMap.SimpleEntry<Boolean, String> result = validateAggiornaDatiPersonaliInput();
        if (Boolean.FALSE.equals(result.getKey())) {
            JOptionPane.showMessageDialog(rootPane, result.getValue(),
                    "Errore", JOptionPane.ERROR_MESSAGE);
        } else {
            result = ControllerUtente.getInstance().aggiornaDatiPersonali(nomeField.getText(), cognomeField.getText(),
                    emailField.getText(), passwordField.getPassword(), telefonoField.getText(), autoField.getText(),
                    (Integer) postiSpinner.getValue());

            if (Boolean.FALSE.equals(result.getKey())) {
                JOptionPane.showMessageDialog(rootPane, result.getValue(), "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(rootPane, result.getValue(), "OK", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private AbstractMap.SimpleEntry<Boolean, String> validateAggiornaDatiPersonaliInput() {
        if (nomeField.getText().isBlank() || cognomeField.getText().isBlank() ||
                emailField.getText().isBlank() || passwordField.getPassword().length == 0 ||
                telefonoField.getText().isBlank()) {
            return new AbstractMap.SimpleEntry<>(false, "Riempi i campi obbligatori");
        }

        if (!autoField.getText().isBlank() && (Integer) postiSpinner.getValue() == 0 ||
                (autoField.getText().isBlank() && (Integer) postiSpinner.getValue() != 0)) {
            return new AbstractMap.SimpleEntry<>(false, "Il valore dei posti disponibili è incorretto");
        }

        Pattern specialCharRegex = Pattern.compile("[^a-zA-Z]", Pattern.CASE_INSENSITIVE);
        Pattern numberRegex = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        Pattern telRegex = Pattern.compile("[^+0-9]", Pattern.CASE_INSENSITIVE);
        Matcher nomeMatcher = specialCharRegex.matcher(nomeField.getText());
        Matcher nomeMatcherNumber = numberRegex.matcher(nomeField.getText());
        Matcher cognomeMatcher = specialCharRegex.matcher(cognomeField.getText());
        Matcher cognomeMatcherNumber = numberRegex.matcher(cognomeField.getText());
        Matcher telefonoMatcher = telRegex.matcher(telefonoField.getText());


        if (nomeField.getText().length() > 50) {
            return new AbstractMap.SimpleEntry<>(false, "Il nome supera la lunghezza massima di 50 caratteri");
        }

        if (nomeMatcher.find() || nomeMatcherNumber.find()) {
            return new AbstractMap.SimpleEntry<>(false, "Il nome contiene caratteri non validi");
        }

        if (cognomeField.getText().length() > 50) {
            return new AbstractMap.SimpleEntry<>(false, "Il cognome supera la lunghezza massima di 50 caratteri");
        }

        if (cognomeMatcher.find() || cognomeMatcherNumber.find()) {
            return new AbstractMap.SimpleEntry<>(false, "Il cognome contiene caratteri non validi");
        }

        if (emailField.getText().length() > 50) {
            return new AbstractMap.SimpleEntry<>(false, "L'email supera la lunghezza massima di 50 caratteri");
        }

        if (emailField.getText().indexOf('@') == -1) {
            return new AbstractMap.SimpleEntry<>(false, "L'email non è valida");
        }

        if (telefonoField.getText().length() > 15) {
            return new AbstractMap.SimpleEntry<>(false, "Il contatto telefonico supera la lunghezza massima di 15 caratteri");
        }

        if (telefonoMatcher.find()) {
            return new AbstractMap.SimpleEntry<>(false, "Il contatto telefonico contiene caratteri non validi");
        }

        if (passwordField.getPassword().length > 50 || passwordField.getPassword().length < 4) {
            return new AbstractMap.SimpleEntry<>(false, "La password deve essere compresa tra 4 e 50 caratteri");
        }

        if (autoField.getText().length() > 50) {
            return new AbstractMap.SimpleEntry<>(false, "Il modello dell'automobile supera la lunghezza massima di 50 caratteri");
        }
        return new AbstractMap.SimpleEntry<>(true, "OK");
    }

    private void condividiViaggio() {
        AbstractMap.SimpleEntry<Boolean, String> result = validateCondividiViaggioInput();

        if (Boolean.FALSE.equals(result.getKey()))
            JOptionPane.showMessageDialog(mainWindowPanel, result.getValue(), "Errore", JOptionPane.ERROR_MESSAGE);
        else {
            // Variabili temporanee per facilitare la lettura
            String luogoPartenza = condividiLuogoPartenzaField.getText();
            String luogoDestinazione = condividiLuogoDestinazioneField.getText();
            LocalDateTime dataPartenza = condividiDataOraPartenzaPicker.getDateTimeStrict();
            LocalDateTime dataArrivo = condividiDataOraArrivoPicker.getDateTimeStrict();
            float contributoSpese = Float.parseFloat(condividiContributoSpeseField.getText());

            result = ControllerUtente.getInstance().condividiViaggio(luogoPartenza, luogoDestinazione,
                    dataPartenza, dataArrivo, contributoSpese);

            String title = Boolean.FALSE.equals(result.getKey()) ? "Errore" : "Info";
            int messageType = Boolean.FALSE.equals(result.getKey()) ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;

            JOptionPane.showMessageDialog(mainWindowPanel, result.getValue(), title, messageType);
        }
    }

    private AbstractMap.SimpleEntry<Boolean, String> validateCondividiViaggioInput() {
        if (condividiLuogoPartenzaField.getText().isBlank() || condividiLuogoDestinazioneField.getText().isBlank() ||
                condividiDataOraPartenzaPicker.timePicker.getTime() == null ||
                condividiDataOraPartenzaPicker.datePicker.getDate() == null ||
                condividiDataOraArrivoPicker.timePicker.getTime() == null ||
                condividiDataOraArrivoPicker.datePicker.getDate() == null ||
                condividiContributoSpeseField.getText().isBlank()) {
            return new AbstractMap.SimpleEntry<>(false, "Riempi i campi obbligatori");
        }

        Pattern specialCharRegex = Pattern.compile("[^a-z A-Z]", Pattern.CASE_INSENSITIVE);
        Pattern floatRegex = Pattern.compile("^[+]?([0-9]*[.])?[0-9]+$", Pattern.CASE_INSENSITIVE);
        Matcher condividiLuogoPartenzaMatcher = specialCharRegex.matcher(condividiLuogoPartenzaField.getText());
        Matcher condividiLuogoDestinazioneMatcher = specialCharRegex.matcher(condividiLuogoDestinazioneField.getText());
        Matcher condividiContributoSpeseMatcher = floatRegex.matcher(condividiContributoSpeseField.getText());

        if (condividiLuogoPartenzaField.getText().length() > 50) {
            return new AbstractMap.SimpleEntry<>(false, "Il luogo di partenza supera la lunghezza massima di 50 caratteri");
        }

        if (condividiLuogoPartenzaMatcher.find()) {
            return new AbstractMap.SimpleEntry<>(false, "Il luogo di partenza contiene caratteri non validi");
        }

        if (condividiLuogoDestinazioneField.getText().length() > 50) {
            return new AbstractMap.SimpleEntry<>(false, "Il luogo di destinazione supera la lunghezza massima di 50 caratteri");
        }

        if (condividiLuogoDestinazioneMatcher.find()) {
            return new AbstractMap.SimpleEntry<>(false, "Il luogo di destinazione contiene caratteri non validi");
        }

        if (!condividiContributoSpeseMatcher.find()) {
            return new AbstractMap.SimpleEntry<>(false, "Il contributo spese contiene caratteri non validi");
        }

        LocalDateTime dataPartenzaTemp = condividiDataOraPartenzaPicker.getDateTimeStrict();
        LocalDateTime dataArrivoTemp = condividiDataOraArrivoPicker.getDateTimeStrict();

        if (dataPartenzaTemp.isAfter(dataArrivoTemp) || dataPartenzaTemp.isEqual(dataArrivoTemp)) {
            return new AbstractMap.SimpleEntry<>(false, "La data ed ora di arrivo deve essere successiva alla " +
                    "data ed ora di partenza.");
        }

        if (dataPartenzaTemp.isBefore(LocalDateTime.now()) || dataPartenzaTemp.isEqual(LocalDateTime.now())) {
            return new AbstractMap.SimpleEntry<>(false, "La data di partenza è invalida.");
        }

        return new AbstractMap.SimpleEntry<>(true, "OK");

    }

    private void ricercaViaggi() {
        AbstractMap.SimpleEntry<Boolean, Object> result;
        result = ControllerUtente.getInstance().ricercaViaggio(luogoPartenzaField.getText(),
                luogoArrivoField.getText(),
                dataPartenzaPicker.getDate());

        if (Boolean.TRUE.equals(result.getKey())) {
            ArrayList<MyDto> viaggiTrovati = (ArrayList<MyDto>)result.getValue();
            ArrayList<String> rows = new ArrayList<>();
            for (MyDto viaggi : viaggiTrovati) {
                rows.add(viaggi.getCampo1());
                rows.add(viaggi.getCampo2());
                rows.add(viaggi.getCampo3());
                rows.add(viaggi.getCampo4());
                rows.add(viaggi.getCampo5());
                rows.add(viaggi.getCampo6());
                rows.add(viaggi.getCampo7());
            }

            if (rows.isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Non è stato trovato alcun viaggio corrispondente ai " +
                        "criteri di ricerca inseriti.", "Nessun viaggio trovato", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String[] columnNames = {"Id viaggio", "Partenza", "Destinazione", "Data e ora partenza",
                    "Data e ora arrivo", "Contributo spese", "Autista"};
            String[][] data = new String[rows.size() / columnNames.length][columnNames.length];

            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++)
                {
                    data[i][j] = rows.get(i * columnNames.length + j);
                }
            }
            populateTable(viaggiTrovatiTable, columnNames, data);

        } else {
            JOptionPane.showMessageDialog(rootPane, result.getValue(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private AbstractMap.SimpleEntry<Boolean, String> ricercaViaggiValidateInput() {
        if (luogoPartenzaField.getText().isBlank()) {
            return new AbstractMap.SimpleEntry<>(false, "Il campo luogo di partenza non può essere vuoto");
        }

        if (luogoArrivoField.getText().isBlank()) {
            return new AbstractMap.SimpleEntry<>(false, "Il campo luogo di destinazione non può essere vuoto");
        }

        if (dataPartenzaPicker.getDate() == null) {
            return new AbstractMap.SimpleEntry<>(false, "Il campo data di partenza non può essere vuoto");
        }

        return new AbstractMap.SimpleEntry<>(true, "OK");
    }

    private void visualizzaPrenotazioniEffettuate() {

        String[] columnNames = {"Id prenotazione", "Autista", "Viaggio", "Stato"};
        AbstractMap.SimpleEntry<Boolean, Object> result =
                ControllerUtente.getInstance().visualizzaPrenotazioniEffettuate();
        if (Boolean.FALSE.equals(result.getKey())) {
            JOptionPane.showMessageDialog(rootPane, result.getValue(), "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ArrayList<MyDto> prenotazioniEffettuate = (ArrayList<MyDto>)result.getValue();
        ArrayList<String> rows = new ArrayList<>();
        for (MyDto prenotazioni : prenotazioniEffettuate) {
            rows.add(prenotazioni.getCampo1());
            rows.add(prenotazioni.getCampo2());
            rows.add(prenotazioni.getCampo3());
            rows.add(prenotazioni.getCampo4());
        }

        String[][] data = new String[rows.size() / columnNames.length][columnNames.length];

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++)
            {
                data[i][j] = rows.get(i * columnNames.length + j);
            }
        }

        populateTable(prenotazioniEffettuateTable, columnNames, data);
    }

    private void prenotaViaggio(long idViaggio) {
        AbstractMap.SimpleEntry<Boolean, String> result = ControllerUtente.getInstance().prenotaViaggio(idViaggio);

        String title = Boolean.FALSE.equals(result.getKey()) ? "Errore" : "Info";
        int messageType =
                Boolean.FALSE.equals(result.getKey()) ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
        JOptionPane.showMessageDialog(mainWindowPanel, result.getValue(), title, messageType);
    }

    private void valutaAutista(long idPrenotazione, String nomeAutista) {
        ValutaUtente valutaUtente = new ValutaUtente(idPrenotazione, nomeAutista);
        valutaUtente.setVisible(true);
    }

    private void visualizzaViaggiCondivisi() {
        /* TODO: qualcosa del genere */
        // String data[][] = ControllerUtente.getInstance().visualizzaViaggiCondivisi();

        String[] columnNames = {"Id viaggio", "Partenza", "Destinazione", "Data e ora partenza",
                "Data e ora arrivo", "Contributo spese"};
        String[][] data = {{String.valueOf(3), "Roma", "Napoli", "05/04/2024 15:30:00", "05/04/2024 17:10:00", String.valueOf(1.49f)},
                {String.valueOf(4), "Torino", "Roma", "14/05/2024 10:30:00", "14/05/2024 18:00:00", String.valueOf(3.49f)},
                {String.valueOf(5), "Casoria", "Bacoli", "25/06/2024 08:30:00", "25/06/2024 09:20:00", String.valueOf(1.49f)}};

        populateTable(viaggiCondivisiTable, columnNames, data);
    }

    private void visualizzaPrenotazioni(long idViaggio) {
        /* TODO: idViaggio 0 significa nessun viaggio selezionato, la ricerca dovrebbe tornare null e si
            dovrebbe svuotare da sola la tabella */
        // String[][] data = ControllerUtente.getInstance().visualizzaPrenotazioni();
        String[] columnNames = {"Id prenotazione", "Passeggero", "Viaggio", "Stato"};
    }
}

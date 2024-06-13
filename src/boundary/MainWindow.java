package boundary;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.AbstractMap;
import java.util.ArrayList;

import com.github.lgooddatepicker.components.DatePicker;
import control.ControllerUtente;
import dto.*;

public class MainWindow extends JFrame {
    private JPanel mainWindowPanel;
    private JLabel welcomeLabel;
    private JTabbedPane contentTab;
    private JPanel circularLogoPanel;
    private JButton accountTabButton;
    private JButton homeTabButton;
    private JButton prenotazioniTabButton;
    private JTable prenotazioniTable;
    private JButton gestisciPrenotazioneButton;
    private JEditorPane homeEditorPane;
    private JButton ricercaTabButton;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JTextField autoField;
    private JSpinner postiSpinner;
    private JButton aggiornaDatiPersonaliButton;
    private JPasswordField passwordField;
    private JTextField telefonoField;
    private JTextField luogoPartenzaField;
    private JTextField luogoArrivoField;
    private JTable viaggiTable;
    private JButton prenotaViaggioButton;
    private DatePicker dataPartenzaPicker;
    private JButton cercaViaggioButton;
    private JButton condividiTabButton;
    private JButton viaggiTabButton;

    public MainWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pay-n-go REBORN");
        setSize(1000, 800);
        setMinimumSize(new Dimension(1000, 800));
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().add(new JScrollPane(mainWindowPanel), BorderLayout.CENTER);
        if (UtenteCorrente.getInstance().getIdUtenteCorrente() == 0)
        {
            JOptionPane.showMessageDialog(rootPane, "Non è presente una sessione utente attiva!", "Errore",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } else if (UtenteCorrente.getInstance().getIdUtenteCorrente() == 1) {
            FormGestore formGestore = new FormGestore();
            formGestore.setVisible(true);
            setVisible(false);
        }

        welcomeLabel.setText(String.format("Ciao %s %s!",
                UtenteCorrente.getInstance().getNome(), UtenteCorrente.getInstance().getCognome()));

        contentTab.setUI(new BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tabPlacement, int runCount, int maxTabHeight) {
                    return -1;
            }
        });

        homeTabButton.addActionListener(actionEvent -> {
            contentTab.setSelectedIndex(0);
            homeTabButton.setBackground(new Color(15, 53, 156));
            homeTabButton.setForeground(Color.WHITE);
            accountTabButton.setBackground(new Color(240, 155, 50));
            accountTabButton.setForeground(Color.BLACK);
            ricercaTabButton.setBackground(new Color(240, 155, 50));
            ricercaTabButton.setForeground(Color.BLACK);
            prenotazioniTabButton.setBackground(new Color(240, 155, 50));
            prenotazioniTabButton.setForeground(Color.BLACK);
            condividiTabButton.setBackground(new Color(240, 155, 50));
            condividiTabButton.setForeground(Color.BLACK);
            viaggiTabButton.setBackground(new Color(240, 155, 50));
            viaggiTabButton.setForeground(Color.BLACK);
        });

        accountTabButton.addActionListener(actionEvent -> {
            contentTab.setSelectedIndex(1);
            homeTabButton.setBackground(new Color(240, 155, 50));
            homeTabButton.setForeground(Color.BLACK);
            accountTabButton.setBackground(new Color(15, 53, 156));
            accountTabButton.setForeground(Color.WHITE);
            ricercaTabButton.setBackground(new Color(240, 155, 50));
            ricercaTabButton.setForeground(Color.BLACK);
            prenotazioniTabButton.setBackground(new Color(240, 155, 50));
            prenotazioniTabButton.setForeground(Color.BLACK);
            condividiTabButton.setBackground(new Color(240, 155, 50));
            condividiTabButton.setForeground(Color.BLACK);
            viaggiTabButton.setBackground(new Color(240, 155, 50));
            viaggiTabButton.setForeground(Color.BLACK);
        });

        ricercaTabButton.addActionListener(actionListener -> {
            contentTab.setSelectedIndex(2);
            homeTabButton.setBackground(new Color(240, 155, 50));
            homeTabButton.setForeground(Color.BLACK);
            accountTabButton.setBackground(new Color(240, 155, 50));
            accountTabButton.setForeground(Color.BLACK);
            ricercaTabButton.setBackground(new Color(15, 53, 156));
            ricercaTabButton.setForeground(Color.WHITE);
            prenotazioniTabButton.setBackground(new Color(240, 155, 50));
            prenotazioniTabButton.setForeground(Color.BLACK);
            condividiTabButton.setBackground(new Color(240, 155, 50));
            condividiTabButton.setForeground(Color.BLACK);
            viaggiTabButton.setBackground(new Color(240, 155, 50));
            viaggiTabButton.setForeground(Color.BLACK);
        });

        prenotazioniTabButton.addActionListener(actionListener -> {
            contentTab.setSelectedIndex(3);
            homeTabButton.setBackground(new Color(240, 155, 50));
            homeTabButton.setForeground(Color.BLACK);
            accountTabButton.setBackground(new Color(240, 155, 50));
            accountTabButton.setForeground(Color.BLACK);
            ricercaTabButton.setBackground(new Color(240, 155, 50));
            ricercaTabButton.setForeground(Color.BLACK);
            prenotazioniTabButton.setBackground(new Color(15, 53, 156));
            prenotazioniTabButton.setForeground(Color.WHITE);
            condividiTabButton.setBackground(new Color(240, 155, 50));
            condividiTabButton.setForeground(Color.BLACK);
            viaggiTabButton.setBackground(new Color(240, 155, 50));
            viaggiTabButton.setForeground(Color.BLACK);
        });

        condividiTabButton.addActionListener(actionListener -> {
            contentTab.setSelectedIndex(4);
            homeTabButton.setBackground(new Color(240, 155, 50));
            homeTabButton.setForeground(Color.BLACK);
            accountTabButton.setBackground(new Color(240, 155, 50));
            accountTabButton.setForeground(Color.BLACK);
            ricercaTabButton.setBackground(new Color(240, 155, 50));
            ricercaTabButton.setForeground(Color.BLACK);
            prenotazioniTabButton.setBackground(new Color(240, 155, 50));
            prenotazioniTabButton.setForeground(Color.BLACK);
            condividiTabButton.setBackground(new Color(15, 53, 156));
            condividiTabButton.setForeground(Color.WHITE);
            viaggiTabButton.setBackground(new Color(240, 155, 50));
            viaggiTabButton.setForeground(Color.BLACK);
        });

        viaggiTabButton.addActionListener(actionListener -> {
            contentTab.setSelectedIndex(5);
            homeTabButton.setBackground(new Color(240, 155, 50));
            homeTabButton.setForeground(Color.BLACK);
            accountTabButton.setBackground(new Color(240, 155, 50));
            accountTabButton.setForeground(Color.BLACK);
            ricercaTabButton.setBackground(new Color(240, 155, 50));
            ricercaTabButton.setForeground(Color.BLACK);
            prenotazioniTabButton.setBackground(new Color(240, 155, 50));
            prenotazioniTabButton.setForeground(Color.BLACK);
            condividiTabButton.setBackground(new Color(240, 155, 50));
            condividiTabButton.setForeground(Color.BLACK);
            viaggiTabButton.setBackground(new Color(15, 53, 156));
            viaggiTabButton.setForeground(Color.WHITE);
        });

        contentTab.addChangeListener(e -> {
            if (contentTab.getSelectedIndex() == 0) {

            } else if (contentTab.getSelectedIndex() == 1) {
                nomeField.setText(UtenteCorrente.getInstance().getNome());
                cognomeField.setText(UtenteCorrente.getInstance().getCognome());
                emailField.setText(UtenteCorrente.getInstance().getEmail());
                passwordField.setText(UtenteCorrente.getInstance().getPassword());
                telefonoField.setText(UtenteCorrente.getInstance().getContattoTelefonico());
                autoField.setText(UtenteCorrente.getInstance().getAuto());
                postiSpinner.setValue(Integer.valueOf(UtenteCorrente.getInstance().getPostiDisp()));

            // TODO: forse non serve        } else if (contentTab.getSelectedIndex() == 2) {

            } else if (contentTab.getSelectedIndex() == 3) {
                /* TODO qualcosa del genere */
                // ControllerUtente.getInstance().visualizzaPrenotazioni

                Object[][] data = {{"1", "Nome Cognome", "3"}, {"2", "Name Surname", "3"}, {"3", "Mario Rossi", "4"}};
                String[] columnNames = {"Id prenotazione", "Passeggero", "Viaggio"};
                TableModel tableModel = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                prenotazioniTable.setModel(tableModel);
                prenotazioniTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                prenotazioniTable.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 16));
                DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
                headerRenderer.setBackground(new Color(48, 48, 48));
                headerRenderer.setOpaque(true);
                prenotazioniTable.getTableHeader().setDefaultRenderer(headerRenderer);
                prenotazioniTable.getTableHeader().setReorderingAllowed(false);
                prenotazioniTable.getTableHeader().setResizingAllowed(false);
            }
        });

        prenotazioniTable.getSelectionModel().addListSelectionListener(selectionEvent ->
                prenotaViaggioButton.setEnabled(viaggiTable.getSelectedRow() != -1));

        cercaViaggioButton.addActionListener(actionEvent -> {

            AbstractMap.SimpleEntry<Boolean, Object> result;

            result = ControllerUtente.getInstance().ricercaViaggio(luogoPartenzaField.getText(),
                                                                   luogoArrivoField.getText(),
                                                                   dataPartenzaPicker.getDate());

            if (result.getKey()) {
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
                String[] columnNames = {"Id viaggio", "Partenza", "Destinazione", "Data e ora partenza", "Data e ora arrivo", "Contributo spese", "Autista"};
                String[][] data = new String[rows.size() / columnNames.length][columnNames.length];

                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < data[i].length; j++)
                    {
                        data[i][j] = rows.get(i * columnNames.length + j);
                    }
                }

                TableModel tableModel = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                viaggiTable.setModel(tableModel);
                viaggiTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                viaggiTable.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 16));
                DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
                headerRenderer.setBackground(new Color(48, 48, 48));
                headerRenderer.setOpaque(true);
                viaggiTable.getTableHeader().setDefaultRenderer(headerRenderer);
                viaggiTable.getTableHeader().setReorderingAllowed(false);
                viaggiTable.getTableHeader().setResizingAllowed(false);
            } else {
                JOptionPane.showMessageDialog(rootPane, result.getValue(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        viaggiTable.getSelectionModel().addListSelectionListener(selectionEvent ->
                prenotaViaggioButton.setEnabled(viaggiTable.getSelectedRow() != -1));

        prenotaViaggioButton.addActionListener(actionEvent -> {

        });
    }

    private void createUIComponents() {
        circularLogoPanel = new MainWindowCircularLogoPanel();
    }
}

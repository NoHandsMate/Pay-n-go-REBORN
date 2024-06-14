package boundary;

import control.ControllerUtente;
import dto.MyDto;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

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
            }
        });

        utentiTable.getSelectionModel().addListSelectionListener(selectionEvent -> visualizzaValutazioniUtente());
    }

    private void createUIComponents() {
        circularLogoPanel = new ImagePanel("resources/payngo-circlogo.png", 32, 32);
    }

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

    private void generaReportUtenti() {
        /* TODO: qualcosa del tipo String[][] data = Controller.getInstance().generaReportUtenti() */

        String[][] data = {{String.valueOf(1), "Matteo Arnese", "matteo.arnese@github.com", String.valueOf(4.65f)},
                {String.valueOf(2), "Emanuele Barbato", "e@barb.com", String.valueOf(3.69f)}};
        String[] columnNames = {"Id Utente", "Nome e cognome", "Email", "Valutazione media"};
    }

    private void visualizzaValutazioniUtente() {
        /* TODO: implementa */
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
}

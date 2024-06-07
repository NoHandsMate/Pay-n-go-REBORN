package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class Prova extends JFrame {
    private JPanel provaPanel;
    private JTable tableProva;
    private JButton confirmButton;
    private JTextField nameTextField;

    public Prova() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Form di prova");
        //setSize(800, 800);
        //getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(provaPanel), BorderLayout.CENTER);
        Object[][] data = {{"A", "B"}, {"C", "D"}, {"E", "F"}};
        String[] columnNames = {"Lettera1", "Lettera2"};
        TableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableProva.setModel(tableModel);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Prova window = new Prova();
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

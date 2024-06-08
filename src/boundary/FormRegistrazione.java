package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormRegistrazione extends JFrame {
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JTextField autoField;
    private JPasswordField passwordField;
    private JSpinner postiSpinner;
    private JButton registratiButton;
    private JTextField telefonoField;

    public FormRegistrazione() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Registrazione");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        registratiButton.addActionListener(actionEvent -> {
          boolean result = validateInput();

          if (!result) {
              JOptionPane.showMessageDialog(rootPane, "Riempi i campi obbligatori!",
                                       "Errore", JOptionPane.ERROR_MESSAGE);
          }

        });
    }

    private boolean validateInput() {
        if (nomeField.getText().isBlank() || cognomeField.getText().isBlank() ||
            emailField.getText().isBlank() || passwordField.getPassword().length == 0 ||
            telefonoField.getText().isBlank()) {
            return false;
        }

        if (autoField.getText().isBlank() && (Integer) postiSpinner.getValue() != 0){
            return false;
        }

        return true;
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
               FormRegistrazione window = new FormRegistrazione();
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

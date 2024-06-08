package boundary;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap;
import control.ControllerUtente;


public class FormRegistrazione extends JFrame {
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JTextField autoField;
    private JPasswordField passwordField;
    private JSpinner postiSpinner;
    private JButton registratiButton;
    private JTextField telefonoField;
    private JPanel registrazionePanel;

    public FormRegistrazione() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Registrazione");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().add(new JScrollPane(registrazionePanel), BorderLayout.CENTER);
        registratiButton.addActionListener(actionEvent -> {
          AbstractMap.SimpleImmutableEntry<Boolean, String> result = validateInput();

          if (!result.getKey()) {
              JOptionPane.showMessageDialog(rootPane, result.getValue(),
                                       "Errore", JOptionPane.ERROR_MESSAGE);
          } else {
              /*TODO: Chiama il metodo registraUtente del controller*/
          }

        });
    }

    private AbstractMap.SimpleImmutableEntry<Boolean, String> validateInput() {
        if (nomeField.getText().isBlank() || cognomeField.getText().isBlank() ||
            emailField.getText().isBlank() || passwordField.getPassword().length == 0 ||
            telefonoField.getText().isBlank()) {
            return new AbstractMap.SimpleImmutableEntry<>(false, "Riempi i campi obbligatori!");
        }

        if (!autoField.getText().isBlank() && (Integer) postiSpinner.getValue() == 0){
            return new AbstractMap.SimpleImmutableEntry<>(false, "Il valore dei posti disponibili");
        }

        return new AbstractMap.SimpleImmutableEntry<>(true, "OK");
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

package boundary;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap;

import control.ControllerUtente;
import utility.Utilities;

/**
 * Classe del package boundary nel modello BCED, essa implementa l'interfaccia utilizzabile dagli utenti per registrarsi
 * al sistema.
 */
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

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Registrazione");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().add(new JScrollPane(registrazionePanel), BorderLayout.CENTER);
        getRootPane().setDefaultButton(registratiButton);
        postiSpinner.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

        registratiButton.addActionListener(actionEvent -> {
          AbstractMap.SimpleEntry<Boolean, String> result = Utilities.validateDatiPersonali(nomeField.getText(),
                  cognomeField.getText(),
                  emailField.getText(),
                  passwordField.getPassword(),
                  telefonoField.getText(),
                  autoField.getText(),
                  (Integer) postiSpinner.getValue());

          if (Boolean.FALSE.equals(result.getKey())) {
              JOptionPane.showMessageDialog(rootPane, result.getValue(),
                                       "Errore", JOptionPane.ERROR_MESSAGE);
          } else {
              result = ControllerUtente.getInstance().registraUtente(nomeField.getText(), cognomeField.getText(),
                      emailField.getText(), autoField.getText(),
                      passwordField.getPassword(), (Integer) postiSpinner.getValue(),
                      telefonoField.getText());

              if (Boolean.FALSE.equals(result.getKey())) {
                  JOptionPane.showMessageDialog(rootPane, result.getValue(), "Errore", JOptionPane.ERROR_MESSAGE);
              } else {
                  JOptionPane.showMessageDialog(rootPane, result.getValue(), "OK", JOptionPane.INFORMATION_MESSAGE);
                  MainWindow mainWindow = new MainWindow();
                  mainWindow.setVisible(true);
                  setVisible(false);
              }

          }

        });
    }
}

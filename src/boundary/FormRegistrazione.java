package boundary;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import control.ControllerUtente;
import utility.Utilities;


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

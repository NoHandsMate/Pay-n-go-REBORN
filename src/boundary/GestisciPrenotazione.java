package boundary;

import control.ControllerUtente;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap;

/**
 * Classe del package boundary nel modello BCED, essa implementa la l'interfaccia utilizzabile dagli utenti registrati
 * per gestire una prenotazione su un viaggio da loro condiviso.
 */
public class GestisciPrenotazione extends JDialog {
    private final long idPrenotazione;
    private JPanel contentPane;
    private JButton accettaButton;
    private JButton rifiutaButton;
    private JEditorPane infoPane;

    public GestisciPrenotazione(long idPrenotazione) {
        this.idPrenotazione = idPrenotazione;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        setModal(true);
        setSize(450, 300);
        setMinimumSize(new Dimension(450, 300));
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(accettaButton);

        infoPane.setText(infoPane.getText().replace("placeholder", "Seleziona come vuoi gestire la prenotazione " + idPrenotazione));

        accettaButton.addActionListener(actionEvent -> gestisciPrenotazione(true));
        rifiutaButton.addActionListener(actionEvent -> gestisciPrenotazione(false));
    }

    private void gestisciPrenotazione(boolean accettata) {
        AbstractMap.SimpleEntry<Boolean, String> result = ControllerUtente.getInstance().gestisciPrenotazione(idPrenotazione, accettata);
        if (Boolean.FALSE.equals(result.getKey())) {
            JOptionPane.showMessageDialog(contentPane, result.getValue(), "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(contentPane, result.getValue(), "Info", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}

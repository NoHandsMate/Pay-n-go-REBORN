package boundary;

import control.ControllerUtente;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.AbstractMap;

public class ValutaUtente extends JDialog {
    private long idPrenotazione;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea descrizioneTextArea;
    private JPanel oneStarPanel;
    private JPanel twoStarPanel;
    private JPanel threeStarPanel;
    private JPanel fourStarPanel;
    private JPanel fiveStarPanel;
    private JLabel charCountLabel;
    private JLabel valutaUtenteLabel;
    private int numeroStelle;

    private static final String STARVOID = "resources/starVoid.png";
    private static final String STARFULL = "resources/starFull.png";
    private static final String STARHOVER = "resources/starHover.png";


    private JPanel[] starPanels = {oneStarPanel, twoStarPanel, threeStarPanel, fourStarPanel, fiveStarPanel};

    public ValutaUtente(long idPrenotazione, String nomeAutista) {
        this.idPrenotazione = idPrenotazione;
        this.numeroStelle = 0;
        setContentPane(contentPane);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        valutaUtenteLabel.setText("Valuta il tuo viaggio con " + nomeAutista);

        oneStarPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                starClickHandle(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                starHoverHandle(0);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                starLeaveHandle();
            }

        });

        oneStarPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                starClickHandle(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                starHoverHandle(0);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                starLeaveHandle();
            }

        });

        twoStarPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                starClickHandle(1);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                starHoverHandle(1);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                starLeaveHandle();
            }

        });

        threeStarPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                starClickHandle(2);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                starHoverHandle(2);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                starLeaveHandle();
            }

        });
        fourStarPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                starClickHandle(3);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                starHoverHandle(3);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                starLeaveHandle();
            }

        });

        fiveStarPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                starClickHandle(4);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                starHoverHandle(4);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                starLeaveHandle();
            }

        });

        descrizioneTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCharCountLabel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCharCountLabel();
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
                updateCharCountLabel();
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        AbstractMap.SimpleEntry<Boolean, String> result = validateValutazione();
        if (Boolean.FALSE.equals(result.getKey()))
            JOptionPane.showMessageDialog(contentPane, result.getValue(), "Errore", JOptionPane.ERROR_MESSAGE);
        else {
            result = ControllerUtente.getInstance().valutaUtente(idPrenotazione, numeroStelle, descrizioneTextArea.getText());

            String title = !result.getKey() ? "Error" : "Info";
            int messageType = !result.getKey() ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;

            JOptionPane.showMessageDialog(contentPane, result.getValue(), title, messageType);
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void createUIComponents() {
        oneStarPanel = new ImagePanel(STARVOID, 32, 32);
        twoStarPanel = new ImagePanel(STARVOID, 32, 32);
        threeStarPanel = new ImagePanel(STARVOID, 32, 32);
        fourStarPanel = new ImagePanel(STARVOID, 32, 32);
        fiveStarPanel = new ImagePanel(STARVOID, 32, 32);
    }

    private void starClickHandle(int index) {
        numeroStelle = index + 1;
        for (int i = 0; i < starPanels.length; i++) {
            if (i > index)
                ImagePanel.changeImage(starPanels[i], STARVOID, 32, 32);
            else
                ImagePanel.changeImage(starPanels[i], STARFULL, 32, 32);
        }
    }

    private void starHoverHandle(int index) {
        if (numeroStelle == 0)
        {
            for (int i = 0; i < starPanels.length; i++) {
                if (i > index)
                    ImagePanel.changeImage(starPanels[i], STARVOID, 32, 32);
                else
                    ImagePanel.changeImage(starPanels[i], STARHOVER, 32, 32);
            }
        }
    }

    private void starLeaveHandle() {
        for (int i = 0; i < starPanels.length && numeroStelle == 0; i++) {
            ImagePanel.changeImage(starPanels[i], STARVOID, 32, 32);
        }
    }

    private void updateCharCountLabel() {
        int charCount = descrizioneTextArea.getText().length();
        charCountLabel.setText(charCount + " / 1000");
        if (charCount > 1000)
            charCountLabel.setForeground(Color.RED);
        else
            charCountLabel.setForeground(Color.WHITE);
    }

    private AbstractMap.SimpleEntry<Boolean, String> validateValutazione() {
        if (numeroStelle == 0) {
            return new AbstractMap.SimpleEntry<>(false, "Seleziona almeno una stella.");
        }
        else if (numeroStelle > 5 || numeroStelle < 0) {
            return new AbstractMap.SimpleEntry<>(false, "Numero di stelle invalido.");
        }
        else if (descrizioneTextArea.getText().length() > 1000) {
            return new AbstractMap.SimpleEntry<>(false, "La descrizione supera il limite massimo di 1000 caratteri.");
        }
        return  new AbstractMap.SimpleEntry<>(true, "OK");
    }
}

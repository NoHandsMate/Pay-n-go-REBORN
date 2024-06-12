package boundary;

import javax.swing.*;
import java.awt.*;

public class FormGestore extends JFrame {
    private JPanel gestorePanel;

    public FormGestore() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pay-n-go REBORN - Amministrazione");
        setSize(1000, 800);
        setMinimumSize(new Dimension(1000, 800));
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().add(new JScrollPane(gestorePanel), BorderLayout.CENTER);
    }
}

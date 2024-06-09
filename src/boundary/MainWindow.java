package boundary;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private JPanel mainWindowPanel;
    private JLabel welcomeLabel;

    public MainWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pay-n-go REBORN");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().add(new JScrollPane(mainWindowPanel), BorderLayout.CENTER);
    }
}

package boundary;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class MainWindowCircularLogoPanel extends JPanel {

    private transient BufferedImage pngCircularLogoImage;

    public MainWindowCircularLogoPanel() {
        try {
            pngCircularLogoImage = ImageIO.read(new File("resources/payngo-circlogo.png"));
        } catch (IOException e) {
            Logger logger = Logger.getLogger("loggerMainWindowCircularLogoPanel");
            logger.warning(String.format("Non è stato possibile caricare il logo.%n%s", e.getMessage()));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int imgWidth = 42;
        int imgHeight = 42;
        g.drawImage(pngCircularLogoImage, 3, 3, imgWidth, imgHeight, this);
    }

}

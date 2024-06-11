package boundary;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class MainWindowCircularLogoPanel extends JPanel {

    private BufferedImage pngCircularLogoImage;
    private Logger logger = Logger.getLogger("loggerMainWindowCircularLogoPanel");

    public MainWindowCircularLogoPanel() {
        try {
            pngCircularLogoImage = ImageIO.read(new File("resources/payngo-circlogo.png"));
        } catch (IOException e) {
            logger.info(String.format("Non è stato possibile caricare il logo.%n%s", e.getMessage()));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int imgWidth = 48;
        int imgHeight = 48;
        g.drawImage(pngCircularLogoImage, 0, 0, imgWidth, imgHeight, this);
    }

}

package boundary;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class HomePageLogoPanel extends JPanel {

    private BufferedImage pngLogoImage;
    private Logger logger = Logger.getLogger("loggerHomePageLogoPanel");

    public HomePageLogoPanel() {
        try {
            pngLogoImage = ImageIO.read(new File("resources/payngo-reborn-logo.png"));
        } catch (IOException e) {
            logger.info(String.format("Non è stato possibile caricare il logo.%n%s", e.getMessage()));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(pngLogoImage, 0, 0, this); // see javadoc for more info on the parameters
    }

}

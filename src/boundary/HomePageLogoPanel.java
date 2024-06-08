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
        int imgWidth = 2437;
        int imgHeight = 1112;
        float aspectRatio = Math.min(((float) this.getWidth() / (float) imgWidth),
                ((float) this.getHeight() / (float) imgHeight));
        int newWidth = (int) (imgWidth * aspectRatio);
        int newHeight = (int) (imgHeight * aspectRatio);
        int newX = (this.getWidth() - newWidth) / 2;
        int newY = (this.getHeight() - newHeight) / 2;
        g.drawImage(pngLogoImage, newX, newY, newWidth, newHeight, this);
    }

}

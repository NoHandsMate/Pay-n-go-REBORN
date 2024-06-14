package boundary;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ImagePanel extends JPanel {

    private transient BufferedImage pngLogoImage;
    private int imageWidth;
    private int imageHeight;

    public ImagePanel(String imagePath, int imageWidth, int imageHeight) {
        try {
            pngLogoImage = ImageIO.read(new File(imagePath));
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
        } catch (IOException e) {
            Logger.getLogger("imagePanelLogger").warning(
                    String.format("Non è stato possibile caricare il logo.%n%s", e.getMessage()));
        }
    }

    public static void changeImage(JPanel jPanel,
                                   String imagePath,
                                   int imageWidth,
                                   int imageHeight) {
        try {
            ImagePanel imagePanel = (ImagePanel) jPanel;
            imagePanel.pngLogoImage = ImageIO.read(new File(imagePath));
            imagePanel.imageWidth = imageWidth;
            imagePanel.imageHeight = imageHeight;
            imagePanel.repaint();
        } catch (IOException e) {
            Logger.getLogger("imagePanelLogger").warning(
                    String.format("Non è stato possibile caricare il logo.%n%s", e.getMessage()));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        float aspectRatio = Math.min(((float) this.getWidth() / (float) imageWidth),
                ((float) this.getHeight() / (float) imageHeight));
        int newWidth = (int) (imageWidth * aspectRatio);
        int newHeight = (int) (imageHeight * aspectRatio);
        int newX = (this.getWidth() - newWidth) / 2;
        int newY = (this.getHeight() - newHeight) / 2;
        g.drawImage(pngLogoImage, newX, newY, newWidth, newHeight, this);
    }

}

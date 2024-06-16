package boundary;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Estensione di JPanel che consente la visualizzazione di un'immagine.
 */
public class ImagePanel extends JPanel {

    /**
     * L'immagine da visualizzare.
     */
    private transient BufferedImage pngLogoImage;

    /**
     * La larghezza in pixel dell'immagine.
     */
    private int imageWidth;

    /**
     * L'altezza in pixel dell'immagine.
     */
    private int imageHeight;

    /**
     * ImagePanel è una estensione di JPanel che consente di visualizzare staticamente a schermo un'immagine situata in
     * una source directory del progetto attuale.
     * @param imagePath il percorso dell'immagine da visualizzare.
     * @param imageWidth la larghezza originale, in pixel, dell'immagine da visualizzare.
     * @param imageHeight l'altezza originale, in pixel, dell'immagine da visualizzare.
     */
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

    /**
     * Funzione statica che permette il cambiamento dinamico dell'immagine da visualizzare. Utilizzabile nel caso si
     * sia creato un riferimento a JPanel al quale è stato assegnato un JPanel, e si vuole cambiare l'immagine
     * mostrata da tale JPanel.
     * @param jPanel l'ImagePanel di cui si vuole cambiare l'immagine visualizzata. Non è possibile fornire un JPanel
     *               generico.
     * @param imagePath il percorso dell'immagine da visualizzare.
     * @param imageWidth la larghezza originale, in pixel, dell'immagine da visualizzare.
     * @param imageHeight l'altezza originale, in pixel, dell'immagine da visualizzare.
     */
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

    /**
     * Override della funzione <code>paintComponent</code> di JPanel, adattata per la visualizzazione dell'immagine
     * desiderata.
     * @param g the <code>Graphics</code> object to protect
     */
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

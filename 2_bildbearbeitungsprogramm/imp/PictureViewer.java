package imp;

/**
 * Zeigt ein Bild in einem Scrollbereich an. 
 * Es ist möglich das Bild zu zoomen und mehrere Versionen des Bildes zu speichern, um eine "Rückgängig" Operation durchzuführen.
 * @author Thomas Schaller
 * @version 1.0 vom 01.02.2019
 */

import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;
import java.util.Vector;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class PictureViewer extends ScrollPane
{
   
  // das aktuelle Bild
  private Picture picture;
    
  // Bilder für den Züruck-Modus speichern
  private static final int ANZ_BACK = 20;  
  private Vector<BufferedImage> history;
  
  // Zeichenfläche
  private Image scrollImageIcon;
  private ImageView imageLabel;
  
  // Zoom Faktor
  private double zoomFactor;
  public static final int FIT = -1;
  public static final int NORMAL = 1;
  
  
  
  /**
   * Erzeugt ein ScrollPanel mit integriertem Bild der Größe 1000x1000
   */
  public PictureViewer() {
    this(1000,1000);
  }

  /**
   * Erzeugt ein ScrollPanel mit integriertem Bild der angegebenen Größe 
   * @param width Breite des Bildes
   * @param height Höhe des Bildes
   */
  public PictureViewer(int width, int height) {
    this(width,height, "D0D0D0");
  }
  
  /**
   * Erzeugt ein ScrollPanel mit integriertem Bild der angegebenen Größe 
   * @param width Breite des Bildes
   * @param height Höhe des Bildes
   * @param background Farbe des Hintergrunds als HEX-String (z.B. "FF3A45")
   */
  public PictureViewer(int width, int height, String background) {
    this(new Picture(width,height, background));
  }
 
  /**
   * Erzeugt ein ScrollPanel mit integriertem Bild aus einer Bilddatei
   * @param filename Name des Bildes
   */
  public PictureViewer(String filename) {
    this(new Picture(filename));
  }
  
  /**
   * Erzeugt ein ScrollPanel und zeigt das Bild-Objekt an
   * @param picture anzuzeigendes Bild
   */
  public PictureViewer(Picture picture)
  {
    this.picture=picture;
    
    zoomFactor=1;
    
    scrollImageIcon = SwingFXUtils.toFXImage(picture.getImage(), null);
    //new Image(picture.getImage().getScaledInstance(picture.getImage().getWidth(), picture.getImage().getHeight(), Image.SCALE_FAST));
    imageLabel = new ImageView(scrollImageIcon);
    imageLabel.setPreserveRatio(true);
//    imageLabel.setVerticalAlignment(JLabel.CENTER);
//    imageLabel.setHorizontalAlignment(JLabel.CENTER);
    setContent(imageLabel);
    
//    this.setBorder(BorderFactory.createLineBorder(Color.black));
    picture.setObserver(this);
    history = new Vector<BufferedImage>();
    
  }
  
  
  /**
   * Setzt das anzuzeigende Bild neu
   * @param picture anzuzeigendes Bild
   * @param saveOldImage soll das aktuelle Bild in der Historie gespeichert werden
   */
  public void setImage(Picture picture, boolean saveOldImage) {
      if (saveOldImage) pushImage();
      this.picture = picture;
      repaint();
  }  
  
  /**
   * Liefert das angezeigte Bild
   * @return angezeigtes Bild
   */
  public Picture getImage() {
      return this.picture;
  }
  

  
  
  /** 
   * Erzeugt eine Kopie eines BufferedImage.
   * @param Originalbild
   * @return Kopie des Bildes
   */
  private BufferedImage deepCopy(BufferedImage bi) {
    ColorModel cm = bi.getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    WritableRaster raster = bi.copyData(null);
    return new BufferedImage(cm, raster, isAlphaPremultiplied, null).getSubimage(0, 0, bi.getWidth(), bi.getHeight());
  }
  
  
  /**
   * Speichert das übergebene Bild in der History.
   * @param b zu speicherndes Bild
   */
  public void pushImage() {
      if( this.ANZ_BACK > 0) {
        if(history.size() == this.ANZ_BACK) {
            history.removeElementAt(0);
        }
        
        history.add(deepCopy(picture.getImage()));
      }   
  }   
  
  /**
   * Ruft das letzte abgespeicherte Bild aus der History wieder auf.
   */
  public void back() {
    int anz = history.size();
    if(anz>0) {
      BufferedImage img = history.get(anz-1);
      history.removeElementAt(anz-1);
      picture.setImage(img);
      repaint();
    }
  }


  
  /**
   * Setzt das angezeigt Bild neu und beachtet dabei den Zoomfaktor.
   */
  public void repaint() {
    if( picture != null) {
      double factor= zoomFactor;
      if (zoomFactor == FIT) {
        double factorw = ((double) getWidth()-2) / picture.getWidth();
        double factorh = ((double) getHeight()-2) / picture.getHeight();
        factor = Math.min(factorw, factorh);
      }
      int width = (int) (picture.getWidth()*factor);
      int height = (int) (picture.getHeight()*factor);
      
      scrollImageIcon = SwingFXUtils.toFXImage(picture.getImage(), null);
      imageLabel.setFitWidth(width);
      imageLabel.setFitHeight(height);
      imageLabel.setImage(scrollImageIcon);
      
    //new Image(picture.getImage().getScaledInstance(picture.getImage().getWidth(), picture.getImage().getHeight(), Image.SCALE_FAST));
//    imageLabel = new ImageView(scrollImageIcon);
      
   //   scrollImageIcon.setImage(picture.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
   //   revalidate();
    }
  }

  /**
   * Setzt den Zoom-Faktor für das Bild.
   * Als Zoomfaktor sind auch die Konstanten Bildanzeiger.FIT (auf Bildschirmgröße zoomen) und Bildanzeiger.NORMAL (100%) möglich.
   * @param factor Zoomfaktor (1.0 = 100%). 
   */
  public void setZoom(double factor)
  {
    zoomFactor = factor;
    repaint();
  }
  
  

  
  
  
}

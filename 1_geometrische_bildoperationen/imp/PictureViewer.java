package imp;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Vector;
import javax.imageio.*;
import java.io.*;

/**
 * Der PictureViewer ist ein JFrame, der in der Lage ist ein
 * Objekt der Klasse Picture anzuzeigen. Zusätzlich können 
 * mehrere Verarbeitungsschritte gespeichert werden, um ein
 * "zurück"-Funktion zu ermöglichen.
 * 
 * @author Thomas Schaller
 * @version V1.2 vom 06.12.2019
 */
public class PictureViewer extends JFrame implements MouseWheelListener
{
    public static final int FIT = -1;
    public static final int NORMAL = 1;

    private static final int ANZ_BACK = 0;

    private double zoom;
    protected Picture  picture = null;
    private Vector<BufferedImage> history;
    private JLabel imagePane = new JLabel();
    private JScrollPane scrollPane;
    // private boolean antialiasing;

    /**
     * Erzeugt ein ScrollPanel der Größe 500x400
     */
    public PictureViewer() {
        this(800,500);
    }

    /**
     * Erzeugt ein ScrollPanel der angegebenen Größe 
     * @param width Breite des Bildes
     * @param height Höhe des Bildes
     */
    public PictureViewer(int width, int height) {
        this(new Picture(width, height));
    }

    public PictureViewer(Picture p) {
        picture = p;
        picture.setObserver(this);
        this.history = new Vector<BufferedImage>();
        this.zoom = NORMAL;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        imagePane.setPreferredSize(new Dimension(p.getWidth(), p.getHeight()));
        scrollPane = new JScrollPane( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        scrollPane.setViewportView( imagePane );

        cp.add(scrollPane, BorderLayout.CENTER);
        pack();
        this.setVisible(true);

        repaint();

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - getSize().width);
        //int x = 0;
        int y = 0;
        setLocation(x, y);

        scrollPane.addMouseWheelListener(this);
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

            BufferedImage b = new BufferedImage(picture.getWidth(), picture.getHeight(), picture.getImage().getType());
            Graphics g = b.getGraphics();
            g.drawImage(picture.getImage(), 0, 0, null);
            g.dispose();

            history.add(b);
        }   
    }

    /**
     * Ruft das letzte abgespeicherte Bild aus der History wieder auf.
     */
    private void popImage() {
        int anz = history.size();
        if(anz>0) {
            BufferedImage i = history.get(anz-1);
            history.removeElementAt(anz-1);
            picture.setImage(i);
            repaint();
        }
    }

    /**
     * Ruft das letzte abgespeicherte Bild aus der History wieder auf.
     */
    public void back() {
        popImage();
    }

    /**
     * Setzt den Zoom-Faktor für das Bild.
     * Als Zoomfaktor sind auch die Konstanten Bildanzeiger.FIT (auf Bildschirmgröße zoomen) und Bildanzeiger.NORMAL (100%) möglich.
     * @param factor Zoomfaktor (1.0 = 100%). 
     */
    public void setZoom(double zoom) {
        if(zoom>0.01 && zoom<10.0) {
            this.zoom = zoom;
        }
        // else this.zoom = NORMAL;
        resize();
        repaint();
    }

    // public void setAntialiasing(boolean antialiasing) {
    // this.antialiasing = antialiasing;
    // }

    /**
     * Setzt die automatische Neuanzeige des Bildes.
     * Mit dieser Methode kann man einstellen, ob nach jedem Zeichenbefehl
     * die Anzeige auf dem Bildschirm aktualisiert werden soll. Bei sehr 
     * vielen Zeichenbefehlen wird die Ausgabe dadurch sehr langsam. Es reicht
     * eine Anzeige am Ende der Zeichenbefehle. Rufen Sie dann für das Neuzeichnen
     * die Methode refresh() auf.
     * @param autoRefresh true, wenn nach jedem Zeichenbefehl die Anzeige aktualisiert werden soll.
     */
    public void setAutoRefresh(boolean autoRefresh) {
        picture.setAutoRefresh(autoRefresh);
    }

    /**
     * Sorgt für die Aktualisierung der Bildschrimanzeige. Das aktuelle Bild
     * wird dadurch angezeigt. Durch Einstellung von autoRefresh kann die 
     * Anzeige automatisiert werden.
     */
    public void refresh() {
        repaint();
    }

    /**
     * Passt die Framegröße an das anzuzeigende Bild an.
     */
    public void resize() {
        int pref_x = (int) (picture.getWidth()*zoom);
        int pref_y = (int) (picture.getHeight()*zoom);
        imagePane.setPreferredSize(new Dimension(pref_x,pref_y));

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        if((pref_x > d.getWidth()-30) || (pref_y > d.getHeight()-100)) {
            pref_x = Math.min(pref_x, (int) d.getWidth()-30);
            pref_y = Math.min(pref_y, (int) d.getHeight()-100);
            scrollPane.setPreferredSize(new Dimension(pref_x,pref_y));
        } else scrollPane.setPreferredSize(null);

        imagePane.revalidate();  
        pack();
    }

    /**
     * Setzt das angezeigt Bild neu und beachtet dabei den Zoomfaktor.
     */

    public void repaint() {
        double faktor = zoom;

        if (zoom == FIT) {
            double faktorw = (double) imagePane.getWidth() / picture.getWidth();
            double faktorh = (double) imagePane.getHeight() / picture.getHeight();
            faktor = Math.min(faktorw, faktorh);
        }
        int disp_width = (int) (picture.getWidth()*faktor);
        int disp_height = (int) (picture.getHeight()*faktor);

        BufferedImage image = new BufferedImage(disp_width, disp_height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();

        if(picture.isAntialiasing()) {
            picture.antialise();
        }
        else g.getRenderingHints().clear();

        g.drawImage(picture.getImage(),0,0,disp_width, disp_height, 0, 0, picture.getWidth(), picture.getHeight(), null);
        g.setColor(new java.awt.Color(0,0,0));
        g.setStroke(new BasicStroke((float) 1));
        g.drawRect(0,0,disp_width-1, disp_height-1); 

        imagePane.setIcon(new ImageIcon(image));
        imagePane.repaint();

    }

    /** Setzt ZoomFaktor über MouseWheel
     * 
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if ( notches < 0) {
            setZoom(zoom * 1.1);
        } else {
            setZoom(zoom * 1.0/1.1); 
        }
    }
}

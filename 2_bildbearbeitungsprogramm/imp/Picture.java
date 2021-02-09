package imp;

import java.awt.image.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.util.Vector;
import javax.imageio.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.awt.geom.AffineTransform;

/**
 *
 * Bildklasse fuer die Simulation von Processing-Befehlen
 * 
 * Diese Klasse stellt ein BufferedImage bereit, in das mit Processing-Befehlen gezeichnet
 * werden kann. 
 * Zusaetzlich kann ein Bildanzeiger ueber jede Aenderung des Bildes informiert werden,
 * um "Zurueck"-Befehle zu ermoeglichen. Der Bildanzeiger ist entweder eine normale Java
 * ScrollPane oder ein Actor aus Greenfoot.
 * Die Dokumentation der einzelnen Zeichenmethoden ist der Processing-Reference 
 * (https://processing.org/reference/ steht unter CC-Lizenz: https://creativecommons.org/) 
 * entnommen und mit Deepl.com ins Deutsche uebersetzt.
 *
 * @version 1.2 from 06.12.2019
 * @author Thomas Schaller (ZPG Informatik Klasse 9)
 */

public class Picture{

    // Einstellungmoeglichkeiten fuer das Zeichnen von Rechtecken und Ellipsen 
    // RADIUS = Mittelpunkt+Radius wird gegeben, CENTER = Mittelpunkt und Breite/Hoehe wird gegeben, 
    // CORNER = Linke obere Ecke + Breite/Hoehe,  CORNERS = Linke obere und rechte untere Ecke
    public static final int RADIUS = 1;
    public static final int CENTER = 2;
    public static final int CORNER = 3;
    public static final int CORNERS = 4;

    // gespeichertes Bild, 
    private BufferedImage image;
    private Graphics2D g;
    private boolean antialiasing;

    // aktuelle Farbeinstellungen
    private Color background;
    private Color pencolor;
    private Color fillcolor;

    // aktuelle Stiftdicke
    private double stroke;

    // aktueller Koordinatenmodus von Rechtecken und Ellipsen
    private int ellipseMode = CENTER;
    private int rectMode = CORNER;

    // aktueller Font
    private Font textfont = null;

    // muss ein Bildanzeiger benachrichtigt werden
    private PictureViewer observer = null;
    private boolean autorefresh = true;

    /** 
     * Erzeugt ein Bild mit Standardgroesse 500x400
     */
    public Picture() {
        this(500,400);
    }

    /**
     * Erzeugt ein Bild der angegeben Groesse
     * @param width Breite des Bildes
     * @param height Hoehe des Bildes
     */
    public Picture(int width, int height) {
        this(width,height, "D0D0D0");
    }

    /** 
     * Erzeugt ein Bild aus einer Datei
     * @param filename Dateiname des Bildes
     */
    public Picture(String filename) {
        this.antialiasing = true;
        load(filename);
        
    }

    /**
     * Erzeugt ein Bild der angegebenen Groesse mit festgelegtem Hintergrund
     * @param width Breite des Bildes
     * @param height Hoehe des Bildes
     * @param background Farbe des Hintergrunds
     */
    public Picture(int width, int height, String background) {
        this.antialiasing = true;
        this.background = decode(background);
        this.pencolor = new Color(0,0,0);
        this.stroke = 1;
        this.fillcolor = null;
        makeImage(width, height);
       
    }

    public void showInFrame() {
        PictureViewer v = new PictureViewer(this);
    }

    private void makeImage(int width, int height){
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) this.image.getGraphics();
        g.setColor(this.background);
        g.fillRect(0,0,width-1, height-1);
    }

    protected void antialise() {
        // Antialiasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Rendering
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        // Text
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // Color
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        // Sonstiges
        // g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        // g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        // g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        // g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);       
        // g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
    }

    public void setAntialising(boolean neuerWert) {
        this.antialiasing = neuerWert;
    }

    public boolean isAntialiasing() {
        return antialiasing;
    }

    /** 
     * Legt fest, wer das Bild anzeigt.
     * Diese ermoeglicht die Benachrichtung des Observers, wenn sich das Bild aendert.
     * @param observer Anzeiger des Bildes
     */
    public void setObserver(PictureViewer observer) {
        this.observer= observer;
    }

    public PictureViewer getObserver() {
        return observer;
    }

    /** 
     * Direktes Setzen des Bildes (fuer interne Zwecke)
     * @param b Bild, das gespeichert werden soll.
     */
    public void setImage(BufferedImage b) {
        image = b;
    }

    /** 
     * Direktes Abfragen des Bildes (fuer interne Zwecke)
     * @return Bild, das gerade gespeichert ist.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Definiert die Dimension der Breite und Hoehe des Anzeigefensters in Pixeleinheiten. 
     * Die eingebauten Variablen Breite und Hoehe werden durch die an diese Funktion uebergebenen Parameter festgelegt. So weist beispielsweise 
     * der Befehl size(640, 480) der Variablen Breite 640 und der Variablen Hoehe 480 zu. 
     * @param width Breite des Bildes
     * @param height Hoehe des Bildes
     */
    public void size(int width, int height){
        pushImage();
        makeImage(width, height);

        g.setColor(background);
        g.fillRect(0,0,width-1, height-1);
        
        repaint();
    }

    /** 
     * Liefert die Breite des Bildes zurueck.
     * @return Breite des Bildes
     */
    public int getWidth() {
        return image.getWidth();
    }

    /** 
     * Liefert die Hoehe des Bildes zurueck.
     * @return Hoehe des Bildes
     */
    public int getHeight() {
        return image.getHeight();
    }

    /**
     * Erzeugt eine Kopie des Bildes und uebergibt sie an den Observer (falls existent), damit dieser die Versionen speichern kann
     */
    private void pushImage() {
        if(observer != null) {
            observer.pushImage();
        }
    }   


    /**
     * Legt fest, ob nach jedem Zeichenbefehl automatisch das Bild auch in
     * der Oberflaeche aktualisiert wird. Die Einstellung "false" beschleunigt
     * das Zeichnen aufwaendiger Bilder und verhindert "Flackern".
     * Das Neuzeichnen kann durch die Methode "refresh" gezielt ausgeloest werden.
     * @param autorefresh true = nach jedem Zeichenbefehl die Anzeige aktualisieren, false= nur durch die Methode refresh neu zeichnen
     */
    public void setAutoRefresh(boolean autoRefresh) {
        this.autorefresh = autoRefresh;
    }

    /**
     * Auch die anzeigenden Klasse wird zum Neuzeichnen aufgefordert.
     */
    private void repaint() {
        if(observer != null && autorefresh) {
            observer.repaint();
        }
    }

    /**
     * Ein repaint() (das Neuzeichnen) kann manuell erzwungen werden.
     */
    public void forceRepaint() {
        if(observer != null) {
            observer.repaint();
        }
    }

    // ----------------------------------------- Zeichenfunktionen -----------------------------------------------
    /** 
     * Loescht den Inhalt des Bildes.
     * Der Hintergrund wird mit der Hintergrundfarbe neu gefuellt.
     */

    public void clear(){
        pushImage();
        makeImage(image.getWidth(), image.getHeight());

        g.setColor(background);
        g.fillRect(0,0,image.getWidth()-1, image.getHeight()-1);
        repaint();
    }

    /** 
     * Konvertiert die in einem bestimmten Modus gegebenen Koordinaten in die Java-uebliche Links_Oben_Breite_Hoehe Version
     * Die Aenderungen werden direkt im Array vorgenommen
     * @param coord Array mit vier Koordinateneintraegen im gegebenen Modus
     * @param mode  Modus der Koordinaten (CORNER, CORNERS, RADIUS oder CENTER)
     */
    private void convert(int[] coord, int mode) {
        switch(mode) {
            case CORNER: break;
            case CORNERS: coord[2] -= coord[0]; coord[3] -= coord[1]; break;
            case RADIUS: coord[2] *= 2; coord[3] *=2;
            case CENTER: coord[0] -= coord[2]/2; coord[1] -= coord[3]/2;
        }
    }

    /**
     * Aendert den Koordinaten-Modus beim Zeichnen von Rechtecken.
     * Aendert die Position, von der aus Rechtecke gezeichnet werden, indem es die Art und Weise aendert, wie Parameter, die an rect() uebergeben werden, interpretiert werden.
     * Der Standardmodus ist rectMode(Bild.CORNER), der die ersten beiden Parameter von rect() als die linke obere Ecke der Form interpretiert, 
     * waehrend der dritte und vierte Parameter seine Breite und Hoehe sind.
     * rectMode(Bild.CORNERS) interpretiert die ersten beiden Parameter von rect() als die Position einer Ecke 
     * und die dritten und vierten Parameter als die Position der gegenueberliegenden Ecke.
     * rectMode(Bild.CENTER) interpretiert die ersten beiden Parameter von rect() als Mittelpunkt der Form, 
     * waehrend der dritte und vierte Parameter seine Breite und Hoehe sind.
     * rectMode(RADIUS) verwendet auch die ersten beiden Parameter von rect() als Mittelpunkt der Form, 
     * verwendet aber den dritten und vierten Parameter, um die Haelfte der Breite und Hoehe der Formen festzulegen.
     * @param mode Modus der Koordinateninterpretation (CORNER, CORNERS, CENTER oder RADIUS)
     */
    public void rectMode(int mode) {
        rectMode = mode;
    }

    /**
     * Aendert den Koordinaten-Modus beim Zeichnen von Kreisen/Ellipsen.
     * Aendert die Position, von der aus Kreise/Ellipsen gezeichnet werden, indem es die Art und Weise aendert, wie Parameter, die an ellipse() uebergeben werden, interpretiert werden.
     * Der Standardmodus ist ellipseMode(Bild.CENTER), der die ersten beiden Parameter von ellipse() als Mittelpunkt der Form interpretiert, 
     * waehrend der dritte und vierte Parameter seine Breite und Hoehe sind.
     * ellipseMode(Bild.CORNER) interpretiert die ersten beiden Parameter von ellipse() als die Position einer Ecke 
     * und die dritten und vierten Parameter als Breite und Hoehe der Form.
     * ellipseMode(Bild.CORNERS) interpretiert die ersten beiden Parameter von ellipse() als die Position einer Ecke 
     * und die dritten und vierten Parameter als die Position der gegenueberliegenden Ecke.
     * ellipseMode(RADIUS) verwendet auch die ersten beiden Parameter von ellipse() als Mittelpunkt der Form, 
     * verwendet aber den dritten und vierten Parameter, um die Haelfte der Breite und Hoehe der Formen festzulegen.
     * @param mode Modus der Koordinateninterpretation (CORNER, CORNERS, CENTER oder RADIUS)
     */
    public void ellipseMode(int mode) {
        ellipseMode = mode;
    }

    /**
     * Zeichnet eine Linie (einen direkten Weg zwischen zwei Punkten) auf den Bildschirm. 
     * Um eine Linie einzufaerben, verwenden Sie die {@link #stroke(int, int, int) stroke()} Funktion. Eine Zeile kann nicht gefuellt werden, daher hat die Funktion fill() keinen 
     * Einfluss auf die Farbe einer Zeile. Linien werden standardmaessig mit einer Breite von einem Pixel gezeichnet, dies kann jedoch mit der Funktion 
     * {@link #strokeWeight(double) strokeWeight()} geaendert werden.
     * @param x1 x-Koordinate des 1. Punktes
     * @param y1 y-Koordinate des 1. Punktes
     * @param x2 x-Koordinate des 2. Punktes
     * @param y2 y-Koordinate des 2. Punktes
     */
    public void line(int x1, int y1, int x2, int y2) {
        pushImage();  

        if (stroke > 0) {
            g.setColor(pencolor);
            g.setStroke(new BasicStroke((float) stroke));
            // if(antialiasing) antialise(); 
            g.drawLine(x1, y1, x2, y2);
        }
        repaint();
    }

    /**
     * Zeichnet ein Rechteck auf das Bild. 
     * Standardmaessig legen die ersten beiden Parameter die Position der linken oberen Ecke fest, der dritte die Breite und der vierte die Hoehe. 
     * Die Art und Weise, wie diese Parameter interpretiert werden, kann jedoch mit der Funktion {@link #rectMode(int) rectMode()} geaendert werden.
     * Durch den Befehl {@link #fill(int,int,int) fill()} /{@link #noFill() noFill()}  kann die Fuellfarbe des Rechtecks gewaehlt werden, durch {@link #stroke(int, int, int) stroke()}/{@link #noStroke() noStroke()}  die Rahmenfarbe.
     * @param a meist die x-Koordinate der linken oberen Ecke (kann durch rectMode() geaendert werden).
     * @param b meist die y-Koordinate der linken oberen Ecke (kann durch rectMode() geaendert werden).
     * @param c meist die Breite des Rechtecks (kann durch rectMode() geaendert werden).
     * @param d meist die Hoehe des Rechtecks (kann durch rectMode() geaendert werden).
     * 
     */
    public void rect(int a, int b, int c, int d) {
        pushImage();

        int[] coord = {a,b,c,d};
        convert(coord, rectMode);
        if(fillcolor != null) {
            g.setColor(fillcolor);
            g.fillRect(coord[0], coord[1], coord[2], coord[3]);
        }
        if(pencolor != null) {
            g.setColor(pencolor);
            g.setStroke(new BasicStroke((float) stroke));
            g.drawRect(coord[0], coord[1], coord[2], coord[3]);
        }  
        repaint();
    }

    /**
     * Zeichnet eine Ellipse/Kreis auf das Bild. 
     * Standardmaessig legen die ersten beiden Parameter die Position des Mittelpunkts fest, der dritte die Breite und der vierte die Hoehe. 
     * Die Art und Weise, wie diese Parameter interpretiert werden, kann jedoch mit der Funktion {@link #ellipseMode(int) ellipseMode()} geaendert werden.
     * Durch den Befehl {@link #fill(int,int,int) fill()} /{@link #noFill() noFill()} kann die Fuellfarbe des Rechtecks gewaehlt werden, durch {@link #stroke(int, int, int) stroke()}/{@link #noStroke() noStroke()}  die Rahmenfarbe.
     * @param a meist die x-Koordinate des Mittelpunkts (kann durch ellipseMode() geaendert werden).
     * @param b meist die y-Koordinate des Mittelpunkts (kann durch ellipseMode() geaendert werden).
     * @param c meist die Breite des Rechtecks (kann durch ellipseMode() geaendert werden).
     * @param d meist die Hoehe des Rechtecks (kann durch ellipseMode() geaendert werden).
     * 
     */
    public void ellipse(int a, int b, int c, int d) {
        pushImage();      

        int[] coord = {a,b,c,d};
        convert(coord, ellipseMode);
        if(fillcolor != null) {
            g.setColor(fillcolor);
            g.fillOval(coord[0], coord[1], coord[2], coord[3]);
        }
        if(pencolor != null) {
            g.setColor(pencolor);
            g.setStroke(new BasicStroke((float) stroke));
            // if(antialiasing) antialise();
            g.drawOval(coord[0], coord[1], coord[2], coord[3]);
        }  
        repaint();
    }

    /**
     * Zeichnet ein Dreieck auf das Bild. 
     * Ein Dreieck ist eine Ebene, die durch die Verbindung von drei Punkten entsteht. Die ersten beiden Argumente spezifizieren den 
     * ersten Punkt, die mittleren beiden Argumente spezifizieren den zweiten Punkt und die letzten beiden Argumente spezifizieren den dritten Punkt. 
     * Durch den Befehl {@link #fill(int,int,int) fill()} /{@link #noFill() noFill()} kann die Fuellfarbe des Rechtecks gewaehlt werden, durch {@link #stroke(int, int, int) stroke()}/{@link #noStroke() noStroke()}  die Rahmenfarbe.
     * @param x1 meist die x-Koordinate des 1. Punkts.
     * @param y1 meist die y-Koordinate des 1. Punkts.
     * @param x2 meist die x-Koordinate des 2. Punkts.
     * @param y2 meist die y-Koordinate des 2. Punkts.
     * @param x3 meist die x-Koordinate des 3. Punkts.
     * @param y3 meist die y-Koordinate des 3. Punkts.
     */
    public void triangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        int px[] = {x1, x2, x3};
        int py[] = {y1, y2, y3};
        polygon(px, py);
    }

    /**
     * Zeichnet ein Viereck auf das Bild. 
     * Ein Viereck ist ein vierseitiges Polygon. Es ist aehnlich wie ein Rechteck, aber die Winkel zwischen seinen Kanten 
     * sind nicht auf neunzig Grad beschraenkt. Das erste Paar von Parametern (x1,y1) setzt den ersten Scheitelpunkt und die nachfolgenden 
     * Paare sollten im Uhrzeigersinn oder gegen den Uhrzeigersinn um die definierte Form herum verlaufen. 
     * Durch den Befehl {@link #fill(int,int,int) fill()} /{@link #noFill() noFill()} kann die Fuellfarbe des Rechtecks gewaehlt werden, durch {@link #stroke(int, int, int) stroke()}/{@link #noStroke() noStroke()}  die Rahmenfarbe.
     * @param x1 meist die x-Koordinate des 1. Punkts.
     * @param y1 meist die y-Koordinate des 1. Punkts.
     * @param x2 meist die x-Koordinate des 2. Punkts.
     * @param y2 meist die y-Koordinate des 2. Punkts.
     * @param x3 meist die x-Koordinate des 3. Punkts.
     * @param y3 meist die y-Koordinate des 3. Punkts.
     * @param x4 meist die x-Koordinate des 3. Punkts.
     * @param y4 meist die y-Koordinate des 3. Punkts.
     */
    public void quad(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        int px[] = {x1, x2, x3, x4};
        int py[] = {y1, y2, y3, y4};
        polygon(px, py);
    }

    /**
     * Zeichnet ein Polygon auf das Bild.
     * Gleich lange Listen von x und y-Koordinaten bestimmen die Eckpunkte des Polygons.
     * Durch den Befehl {@link #fill(int,int,int) fill()} /{@link #noFill() noFill()} kann die Fuellfarbe des Rechtecks gewaehlt werden, durch {@link #stroke(int, int, int) stroke()}/{@link #noStroke() noStroke()} die Rahmenfarbe.
     * @param x Liste der x-Koordinaten der Punkte.
     * @param y Liste der y-Koordinaten der Punkte.
     */

    public void polygon(int[] x, int[] y) {
        pushImage();     

        if(fillcolor != null) {
            g.setColor(fillcolor);
            g.fillPolygon(x,y, y.length);
        }
        if(pencolor != null) {
            g.setColor(pencolor);
            g.setStroke(new BasicStroke((float) stroke));
            // if(antialiasing) antialise();
            g.drawPolygon(x, y, x.length);
        }  
        repaint();
    }

    /**
     * Zeichnet einen Punkt, d.h. einen Kreis in der Dimension eines Pixels. 
     * Der erste Parameter ist der x-Wert fuer den Punkt, der zweite Wert ist der y-Wert fuer den Punkt. 
     * @param x x-Koordinate des Punktes
     * @param y y-Koordinate des Punktes
     */
    public void point(int x, int y) {
        ellipse(x,y,1, 1);
    }

    // ----------------------------------------- Schriftdarstellung -----------------------------------------------

    /**
     * Gibt einen Text an den gegebenen Koordinaten aus
     * Zur Ausgabe des Textes wird der ausgewaehlte Font verwendet. Dieser muss vorher mit {@link #textFont(Font) textFont() } festgelegt.
     * @param s Text, der angezeigt werden soll
     * @param x x-Koordinate des Textanfangs
     * @param y y-Koordinate der Grundlinie des Textes.
     */
    public void text(String s, int x, int y) {
        pushImage();     

        if(pencolor != null) {
            if(fillcolor == null)
                g.setColor(Color.black);
            else 
                g.setColor(fillcolor);
            g.setStroke(new BasicStroke((float) stroke));
            g.setFont(textfont);
            // if(antialiasing)g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.drawString(s, x, y);
        }  
        repaint();
    }

    /**
     * Legt die Schriftart fuer Textausgaben fest.
     * Jeder uebliche Java-Font kann verwendet werden. Er kann mit z.B. Font f = new Font( "Arial", Font.PLAIN, 14 ); definiert werden. 
     * @param font ein Font-Objekt 
     */
    public void textFont(Font font) {
        this.textfont = font;
    }

    // ----------------------------------------- Farbfestlegungen -----------------------------------------------
    /** 
     * Hilfsfunktion zur Interpretation von Farben
     */
    private Color decode(String color) {
        try{
            return new Color(
                Integer.valueOf( color.substring( 0, 2 ), 16 ),
                Integer.valueOf( color.substring( 2, 4 ), 16 ),
                Integer.valueOf( color.substring( 4, 6 ), 16 ) );
        } catch (Exception e) {
            System.out.println("Falscher Farbcode");
            return Color.BLACK;
        }
    }

    /** 
     * Hilfsfunktion zur Interpretation von Farben
     */
    private Color decode(int color) {
        try{
            if(color >=0 && color < 256) {
                return new Color(color,color,color);
            } else {
                int r = color / 0x010000 % 0xFF;
                int g = color / 0x000100 % 0xFF;
                int b = color % 0xFF;
                // System.out.println(""+r+","+g+","+b);
                return new Color(r, g, b );
            }
        } catch (Exception e) {
            System.out.println("Falscher Farbcode");
            return Color.BLACK;
        }
    }

    /**
     * Legt die Farbe fest, mit der Linien und Raender um Formen gezeichnet werden. 
     * Diese Farbe wird hexadezimal in Form der RGB angegeben: z.B.  "CCFFAA" oder "004E23". Die Syntax verwendet sechs Ziffern - je zwei fuer die roten, gruenen und blauen Komponenten,
     * um eine Farbe anzugeben (genau wie Farben typischerweise in HTML und CSS angegeben werden). 
     * @param pencolor Stiftfarbe in Hexadezimaldarstellung
     */
    public void stroke(String pencolor) {
        this.pencolor = decode(pencolor);
    }

    /**
     * Legt die Farbe fest, mit der Linien und Raender um Formen gezeichnet werden. 
     * Diese Farbe wird entweder als Graustufe (0-255) oder als 3-Byte RGB-Wert angegeben
     * @param pencolor Stiftfarbe (0-255: Graustufe zwischen 0 schwarz und 255 weiss, sonst: c wird als 3-Byte RGB-Wert interpretiert)
     */
    public void stroke(int pencolor) {
        this.pencolor=decode(pencolor);
    }

    /**
     * Legt die Farbe fest, mit der Linien und Raender um Formen gezeichnet werden. 
     * Diese Farbe wird komponentenweise als RGB-Wert angegeben
     * @param r Rotanteil (0-255) der Stiftfarbe
     * @param g Gruenanteil (0-255) der Stiftfarbe
     * @param b Blauanteil (0-255) der Stiftfarbe
     */
    public void stroke(int r, int g, int b) {
        this.pencolor = new Color(r,g,b);
    }

    /**
     * Legt fest, dass keine Linien oder Raender um Formen gezeichnet werden soll. 
     */
    public void noStroke() {
        this.pencolor = null;
    }

    /**
     * Legt die Breite des Strichs fuer Linien, Punkte und den Rand um Formen fest. 
     * Alle Breiten werden in Pixeleinheiten angegeben. 
     * @param width Breite in Pixel
     */
    public void strokeWeight(double width) {
        this.stroke = width;
    }

    /**
     * Legt die Farbe fest, mit der Formen gefuellt werden. 
     * Diese Farbe wird hexadezimal in Form der RGB angegeben: z.B.  "CCFFAA" oder "004E23". Die Syntax verwendet sechs Ziffern - je zwei fuer die roten, gruenen und blauen Komponenten,
     * um eine Farbe anzugeben (genau wie Farben typischerweise in HTML und CSS angegeben werden). 
     * @param fillcolor Fuellfarbe in Hexadezimaldarstellung
     */
    public void fill(String fillcolor) {
        this.fillcolor = decode(fillcolor);
    }

    /**
     * Legt die Farbe fest, mit der Formen gefuellt werden.
     * Diese Farbe wird entweder als Graustufe (0-255) oder als 3-Byte RGB-Wert angegeben.
     * @param fillcolor Fuellfarbe (0-255: Graustufe zwischen 0 schwarz und 255 weiss, sonst: c wird als 3-Byte RGB-Wert interpretiert)
     */
    public void fill(int fillcolor) {
        this.fillcolor=decode(fillcolor);
    }

    /**
     * Legt die Farbe fest, mit der Formen gefuellt werden.
     * Diese Farbe wird komponentenweise als RGB-Wert angegeben.
     * @param r Rotanteil (0-255) der Fuellfarbe
     * @param g Gruenanteil (0-255) der Fuellfarbe
     * @param b Blauanteil (0-255) der Fuellfarbe
     */
    public void fill(int r, int g, int b) {
        this.fillcolor = new Color(r,g,b);
    }

    /** Legt fest, dass die Formen nicht gefuellt werden sollen.
     */
    public void noFill() {
        this.fillcolor = null;
    }

    /**
     * Die Funktion background() setzt die Farbe, die fuer den Hintergrund des Bildes verwendet wird. Der Standardhintergrund ist hellgrau. 
     * Es ist nicht moeglich, den Alpha-Parameter Transparenz mit Hintergrundfarben auf der Hauptzeichnungsoberflaeche zu verwenden. 
     * @param c Farbe fuer den Hintergrund (0-255: Graustufe zwischen 0 schwarz und 255 weiss, sonst: c wird als 3-Byte RGB-Wert interpretiert)
     */
    public void background(int c) {
        if(c < 256) {
            this.background=new Color(c,c,c);
        } else {
            int r = c / 0x010000;
            int g = c / 0x000100 % 0xFF;
            int b = c % 0xFF;
            this.background= new Color(r, g, b );
        }
        this.clear();
    }

    /**
     * Die Funktion background() setzt die Farbe, die fuer den Hintergrund des Bildes verwendet wird. Der Standardhintergrund ist hellgrau. 
     * Es ist nicht moeglich, den Alpha-Parameter Transparenz mit Hintergrundfarben auf der Hauptzeichnungsoberflaeche zu verwenden. 
     * @param r Rotanteil (0-255) der Hintergrundfarbe
     * @param g Gruenanteil (0-255) der Hintergrundfarbe
     * @param b Blauanteil (0-255) der Hintergrundfarbe
     */
    public void background(int r, int g, int b) {
        this.background=new Color(r,g,b);
        this.clear();
    }

    /**
     * Die Funktion background() setzt die Farbe, die fuer den Hintergrund des Bildes verwendet wird. Der Standardhintergrund ist hellgrau. 
     * Es ist nicht moeglich, den Alpha-Parameter Transparenz mit Hintergrundfarben auf der Hauptzeichnungsoberflaeche zu verwenden. 
     * @param hex String    Farbe in Hexadezimalangabe
     */
    public void background(String hex) {
        this.background = decode(hex);
        this.clear();
    }

    // ----------------------------------------- Dateioperationen -----------------------------------------------
    /** 
     * Laedt ein Bild aus dem Dateisystem.
     * Laedt ein Bild von einem Datentraeger und setzt Stiftfarbe und Fuellfarbe auf Standardwerte zurueck.
     * @param filename Dateiname des Bildes
     */
    public void load(String filename) {
        try{
            this.image = ImageIO.read(new File(filename));
            this.g = (Graphics2D) image.getGraphics();
            this.background = decode("D0D0D0");
            this.pencolor = new Color(0,0,0);
            this.fillcolor = null;
            this.stroke = 1;
           
            this.repaint();
        } catch(Exception e) {
            System.out.println("Fehler beim Einlesen der Bilddatei");
        }
    }

    /** 
     * Speichert ein Bild.
     * Speichert ein Bild auf einem Datentraeger. Zulaessig sind die Dateiformate PNG und GIF. Die Dateiendung legt den Typ fest.
     * Standardmaessig wird die Dateiendung .png ergaenzt, wenn keine angegeben ist.
     * @param filename Dateiname des Bildes
     */
    public void save(String filename) {
        try{
            String[] fn = filename.split("\\.");
            if (fn.length== 1) {
                ImageIO.write(image, "PNG", new File(filename+".png"));
            } else {

                if (fn.length == 2 && (fn[1].toUpperCase().equals("PNG") ||
                    fn[1].toUpperCase().equals("GIF"))){
                    ImageIO.write(image, fn[1], new File(filename)); 
                }else {
                    System.out.println("Unbekanntes Bildformat");
                }
            }
        } catch(Exception e) {
            System.out.println("Fehler beim Speichern");
        }
    }

    // ----------------------------------------- Sonstiges -----------------------------------------------  

    /**
     * Liefert das Bild als zweidimensionales Pixel-Array.
     * @return zweidimensionales Array von Color-Objekten, die den Pixeln des Bildes entsprechen.
     */

    public Color[][] getPixelArray() {
        Color[][] pixel = new Color[image.getWidth()][image.getHeight()];  
        for(int x=0; x < image.getWidth(); x++){
            for(int y=0; y < image.getHeight(); y++) {
                pixel[x][y] = new Color(image.getRGB(x,y));
            }
        }
        return pixel;
    }

    /**
     * Setzt das Bild neu auf Basis des Pixel-Arrays.
     * Die Groesse des Bildes wird nicht automatisch an das Array angepasst.
     * @param pixel zweidimensionales Array von Color-Objekten
     */
    public void setPixelArray(Color[][] pixel) {
        size(pixel.length,pixel[0].length);

        for(int x=0; x < image.getWidth(); x++){
            for(int y=0; y < image.getHeight(); y++) {
                g.setColor(pixel[x][y]);
                g.fillRect(x, y, 1, 1);
            }
        }
        repaint();
    }

    /**
     * Hilfsfunktion zum Verzoegern der Ausgabe
     * @param millis Wartezeit in Millisekunden
     */
    public void delay(int millis) {
        try{
            Thread.sleep(millis);
        } catch(Exception e) {
            System.out.println("Fehler beim Verzoegern der Ausgabe");
        }
    }
}

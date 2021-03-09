import imp.*;
import java.awt.Color;
import java.util.Random;

/**
 * Algorithmen zur Änderung der Pixelpositionen eines Pictures
 * z.B. drehen, spiegeln usw.
 *
 * @author Uli Göppert
 * @version 8.3.21
 */
public class GeometrischeBildoperationen 
{
    /** spiegeleHorizontal spiegelt das Bild, so dass rechts und links getauscht werden
     * @param originalbild Ein Bild (Picture), das gespiegelt werden soll
     * @return Eine gespiegelte Kopie des Bildes
     */

    public  Picture spiegelHorizontal(Picture originalbild) {
        int breite = originalbild.getWidth();
        int hoehe  = originalbild.getHeight();

        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                pixelNeu[x][y] = pixel[(breite-1)-x][y];
            }
        }

        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }

    /** spiegeleVertikal spiegelt das Bild, so dass oben und unten getauscht werden
     * @param originalbild Ein Bild (Picture), das gespiegelt werden soll
     * @return Eine gespiegelte Kopie des Bildes
     */

    public  Picture spiegelVertikal(Picture originalbild) {
        int breite = originalbild.getWidth();
        int hoehe  = originalbild.getHeight();

        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];
        
        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                pixelNeu[x][y] = pixel[x][(hoehe-1)-y];
            }
        }
       
        Picture neuesBild = new Picture(); 
        neuesBild.setPixelArray(pixelNeu);  
        return neuesBild;
    }


    /** dreheRechts dreht das Bild um 90° nach rechts
     * @param originalbild Ein Bild (Picture), das gedreht werden soll
     * @return Eine gedrehte Kopie des Bildes
     */

    public Picture dreheRechts(Picture originalbild) {
        int breite = originalbild.getHeight();
        int hoehe  = originalbild.getWidth();

        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) { 
                pixelNeu[x][y] = pixel[y][(breite-1)-x];
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu);
        return neuesBild;
    }

    /** dreheLinks dreht das Bild um 90° nach links
     * @param originalbild Ein Bild (Picture), das gedreht werden soll
     * @return Eine gedrehte Kopie des Bildes
     */
    public  Picture dreheLinks(Picture originalbild) {
        int breite = originalbild.getHeight();
        int hoehe  = originalbild.getWidth();

        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) { 
                pixelNeu[x][y] = pixel[(hoehe-1)-y][x];
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;

    }

    /** drehe180 dreht das Bild um 180° 
     * @param originalbild Ein Bild (Picture), das gedreht werden soll
     * @return Eine gedrehte Kopie des Bildes
     */
    public  Picture drehe180(Picture originalbild) {
      Picture bild90 = dreheLinks(originalbild);
      Picture bild180 = dreheLinks(bild90);
      return bild180;
    }

}

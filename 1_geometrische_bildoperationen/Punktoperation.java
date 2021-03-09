import imp.*;
import java.awt.Color;
import java.util.Random;

/**
 * Algorithmen zur Änderung der Pixelpositionen eines Pictures
 * z.B. drehen, spiegeln usw.
 *
 * @author Valentin Grießer
 * @version 9.3.
 */
public class Punktoperation 
{
   
    
    /** spiegeleHorizontal spiegelt das Bild, so dass rechts und links getauscht werden
     * @param originalbild Ein Bild (Picture), das gespiegelt werden soll
     * @return Eine gespiegelte Kopie des Bildes
     */

  
    
    public  Picture graustufenDurchschnitt(Picture originalbild) {
        int graudurchschnitt=0;
        int breite = originalbild.getWidth();
        int hoehe  = originalbild.getHeight();
        
        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                graudurchschnitt=((pixel[x][y].getRed()+pixel[x][y].getGreen()+pixel[x][y].getBlue())/3);// Durchschnitt: noch durch 3 teilen...
                pixelNeu[x][y] = new Color(graudurchschnitt,graudurchschnitt,graudurchschnitt);
            }
        }

        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    
}

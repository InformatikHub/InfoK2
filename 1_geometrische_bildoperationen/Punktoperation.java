import imp.*;
import java.awt.Color;
import java.util.Random;

/**
 * Algorithmen zur Änderung der Pixelpositionen eines Pictures
 * z.B. drehen, spiegeln usw.
 *
 * @author Rebecca Hogg
 * @version 9.3.
 * 
 * super gemacht, Gruß Gö
 * 
 */
public class Punktoperation
{
    /** spiegeleHorizontal spiegelt das Bild, so dass rechts und links getauscht werden
     * @param originalbild Ein Bild (Picture), das gespiegelt werden soll
     * @return Eine gespiegelte Kopie des Bildes
     */

    public  Picture graustufenDurchschnitt(Picture originalbild) {
        int graudurchschnitt = 0;
        int breite = originalbild.getWidth();
        int hoehe  = originalbild.getHeight();

        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                graudurchschnitt = ((pixel[x][y].getRed()+pixel[x][y].getGreen()+pixel[x][y].getBlue())/3);
                pixelNeu [x][y] = new Color(graudurchschnitt,graudurchschnitt,graudurchschnitt);
            }
        }

        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    
    public  Picture graustufenMin(Picture originalbild) {
        int grauMin = 0;
        int breite = originalbild.getWidth();
        int hoehe  = originalbild.getHeight();

        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                int Red = pixel[x][y].getRed();
                int Green = pixel[x][y].getGreen();
                int Blue = pixel[x][y].getBlue();
                
                if(Red < Green || Red < Blue) {
                  grauMin = pixel[x][y].getRed();
                }
                
                if(Green < Red || Red < Blue) {
                  grauMin = pixel[x][y].getGreen();
                }
                
                if(Blue < Red || Red < Green) {
                  grauMin = pixel[x][y].getBlue();
                }
                pixelNeu [x][y] = new Color(grauMin,grauMin,grauMin);
            }
        }

        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    
    public  Picture graustufenMax(Picture originalbild) {
        int grauMax = 0;
        int breite = originalbild.getWidth();
        int hoehe  = originalbild.getHeight();

        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                int Red = pixel[x][y].getRed();
                int Green = pixel[x][y].getGreen();
                int Blue = pixel[x][y].getBlue();
                
                if(Red > Green || Red > Blue) {
                  grauMax = pixel[x][y].getRed();
                }
                
                if(Green > Red || Red > Blue) {
                  grauMax = pixel[x][y].getGreen();
                }
                
                if(Blue > Red || Red > Green) {
                  grauMax = pixel[x][y].getBlue();
                }
                pixelNeu [x][y] = new Color(grauMax,grauMax,grauMax);
            }
        }

        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
}

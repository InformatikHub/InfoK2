import imp.*;
import java.awt.Color;
import java.util.Random;

/**
 * Algorithmen zur Änderung der Pixelpositionen eines Pictures
 * z.B. drehen, spiegeln usw.
 *
 * @author Thomas Schaller
 * @version 1.1 (28.11.2019)
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
    
    public Picture spiegelVertikal(Picture original){
        int breite = original.getWidth();
        int hoehe  = original.getHeight();

        Color[][] pixel = original.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                pixelNeu[x][y] = pixel[x][(hoehe-1)-y]; //gut
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    
    public Picture dreheLinks(Picture original){
        int breite = original.getWidth();
        int hoehe  = original.getHeight();

        Color[][] pixel = original.getPixelArray();
        Color[][] pixelNeu = new Color[hoehe][breite];

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                pixelNeu[y][x] = pixel[x][y]; //stimmt so nicht, zusätzlich wird das Bild noch bzgl. vorne und hinten gedreht.
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    
    public Picture dreheRechts(Picture original){
        int breite = original.getWidth();
        int hoehe  = original.getHeight();

        Color[][] pixel = original.getPixelArray();
        Color[][] pixelNeu = new Color[hoehe][breite];//gut

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                pixelNeu[y][x] = pixel[breite-1-x][hoehe-1-y]; //stimmt so auch nicht, du spiegelst einmal von oben nach unten zuviel
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    
    public Picture drehe180(Picture original){
        int breite = original.getWidth();
        int hoehe  = original.getHeight();

        Color[][] pixel = original.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                pixelNeu[x][y] = pixel[breite-1-x][(hoehe-1)-y]; //gut
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    public  Picture spiegelHorizontalMitte(Picture originalbild) {
        return spiegelHorizontalWo(originalbild, originalbild.getWidth()/2);
    }   
    
    public  Picture spiegelHorizontalWo(Picture originalbild, int wo) { //geniale Idee !!
        int breite = originalbild.getWidth();
        int hoehe  = originalbild.getHeight();
        int help = 0;

        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];
        
        if(wo < breite/2){
            for(int x = 0; x < wo; x++) {//gespiegeltes Bild
                for(int y = 0; y < hoehe; y++) {
                    pixelNeu[x][y] = pixel[wo*2 + help - x -1][y]; //help = 0 konstant ??
                }
            }
            for(int x = wo; x < breite; x++) { //ungespiegeltes Bildübernommen
                for(int y=0;y < hoehe; y++) {
                    pixelNeu[x][y] = pixel[x][y]; //also ab wo spiegelst du nichts mehr...
                }
            }
        }else{
            for(int x = 0; x < wo; x++) {
                for(int y = 0; y < hoehe; y++) {
                    pixelNeu[x][y] = pixel[x][y];
                }
            }
            for(int x = wo; x < breite; x++) {
                for(int y=0;y < hoehe; y++) {
                    pixelNeu[x][y] = pixel[wo-(x-wo)][y]; //warum brauchst du hier +help -1 nicht mehr? (oben nicht aufgeräumt?)
                }
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }   
}

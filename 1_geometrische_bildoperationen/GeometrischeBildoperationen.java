import imp.*;
import java.awt.Color;
import java.util.Random;

/**
 * Algorithmen zur Änderung der Pixelpositionen eines Pictures
 * z.B. drehen, spiegeln usw.
 *
 * @author Sarah Gehringer 
 * @version 1.1
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

    /** spiegeleVertikal spiegelt das Bild, so dass rechts und links getauscht werden
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
                pixelNeu[x][y] = pixel[x][(hoehe-1)-y]; //sehr gut!
            }
        }

        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }

    /** dreheLinks 
     * @param originalbild Ein Bild (Picture), 
     * @return Eine gedrehte Kopie des Bildes
     */

    public  Picture dreheLinks(Picture originalbild) {
        int breite = originalbild.getWidth();
        int hoehe  = originalbild.getHeight();

        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[hoehe][breite]; //ja

        for(int x=0; x < hoehe; x++) {
            for(int y=0;y < breite; y++) {
                pixelNeu[x][y] = pixel[(breite-1)-y][x]; //super gemacht!
            }
        }

        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }

    /** dreheRechtss 
     * @param originalbild Ein Bild (Picture),
     * @return Eine gedrehte Kopie des Bildes
     */

    public  Picture dreheRechts(Picture originalbild) {
        int breite = originalbild.getWidth();
        int hoehe  = originalbild.getHeight();

        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[hoehe][breite]; // genau!

        for(int x=0; x < hoehe; x++) {
            for(int y=0;y < breite; y++) {
                pixelNeu[x][y] = pixel[y][(hoehe-1)-x]; //perfekt!
            }
        }

        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }

    /** drehe180° 
     * @param originalbild Ein Bild (Picture),
     * @return Eine gedrehte Kopie des Bildes
     */

    public  Picture drehe180(Picture originalbild) {
        int breite = originalbild.getWidth();
        int hoehe  = originalbild.getHeight();

        Picture neuesBild1 = new Picture();
        Picture neuesBild2 = new Picture();

        neuesBild1 = dreheLinks(originalbild); 
        neuesBild2 = dreheLinks(neuesBild1); //sehr clever gelöst!! 

        return neuesBild2;
    }

    public void farbTest() {//ich schick dir einen Link zur Farbwahl, oder wozu sollte diese Methode dienen?
        Color f1;
        Color f2;
        f1 = new Color(200,0,255); 
        f2 = new Color(0xC800FF);

        // Test Ausgabe
        Color[][] pixelNeu = new Color[500][500];
        int x,y;
        for (x=0;x<500;x++)
            for (y=0;y<500;y++)
                pixelNeu[x][y]=f2;
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu);
        for (x=0;x<500;x++)
            for (y=0;y<500;y++)
                pixelNeu[x][y]=f2;
        Picture neuesBild2 = new Picture();
        neuesBild2.setPixelArray(pixelNeu);                

    }
}

import imp.*;
import java.awt.*;
import java.util.Random;
import java.util.*;
/**
* Beschreiben Sie hier die Klasse Punktoperationen.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Punktoperationen {
    public Picture graustufenDurchschnitt(Picture original) {
        int breite = original.getWidth();
        int hoehe  = original.getHeight();

        Color[][] pixel = original.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];
        Color tmp;
        int ehre = 0;
        Picture newImg = original;

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                ehre = (pixel[x][y].getRed() + pixel[x][y].getGreen() + pixel[x][y].getBlue()) /3;
                tmp = new Color(ehre,ehre,ehre);
                pixelNeu[x][y] = tmp;
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    
     public Picture graustufenNatuerlich(Picture original) {
        int breite = original.getWidth();
        int hoehe  = original.getHeight();

        Color[][] pixel = original.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];
        Color tmp;
        int ehre = 0;
        Picture newImg = original;

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                ehre = (int)(pixel[x][y].getRed()*0.299 + pixel[x][y].getGreen()*0.587 + pixel[x][y].getBlue()*0.114) ;
                tmp = new Color(ehre,ehre,ehre);
                pixelNeu[x][y] = tmp;
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    
    public Picture graustufenMin(Picture original) {
        int breite = original.getWidth();
        int hoehe  = original.getHeight();
        int tmp;
        
        Color px;
        Color[][] pixel = original.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                tmp = getMinColor(pixel[x][y]);
                px = new Color(tmp,tmp,tmp);
                pixelNeu[x][y] = px;
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    
    public Picture graustufenMax(Picture original) {
        int breite = original.getWidth();
        int hoehe  = original.getHeight();
        int tmp;
        
        Color px;
        Color[][] pixel = original.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                tmp = getMaxColor(pixel[x][y]);
                px = new Color(tmp,tmp,tmp);
                pixelNeu[x][y] = px;
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    
    public int getMaxColor(Color pixel){
        int red,green,blue;
        red = pixel.getRed();
        green = pixel.getGreen();
        blue = pixel.getBlue();
        if((red > blue && red > green) || (red > blue)){
            return red;
        }else if ((blue > red && blue > green) || (blue > red)){
            return blue;
        }else{
            return green;
        }
    }
    
    public int getMinColor(Color pixel){
        int red,green,blue;
        red = pixel.getRed();
        green = pixel.getGreen();
        blue = pixel.getBlue();
        if((red < blue && red < green) || (red < blue)){
            return red;
        }else if ((blue < red && blue < green) || (blue < red)){
            return blue;
        }else{
            return green;
        }
    }
}

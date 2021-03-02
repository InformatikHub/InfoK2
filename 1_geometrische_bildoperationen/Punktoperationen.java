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
        int ehre = 0;
        
        Color[][] pixel = original.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];
        Color tmp;

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                ehre = (pixel[x][y].getRed() + pixel[x][y].getGreen() + pixel[x][y].getBlue()) /3;
                tmp = new Color(ehre,ehre,ehre); //super !
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

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                ehre = (int)(pixel[x][y].getRed()*0.299 + pixel[x][y].getGreen()*0.587 + pixel[x][y].getBlue()*0.114) ;
                tmp = new Color(ehre,ehre,ehre); //super!
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
                tmp = getMinColor(pixel[x][y]);//gut, eigene Methode ausgelagert !
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
        int tmp = 4;
        
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
        int[] rgb = {pixel.getRed(), pixel.getGreen(), pixel.getBlue()};
        int tmp = 0;
         for(int i = 0; i < rgb.length; i++){
            for(int j = 0; j < rgb.length; j++){
                for(int k = 0; k < rgb.length; k++){
                    if(rgb[i] > rgb[j] && rgb[i] > rgb[k] && rgb[i] > rgb[tmp]) tmp = i;
                }
            }
        }
        return rgb[tmp];
    }
    
    public int getMax(int a, int b, int c){
        int[] rgb = {a,b,c};
        int tmp = 0;
        for(int i = 0; i < rgb.length; i++){
            for(int j = 0; j < rgb.length; j++){
                for(int k = 0; k < rgb.length; k++){
                    if(rgb[i] > rgb[j] && rgb[i] > rgb[k] && rgb[i] > rgb[tmp]) tmp = i;
                }
            }
        }
        return rgb[tmp];
    }
    
    public int getMinColor(Color pixel){
        int[] rgb = {pixel.getRed(), pixel.getGreen(), pixel.getBlue()};
        int tmp = 0;
         for(int i = 0; i < rgb.length; i++){
            for(int j = 0; j < rgb.length; j++){
                for(int k = 0; k < rgb.length; k++){
                    if(rgb[i] < rgb[j] && rgb[i] < rgb[k] && rgb[i] < rgb[tmp]) tmp = i;
                }
            }
        }
        return rgb[tmp];
    }
}

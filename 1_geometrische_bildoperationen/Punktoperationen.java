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
public class Punktoperationen{
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
    
    public Picture invertiere(Picture original) {
        int breite = original.getWidth();
        int hoehe  = original.getHeight();
        int red, green, blue;
        Color px;
       
        Color[][] pixel = original.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];

        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
               red = 255 - pixel[x][y].getRed();
               green = 255 - pixel[x][y].getGreen();
               blue = 255 - pixel[x][y].getBlue();
               px = new Color(red,green,blue);
               pixelNeu[x][y] = px;
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    
     public Picture setTwoFarbenEqual(Picture original, int end, int start) {
        int breite = original.getWidth();
        int hoehe  = original.getHeight();
        int fahrrad;
        Color px;
       
        Color[][] pixel = original.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];
        assert start >=0 && start <3 && end >=0 && end <3;
        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                if(start==0){
                    fahrrad = pixel[x][y].getRed();
                }else if(start == 1){
                    fahrrad = pixel[x][y].getGreen();
                }else{
                    fahrrad = pixel[x][y].getBlue();
                }
                if(end == 0){
                    px = new Color(fahrrad, pixel[x][y].getGreen(), pixel[x][y].getBlue());
                }else if(end == 1){
                    px = new Color(pixel[x][y].getGreen(),fahrrad, pixel[x][y].getBlue());
                }else{
                    px = new Color(pixel[x][y].getRed(), pixel[x][y].getBlue(), fahrrad);
                }
                
                pixelNeu[x][y] = px;
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    
    public Picture ernst(Picture original, int start, int end) {
        int breite = original.getWidth();
        int hoehe  = original.getHeight();
        int r,g,b;
        Color px;
       
        Color[][] pixel = original.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];
        assert start >=0 && start <3 && end >=0 && end <3;
        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                r = pixel[x][y].getRed();
                g = pixel[x][y].getGreen();
                b = pixel[x][y].getBlue();
                if(start==0){
                    if(end==0){
                        r = pixel[x][y].getRed();
                    }else if(end == 1){
                        r = pixel[x][y].getGreen();
                        g = pixel[x][y].getRed();
                    }else{
                        r = pixel[x][y].getBlue();
                        b = pixel[x][y].getRed();
                    }
                }else if(start == 1){
                    if(end==0){
                        g = pixel[x][y].getRed();
                    }else if(end == 1){
                        g = pixel[x][y].getGreen();
                        r = pixel[x][y].getRed();
                    }else{
                        g = pixel[x][y].getBlue();
                    }
                }else{
                    if(end==0){
                        b = pixel[x][y].getRed();
                        r = pixel[x][y].getBlue();
                    }else if(end == 1){
                        b = pixel[x][y].getGreen();
                        g = pixel[x][y].getBlue();
                    }else{
                        b = pixel[x][y].getBlue();
                    }
                }
                px = new Color(r,g,b);
                
                pixelNeu[x][y] = px;
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
     public Picture futoi(Picture original) {
        int breite = original.getWidth();
        int hoehe  = original.getHeight();
        int r,g,b;
        Color px;
       
        Color[][] pixel = original.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];
        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                r = pixel[x][y].getGreen();
                g = pixel[x][y].getRed();
                b = pixel[x][y].getBlue();
                px = new Color(r,g,b);
                
                pixelNeu[x][y] = px;
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    } 
    public Picture farbaenderung (Picture original, double faktor_r, double faktor_g, double faktor_b){
    int breite = original.getWidth();
        int hoehe  = original.getHeight();
        int r,g,b;
        Color px;
       
        Color[][] pixel = original.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];
        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                r = (int) (pixel[x][y].getRed() * faktor_r);
                g = (int) (pixel[x][y].getGreen() * faktor_g);
                b = (int) (pixel[x][y].getBlue() * faktor_b);
                
                r = setVarTo0To255(r);
                g = setVarTo0To255(g);
                b = setVarTo0To255(b);
                
                px = new Color(r,g,b);
                
                pixelNeu[x][y] = px;
            }
        }
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    public int setVarTo0To255(int tmp){
        if(tmp < 0) tmp = 0;
        if(tmp > 255) tmp = 255;
        return tmp;
    }
}
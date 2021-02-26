import imp.*;
import java.awt.Color;
import java.util.Random;

/**
 * Beschreiben Sie hier die Klasse Punktoperation.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Punktoperation
{
    
    public Picture graustufenDurchschnitt(Picture originalbild){
        int breite = originalbild.getWidth();
        int hoehe  = originalbild.getHeight();
        int graudurchschnitt=0;
        
        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];
        
        
        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                graudurchschnitt=(pixel[x][y].getRed()+pixel[x][y].getBlue()+pixel[x][y].getGreen())/3;
                
                pixelNeu[x][y]= new Color(graudurchschnitt,graudurchschnitt,graudurchschnitt); // sehr gut !
            }
        }
        
        
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    
    public Picture graustufenMin(Picture originalbild){
        int breite = originalbild.getWidth();
        int hoehe  = originalbild.getHeight();
        int grauMin=0;
        
        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];
        
        
        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                int rotanteil=pixel[x][y].getRed();
                int blauanteil=pixel[x][y].getBlue();
                int grünanteil=pixel[x][y].getGreen();
                
                if(rotanteil<blauanteil && rotanteil<grünanteil){
                     grauMin=rotanteil;
                }else if(blauanteil<rotanteil && blauanteil<grünanteil){
                     grauMin=blauanteil;
                } else{
                     grauMin=grünanteil;
                }
                
                pixelNeu[x][y]= new Color(grauMin,grauMin,grauMin); //super !
            }
        }
      
        
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    
    public Picture graustufenMax(Picture originalbild){
        int breite = originalbild.getWidth();
        int hoehe  = originalbild.getHeight();
        int grauMax=0;
        
        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];
        
        
        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                int rotanteil=pixel[x][y].getRed();
                int blauanteil=pixel[x][y].getBlue();
                int grünanteil=pixel[x][y].getGreen();
                
                if(rotanteil>blauanteil && rotanteil>grünanteil){
                     grauMax=rotanteil;
                }else if(blauanteil>rotanteil && blauanteil>grünanteil){
                     grauMax=blauanteil;
                } else{
                     grauMax=grünanteil;
                }
                
                
                pixelNeu[x][y]= new Color(grauMax,grauMax,grauMax); // perfekt
            }
        }
      
        
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
    
    public Picture graustufenNatuerlich(Picture originalbild){
        int breite = originalbild.getWidth();
        int hoehe  = originalbild.getHeight();
        int grauNat=0;
        
        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[breite][hoehe];
        
        
        for(int x=0; x < breite; x++) {
            for(int y=0;y < hoehe; y++) {
                double rotanteil=pixel[x][y].getRed()*0.299;
                double blauanteil=pixel[x][y].getBlue()*0.114;
                double grünanteil=pixel[x][y].getGreen()*0.587;
                
                grauNat=(int)(rotanteil+blauanteil+grünanteil);
                
                pixelNeu[x][y]= new Color(grauNat,grauNat,grauNat); // auch gelöst!
            }
        }
      
        
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }
}

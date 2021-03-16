import imp.*;
import java.awt.Color;
import java.util.Random;

/**
 * Beschreiben Sie hier die Klasse Mehrpixeloperation.
 * 
 * @author Alwin Choi
 * @version 9.3
 */
public class Mehrpixeloperation
{
   
    public Picture weichzeichnen(Picture originalbild){
        int breite = originalbild.getWidth();
        int hoehe  = originalbild.getHeight();
        
        int arr[][] = {{2, -4, 2},{-2, 5, -2},{3,-6,3}};
        
        
        Color[][] pixel = originalbild.getPixelArray();
        Color[][] pixelNeu = new Color[breite-2][hoehe-2];
        
        for(int i=0;i<breite-2;i++){
            for(int j=0;j<hoehe-2;j++){
                int reddurchschnitt=0;
                int bluedurchschnitt=0;
                int greendurchschnitt=0;
                int tmpone=0;
                int tmptwo=0;
                for(int k=0+i;k<3+i;k++){
                    tmpone=0;
                    for(int l=0+j;l<3+j;l++){
                        int red=pixel[k][l].getRed()*arr[tmpone][tmptwo];
                        reddurchschnitt+=red;
                        
                        int blue=pixel[k][l].getBlue()*arr[tmpone][tmptwo];
                        bluedurchschnitt+=blue;
                        
                        int green=pixel[k][l].getGreen()*arr[tmpone][tmptwo];
                        greendurchschnitt+=green;
                        
                        tmpone++;
                        
                    }
                    tmptwo++;
                }
                //reddurchschnitt/=9;
                //bluedurchschnitt/=9;
                //greendurchschnitt/=9;
                
                if(reddurchschnitt>250){reddurchschnitt=250;}
                else if(reddurchschnitt<0){reddurchschnitt=0;}
                if(bluedurchschnitt>250){bluedurchschnitt=250;}
                else if(bluedurchschnitt<0){bluedurchschnitt=0;}
                if(greendurchschnitt>250){greendurchschnitt=250;}
                else if(greendurchschnitt<0){greendurchschnitt=0;}
                
                
                pixelNeu[i][j]=new Color(reddurchschnitt,bluedurchschnitt,greendurchschnitt);
            }
        }
        
        
        Picture neuesBild = new Picture();
        neuesBild.setPixelArray(pixelNeu); 
        return neuesBild;
    }

    
    public int[][] filter(){
        int arr[][] = {{0, -1, 0},{-1, 4, -1},{0,-1,0} };
        
        return arr;
    }
}


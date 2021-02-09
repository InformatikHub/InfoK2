package imp;

import java.awt.Color;


/**
 * Write a description of class PixelColor here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class HSB 
{
 
    
   public static double getHue(Color c) {
      float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
    return hsb[0];
}

   public static double getSaturation(Color c) {
      float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
    return hsb[1];
}

   public static double getBrightness(Color c) {
      float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
    return hsb[2];
}

 public static Color getColor(double h, double s, double b) {
     return new Color(Color.HSBtoRGB((float) h, (float) s, (float) b));
    }


}

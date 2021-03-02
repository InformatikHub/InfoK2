package imp;

   

/**
 * Repräsentiert eine Zeile eines Table-Objekts.
 * Erlaubt einen einfachen Zugriff auf die einzelnen Einträge in dieser Zeile.
 * 
 * @author Thomas Schaller
 * @version V1.0 vom 01.02.2019
 */

import java.io.File; 
import java.io.IOException; 
import java.util.List;
import java.util.ArrayList; 
import java.io.*;
import java.util.Scanner;

import org.jdom.Document; 
import org.jdom.Element; 
import org.jdom.Attribute; 
import org.jdom.JDOMException; 
import org.jdom.input.SAXBuilder; 
import org.jdom.output.XMLOutputter; 
import org.jdom.output.Format;
import java.text.NumberFormat;

 

public class TableRow  
{
    // Verweis auf das ganze Dokument
  private Document doc;
    // Verweis auf die Zeile, für die dieses Objekt steht
  private Element current;
    // Verweis auf die Kopfzeile
  private Element header;
    // Für die Interpretation von Zahlenwerten
  NumberFormat format = NumberFormat.getInstance();

    // Ende Attribute
    /**
   *  Erzeugt ein TableRow-Objekt.
   * Diese Methode ist für den internen Gebraucht. Einige Methode der Table-Klasse erzeugen mit diesem Konstruktor TableRow-Objekte.
   * @param doc JDOM-Dokument, das für die ganze Tabelle steht.
   * @param row JDOM-Element, das für die aktuelle Zeile steht.
   */
  public TableRow(Document doc, Element row) {
    this.doc = doc;
    this.current = row;
    this.header = doc.getRootElement().getChild("Header");
  }
  
  /**
   * Liefert die Anzahl der Spalten der Zeile.
   * @return Anzahl der Spalten
   */
  public int getColumnCount() {
    return header.getChildren().size();
  }
  
  /**
   * Liefert den Titel einer Spalte
   * @param i Nummer der Spalte
   * @return Name der Spalte
   */
  public String getColumnTitle(int i) {
    if(i< getColumnCount()) {
      return ((List<Element>) (header.getChildren())).get(i).getText();
    } else {
      return "";
    }
  }
  
   /**
   * Liefert die Nummer einer Spalte
   * @param name Name der Spalte
   * @return Nummer der Spalte
   */
  public int getColumn(String name) {
    List<Element> columns = header.getChildren();
    int i = 0;
    while (i < columns.size()) {
      if (columns.get(i).getText().toLowerCase().equals(name.toLowerCase())) {
        return i;
      }
      i++;
    } // end of while
    return -1;
  }
    
    
    /**
    * Erzeugt eine neue Zeile mit i Spalten
    * Wenn bisher nicht genügend Spalten vorhanden sind, werden automatisch neue Spalten hinzugefügt (auch zum Header)
    * @param i Anzahl der Spalten
    */
  private Element buildRow(int i) {
    List<Element> columns = header.getChildren();
    Element entry=null;
    for(int j=0; j<=i; j++) {
      
      if(j==columns.size()) {
        Element h = new Element("Column");
        h.setAttribute("type", "unknown");
        header.addContent(h);
        columns = header.getChildren();
      }
      if(j==current.getChildren().size()) {
        entry = new Element("Entry");
        current.addContent(entry);
        
      }
      
    }
    return entry;
    
  }
    
    /**
    * Erzeugt eine neue Zeile.
    * Es werden genügend Spalten erzeugt, dass ein Wert in Spalte "name" eingetragen werden kann
    * @param name Name der Spalte
    */
  private Element buildRow(String name) {
    List<Element> columns = header.getChildren();
    int i = 0;
    for(Element c: columns) {
      
      if(c.getText().toLowerCase().equals(name.toLowerCase())) {
        return buildRow(i);
      }
      i++;
    }
    return null;
    
  }
    
    /**
    * Liefert den Wert einer Zelle als String
    * @param i Nummer der Spalte
    * @return Wert der Zelle
    */
  public String getString(int i) {
    if(i >= current.getContent().size()) return "";
    Element e = (Element) current.getContent(i) ;
    if(e!=null) {
      return e.getText();
    } else {
      return "";
    }
  }
    
    /**
    * Liefert den Wert einer Zelle als String
    * @param name Name der Spalte
    * @return Wert der Zelle
    */
  public String getString(String name) {
   return getString(getColumn(name));
  }
    
    /**
    * Setzt den Wert einer Zelle als String
    * @param i Nummer der Spalte
    * @param text neuer Wert der Zelle
    */
  public void setString(int i, String text) {
    
    Element e = buildRow(i);
    if(e!=null) e.setText(text);
  }
    
    /**
    * Setzt den Wert einer Zelle als String
    * @param name Name der Spalte
    * @param text neuer Wert der Zelle
    */
  public void setString(String name, String text) {
    Element e = buildRow(name);
    if(e!=null) e.setText(text);
  }
    
    
    /**
    * Liefert den Wert einer Zelle als Int-Zahl
    * @param i Nummer der Spalte
    * @return Wert der Zelle
    */
  public int getInt(int i) {
    try{
      Element e = (Element) current.getContent(i) ;
      return Integer.parseInt(e.getText());
    } catch(Exception e) {
      return 0;
    }
  }
    
    /**
    * Liefert den Wert einer Zelle als Int-Zahl
    * @param name Name der Spalte
    * @return Wert der Zelle
    */
  public int getInt(String name) {
    return getInt(getColumn(name));
  }
    
    /**
    * Setzt den Wert einer Zelle als Int-Zahl
    * @param i Nummer der Spalte
    * @param value neuer Wert der Zelle
    */
  public void setInt(int i,int value) {
    
    Element e = buildRow(i);
    if(e!=null) e.setText(""+value);
  }
    
    /**
    * Setzt den Wert einer Zelle als Int-Zahl
    * @param name Name der Spalte
    * @param value neuer Wert der Zelle
    */
  public void setInt(String name, int value) {
    Element e = buildRow(name);
    if(e!=null) e.setText(""+value);
  }
    
    /**
    * Liefert den Wert einer Zelle als Float-Zahl
    * @param i Nummer der Spalte
    * @return Wert der Zelle
    */       
  public float getFloat(int i) {
    try{
      Element e = (Element) current.getContent(i) ;
      return Float.parseFloat(e.getText().replace(",","."));
    } catch(Exception e) {
      return 0;
    }
  }
    
    /**
    * Liefert den Wert einer Zelle als Float-Zahl
    * @param name Name der Spalte
    * @return Wert der Zelle
    */
  public float getFloat(String name) {
    return getFloat(getColumn(name));
  }
    
    /**
    * Setzt den Wert einer Zelle als Float-Zahl
    * @param i Nummer der Spalte
    * @param value neuer Wert der Zelle
    */
  public void setFloat(int i,float value) {
    
    Element e = buildRow(i);
    if(e!=null) e.setText(format.format(value));
  }
    
    /**
    * Setzt den Wert einer Zelle als Float-Zahl
    * @param name Name der Spalte
    * @param value neuer Wert der Zelle
    */
  public void setFloat(String name, float value) {
    Element e = buildRow(name);
    if(e!=null) e.setText(format.format(value));
  }
    
    /**
    * Liefert den Wert einer Zelle als Double-Zahl
    * @param i Nummer der Spalte
    * @return Wert der Zelle
    */
  public double getDouble(int i) {
    try{
      Element e = (Element) current.getContent(i) ;
      return Double.parseDouble(e.getText().replace(",","."));
      
    } catch(Exception e) {
      return 0;
    }
  }
    
    /**
    * Liefert den Wert einer Zelle als Double-Zahl
    * @param name Name der Spalte
    * @return Wert der Zelle
    */
  public double getDouble(String name) {
    return getDouble(getColumn(name));
  }
    
    /**
    * Setzt den Wert einer Zelle als Double-Zahl
    * @param i Nummer der Spalte
    * @param value neuer Wert der Zelle
    */
  public void setDouble(int i,double value) {
    
    Element e = buildRow(i);
    if(e!=null) e.setText(format.format(value));
  }
    
    /**
    * Setzt den Wert einer Zelle als Double-Zahl
    * @param name Name der Spalte
    * @param value neuer Wert der Zelle
    */
  public void setDouble(String name, double value) {
    Element e = buildRow(name);
    if(e!=null) e.setText(format.format(value));
  }
    
}

package imp;  

/**
 * Klasse zum Vereinfachten Zugriff auf XML-Dokumente
 * Diese Klasse ist für den Einsatz in der Schule gedacht und soll den Schülern
 * einen einfachen Zugriff auf XML-Dokumente ermöglichen. Die zur Verfügung 
 * stehenden Befehle sind wie in Processing realisiert. 
 * Dabei ist jeder Teilbaum des Dokuments wieder als XML-Objekt zugreifbar, das
 * intern auf die gleiche XML-Dokumentstruktur zugreift.
 * Dies ermöglicht bei unsachgemäßem Gebrauch die XML-Struktur zu zerstören. Im
 * normalen Gebrauch sollte dies aber nicht relevant sein.
 * 
 * Benötigt: jdom-1.1.3.jar 
 
 * @author Thomas Schaller 
 * @version 1.0 vom 31.01.2019
 */
import java.io.File; 
import java.io.IOException; 
import java.util.List;
import java.util.ArrayList; 
import java.io.*;

import org.jdom.Document; 
import org.jdom.Element; 
import org.jdom.Attribute; 
import org.jdom.JDOMException; 
import org.jdom.input.SAXBuilder; 
import org.jdom.output.XMLOutputter; 
import org.jdom.output.Format;

public class XML {
    // Anfang Attribute
    // XML-Dokumentstruktur
    private Document doc; 
    // Zeiger auf das aktuelle Element
    private Element current;
    // Ende Attribute

    /** 
      * Erzeugt ein leeres XMLDokument 
      */
    public XML() {
        this.doc = new Document();
        this.current = null;
    }

    /** 
      * Erzeugt ein XML-Dokument aus einer Datei 
      * @param filename Dateiname der XML-Datei
      */
    public XML(String filename) {
        loadXML(filename);
    }
  
    /**
   * interner Konstruktor, um ein XML Objekt zu erzeugen, das auf einen bestimmten Knoten verweist
   * @param doc die XML-Dokumentstruktur
   * @param current Zeiger auf das aktuelle Element 
   */
    private XML(Document doc, Element current) {
        this.doc = doc;
        this.current = current;
    }

    // Anfang Methoden
    /** Öffnet das durch den Dateinamen gegebene Dokument 
     *  @param filename Dateiname des XML-Files
     */
    public void loadXML(String filename) {
        doc = null; 
        File f = new File(filename);

        try { 
            // Das Dokument erstellen 
            SAXBuilder builder = new SAXBuilder(); 
            doc = builder.build(f); 

        } catch (JDOMException e) { 
            e.printStackTrace(); 

        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        // Zeiger im Baum auf Root-Element
        current = doc.getRootElement();
    }

    /** Speichert den XML-Baum im angegebenen Dateinamen
     *  @param filename Dateiname des XML-Files
     */
    public void saveXML(String filename) {
        try {
            // new XMLOutputter().output(doc, System.out);
            XMLOutputter xmlOutput = new XMLOutputter();

            // display nice nice
            xmlOutput.setFormat(Format.getPrettyFormat());
            File f = new File(filename);
            FileOutputStream outputFile = new FileOutputStream(f);
            System.out.println("Speicher in : "+f.getAbsolutePath() );
            xmlOutput.output(doc, outputFile);
            outputFile.close();
            System.out.println("File Saved!");
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }

    }

    //----------------------------------------------- Zeigerbewegungen --------------------------------------------------
    /**
   * liefert ein XML-Objekt, das auf den Vaterknoten des aktuellen Elements zeigt.
   * @return Vater des aktuellen Objekts.
   */
    public XML getParent() {
        if(current != null) { 
            Element parent = current.getParentElement();
            if (parent == null) {
                return null;
            } else {
                return new XML(doc, parent);
            }
        }
        return null;
    }
  
    /**
   * Überprüft, ob das Element irgendwelche Kinder hat oder nicht, und gibt das Ergebnis als boolean zurück. 
   * @return true, wenn Kinder vorhanden sind, sonst false
   */
    public boolean hasChildren() {
        if (current == null) {
            return doc.hasRootElement();
        } else {
            return current.getChildren().size()>0;
        }
    }

    /**
   * Ermittelt die Namen aller Kinder des Elements und gibt die Namen als ein Array von Strings zurück. 
   * Dies ist dasselbe wie das Durchlaufen und Aufrufen von getName() auf jedem untergeordneten Element einzeln. 
   * @return Liste aller Namen der Kinder
   */
    public String[] listChildren() {
        if (current == null) {
            if(doc.hasRootElement()) {
                String[] names = new String[0];
                names[0] = doc.getRootElement().getName();
                return names;
            } else {
                return null;
            }
        } else {
            List<Element> ch_element = current.getChildren();
            String[] names = new String[ch_element.size()];
            for(int i=0; i < ch_element.size() ; i++) {
                names[i] = ch_element.get(i).getName();
            }
            return names;
        }
    }
  
    /**
   * Liefert alle Kinder des Elements als Array von XML-Objekten. 
   * @return Array der Kinder als XML-Objekte
   */
    public XML[] getChildren() {
        if (current == null) {
            if(doc.hasRootElement()) {
                XML[] ch_xml = new XML[1];
                ch_xml[0] = new XML(doc, doc.getRootElement());
                return ch_xml;
            } else {
                return null;
            }
        } else {
            List<Element> ch_element = current.getChildren();
            XML[] ch_xml = new XML[ch_element.size()];
            for(int i=0; i < ch_element.size() ; i++) {
                ch_xml[i] = new XML(doc, ch_element.get(i));
            }
            return ch_xml;
        }
    }

    /**
   * Liefert bestimmte Kinder des Elements als Array von XML-Objekten. 
   * Die Methode gibt dabei alle Kinder zurück, die dem angegebenen Namen entsprechen. 
   * @param name Name der gesuchten Kind-Objekte
   * @return Array der Kinder als XML-Objekte
   */
    public XML[] getChildren(String name) {
        if (current == null) {
            if(doc.hasRootElement()) {
                XML[] ch_xml = new XML[1];
                ch_xml[0] = new XML(doc, doc.getRootElement());
                if(doc.getRootElement().getName().equals(name)){
                    return ch_xml;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            List<Element> ch_element = current.getChildren(name);
            XML[] ch_xml = new XML[ch_element.size()];
            for(int i=0; i < ch_element.size() ; i++) {
                ch_xml[i] = new XML(doc, ch_element.get(i));
            }
            return ch_xml;
        }
    }
  
    /**
   * Liefert das erste Kind des Elements mit einem bestimmten Namen. 
   * Die Methode gibt das erste Kind zurück, das dem angegebenen Namen entsprechen. 
   * @param name Name des gesuchten Kind-Objektes
   * @return Kind als XML-Objekt
   */

    public XML getChild(String name) {
        if (current == null) {
            Element e = doc.getRootElement();
            if (e.getName().equals(name)) {
                return new XML(doc, e);
            } else {
                return null;
            }
        } else {
            String[] names = name.split("/");
            Element e = current;
            int i = 0;
            while(i < names.length) {
                e = e.getChild(names[i]);
                if (e==null) return null;
                i++;
            }
            return new XML(doc, e);
        }
    }
  
  /**
   * Liefert das i. Kind des Elements. 
   * Die Methode gibt das i. Kind des aktuellen Elements zurück.
   * @param i Nummer des Kindes
   * @return Kind als XML-Objekt
   */
    public XML getChild(int i) {
        if (current == null) {
            return new XML(doc, doc.getRootElement());
        } else {
            List<Element> ch_element = current.getChildren();
            if (i>=ch_element.size()) return null;
            return new XML(doc, ch_element.get(i));
        }
    }

    //--------------------------------------------------- Methoden für das aktuelle Element -------------------------------------------------
  /** 
   * Frage den Namen des aktuellen Elements ab
   * @return Namen des Elements
   */
    public String getName() {
        if (current==null) return "";
        return current.getName();
    }

  /** 
   * Setze den Namen des aktuellen Elements. 
   * @param name Neuer Name des Elements
   */
    public void setName(String name) {
        if (current==null) return;
        current.setName(name);
    }
  
  /**
   * liefert die Anzahl der Attribute eines Elements.
   * @return Anzahl des Attribute
   */
    public int getAttributeCount() {
        if (current == null) return 0;
        return current.getAttributes().size();
    }
  
 /**
   * liefert zurück, ob das aktuelle Element Attribute hat .
   * @return true, wenn es Attribute gibt
   */
    public boolean hasAttribute() {
        if (current == null) return false;
        return current.getAttributes().size()>0;
    }

  /**
   * Ruft alle Attribute des angegebenen Elements ab und gibt sie als Array von Strings zurück.
   * @return Liste der Attributnamen
   */
    public String[] listAttributes() {
        if (current == null) return null;
        List<Attribute> attr = current.getAttributes();
        String[] names = new String[attr.size()];
        for(int i=0; i < attr.size() ; i++) {
            names[i] = attr.get(i).getName();
        }
        return names;
    }
  
  /** 
   * Fragt einen Attributwert des aktuellen Elements ab
   * @param attribute Name des Attributs
   * @return Wert des Attributs
   */
    public String getString(String attribute) {
        if (current==null) return "";
        return current.getAttributeValue(attribute);
    }
  
  /** 
   * Fragt einen Attributwert des aktuellen Elements ab
   * Sollte es das Attribut nicht geben, wird ein default-Wert zurückgegeben
   * @param attribute Name des Attributs
   * @param defaultValue Standardwert, falls es das Attribut nicht gibt
   * @return Wert des Attributs
   */
    public String getString(String attribute, String defaultValue) {
        if (current==null) return defaultValue;
        return current.getAttributeValue(attribute,defaultValue);
    }

  /** 
   * Setzt einen Attributwert des aktuellen Elements 
   * @param attribute Name des Attributs
   * @param text neuer Wert des Attributs
   */
    public void setString(String attribute, String text) {
        if (current==null) return;
        current.setAttribute(attribute, text);
    }

  /** 
   * Fragt einen Attributwert des aktuellen Elements ab
   * @param attribute Name des Attributs
   * @return Wert des Attributs als Integer-Zahl
   */
    public int getInt(String attribute) {
        if (current==null) return 0;
        try{
            int i = Integer.parseInt(current.getAttributeValue(attribute));
            return i;
        } catch(Exception e) { return 0; }

    }

  /** 
   * Fragt einen Attributwert des aktuellen Elements ab
   * Sollte es das Attribut nicht geben, wird ein default-Wert zurückgegeben
   * @param attribute Name des Attributs
   * @param defaultValue Standardwert, falls es das Attribut nicht gibt
   * @return Wert des Attributs als Integer-Zahl
   */
    public int getInt(String attribute, int defaultValue) {
        if (current==null) return defaultValue;
        try{
            int i = Integer.parseInt(current.getAttributeValue(attribute));
            return i;
        } catch(Exception e) { return defaultValue; }

    }

  /** 
   * Setzt einen Attributwert des aktuellen Elements 
   * @param attribute Name des Attributs
   * @param value neuer Wert des Attributs
   */
    public void setInt(String attribute, int value) {
        if (current==null) return;
        current.setAttribute(attribute, ""+value);
    }

  /** 
   * Fragt einen Attributwert des aktuellen Elements ab
   * @param attribute Name des Attributs
   * @return Wert des Attributs als Float-Zahl
   */
    public float getFloat(String attribute) {
        if (current==null) return 0;
        try{
            float i = Float.parseFloat(current.getAttributeValue(attribute));
            return i;
        } catch(Exception e) { return 0; }

    }

 /** 
   * Fragt einen Attributwert des aktuellen Elements ab
   * Sollte es das Attribut nicht geben, wird ein default-Wert zurückgegeben
   * @param attribute Name des Attributs
   * @param defaultValue Standardwert, falls es das Attribut nicht gibt
   * @return Wert des Attributs als Float-Zahl
   */
      public float getFloat(String attribute, float defaultValue) {
        if (current==null) return defaultValue;
        try{
            float i = Float.parseFloat(current.getAttributeValue(attribute));
            return i;
        } catch(Exception e) { return defaultValue; }

    }

  /** 
   * Setzt einen Attributwert des aktuellen Elements 
   * @param attribute Name des Attributs
   * @param value neuer Wert des Attributs
   */
     public void setFloat(String attribute, float value) {
        if (current==null) return;
        current.setAttribute(attribute, ""+value);
    }

  /** 
   * Fragt einen Attributwert des aktuellen Elements ab
   * @param attribute Name des Attributs
   * @return Wert des Attributs als Double-Zahl
   */
    public double getDouble(String attribute) {
        if (current==null) return 0;
        try{
            double i = Double.parseDouble(current.getAttributeValue(attribute));
            return i;
        } catch(Exception e) { return 0; }

    }

/** 
   * Fragt einen Attributwert des aktuellen Elements ab
   * Sollte es das Attribut nicht geben, wird ein default-Wert zurückgegeben
   * @param attribute Name des Attributs
   * @param defaultValue Standardwert, falls es das Attribut nicht gibt
   * @return Wert des Attributs als double-Zahl
   */
     public double getDouble(String attribute, double defaultValue) {
        if (current==null) return defaultValue;
        try{
            double i = Double.parseDouble(current.getAttributeValue(attribute));
            return i;
        } catch(Exception e) { return defaultValue; }

    }

  /** 
   * Setzt einen Attributwert des aktuellen Elements 
   * @param attribute Name des Attributs
   * @param value neuer Wert des Attributs
   */
    public void setDouble(String attribute, double value) {
        if (current==null) return;
        current.setAttribute(attribute, ""+value);
    }

   /** 
    * Fragt den Inhalt/Text des aktuellen Elements ab
   * @return Inhalt des Elements
    */
    public String getContent() {
        if ( current==null) return "";

        return current.getText();
    }

   /** 
    * Fragt den Inhalt/Text des aktuellen Elements ab
    * Hat das Element keinen Inhalt wird der defaultValue zurückgegeben.
    * @param defaultValue Standardtext
    * @return Inhalt des Elements
    */
    public String getContent(String defaultValue) {
        if ( current==null) return defaultValue;
        String t = current.getText();
        if(t.equals("")) t = defaultValue;
        return t;
    }

   /** 
   * Setzt den Inhalt/Text des aktuellen Elements
   * @param text Neuer Inhalt des Elements
   */
    public void setContent(String text) {
        if ( current==null) return;
        current.setText(text);
    }


   /** 
    * Fragt den Inhalt des aktuellen Elements als Integerzahl ab
    * Hat das Element keinen Inhalt wird der defaultValue zurückgegeben.
    * @param defaultValue Standardwert
    * @return Inhalt des Elements
    */      public int getIntContent(int defaultValue) {
        if ( current==null) return defaultValue;
        try{
            int i = Integer.parseInt(current.getText());
            return i;
        } catch(Exception e) { return defaultValue; }
    }

   /** 
    * Fragt den Inhalt des aktuellen Elements als Integerzahl ab
    * @return Inhalt des Elements
    */    
    public int getIntContent() {
        if ( current==null) return 0;
        try{
            int i = Integer.parseInt(current.getText());
            return i;
        } catch(Exception e) { return 0; }
    }

   /** 
   * Setzt den Inhalt des aktuellen Elements
   * @param value Neuer Inhalt des Elements
   */
    public void setIntContent(int value) {
        if ( current==null) return;
        current.setText(""+value);
    }

 
  /** 
    * Fragt den Inhalt des aktuellen Elements als Floatzahl ab
    * Hat das Element keinen Inhalt wird der defaultValue zurückgegeben.
    * @param defaultValue Standardwert
    * @return Inhalt des Elements
    */    
      public float getFloatContent(float defaultValue) {
        if ( current==null) return defaultValue;
        try{
            float i = Float.parseFloat(current.getText());
            return i;
        } catch(Exception e) { return defaultValue; }
    }

  /** 
    * Fragt den Inhalt des aktuellen Elements als Floatzahl ab
    * @return Inhalt des Elements
    */     
    public float getFloatContent() {
        if ( current==null) return 0;
        try{
            float i = Float.parseFloat(current.getText());
            return i;
        } catch(Exception e) { return 0; }
    }

   /** 
   * Setzt den Inhalt des aktuellen Elements
   * @param value Neuer Inhalt des Elements
   */
       public void setFloatContent(float value) {
        if ( current==null) return;
        current.setText(""+value);
    }

/** 
    * Fragt den Inhalt des aktuellen Elements als Doublezahl ab
    * Hat das Element keinen Inhalt wird der defaultValue zurückgegeben.
    * @param defaultValue Standardwert
    * @return Inhalt des Elements
    */    
      public double getDoubleContent(double defaultValue) {
        if ( current==null) return defaultValue;
        try{
            double i = Double.parseDouble(current.getText());
            return i;
        } catch(Exception e) { return defaultValue; }
    }

  /** 
    * Fragt den Inhalt des aktuellen Elements als Doublezahl ab
    * @return Inhalt des Elements
    */  
     public double getDoubleContent() {
        if ( current==null) return 0;
        try{
            double i = Double.parseDouble(current.getText());
            return i;
        } catch(Exception e) { return 0; }
    }
  
   /** 
   * Setzt den Inhalt des aktuellen Elements
   * @param value Neuer Inhalt des Elements
   */
    public void setDoubleContent(double value) {
        if ( current==null) return;
        current.setText(""+value);
    }
  
  
    // ----------------------------------------------- XML-Struktur aufbauen ------------------------------------------------
    /** Erzeuge neues Element nach der aktuellen Position und setze dieses als aktuelles Element
     * @param name Name des neuen Elements
     * @return neues Element als XML-Objekt
     */
    public XML addChild(String name) {
        Element e = new Element(name);
        if(current == null){    // man ist auf Root-Ebene
            doc.setRootElement(e);

        }
        else {
            current.addContent(e);
        } // end of if-else
        return new XML(doc, e);
    }

    /** 
    * liefert das aktuelle Element als jdom-Element-Objekt
    * @return aktuelles Element
    */
    private Element getCurrent() {
        return current;
    }

    /**
   * löscht ein Kind des aktuellen Knotens.
   * Ist kid kein Kind des aktuellen Elements passiert gar nichts.
   * @param kid XML-Objekt des Kindes
   */
    public void removeChild(XML kid) {
        if (current == null) return;
        Element e = kid.getCurrent();
        int index = current.indexOf(e);
        if(index >= 0) { current.removeContent(e);}
    }

}

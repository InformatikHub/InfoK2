package imp;

 

/**
 * Die Klasse Table vereinfacht den Zugriff auf CSV-Dateien.
 * Die Klassen Table und TableRow ermöglichen einen einfachen Zugriff auf tabellenbasierte
 * Dokumente. 
 * 
 * @author Thomas Schaller
 * @version 1.0 vom 01.02.2019
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

public class Table  
{
    // Standardtrennzeichen für Spalten
    private static final char DEFAULT_SEPARATOR = ';';
    // Standardmarkierung für Texte
    private static final char DEFAULT_QUOTE = '"';
    // Standardtrennzeichen für Dezimalzahlen
    private static final char DEFAULT_COMMA = ',';

    // mögliche Spaltentypen
    private static final String UNKNOWN ="UNKOWN";
    private static final String INT = "INTEGER";
    private static final String DOUBLE = "DOUBLE";
    private static final String FLOAT = "FLOAT";
  
    // interne Verwaltung des Dokuments als JDOM-Document-Objekt
    private Document doc;
    // Verweis auf Element für Kopfzeile
    private Element header;
    // Ende Attribute

    /** 
     * Erzeugt leeres Tabellen-Dokument.
     */
    public Table() {
        this.doc = new Document();
        doc.setRootElement(new Element("CSV-Data"));
        this.header = new Element("Header");
        doc.getRootElement().addContent(header);
    }


    /** 
     * Erzeugt Tabellen-Dokument aus einer CSV-Datei.
     * Liest den Inhalt einer Datei und erstellt ein Tabellenobjekt mit seinen Werten. 
     * Wenn die Datei eine Kopfzeile enthält, fügen Sie "header" in den Parameter options ein. Wenn die Datei keine Kopfzeile hat, 
     * dann lassen Sie einfach die Option "header" weg.
     * @param filename Dateiname der CSV-Datei.
     * @param options Geben Sie hier "header" an, wenn die Datei eine Kopfzeile enthält.
     * @param separator Trennzeichen für Spalten (meist ';' oder ',' oder '\t' für Tab)
     * @param quote Kennung für Texte (meist '"'). 
     */
    public Table(String filename, String options, char separator, char quote) {
        loadCSV(filename, options, separator, quote);
    }

    /** 
     * Erzeugt Tabellen-Dokument aus einer CSV-Datei.
     * Liest den Inhalt einer Datei und erstellt ein Tabellenobjekt mit seinen Werten (Separator = ';', Kennung für Text = '"'). 
     * Wenn die Datei eine Kopfzeile enthält, fügen Sie "header" in den Parameter options ein. Wenn die Datei keine Kopfzeile hat, 
     * dann lassen Sie einfach die Option "header" weg.
     * @param filename Dateiname der CSV-Datei.
     * @param options Geben Sie hier "header" an, wenn die Datei eine Kopfzeile enthält.
     */
    public Table(String filename, String options) {
        loadCSV(filename, options);
    }

    /** 
     * Erzeugt Tabellen-Dokument aus einer CSV-Datei.
     * Liest den Inhalt einer Datei ohne Kopfzeile und erstellt ein Tabellenobjekt mit seinen Werten (Separator = ';', Kennung für Text = '"'). 
     * @param filename Dateiname der CSV-Datei.
     */
    public Table(String filename) {
        loadCSV(filename);
    }

    // Anfang Methoden
    /** 
     * Liest den Inhalt einer CSV-Datei ohne Kopfzeile (Separator = ';', Kennung für Text = '"'). 
     * @param filename Dateiname der CSV-Datei.
     */
    public void loadCSV(String filename) {
        loadCSV(filename, "");
    }

    /** 
     * Liest den Inhalt einer CSV-Datei  (Separator = ';', Kennung für Text = '"'). 
     * Wenn die Datei eine Kopfzeile enthält, fügen Sie "header" in den Parameter options ein. Wenn die Datei keine Kopfzeile hat, 
     * dann lassen Sie einfach die Option "header" weg.
     * @param filename Dateiname der CSV-Datei.
     * @param options Geben Sie hier "header" an, wenn die Datei eine Kopfzeile enthält.
     */
    public void loadCSV(String filename, String options) {
        loadCSV(filename, options, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    /** 
     * Liest den Inhalt einer CSV-Datei.
     * Wenn die Datei eine Kopfzeile enthält, fügen Sie "header" in den Parameter options ein. Wenn die Datei keine Kopfzeile hat, 
     * dann lassen Sie einfach die Option "header" weg.
     * @param filename Dateiname der CSV-Datei.
     * @param options Geben Sie hier "header" an, wenn die Datei eine Kopfzeile enthält.
     * @param separator Trennzeichen für Spalten (meist ';' oder ',' oder '\t' für Tab)
     * @param quote Kennung für Texte (meist '"'). 
     */
  public void loadCSV(String filename, String options, char separator, char quote) {
        doc = new Document(); 
        doc.setRootElement(new Element("CSV-Data"));
        header = new Element("Header");
        doc.getRootElement().addContent(header);
        try {
            File f = new File(filename);
            Scanner scanner = new Scanner(new File(filename));
            if(options.toLowerCase().contains("header") && scanner.hasNext()) {
                List<String> entries = parseLine(scanner.nextLine(), separator, quote);
                int i= 0;
                for(String s : entries) {
                    Element entry = new Element("Column");
                    header.addContent(entry);
                    entry.setText(s);
                    entry.setAttribute("type", "unknown");
                    i++;
                }
            }

            List<Element> cols = header.getChildren();

            while (scanner.hasNext()) {
                Element line = new Element("Row");
                doc.getRootElement().addContent(line);
                List<String> entries = parseLine(scanner.nextLine(), separator, quote);
                int i= 0;

                for(String s : entries) {

                    if(i==cols.size()) {
                        Element entry = new Element("Column");
                        entry.setAttribute("type", "unknown");
                        header.addContent(entry);
                        cols = header.getChildren();
                    }

                    Element entry = new Element("Entry");
                    entry.setText(s);
                    line.addContent(entry);
                    i++;
                }
            }
            scanner.close();

        } catch (Exception e) { 
            System.out.println("Fehler beim Lesen der CSV-Datei");
        }
    }

    /** 
     * Speichert das aktuelle Dokument als CSV-Datei ohne Kopfzeile (Separator = ';', Kennung für Text = '"'). 
     * @param filename Dateiname der CSV-Datei.
     */
    public void saveCSV(String filename) {
        saveCSV(filename, "");
    }

    /** 
     * Speichert das aktuelle Dokument als CSV-Datei (Separator = ';', Kennung für Text = '"'). 
     * Wenn die Datei eine Kopfzeile enthalten, fügen Sie "header" in den Parameter options ein. Wenn die Datei keine Kopfzeile haben soll, 
     * dann lassen Sie einfach die Option "header" weg.
     * @param options Geben Sie hier "header" an, wenn die Datei eine Kopfzeile haben soll.
     * @param filename Dateiname der CSV-Datei.
   */
    public void saveCSV(String filename, String options) {
        saveCSV(filename, options, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    /** 
     * Speichert das aktuelle Dokument als CSV-Datei. 
     * Wenn die Datei eine Kopfzeile enthalten, fügen Sie "header" in den Parameter options ein. Wenn die Datei keine Kopfzeile haben soll, 
     * dann lassen Sie einfach die Option "header" weg.
     * @param options Geben Sie hier "header" an, wenn die Datei eine Kopfzeile haben soll.
     * @param filename Dateiname der CSV-Datei.
     * @param separator Trennzeichen für Spalten (meist ';' oder ',' oder '\t' für Tab)
     * @param quote Kennung für Texte (meist '"'). 
     */    
    public void saveCSV(String filename, String options, char separator, char quote){
        try{
            File f = new File(filename);
            PrintStream outputFile = new PrintStream (f);
            System.out.println("Speicher in : "+f.getAbsolutePath());
            List<Element> columns = header.getChildren();
            String sq = ""+quote;
            String ss = ""+separator;
            if(quote =='"') sq = "\"";
            if(separator =='"') ss = "\"";

            if(options.toLowerCase().contains("header")) {
                String h = "";
                for(Element c : columns) {
                    h += ss + sq + c.getText()+sq;
                }
                outputFile.println(h.substring(1));
            }
            for(int i = 0; i<getRowCount(); i++) {
                String l = "";
                for(String s: getStringRow(i)) {

                    if(s.contains(""+separator)) {
                        if(quote == '"' && s.contains("\"")) {
                            s = s.replace("\"","\"\"");
                        }
                        l += ss + sq + s+sq;
                    } else {
                        l += ss+s;
                    }

                }
                outputFile.println(l.substring(1));
            }
            outputFile.close();
        }
        catch(Exception e) {
            System.out.println("Fehler beim Schreiben der Datei");
        }
    }

    /** Speichert die Tabelle als XML-Dokument.
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
  
    /** HIlfsfunktion für die Analyse einer Dateizeile
      * @param cvsLine Zeile aus der Datei
      * @return Liste von String für die einzelnen Spalten
      */
    private List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    /** HIlfsfunktion für die Analyse einer Dateizeile
      * @param cvsLine Zeile aus der Datei
      * @param sparator Trennzeichen für die Spalten 
      * @return Liste von String für die einzelnen Spalten
      */
    private List<String> parseLine(String cvsLine, char separator) {
        return parseLine(cvsLine, separator, DEFAULT_QUOTE);
    }

    /** HIlfsfunktion für die Analyse einer Dateizeile
      * @param cvsLine Zeile aus der Datei
      * @param sparator Trennzeichen für die Spalten 
      * @param customQuote Kennung für Strings
      * @return Liste von String für die einzelnen Spalten
      */
    private List<String> parseLine(String cvsLine, char separator, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }
    
        //ggf. Default-Value laden
        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separator == ' ') {
            separator = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {   // aktueller Text ist in Quotes eingeschlossen
                startCollectChar = true;

                if (ch == customQuote) {  // Quotes werden beendet, aber Achtung bei "" => Metazeichen
                    inQuotes = false;
                    if (ch == '\"') {
                        doubleQuotesInColumn = true;
                    }

                } else {

                    if (ch == '\"' && !doubleQuotesInColumn) {
                        doubleQuotesInColumn = true;
                    } else {
                        curVal.append(ch);
                        doubleQuotesInColumn = false;
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (ch == '\"'){
                        if(doubleQuotesInColumn) {
                            curVal.append('"');
                            doubleQuotesInColumn = false;
                        } else doubleQuotesInColumn = true;


                    } 
                }
                else {
                    doubleQuotesInColumn = false;
                    if (ch == separator) {

                        result.add(curVal.toString());

                        curVal = new StringBuffer();
                        startCollectChar = false;

                    } else if (ch == '\r') {
                        //ignore LF characters
                        continue;
                    } else if (ch == '\n') {
                        //the end, break!
                        break;
                    } else {
                        curVal.append(ch);
                    }
                }
            }

        }
        result.add(curVal.toString());
        return result;
    }
  
    /** 
     *  Sucht die Nummer einer durch Namen gegebenen Spalte.
     *  @param name Name der Spalte
     *  @return Nummer der Spalte
     */
      
    private int findColumnNumber(String name) {
        List<Element> columns =  header.getChildren();
        int i = 0;
        for(Element c : columns) {
            if (c.getText().toLowerCase().equals(name.toLowerCase())) {
                return i;

            }
            i++;
        }
        return -1;
    }
  
    /**
   * Fügt eine neue Spalte am Ende der Tabelle an.
   */
    public void addColumn() {
        Element entry = new Element("Column");
        entry.setAttribute("type", Table.UNKNOWN);
        header.addContent(entry); 
    }
  
    /** 
   * Fügt eine neue Spalte am Ende der Tabelle an und benennt sie.
   * @param title Bezeichnung der Spalte
   */
    public void addColumn(String title) {
        addColumn();
        Element nc = ((List<Element>)(header.getChildren())).get(header.getChildren().size()-1);
        nc.setText(title);
    }  

    /**
   * Fügt eine neue Spalte am Ende der Tabelle an und benennt und typisiert sie.
   * @param title Bezeichnung der Spalte
   * @param type Typ der Spalte (UNKNOWN, DOUBLE, INTEGER, FLOAT)
   */
    public void addColumn(String title, String type) {
        addColumn(title);
        Element nc = ((List<Element>)(header.getChildren())).get(header.getChildren().size()-1);
        nc.setAttribute("type", type);
    }  
  
    /**
   * Löscht eine Spalte.
   * @param i Nummer der Spalte.
   */
    public void removeColumn(int i) {
        List<Element> lines = doc.getRootElement().getChildren();
        for(Element l : lines) {
            if(l.getChildren().size()>i) l.removeContent(i);
        }
    }
  
   /**
   * Löscht eine Spalte
   * @param name Name der Spalte
   */
    public void removeColumn(String name) {
        try{
            removeColumn(findColumnNumber(name));
        } catch(Exception e) { System.out.println("Unbekannter Spaltenname");}
    }
  
  /**
   * Liefert die Anzahl der Spalten in der Tabelle
   * @return Anzahl der Spalten
   */
    public int getColumnCount() {
        return header.getChildren().size();
    }
  
  /**
   * Liefert die Anzahl der Zeilen in der Tabelle
   * @return Anzahl der Zeilen
   */
    public int getRowCount() {
        return doc.getRootElement().getChildren().size()-1;
    }
  
  /** 
   * Löscht alle Zeilen der Tabelle.
   * Die Spaltenüberschriften und Typen bleiben erhalten.
   */
    public void clearRows() {  
        doc.getRootElement().removeChildren("Row");
    }
  
  /**
   * Fügt eine neue Zeile an das Ende der Tabelle an.
   * @return ein TableRow-Objekt für diese neue Zeile
   */
    public TableRow addRow() {
        Element row = new Element("Row");
        doc.getRootElement().addContent(row);
        return new TableRow(doc, row);
    }
  
  /**
   * Löscht eine Zeile
   * @param i Nummer der Zeile
   */
    public void removeRow(int i) {
        if(i<getRowCount()) {
            doc.getRootElement().removeContent(i);
        }
    }
  
  /** 
   * Liefert eine Zeile der Tabelle
   * @param i Nummer der Zeile
   * @return TableRow-Objekt für diese Zeile
   */
    public TableRow getRow(int i) {
        if(i<getRowCount()) {
            List<Element> rows = doc.getRootElement().getChildren();
            return new TableRow(doc, rows.get(i+1));
        }
        return null;
    }
  
  /**
   * Liefert die ganze Tabelle als Array von TableRow-Objekten
   * @return Array von TableRow-Objekten 
   */
    public TableRow[] rows() {
        TableRow[] rows = new TableRow[getRowCount()];
        for(int i = 0; i < getRowCount(); i++) {
            rows[i] = getRow(i);
        }
        return rows;
    }

  /**
   * Liefert den Wert einer Zelle als Integer-Zahl
   * @param row Zeilennummer
   * @param column Spaltennummer
   * @return Wert der Zelle
   */
    public int getInt(int row, int column) {
        return getRow(row).getInt(column);
    }

  /**
   * Liefert den Wert einer Zelle als Integer-Zahl
   * @param row Zeilennummer
   * @param name Name der Spalte
   * @return Wert der Zelle
   */
    public int getInt(int row, String name) {
        return getRow(row).getInt(name);
    }

  /**
   * Setzt den Wert einer Zelle als Integer-Zahl
   * @param row Zeilennummer
   * @param column Spaltennummer
   * @param value neuer Wert der Zelle
   */
    public void setInt(int row, int column,int value) {
        getRow(row).setInt(column, value);
    }

  /**
   * Setzt den Wert einer Zelle als Integer-Zahl
   * @param row Zeilennummer
   * @param name Name der Spalte
   * @param value neuer Wert der Zelle
   */
    public void setInt(int row, String name, int value) {
        getRow(row).setInt(name, value);
    }

  /**
   * Liefert alle Werte einer Zeile als Integer-Array.
   * @param row Nummer der Zeile
   * @return int-Array, dass alle Werte der Zeile enthält
   */
    public int[] getIntRow(int row) {
        try{
            TableRow trow = getRow(row);
            int anz = getColumnCount();
            int[] r = new int[anz];
            for(int i=0; i<anz; i++) {
                r[i] = trow.getInt(i);
            }
            return r;
        } catch (Exception e) {
            return null;
        }
    }

  /**
   * Liefert alle Werte einer Spalte als Integer-Array.
   * @param column Nummer der Spalte
   * @return int-Array, dass alle Werte der Spalte enthält
   */
    public int[] getIntColumn(int column) {
        try{
            int anz = getRowCount();
            int[] r = new int[anz];
            for(int i=0; i<anz; i++) {
                r[i] = getInt(i, column);
            }
            return r;
        } catch (Exception e) {
            return null;
        }
    }    

  /**
   * Liefert alle Werte einer Spalte als Integer-Array.
   * @param name Name der Spalte
   * @return int-Array, dass alle Werte der Spalte enthält
   */
    public int[] getIntColumn(String name) {
        return getIntColumn(findColumnNumber(name));
    }    

  
  /**
   * Liefert den Wert einer Zelle als Float-Zahl
   * @param row Zeilennummer
   * @param column Spaltennummer
   * @return Wert der Zelle
   */
    public float getFloat(int row, int column) {
        return getRow(row).getFloat(column);
    }

  /**
   * Liefert den Wert einer Zelle als Float-Zahl
   * @param row Zeilennummer
   * @param name Name der Spalte
   * @return Wert der Zelle
   */
    public float getFloat(int row, String name) {
        return getRow(row).getFloat(name);
    }

  /**
   * Setzt den Wert einer Zelle als Float-Zahl
   * @param row Zeilennummer
   * @param column Spaltennummer
   * @param value neuer Wert der Zelle
   */
    public void setFloat(int row, int column,float value) {
        getRow(row).setFloat(column, value);
    }

  /**
   * Setzt den Wert einer Zelle als Float-Zahl
   * @param row Zeilennummer
   * @param name Name der Spalte
   * @param value neuer Wert der Zelle
   */
    public void setFloat(int row, String name, float value) {
        getRow(row).setFloat(name, value);
    }

  /**
   * Liefert alle Werte einer Zeile als Float-Array.
   * @param row Nummer der Zeile
   * @return int-Array, dass alle Werte der Zeile enthält
   */
    public float[] getFloatRow(int row) {
        try{
            TableRow trow = getRow(row);
            int anz = getColumnCount();
            float[] r = new float[anz];
            for(int i=0; i<anz; i++) {
                r[i] = trow.getFloat(i);
            }
            return r;
        } catch (Exception e) {
            return null;
        }
    }

  /**
   * Liefert alle Werte einer Spalte als Float-Array.
   * @param column Nummer der Spalte
   * @return int-Array, dass alle Werte der Spalte enthält
   */
    public float[] getFloatColumn(int column) {
        try{
            int anz = getRowCount();
            float[] r = new float[anz];
            for(int i=0; i<anz; i++) {
                r[i] = getFloat(i, column);
            }
            return r;
        } catch (Exception e) {
            return null;
        }
    }        

  /**
   * Liefert alle Werte einer Spalte als Float-Array.
   * @param name Name der Spalte
   * @return int-Array, dass alle Werte der Spalte enthält
   */
    public float[] getFloatColumn(String name) {
        return getFloatColumn(findColumnNumber(name));
    }    

  /**
   * Liefert den Wert einer Zelle als Double-Zahl
   * @param row Zeilennummer
   * @param column Spaltennummer
   * @return Wert der Zelle
   */
    public double getDouble(int row, int column) {
        return getRow(row).getDouble(column);
    }

  /**
   * Liefert den Wert einer Zelle als Double-Zahl
   * @param row Zeilennummer
   * @param name Name der Spalte
   * @return Wert der Zelle
   */
    public double getDouble(int row, String name) {
        return getRow(row).getDouble(name);
    }

  /**
   * Setzt den Wert einer Zelle als Double-Zahl
   * @param row Zeilennummer
   * @param column Spaltennummer
   * @param value neuer Wert der Zelle
   */
    public void setDouble(int row, int column,double value) {
        getRow(row).setDouble(column, value);
    }

  /**
   * Setzt den Wert einer Zelle als Double-Zahl
   * @param row Zeilennummer
   * @param name Name der Spalte
   * @param value neuer Wert der Zelle
   */
    public void setDouble(int row, String name, double value) {
        getRow(row).setDouble(name, value);
    }

  /**
   * Liefert alle Werte einer Spalte als Double-Array.
   * @param row Nummer der Spalte
   * @return int-Array, dass alle Werte der Spalte enthält
   */
    public double[] getDoubleRow(int row) {
        try{
            TableRow trow = getRow(row);
            int anz = getColumnCount();
            double[] r = new double[anz];
            for(int i=0; i<anz; i++) {
                r[i] = trow.getDouble(i);
            }
            return r;
        } catch (Exception e) {
            return null;
        }
    }

  /**
   * Liefert alle Werte einer Spalte als Double-Array.
   * @param column Nummer der Spalte
   * @return int-Array, dass alle Werte der Spalte enthält
   */
    public double[] getDoubleColumn(int column) {
        try{
            int anz = getRowCount();
            double[] r = new double[anz];
            for(int i=0; i<anz; i++) {
                r[i] = getDouble(i, column);
            }
            return r;
        } catch (Exception e) {
            return null;
        }
    }    

  /**
   * Liefert alle Werte einer Spalte als Double-Array.
   * @param name Name der Spalte
   * @return int-Array, dass alle Werte der Spalte enthält
   */
    public double[] getDoubleColumn(String name) {
        return getDoubleColumn(findColumnNumber(name));
    }    

  /**
   * Liefert den Wert einer Zelle als String
   * @param row Zeilennummer
   * @param column Spaltennummer
   * @return Wert der Zelle
   */
    public String getString(int row, int column) {
        return getRow(row).getString(column);
    }

  /**
   * Liefert den Wert einer Zelle als String
   * @param row Zeilennummer
   * @param name Name der Spalte
   * @return Wert der Zelle
   */
    public String getString(int row, String name) {
        return getRow(row).getString(name);
    }

  /**
   * Setzt den Wert einer Zelle als String
   * @param row Zeilennummer
   * @param column Spaltennummer
   * @param text neuer Wert der Zelle
   */
    public void setString(int row, int column,String text) {
        getRow(row).setString(column, text);
    }

  /**
   * Setzt den Wert einer Zelle als String
   * @param row Zeilennummer
   * @param name Name der Spalte
   * @param text neuer Wert der Zelle
   */
    public void setString(int row, String name, String text) {
        getRow(row).setString(name, text);
    }

  /**
   * Liefert alle Werte einer Spalte als String-Array.
   * @param row Nummer der Spalte
   * @return int-Array, dass alle Werte der Spalte enthält
   */
    public String[] getStringRow(int row) {
        try{
            TableRow trow = getRow(row);
            int anz = getColumnCount();
            String[] r = new String[anz];
            for(int i=0; i<anz; i++) {
                r[i] = trow.getString(i);
            }
            return r;
        } catch (Exception e) {
            return null;
        }
    }

  /**
   * Liefert alle Werte einer Spalte als String-Array.
   * @param column Nummer der Spalte
   * @return int-Array, dass alle Werte der Spalte enthält
   */
    public String[] getStringColumn(int column) {
        try{
            int anz = getRowCount();
            String[] r = new String[anz];
            for(int i=0; i<anz; i++) {
                r[i] = getString(i, column);
            }
            return r;
        } catch (Exception e) {
            return null;
        }
    }    

  /**
   * Liefert alle Werte einer Spalte als String-Array.
   * @param name Name der Spalte
   * @return int-Array, dass alle Werte der Spalte enthält
   */
    public String[] getStringColumn(String name) {
        return getStringColumn(findColumnNumber(name));
    }    

  
  /**
   * Sucht nach einem bestimmtem Wert in einer Zeile.
   * @param value Wert der gesucht werden soll
   * @param column Nummer der Spalte, die durchsucht werden soll
   * @return TableRow-Objekt der Zeile, wenn der Wert gefunden wurde, sonst null
   */
    public TableRow findRow(String value, int column) {
        for(int i=0; i<getRowCount(); i++) {
            if(getString(i,column).equals(value)){
                return getRow(i);
            }
        }
        return null;
    }

  /**
   * Sucht nach einem bestimmtem Wert in einer Zeile.
   * @param value Wert der gesucht werden soll
   * @param name Name der Spalte, die durchsucht werden soll
   * @return TableRow-Objekt der Zeile, wenn der Wert gefunden wurde, sonst null
   */
    public TableRow findRow(String value, String name) {
        return findRow(value, findColumnNumber(name));
    }
  
  /** 
   * Kürzt alle Einträge der Tabelle um unnötige Leerzeichen am Anfang oder Ende
   */
    public void trim() {
        for(int y=0; y<getRowCount(); y++) {
            for (int x =0; x<getColumnCount(); x++) {
                setString(y,x,getString(y,x).trim());
            }
        }
    }
}

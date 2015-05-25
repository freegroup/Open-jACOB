/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/

package de.tif.jacob.util.doclet;

//import com.sun.javadoc.*;

/** Diese Klasse erzeugt eine XML Ausgabedatei in der alle
 * mit dem Javadoc Tag @todo ausgezeichneten Texte
 * aufgef&uuml;hrt sind.
 * @author Christoph Krüger
 * @version 0.9
 * @todo Einbau der Kommandozeilenparameterübergabe
 * @todo Verarbeitung von HMTL Entities ermöglichen
 */
public class ListTodos 
{
    /** Standardmethode f&uuml;r Doclets. Schreibt die XML-Datei
     * ins lokale Verzeichnis
     * @param root Das Root-Document f&uuml;r die dokumentierten
     * Klassen
     * @return wahr, wenn alles geklappt hat
     * @todo Verwenden eines Kommandozeilenparameters für die Ausgabedatei
     * @todo Verwenden eines Kommandozeilenparameters für das Stylesheet
     */  
  /*
    public static boolean start(RootDoc root)
    {
        String tagName = "todo";

        StringBuffer sbuff = new StringBuffer("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<?xml-stylesheet type=\"text/xsl\" href=\"todo.xsl\"?>\n<control>");
        sbuff.append(writeContents(root.classes(), tagName));
        sbuff.append("</control>");
        // Schreiben der XML-Datei auf die Platte
        try
        {
            FileOutputStream fos = new FileOutputStream("todo.xml");
            PrintWriter pw = new PrintWriter(fos,true);
            pw.print(sbuff.toString());
            pw.close();
            fos.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }
*/
    /** Interne Methode, die das XML durch einfache String-Operationen
     * erzeugt
     * @param classes Array der Klassen, die kommentiert sind
     * @param tagName Name des Tags, der die Todos einleitet
     * @return String mit Todos in XML-Auszeichnung
     * @todo Ausgabe des Packagenamens
     */    
  /*
    private static String writeContents(ClassDoc[] classes, String tagName)
    {
        StringBuffer sbuff = new StringBuffer();
        boolean found=false;
        System.out.println("called...............");
        for (int i=0; i < classes.length; i++)
        {
            // Klassennamen immer hinschreiben
            sbuff.append("<class name=\"" + classes[i].name() + "\">\n");
            // Todo Tags hier finden
            Tag[] tags = classes[i].tags(tagName);
            // ... und ausgeben
            if (tags.length > 0)
            {
                    for (int k=0; k < tags.length; k++)
                    {
                            sbuff.append("<todo>" + tags[k].text() + "</todo>\n");
                    }
            }

            // jetzt durch die Methoden gehen
            MethodDoc[] methods = classes[i].methods();
            for (int j=0; j < methods.length; j++)
            {
                // Methodennamen ausgeben
                sbuff.append("<method name=\"" + methods[j].name() + "\">\n");
                tags = methods[j].tags(tagName);
                if (tags.length > 0)
                {
                    for (int k=0; k < tags.length; k++)
                    {
                        sbuff.append("<todo>" + tags[k].text() + "</todo>\n");
                        found=true;
                    }
                }
                sbuff.append("</method>\n");
            }
            sbuff.append("</class>\n");
        }
        if(found)
          return sbuff.toString();
        return "";
    }
    */
}


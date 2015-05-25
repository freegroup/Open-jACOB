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
package de.tif.jacob.util.xml;

import org.xml.sax.helpers.AttributesImpl;

/**
 * Diese Klasse <code>XMLAttributes</code> implementiert das einfache 
 * Setzen und Auslesen von Attributen eines XML-Elements. Diese
 * Klasse wird von {@link XMLWriter} zum Schreiben benutzt. Es soll die jeweilige
 * benutzte SAX-Version verschleiern, da es verschiedene Implementierungen
 * mit dem Namen dieser Klasse gibt.
 * <p>
 * Weiterhin soll diese Klasse eine einfach Migration von 
 * <code>java.Util.jar.Attributes</code> nach <code>org.xml.sax.Attributes</code>
 * ermöglichen.
**/
public class XMLAttributes extends AttributesImpl
    {
  static public final transient String RCS_ID = "$Id: XMLAttributes.java,v 1.1 2007/01/19 09:50:47 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

    /**
     * Konstruktur, erzeugt eine Menge von XML-Attributen, die 
     * für ein XML-Element gesetzt werden kann.
    **/
    public XMLAttributes()
        {
        super();
        }

    /**
     * Konstruktur, erzeugt eine Menge von XML-Attributen, die 
     * für ein XML-Element gesetzt werden kann.
     *
     * @deprecated Die Anzahl der Attributmenge braucht nicht mehr angegeben zu werden.
    **/
    public XMLAttributes( int size )
        {
        super();
        }

    /**
     * Ruft {@link #add(String, String} auf.
     *
     * @deprecated Bitte {@link #add(String, String} aufrufen.
    **/
    public void putValue( String key, String value )
        {
        add( key, value );
        }

    /**
     * Fügt ein Attribute in diese Menge hinzu.
    **/
    public void add( String key, String value )
        {
        addAttribute( /* uri       */ null, 
                      /* localName */ null, 
                      /* qName     */ key, 
                      /* type      */ "CDATA", 
                      value );
        }
    }  //End of class

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

import java.io.IOException;
import java.util.jar.Attributes;

import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;


/**
 * Diese Klasse implementiert das Handler-Interface für SAX. Wird beim 
 * Einlesen bei einem XML Parser eine Instanz aus dieser Klasse als
 * Handler gesetzt, so werden Methoden aus dieser Klasse bei bestimmten
 * Ereignissen aufgerufen.
 * <p>
 * Der eingelesene XML Datenstrom wird an ein Objekt aus {@link XMLWriter}
 * weitergegeben und somit an die Ausgabe 1 zu 1 kopiert.
**/
public class XMLSAXCopyToHandler implements DocumentHandler 
    {
  static public final transient String RCS_ID = "$Id: XMLSAXCopyToHandler.java,v 1.1 2007/01/19 09:50:47 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

    /**
     * Der XML Strom wird in dieses Ziel-Objekt geschrieben
    **/
    private XMLWriter myWriter;

    /**
     * Flag, das Schreiben auf den Ausgabestrom kann verhindert werden.
    **/
    private boolean doWriteToOutput;

    /**
     * Wird beim Schreiben eine Exception geworfen, so wird diese hier 
     * gemerkt.
    **/
    private IOException myException;

    /**
     * Ereuge ein Handler Objekt für den SAX Parser.
     * Das Ergebis des Parsens in den 
     * Ausgabestrom <code>writer</code> geschrieben.
    **/
    public XMLSAXCopyToHandler( XMLWriter writer )
        {
        this.myWriter = writer;
        }


    /**
     * Callback,
    **/
    public void startDocument()
        {
        myException     = null;
        doWriteToOutput = true;
        }

    /**
     * Callback
    **/
    public void endDocument()
        {
        }


    /**
     * Callback
    **/
    public void setDocumentLocator( Locator loc )
        {
        }

    /**
     * Callback
    **/
    public void startElement( String name, AttributeList attributes )
        {
        if ( isWritingOff() )
            return;
            
        try
            {
            myWriter.elementBegin( name, copyFrom( attributes ) );
            }

        catch ( IOException e )
            {
            myException = e;
            }
        }

    /**
     * Konvertiert die Liste der Attribute <code>attributes</code> aus der
     * Klasse <code>AttributeList</code> in eine Liste vom Typ
     * <code>Attributes</code>.
    **/
    private Attributes copyFrom( AttributeList attributes )
        {
        Attributes a = null;
        if ( attributes != null  &&  attributes.getLength() > 0 )
            {
            a = new Attributes( attributes.getLength() );
            for ( int i = 0; i < attributes.getLength(); i++ )
                a.putValue( attributes.getName( i ), attributes.getValue( i ) );
            }

        return a;
        }

    /**
     * Callback
    **/
    public void endElement( String name )
        {
        if ( isWritingOff()  )
            return;
            
        try
            {
            myWriter.elementEnd();  
            }

        catch ( IOException e )
            {
            myException = e;
            }
        }

    /**
     * Callback, der Wert eines Elements wird empfangen.
    **/
    public void characters( char ch[], int start, int length )
        {
        if ( isWritingOff()  )
            return;
            
        try
            {
            myWriter.elementValue( new String( ch, start, length ) );
            }

        catch ( IOException e )
            {
            myException = e;
            }
        }


    /**
     * Callback
    **/
    public void ignorableWhitespace( char ch[], int start, int len )
        {
        }

    /**
     * Callback
    **/
    public void processingInstruction( String target, String data )
        {
        }


    /**
     * Liefert die beim Lesen geworfene Exception zurück.
    **/
    public void getException() throws IOException
        {
        if ( myException != null )
            throw myException;
        }

    /**
     * Liefert TRUE, falls der Engabestrom nicht mehr auf
     * den Ausgabestrom geschrieben werden soll.
    **/
    protected boolean isWritingOff()
        {
        return myException != null  ||  ! doWriteToOutput;
        }

    /**
     * Ermöglicht das Schreiben oder verhindert es, dass der
     * Eingabestrom kopiert wird.
    **/
    protected void setOutputEnabled( boolean flag )
        {
        doWriteToOutput = flag;
        }
        
    } //END CLASS


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

package de.tif.jacob.core.definition.impl;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

import de.tif.jacob.core.definition.IApplicationProvider;
import de.tif.jacob.core.definition.impl.jad.castor.Jacob;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractApplicationProvider implements IApplicationProvider
{
  static public transient final String RCS_ID = "$Id: AbstractApplicationProvider.java,v 1.1 2007/01/19 09:50:33 freegroup Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";
  
  protected AbstractApplicationProvider()
  {
    // nothing more to do
  }
  
  public static void printFormated(Jacob jacob, OutputStream fos, String encoding) throws Exception
  {
    Document doc = XMLUtils.newDocument();
    Marshaller.marshal(jacob, doc);

    OutputStreamWriter sw = new OutputStreamWriter(fos, encoding);
    org.apache.xml.serialize.OutputFormat outFormat = new org.apache.xml.serialize.OutputFormat();
    outFormat.setIndenting(true);
    outFormat.setIndent(2);
    outFormat.setLineWidth(200);
    outFormat.setEncoding(encoding);
    org.apache.xml.serialize.XMLSerializer xmlser = new org.apache.xml.serialize.XMLSerializer(sw, outFormat);

    xmlser.serialize(doc); //replace your_document with reference to document you want to serialize
  }
  
}

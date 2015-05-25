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

package de.tif.jacob.report.impl.transformer.xsl;

import de.tif.jacob.report.impl.transformer.TransformerProvider;

public final class XSLTransformerProvider extends TransformerProvider
{
  static public transient final String RCS_ID = "$Id: XSLTransformerProvider.java,v 1.2 2009/12/22 03:36:32 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
  public Class getTransformerByMimetype(String mimeType)
  {
    if (PDFTransformer.PDF_MIME_TYPE.equals(mimeType))
      return PDFTransformer.class;

    return null;
  }

}

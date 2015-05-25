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

package de.tif.jacob.report.impl.transformer.classic;

import de.tif.jacob.report.impl.transformer.TransformerProvider;

/**
 * Classic report rendering implementation provider, i.e. provider for before
 * jACOB 2.9 report rendering implementation.
 * 
 * @author Andreas Sonntag
 */
public final class ClassicTransformerProvider extends TransformerProvider
{
  static public transient final String RCS_ID = "$Id: ClassicTransformerProvider.java,v 1.2 2010/02/25 22:02:22 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";

  public Class getTransformerByMimetype(String mimeType)
  {
    if (PlainTextTransformer.TEXT_PLAIN_MIME_TYPE.equals(mimeType))
      return PlainTextTransformer.class;

    if (ExcelTransformer.EXCEL_MIME_TYPE.equals(mimeType))
      return ExcelTransformer.class;
    
    if (TextHtmlTransformer.TEXT_HTML_MIME_TYPE.equals(mimeType))
      return TextHtmlTransformer.class;
    
    // IBIS: If needed make dynamically according all transformers provided by de.tif.jacob.transformer.TransformerFactory
    
    return null;
  }
}

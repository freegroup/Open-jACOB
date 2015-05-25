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

package de.tif.jacob.report.impl.transformer.base;

import de.tif.jacob.report.impl.transformer.TransformerProvider;
import de.tif.jacob.report.impl.transformer.base.XMLTransformer;

public final class BaseTransformerProvider extends TransformerProvider
{
  static public transient final String RCS_ID = "$Id: BaseTransformerProvider.java,v 1.2 2009/12/10 13:44:14 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.2 $";
  
  public Class getTransformerByMimetype(String mimeType)
  {
    // IBIS: Application xml specific XML
    if ("text/xml".equals(mimeType))
      return XMLTransformer.class;
    
    if (FormattedTextTransformer.TEXT_FORMATTED_MIME_TYPE.equals(mimeType))
      return FormattedTextTransformer.class;
    
    return null;
  }

}

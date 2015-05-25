/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Tarragon GmbH
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

package de.tif.jacob.report.impl.transformer.csv;

import de.tif.jacob.report.impl.transformer.TransformerProvider;

public final class CSVTransformerProvider extends TransformerProvider
{
  static public transient final String RCS_ID = "$Id: CSVTransformerProvider.java,v 1.1 2010/02/09 23:33:53 ibissw Exp $";
  static public transient final String RCS_REV = "$Revision: 1.1 $";

  public Class getTransformerByMimetype(String mimeType)
  {
    if (CSVTransformer.CSV_MIME_TYPE.equals(mimeType))
      return CSVTransformer.class;

    return null;
  }

}

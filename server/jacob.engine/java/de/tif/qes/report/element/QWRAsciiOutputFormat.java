/*******************************************************************************
 *    This file is part of jACOB
 *    Copyright (C) 2005-2009 Tarragon GmbH
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

package de.tif.qes.report.element;

import de.tif.jacob.report.impl.transformer.base.FormattedTextTransformer;


/**
 * @author Andreas Sonntag
 */
public final class QWRAsciiOutputFormat extends QWROutputFormat
{
  static public final transient String RCS_ID = "$Id: QWRAsciiOutputFormat.java,v 1.3 2009-12-10 13:32:32 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
  protected String getMimetype()
  {
    return FormattedTextTransformer.TEXT_FORMATTED_MIME_TYPE;
  }
}

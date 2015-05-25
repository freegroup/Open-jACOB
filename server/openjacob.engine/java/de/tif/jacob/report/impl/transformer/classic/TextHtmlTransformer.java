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


/**
 * Classic text html report rendering transformer.
 * 
 * @author Andreas Sonntag
 */
public final class TextHtmlTransformer extends ClassicTransformer
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: TextHtmlTransformer.java,v 1.1 2010/02/25 22:02:23 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.1 $";

  public static final String TEXT_HTML_MIME_TYPE = "text/html";

  protected String getMimeType()
  {
    return TEXT_HTML_MIME_TYPE;
  }
}

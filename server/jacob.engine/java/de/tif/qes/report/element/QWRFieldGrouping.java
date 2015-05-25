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

import de.tif.jacob.report.impl.castor.CastorGroup;


/**
 * @author Andreas Sonntag
 */
public final class QWRFieldGrouping
{
  static public final transient String RCS_ID = "$Id: QWRFieldGrouping.java,v 1.4 2009-12-09 09:46:41 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  
  private final int groupedNbr;
  /**
   * Might be <code>null</code>.
   */
  private final QWRCaption groupHeader, groupFooter;

  public QWRFieldGrouping(int groupedNbr, QWRCaption groupHeader, QWRCaption groupFooter)
  {
    this.groupedNbr = groupedNbr;
    this.groupHeader = groupHeader;
    this.groupFooter = groupFooter;
  }

  /**
   * @return the groupedNbr
   */
  protected int getGroupedNbr()
  {
    return groupedNbr;
  }
  
  protected void toCastor(CastorGroup castor) throws Exception
  {
    if (this.groupHeader != null)
      castor.setHeader(this.groupHeader.toCastor());
    if (this.groupFooter != null)
      castor.setFooter(this.groupFooter.toCastor());
  }
}

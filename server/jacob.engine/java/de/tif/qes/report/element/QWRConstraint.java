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

import de.tif.jacob.report.impl.castor.SearchCriteria;


/**
 * @author Andreas Sonntag
 */
public final class QWRConstraint extends QWRElement
{
  static public final transient String RCS_ID = "$Id: QWRConstraint.java,v 1.2 2009-12-07 03:36:09 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  private final String aliasName;
  private final String fieldName;
  private final String fieldConstraint;
  private final QWRConstraintType type;

  public QWRConstraint(String aliasFieldName, String fieldConstraint, QWRConstraintType type)
  {
    String[] split = splitAliasFieldName(aliasFieldName);
    this.aliasName = split[0];
    this.fieldName = split[1];
    this.fieldConstraint = adjust(fieldConstraint, type);
    this.type = type;
  }
  
  private String adjust(String fieldConstraint, QWRConstraintType type)
  {
    // Convert "|Open|Open-1|Open-2|Open-3|Open-4|Escalated|Verify Solution"
    // to "null|Open|Open-1|Open-2|Open-3|Open-4|Escalated|Verify Solution"
    if (fieldConstraint.startsWith("|") && this.type == QWRConstraintType.NOKEY)
    {
      return "null" + fieldConstraint.substring(1);
    }
    return fieldConstraint;
  }
  
  protected SearchCriteria toCastor()
  {
    SearchCriteria castor = new SearchCriteria();
    castor.setTableAlias(this.aliasName);
    castor.setField(this.fieldName);
    castor.setValue(this.fieldConstraint);
    type.toCastor(castor);
    return castor;
  }
  
  protected boolean isSkipped()
  {
    // Quintus enters constraints like:
    //
    // DEFINE CONSTRAINT ON "call.callstatus"
    // "Or..."
    // TYPE NONKEY
    // ;
    //
    // for each constraint of enumeration lists like "Open|Open-1|Open-2|Open-3|Open-4|Escalated|Verify Solution"
    //
    if ("Or...".equalsIgnoreCase(this.fieldConstraint) && this.type == QWRConstraintType.NOKEY)
      return true;
    return false;
  }
}

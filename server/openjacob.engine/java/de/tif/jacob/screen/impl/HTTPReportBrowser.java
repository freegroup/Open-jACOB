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

package de.tif.jacob.screen.impl;

import java.util.List;

import de.tif.jacob.core.data.impl.IDataFieldConstraints;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IBrowserTableField;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.SortOrder;
import de.tif.jacob.screen.IBrowser;

/**
 *
 */
public interface HTTPReportBrowser extends IBrowser
{
  public void addColumn(String alias, String column, String label) throws NoSuchFieldException;
  public void addColumn(String alias, String column, String label, SortOrder order) throws NoSuchFieldException;
  public void addColumn(int index, IBrowserTableField field) throws NoSuchFieldException;
  public void sort(int index , SortOrder order) throws NoSuchFieldException;

  public IBrowserTableField removeColumn(int index) throws NoSuchFieldException;

  public List getBrowserTableFields();
  public String getReportName();
  public boolean isPrivate();
  public IDataFieldConstraints getStartConstraints();
  
  public String       getAnchorTable();
  public IRelationSet getRelationSet();
  public Filldirection getFilldirection();
}

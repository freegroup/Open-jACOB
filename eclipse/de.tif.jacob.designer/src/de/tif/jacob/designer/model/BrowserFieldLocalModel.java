/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
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
/*
 * Created on 22.11.2004
 *
 */
package de.tif.jacob.designer.model;

import org.apache.commons.lang.StringUtils;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserFieldChoice;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserTableFieldType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorSortOrder;

/**
 *
 */
public class BrowserFieldLocalModel extends BrowserFieldModel
{
	protected BrowserFieldLocalModel(JacobModel jacob , BrowserModel browserModel, FieldModel field)
	{
	  super(jacob,browserModel,new CastorBrowserField());
	  this.castor.setLabel(field.getLabel());
    String generatedName = "browser"+StringUtils.capitalise(field.getName());
    int i=1;
    while(browserModel.getBrowserFieldModel(generatedName)!=null)
    {
      generatedName = "browser"+StringUtils.capitalise(field.getName())+(i++);
    }
    this.castor.setName(generatedName);
	  this.castor.setVisible(true);
	  this.castor.setCastorBrowserFieldChoice(new CastorBrowserFieldChoice());
	  this.castor.getCastorBrowserFieldChoice().setTableField(new CastorBrowserTableFieldType());
	  this.castor.getCastorBrowserFieldChoice().getTableField().setTableField(field.getName());
	}
	
	protected BrowserFieldLocalModel(JacobModel jacob , BrowserModel browserModel,  CastorBrowserField field)
	{
	  super(jacob,browserModel,field);
	}
	
  @Override
	public TableAliasModel getTableAliasModel()
	{
	  return browserModel.getTableAliasModel();
	}
	
  @Override
	public FieldModel getFieldModel()
	{
	  return getTableAliasModel().getFieldModel(castor.getCastorBrowserFieldChoice().getTableField().getTableField());
	}

  @Override
	public void renameFieldReference(FieldModel field, String from, String to)
	{
	  if(getTableAliasModel().getTableModel()!=field.getTableModel())
	    return;
	  if(castor.getCastorBrowserFieldChoice().getTableField().getTableField().equals(from))
	    castor.getCastorBrowserFieldChoice().getTableField().setTableField(to);
	}
	
  @Override
  protected void resetI18N()
  {
    setLabel(getFieldModel().getLabel());
    getJacobModel().addI18N(getLabel().substring(1),"",false);
  }
  
  @Override
  public String getSortOrder()
  {
    // Es darf nicht direct der Wert in der Sortorder zur�ck geliefert werden,
    // da in dem SortorderCellEditor mit '==' und nicht mit equals() gepr�ft wird
    //
    if(castor.getCastorBrowserFieldChoice().getTableField().getSortOrder()==null)
      return SORTORDER_NONE;
    else if(castor.getCastorBrowserFieldChoice().getTableField().getSortOrder().toString().equals(SORTORDER_ASCENDING))
      return SORTORDER_ASCENDING;
    else
      return SORTORDER_DESCENDING;
  }
  

  @Override
  public void setSortOrder(String sortOrder)
  {
    if(sortOrder==SORTORDER_NONE)
      castor.getCastorBrowserFieldChoice().getTableField().setSortOrder(null);
    else if(sortOrder == SORTORDER_ASCENDING)
      castor.getCastorBrowserFieldChoice().getTableField().setSortOrder( CastorSortOrder.ASCENDING);
    else
      castor.getCastorBrowserFieldChoice().getTableField().setSortOrder( CastorSortOrder.DESCENDING);
    getBrowserModel().firePropertyChange(PROPERTY_BROWSERCOLUMN_CHANGED,null,this);
  }

  @Override
  public String getError()
  {
    if(getFieldModel().getType().equals(FieldModel.DBTYPE_LONGTEXT))
      return "Longtext is not a valid type of a browser column";
    return null;
  }
  
  
  @Override
  public String getInfo()
  {
    return null;
  }
  
  @Override
  public boolean isInUse()
  {
    return true;
  }
}

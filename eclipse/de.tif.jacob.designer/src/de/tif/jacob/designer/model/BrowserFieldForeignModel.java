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
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserForeignType;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserTableFieldType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorSortOrder;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class BrowserFieldForeignModel extends BrowserFieldModel
{
	protected BrowserFieldForeignModel(JacobModel jacob , BrowserModel browserModel,TableAliasModel alias,  FieldModel field)
	{
	  super(jacob,browserModel,new CastorBrowserField());
    if(getJacobModel().useI18N())
      this.castor.setLabel("%"+alias.getName().toUpperCase());
    else
      this.castor.setLabel(StringUtils.capitalise(alias.getName()));
    
    String generatedName = "browser"+StringUtils.capitalise(alias.getName());
    int i=1;
    while(browserModel.getBrowserFieldModel(generatedName)!=null)
    {
      generatedName = "browser"+StringUtils.capitalise(alias.getName())+(i++);
    }
	  this.castor.setName(generatedName);
	  this.castor.setVisible(true);
	  this.castor.setCastorBrowserFieldChoice(new CastorBrowserFieldChoice());
	  this.castor.getCastorBrowserFieldChoice().setTableField(new CastorBrowserTableFieldType());

	  CastorBrowserForeignType type= new CastorBrowserForeignType();
	  
	  this.castor.getCastorBrowserFieldChoice().getTableField().setForeign(type);
		
    type.setForeignAlias(alias.getName());

    // Diese information ist eigentlich unn�tig, da diese sich errechnet
	  //
	  RelationModel relation = jacob.getRelationModel(getForeignTableAliasModel(), browserModel.getTableAliasModel());
    // Kann passieren wenn man eine Tabelle in den Broiwser zieht welche keinen direkten Bezug zur
    // Anchor table des Browser hat.
    if(relation!=null)
    {
	    type.setRelationToUse(relation.getName());
      type.setFilldirection(CastorFilldirection.valueOf((String)FILLDIRECTIONS.get(0)));
      type.setRelationset(RelationsetModel.RELATIONSET_LOCAL);

      // Falls das ForeingField 'frisch' in eine Gruppe eingefg�gt wird, hat dieses noch
      // keinen Search browser zugeordnet. Dies kann erst passieren wenn die Anchor Table
      // definiert ist......und dies passiert hier.
      //
      type.setBrowserToUse((String)getForeignTableAliasModel().getPossibleBrowserNames().get(0));
    }
	  
	  // Anzeige column einstellen
	  //
    FieldModel representativeField = getForeignTableAliasModel().getTableModel().getRepresentativeFieldModel();
    String displayField = (String) getForeignTableAliasModel().getFieldNames().get(0);
    if (representativeField != null)
      displayField = representativeField.getName();
//	  String displayField = (String)getForeignTableAliasModel().getFieldNames().get(0);
	  this.castor.getCastorBrowserFieldChoice().getTableField().setTableField(displayField);

	}

	protected BrowserFieldForeignModel(JacobModel jacob , BrowserModel browserModel,  CastorBrowserField field)
	{
	  super(jacob,browserModel,field);
	}
	
  protected void resetI18N()
  {
    setLabel("%"+getForeignTableAliasModel().getName().toUpperCase());
    getJacobModel().addI18N(getLabel().substring(1),"",false);
  }

  public TableAliasModel getTableAliasModel()
	{
	  return browserModel.getTableAliasModel();
	}
	
	public TableAliasModel getForeignTableAliasModel()
	{
	  return getJacobModel().getTableAliasModel(castor.getCastorBrowserFieldChoice().getTableField().getForeign().getForeignAlias());
	}
	
	public TableModel getForeignTableModel()
	{
	  return getForeignTableAliasModel().getTableModel();
	}
	
	public FieldModel getFieldModel()
	{
	  return getForeignTableModel().getFieldModel(castor.getCastorBrowserFieldChoice().getTableField().getTableField());
	}
	

  public String getFilldirection()
	{
	  if(castor.getCastorBrowserFieldChoice().getTableField().getForeign().getFilldirection()!=null)
	    return castor.getCastorBrowserFieldChoice().getTableField().getForeign().getFilldirection().toString();
	  return null;
	}
	
	public void setFieldToDisplay(String fieldName)
	{
	  String save = getFieldModel().getName();
	  if(StringUtil.saveEquals(save,fieldName))
	    return;
	  
	  castor.getCastorBrowserFieldChoice().getTableField().setTableField(fieldName);
    firePropertyChange(PROPERTY_BROWSERCOLUMN_CHANGED, null, this);
//    getBrowserModel().firePropertyChange(PROPERTY_BROWSERCOLUMN_CHANGED,null,this);
	}
	
	public void setBrowserToUse(String browserName)
	{
    String save = getBrowserToUse();
    if(StringUtil.saveEquals(save, browserName))
      return;
    
	  browserName = "".equals(browserName)?null:browserName;
    castor.getCastorBrowserFieldChoice().getTableField().getForeign().setBrowserToUse(browserName);
    firePropertyChange(PROPERTY_BROWSERCOLUMN_CHANGED, null, this);
	}

	public void setFilldirection(String direction)
	{
	  if(StringUtil.saveEquals(direction,""))
	    castor.getCastorBrowserFieldChoice().getTableField().getForeign().setFilldirection(null);
	  else
	    castor.getCastorBrowserFieldChoice().getTableField().getForeign().setFilldirection(CastorFilldirection.valueOf(direction));
	}

	public void setRelationset(String relationsetName)
	{
	  relationsetName = "".equals(relationsetName)?null:relationsetName;
    castor.getCastorBrowserFieldChoice().getTableField().getForeign().setRelationset(relationsetName);
	}

	public String getBrowserToUse()
	{
	  return castor.getCastorBrowserFieldChoice().getTableField().getForeign().getBrowserToUse();
	}
	
	public String getRelationset()
	{
	  return castor.getCastorBrowserFieldChoice().getTableField().getForeign().getRelationset();
	}

	/**
	 * Will be called from the parent if a anchro table has been renamed.
	 * 
	 * @param from
	 * @param to
	 */
	protected void renameAliasReference(String from, String to)
	{
	  // rename the anchor table
	  //
	  if(castor.getCastorBrowserFieldChoice().getTableField().getForeign().getForeignAlias().equals(from))
	    castor.getCastorBrowserFieldChoice().getTableField().getForeign().setForeignAlias(to);
	}
	
	protected void renameRelationsetReference(String from, String to)
	{
	  // rename the relationset reference
	  //
	  if(StringUtil.saveEquals(castor.getCastorBrowserFieldChoice().getTableField().getForeign().getRelationset(),from))
	    castor.getCastorBrowserFieldChoice().getTableField().getForeign().setRelationset(to);
	}

	protected void renameBrowserReference(String from, String to)
	{
	  // rename the browser reference
	  //
	  if(StringUtil.saveEquals(castor.getCastorBrowserFieldChoice().getTableField().getForeign().getBrowserToUse(),from))
	    castor.getCastorBrowserFieldChoice().getTableField().getForeign().setBrowserToUse(to);
	}

	public void renameFieldReference(FieldModel field, String from, String to)
	{
	  if(getForeignTableModel()!=field.getTableModel())
	    return;
	  
	  if(castor.getCastorBrowserFieldChoice().getTableField().getTableField().equals(from))
	    castor.getCastorBrowserFieldChoice().getTableField().setTableField(to);
	}

	
	
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
  
	public String getError()
  {
    if(getForeignTableAliasModel()==null)
      return "Foreign field with invalid table alias ["+castor.getCastorBrowserFieldChoice().getTableField().getForeign().getForeignAlias()+"]";
    
    if(getFieldModel()== getForeignTableModel().NULL_FIELD)
      return "No display field defined for foreign field in browser ["+getBrowserModel().getName()+"].";
 //   if(getRelationset()!=null || getBrowserToUse()!=null || getFilldirections())
 //     return "Foreign field with invalid table alias ["+castor.getCastorBrowserFieldChoice().getTableField().getForeign().getForeignAlias()+"]";
    
    return null;
  }
  
  
  public String getInfo()
  {
    return null;
  }
  
  public boolean isInUse()
  {
    return true;
  }
  
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    if( model == getForeignTableAliasModel() || model == getBrowserModel())
      result.addReferences(this);
  }
  
}

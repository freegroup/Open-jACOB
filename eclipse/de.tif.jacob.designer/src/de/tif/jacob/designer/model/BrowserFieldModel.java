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

import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField;
import de.tif.jacob.designer.actions.ShowBrowserEditorAction;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public abstract class BrowserFieldModel extends ObjectModel implements IOpenable
{
	final CastorBrowserField castor;
	final BrowserModel       browserModel;
	
  public abstract void   renameFieldReference(FieldModel field, String from, String to);
  public abstract String getSortOrder();
  public abstract void   setSortOrder(String sortORder);
  protected abstract void resetI18N();

  protected BrowserFieldModel(JacobModel jacob , BrowserModel browserModel,  CastorBrowserField field)
	{
    super(jacob);
		this.castor        = field;
		this.browserModel = browserModel;
	}
	
	
  @Override
	public String getLabel()
	{
		return castor.getLabel();
	}
	
 
  
  public String getExtendedDescriptionLabel()
  {
    return this.getName();
  }
  
  
  public TableAliasModel getTableAliasModel()
	{
	  return browserModel.getTableAliasModel();
	}
	
	/**
   * 
   * @param label
   */
  public void setLabel(String label)
  {
    String save = getLabel();
    if (StringUtil.saveEquals(label, save))
      return;
    
    castor.setLabel(label);

    firePropertyChange(PROPERTY_LABEL_CHANGED, save, label);
  }

 
  @Override
  public String getName()
	{
	  return castor.getName();
	}
	
	/**
   * 
   * @param label
   */
  public void setName(String label) throws Exception
  {
    String save = getName();
    if (StringUtil.saveEquals(label, save))
      return;
    
    castor.setName(label);

    firePropertyChange(PROPERTY_NAME_CHANGED, save, label);
  }

  public boolean getConfigureable()
  {
    return castor.getConfigureable();
  }
  
  public boolean getVisible()
  {
    return castor.getVisible();
  }
  
  /**
   * 
   * @param label
   */
  public void setConfigureable(boolean flag)
  {
    boolean save = getConfigureable();
    if (save == flag)
      return;
    
    castor.setConfigureable(flag);

    firePropertyChange(PROPERTY_ELEMENT_CHANGED, save, flag);
  }

  /**
   * 
   * @param label
   */
  public void setVisible(boolean flag)
  {
    boolean save = getVisible();
    if (save == flag)
      return;
    
    castor.setVisible(flag);

    firePropertyChange(PROPERTY_ELEMENT_CHANGED, save, flag);
  }
  
  public FieldModel getFieldModel()
	{
    return null;
	}
	
	
  public BrowserModel getBrowserModel()
	{
	  return browserModel;
	}
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.ObjectModel#firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
   */
  public final void firePropertyChange(String propertyName, Object oldValue, Object newValue)
  {
    super.firePropertyChange(propertyName, oldValue, newValue);
    
    // additionally inform parent that any child property has changed
    browserModel.firePropertyChange(ObjectModel.PROPERTY_BROWSERCOLUMN_CHANGED, null, this);
  }
  
  public CastorBrowserField getCastor()
  {
    return castor;
  }

  @Override
  public final String getWarning()
  {
    String caption = getLabel();
    if (caption != null && caption.startsWith("%") && getJacobModel().hasI18NKey(caption.substring(1)) == false)
      return "No localization entry for [" + caption.substring(1) + "] found";
    return null;
  }
  
  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    if(model == getFieldModel())
      result.addReferences(this);
  }
  
  
  @Override
  public ObjectModel getParent()
  {
    return getBrowserModel();
  }
  
  public void openEditor()
  {
    new ShowBrowserEditorAction()
    {
      @Override
      public BrowserModel getBrowserModel()
      {
        return BrowserFieldModel.this.getBrowserModel();
      }
    }.run(null);
  } 
}

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
 * Created on 03.11.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDataSource;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorDataSourceReconfigureType;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.IntegerPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PhysicalDataModel extends ObjectModel
{
  private final CastorDataSource castor;
  
  public PhysicalDataModel( JacobModel jacob,String name)
  {
    super(jacob);
    this.castor = new CastorDataSource();
    this.castor.setName(name);
    this.castor.setDesc("Fill description of datasource.");
  }


  public PhysicalDataModel( JacobModel jacob, CastorDataSource source)
  {
    super(jacob);
    this.castor  = source;
  }

  
  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
    
    IPropertyDescriptor[]  descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    
    descriptors[superDescriptors.length] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_RECONFIGURE, "Reconfigure Mode" ,(String[]) RECONFIGURE_MODES.toArray(new String[0]), PROPERTYGROUP_COMMON);
    return descriptors;
  }
  
  public void setPropertyValue(Object propName, Object val)
  {
    if (propName == ID_PROPERTY_RECONFIGURE)
      setReconfigureMode((String) RECONFIGURE_MODES.get(((Integer) val).intValue()));
    else
      super.setPropertyValue(propName, val);
  }
  
  public Object getPropertyValue(Object propName)
  {
    if (propName == ID_PROPERTY_RECONFIGURE)
      return new Integer(RECONFIGURE_MODES.indexOf(getReconfigureMode()));
    
    return super.getPropertyValue(propName);
  }

  /**
   * @return Returns the name.
   */
  public String getName()
  {
    return castor.getName();
  }
  
  public void setName(String name)
  {
    String save = getName();
    if(StringUtil.saveEquals(save,name))
      return;
    
    Iterator iter= getTableModels().iterator();
    while (iter.hasNext())
    {
      TableModel table = (TableModel) iter.next();
      if(table.getCastor().getDatasource().equals(save))
        table.getCastor().setDatasource(name);
      
    }
    castor.setName(name);
		firePropertyChange(PROPERTY_NAME_CHANGED, save, name);
  }
  
  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#getAlign()
   */
  public String getReconfigureMode()
  {
    if(getCastor().getReconfigure()==null)
      return RECONFIGUREMODE_FULL;
    return getCastor().getReconfigure().toString();
  }
  
  /* 
   * @see de.tif.jacob.designer.model.IFontProviderModel#setAlign(java.lang.String)
   */
  public void setReconfigureMode(String mode)
  {
    String save = getReconfigureMode();
    if(save.equals(mode))
      return;
    
    getCastor().setReconfigure(CastorDataSourceReconfigureType.valueOf(mode));
    firePropertyChange(PROPERTY_ELEMENT_CHANGED, save, mode);
  }
  
  @Override
  public String getImageBaseName()
  {
    return super.getImageBaseName()+ "_"+getReconfigureMode();
  }


  /**
   * 
   * @return List[TableModel]
   */
  public List<TableModel> getTableModels()
  {
    List<TableModel> allTables = getJacobModel().getTableModels();
    List<TableModel> result = new ArrayList<TableModel>();
    for (TableModel table : allTables)
    {
      if (table.getDatasourceModel()==this)
        result.add(table);
    }
    return result;
  }

  public String getError()
  {
    return null;
  }
  
  public String getWarning()
  {
    return null;
  }
  
  public String getInfo()
  {
    if(getTableModels().size()==0)
      return "Physical data model contains no database tables.";
    
    return null;
  }
  
  public boolean isInUse()
  {
    return getTableModels().size()>0;
  }
  
  protected CastorDataSource getCastor()
  {
    return castor;
  }


  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }
  
  @Override
  public ObjectModel getParent()
  {
    return getJacobModel().getApplicationModel();
  }
}

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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowser;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.core.definition.impl.jad.castor.types.BrowserType;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.actions.ShowBrowserEditorAction;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.exception.InvalidJadFileException;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazz.ClassUtil;

/**
 *
 */
public class BrowserModel extends ObjectWithPropertyModel implements PropertyChangeListener, IOpenable
{
	private final CastorBrowser castor;
	private final List<BrowserFieldModel> fields = new ArrayList<BrowserFieldModel>();
	
  private TableAliasModel tableAliasModel; // cache them for performance reason
	/**
	 * Create a valid default browser for the hands over table.
	 * 
	 * @param jacob
	 * @param table
	 */
	public BrowserModel(TableAliasModel alias, String name)
	{
	  super(alias.getJacobModel());
		castor = new CastorBrowser();
		castor.setAlias(alias.getName());
		castor.setName(name);
		castor.setType(BrowserType.INFORM);
		
		// es wird per Standard das erste Feld in den Browser mit aufgenommen
		//
		FieldModel firstField = (FieldModel)alias.getFieldModels().get(0);
	  BrowserFieldModel  bfield = new BrowserFieldLocalModel(getJacobModel(), this, firstField);
	  fields.add(bfield);
	  castor.addField(bfield.getCastor());
    
    // Es wird nachgesehen ob die Tabelle ein "Representative Field" hat. Wenn ja wird
    // dies in die Browserdefinition it aufgenommen
    //
	  FieldModel field = alias.getTableModel().getRepresentativeFieldModel();
    if(field!=null && field != firstField)
    {
      BrowserFieldModel  representativeField = new BrowserFieldLocalModel(getJacobModel(), this, field);
      fields.add(representativeField);
      castor.addField(representativeField.getCastor());
    }
    
    this.tableAliasModel= alias;
    alias.getJacobModel().addPropertyChangeListener(this);
	}

	
	protected BrowserModel(JacobModel jacob, CastorBrowser browser) throws Exception
	{
	  super(jacob);
		this.castor=browser;
	  
		for(int i= 0; i< browser.getFieldCount(); i++)
		{
		  CastorBrowserField cfield = browser.getField(i);
		  // it is a calculated field and has no relation to a db-table
		  //
		  if(cfield.getCastorBrowserFieldChoice().getRuntime()!=null)
		    fields.add(new BrowserFieldCalculatedModel(jacob, this, browser.getField(i)));
		  // it is a local table field
		  //
		  else if(cfield.getCastorBrowserFieldChoice().getTableField().getForeign()==null)
		    fields.add(new BrowserFieldLocalModel(jacob, this, browser.getField(i)));
		  // if is a foreign field 
		  //
		  else if(cfield.getCastorBrowserFieldChoice().getTableField().getForeign()!=null)
		    fields.add(new BrowserFieldForeignModel(jacob, this, browser.getField(i)));
		  // create Error
		  //
		  else
		    throw new InvalidJadFileException("Unknown type of browser field.");
		}
    jacob.addPropertyChangeListener(this);
	}
	
	public String getName()
	{
		return castor.getName();
	}
	
  /**
   * Der Browser hat ein anderes Icon falls dieser eine "ConnectBy" hat. Er bekommt dann
   * ein TreeBrowser-Icon.
   */
  public String getImageBaseName()
  {
    if(getConnectByKey()==null)
      return super.getImageBaseName(); 
    return "Tree"+super.getImageBaseName();
  }
  
  
  @Override
  public String getExtendedDescriptionLabel()
  {
    // In alten Applicationsdefinitionen (Konvertierung von Quinntus ADL,ADF) kann es vorkommen, dass ein Tablealias nicht
    // korrekt gelöscht wurden, und dieser im Browser noch referenziert wird.
    //
    if(getTableAliasModel()!=null)
      return getName()+ "<"+getTableAliasModel().getName()+">";
    return getName()+ "<UNKNOWN>";
  }


  public KeyModel getConnectByKey()
  {
    if(castor.getConnectByKey()!=null)
      return getTableAliasModel().getKeyModel(castor.getConnectByKey().getName());
    return null;
  }
  
  /**
   * 
   * @param name
   */
  public void setConnectByKey(KeyModel key)
  {
    KeyModel save = getConnectByKey();
    if(save == key)
      return;
    
    if(key!=null)
      castor.setConnectByKey(key.getCastor());
    else
      castor.setConnectByKey(null);
    
    firePropertyChange(PROPERTY_KEY_CHANGED, save, key);
  }

  /**
   * 
   * @param name
   */
  public void setName(String name)
  {
    String save = getName();
    if (StringUtil.saveEquals(name, save))
      return;
    
    castor.setName(name);

    getJacobModel().renameBrowserReference(save, name);

    firePropertyChange(PROPERTY_NAME_CHANGED, save, name);
  }
  
  
  /**
   * 
   */
  public boolean isInUse()
	{
    ReferenceSearchResult result = new ReferenceSearchResult(null);
    getJacobModel().addReferrerObject(result,this);
	  return result.getReferences().size()>0;
	}
	
  public String getError()
  {
    if(getTableAliasModel()==null)
      return "unable to find table alias ["+castor.getAlias()+"]";
    
    // Falls ein Feld ein Fehler hat, dann hat der Browser auch einen
    //
    Iterator iter = getBrowserFieldModels().iterator();
    while (iter.hasNext())
    {
      try
      {
	      BrowserFieldModel obj = (BrowserFieldModel) iter.next();
	      String error = obj.getError();
        if(error!=null)
           return error;
      }
      catch (Exception e)
      {
        return "unknown error in  browser definition";
      }
    }
    
	  if(getBrowserFieldModels().size()==0)
	    return "Browser <"+getName()+"> must contain at least one column.";
	  
    return null;
  }
  
  public String getWarning()
  {
	  if(!isInUse())
	    return "Browser <"+getName()+"> is not used by any UI element.";
	  
    return null;
  }
  
  public String getInfo()
  {
    return null;
  }

	public TableAliasModel getTableAliasModel()
	{
    if(tableAliasModel!=null)
      return tableAliasModel;
    
    tableAliasModel=getJacobModel().getTableAliasModel(castor.getAlias());
    
    if(tableAliasModel==null)
      throw new RuntimeException("Unable to find requested table alias ["+castor.getAlias()+"] in the JAD definition.");
    
    return tableAliasModel;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors()
	{
		IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
		for (int i = 0; i < superDescriptors.length; i++)
			descriptors[i] = superDescriptors[i];
		descriptors[superDescriptors.length]   = new TextPropertyGroupingDescriptor(ID_PROPERTY_NAME,  "Name", PROPERTYGROUP_COMMON);

		return descriptors;
	}
	
	public void setPropertyValue(Object propName, Object val)
	{
		try 
		{
			if (propName == ID_PROPERTY_NAME)
				setName((String) val);
			else
				super.setPropertyValue(propName, val);
		} 
		catch (Exception e) 
		{
			JacobDesigner.showException(e);
		}
	}
	
	public Object getPropertyValue(Object propName)
	{
		if (propName == ID_PROPERTY_NAME)
			return getName();
		return super.getPropertyValue(propName);
	}
    
	/**
	 * will be called if the table alias has been renamed
	 * 
	 * @param from
	 * @param to
	 */
	protected void renameAliasReference(String from, String to)
	{
	  // rename the anchor table
	  //
	  if(castor.getAlias().equals(from))
	    castor.setAlias(to);
	  
	  // rename all anchor tables of the foreign fields in the browser
	  //
	  for(BrowserFieldModel bfield: fields)
	  {
	    if(bfield instanceof BrowserFieldForeignModel)
	    {
	      BrowserFieldForeignModel ffield = (BrowserFieldForeignModel)bfield;
	      ffield.renameAliasReference(from,to);
	    }
	  }
	}
	
	protected void renameRelationsetReference(String from, String to)
	{
	  // rename all anchor tables of the foreign fields in the browser
	  //
    for(BrowserFieldModel bfield: fields)
    {
	    if(bfield instanceof BrowserFieldForeignModel)
	    {
	      BrowserFieldForeignModel ffield = (BrowserFieldForeignModel)bfield;
	      ffield.renameRelationsetReference(from,to);
	    }
	  }
	}
	
	protected void renameBrowserReference(String from, String to)
	{
	  // rename all anchor tables of the foreign fields in the browser
	  //
    for(BrowserFieldModel bfield: fields)
    {
	    if(bfield instanceof BrowserFieldForeignModel)
	    {
	      BrowserFieldForeignModel ffield = (BrowserFieldForeignModel)bfield;
	      ffield.renameBrowserReference(from,to);
	    }
	  }
	}

	protected void renameFieldReference(FieldModel field, String from, String to)
	{
	  // rename all anchor tables of the foreign fields in the browser
	  //
    for(BrowserFieldModel bfield: fields)
    {
      bfield.renameFieldReference(field, from,to);
	  }
	}

	protected void createMissingI18NKey()
	{
    for(BrowserFieldModel bfield: fields)
    {
	    String label= bfield.getLabel();
	    if(label !=null && label.startsWith("%") && !getJacobModel().hasI18NKey(label.substring(1)))
	      getJacobModel().addI18N(label.substring(1),"",false);
	  }
	}
	
	protected void renameI18NKey(String from, String to)
	{
    for(BrowserFieldModel bfield: fields)
    {
	    if(bfield.getLabel()!=null && bfield.getLabel().equals(from))
	      bfield.setLabel(to);
	  }
	}

	protected void resetI18N()
	{
    for(BrowserFieldModel bfield: fields)
    {
	    bfield.resetI18N();
	  }
	}

	protected boolean isI18NKeyInUse(String key)
	{
    for(BrowserFieldModel bfield: fields)
    {
	    if(bfield.getLabel()!=null && bfield.getLabel().equals(key))
	      return true;
	  }
	  return false;
	}

	public void addElement(FieldModel field)
	{
	  BrowserFieldModel  bfield = new BrowserFieldLocalModel(getJacobModel(), this, field);
	  
	  fields.add(bfield);
	  castor.addField(bfield.getCastor());
	  
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null, bfield);
    getJacobModel().firePropertyChange(PROPERTY_BROWSER_CHANGED,null,this);
	}
	
	public void addElement(TableAliasModel alias, FieldModel field)
	{
	  BrowserFieldModel  bfield = new BrowserFieldForeignModel(getJacobModel(),this,alias, field);
	  
	  fields.add(bfield);
	  castor.addField(bfield.getCastor());
	  
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null, bfield);
    getJacobModel().firePropertyChange(PROPERTY_BROWSER_CHANGED,null,this);
	}

	public void removeElement(BrowserFieldModel field)
	{
	  int index = fields.indexOf(field);
	  if(index!=-1)
	  {
		  fields.remove(field);
		  castor.removeField(index);
		  
	    firePropertyChange(PROPERTY_ELEMENT_REMOVED, field, null);
	    getJacobModel().firePropertyChange(PROPERTY_BROWSER_CHANGED,null,this);
	  }
	}
	
	/**
	 * Alle Reference auf ein Tabellenfeld lï¿½schen
	 * @param field
	 */
	public void removeElement(FieldModel field)
	{
	  List toRemove = new ArrayList();
	  
    for(BrowserFieldModel bfield: fields)
    {
	    if(bfield.getFieldModel()==field)
	      toRemove.add(bfield);
	  }
	  Iterator iter = toRemove.iterator();
	  while(iter.hasNext())
	  {
	    BrowserFieldModel bfield= (BrowserFieldModel)iter.next();
	    removeElement(bfield);
	  }
	}
	
	public void upElement(BrowserFieldModel field)
	{
	  int index = fields.indexOf(field);
	  
	  fields.remove(field);
	  CastorBrowserField castorField = castor.removeField(index);
    firePropertyChange(PROPERTY_ELEMENT_REMOVED, field, null);
    
    fields.add(index-1,field);
    castor.addField(index-1,castorField);
    firePropertyChange(PROPERTY_ELEMENT_ADDED,  null, field);
    getJacobModel().firePropertyChange(PROPERTY_BROWSER_CHANGED,  null, this);
	}

	
	public void downElement(BrowserFieldModel field)
	{
	  int index = fields.indexOf(field);
	  
	  fields.remove(field);
	  CastorBrowserField castorField = castor.removeField(index);
    firePropertyChange(PROPERTY_ELEMENT_REMOVED, field, null);
    
    fields.add(index+1,field);
    castor.addField(index+1,castorField);
    firePropertyChange(PROPERTY_ELEMENT_ADDED,  null, field);

    getJacobModel().firePropertyChange(PROPERTY_BROWSER_CHANGED,  null, this);
	}

	/**
	 * 
	 * @return 
	 */
	public BrowserFieldModel getBrowserFieldModel(String name)
	{
    for(BrowserFieldModel bfield: fields)
    {
      if(bfield.getName().equals(name))
        return bfield;
    }
		return null;
	}
	
  /**
   * 
   * @return List[BrowserFieldModel]
   */
  public List<BrowserFieldModel> getBrowserFieldModels()
  {
    return new ArrayList<BrowserFieldModel>(fields);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorProperty(int)
   */
  CastorProperty getCastorProperty(int index)
  {
    return this.castor.getProperty(index);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorPropertyCount()
   */
  int getCastorPropertyCount()
  {
    return this.castor.getPropertyCount();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void addCastorProperty(CastorProperty property)
  {
    this.castor.addProperty(property);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void removeCastorProperty(CastorProperty property)
  {
    for(int i=0;i<castor.getPropertyCount();i++)
    {
      if(castor.getProperty(i)==property)
      {
        castor.removeProperty(i);
        return;
      }
    }
    throw new ArrayIndexOutOfBoundsException("property ["+property.getName()+"] is not part of "+getClass().getName());
  }

  public CastorBrowser getCastor()
	{
		return castor;
	}
  
  public void propertyChange(PropertyChangeEvent arg0)
  {
    // meine gecachete Tabelle ha sich geändert. Eventuell wurde diese gelöscht
    // Keine weitere Untersuchung notwendig
    if(arg0.getOldValue()== tableAliasModel)
      tableAliasModel=null;
  
  }


  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    if(model == getTableAliasModel())
      result.addReferences(this);
    
    for(BrowserFieldModel bfield: fields)
    {
        bfield.addReferrerObject(result, model);
    }
  }

  @Override
  public ObjectModel getParent()
  {
    return getJacobModel().getApplicationModel();
  }

  public void openEditor()
  {
    new ShowBrowserEditorAction()
    {
      @Override
      public BrowserModel getBrowserModel()
      {
        return BrowserModel.this;
      }
    }.run(null);
  }



  protected void renameKeyReference(KeyModel key, String from, String to)
  {
    TableModel table=getTableAliasModel().getTableModel();

    if(table != key.getTableModel())
      return;
    if(castor.getConnectByKey()==null)
      return;
    if( castor.getConnectByKey().getName().equals(from))
      castor.getConnectByKey().setName(to);
  }
}

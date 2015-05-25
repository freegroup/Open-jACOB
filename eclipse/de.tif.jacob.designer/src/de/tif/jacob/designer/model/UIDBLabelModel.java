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
 * Created on Oct 19, 2004
 *
 */
package de.tif.jacob.designer.model;

import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.Label;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ReadonlyTextPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class UIDBLabelModel extends UILabelModel implements UIDBLocalInputFieldModel
{
  public UIDBLabelModel()
  {
    super();
  }

  protected UIDBLabelModel(JacobModel jacob, UIGroupContainer container, UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container, group, guiElement);
  }
  
  public String getDefaultDbType()
  {
    return FieldModel.DBTYPE_TEXT;
  }

  public String getDefaultNameSuffix()
  {
    return StringUtils.capitalise(getFieldModel().getName());
  }
  
  public String getDefaultCaption()
  {
    return getFieldModel().getName();
  }

  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
    IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 2];
    for (int i = 0; i < superDescriptors.length; i++)
    {
      descriptors[i] = superDescriptors[i];
      // Bei einem Datenbank gebundenem Label kann man den Text im Designer nicht �ndern
      if (descriptors[i].getId() == ID_PROPERTY_LABEL)
        descriptors[i] = new ReadonlyTextPropertyGroupingDescriptor(ID_PROPERTY_LABEL, "Label", PROPERTYGROUP_COMMON);
    }
    descriptors[superDescriptors.length] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_FIELD, "Field", (String[]) getJacobModel().getTableAliasModel(getGroupModel().getTableAlias()).getFieldNames(this).toArray(new String[0]), PROPERTYGROUP_DB);
    descriptors[superDescriptors.length+1] = new TextPropertyGroupingDescriptor(ID_PROPERTY_DBLABEL_DEFAULT_VALUE, "Default Label If Content NULL", PROPERTYGROUP_DB);
    return descriptors;
  }

	public void setPropertyValue(Object propName, Object val)
	{
    if (propName ==ID_PROPERTY_DBLABEL_DEFAULT_VALUE)
      getCastor().getCastorGuiElementChoice().getLabel().setNullDefaultValue(StringUtils.stripToNull((String) val));
    else if (propName instanceof String && ((String) propName).equals(ID_PROPERTY_FIELD))
		  setField(((Integer)val).intValue());
		else
			super.setPropertyValue(propName, val);
	}
	

  public Object getPropertyValue(Object propName)
	{
    if (propName ==ID_PROPERTY_DBLABEL_DEFAULT_VALUE)
      return StringUtil.toSaveString(getCastor().getCastorGuiElementChoice().getLabel().getNullDefaultValue());
 		if (propName == ID_PROPERTY_FIELD)
		  return new Integer(getJacobModel().getTableAliasModel(getGroupModel().getTableAlias()).getFieldNames(this).indexOf(getFieldModel().getName()));
		else
			return super.getPropertyValue(propName);
	}
	
	public FieldModel getFieldModel()
	{
	  if(getCastor().getCastorGuiElementChoice().getLabel().getTableField()==null)
	  {
	    String defaultField = getGroupModel().getTableAliasModel().getFieldNames(this).get(0).toString();
	    getCastor().getCastorGuiElementChoice().getLabel().setTableField(defaultField);
	  }
	  return getGroupModel().getTableAliasModel().getFieldModel(getCastor().getCastorGuiElementChoice().getLabel().getTableField());
	}
	
	public String getLabel() 
	{
		return "<"+getGroupTableAlias()+"."+ getCastor().getCastorGuiElementChoice().getLabel().getTableField()+">";
	}

	public void setField(int index)
	{
    if(index==-1)
	    setField(getGroupModel().getTableAliasModel().getTableModel().NULL_FIELD.getName());
    else
      setField((String)getJacobModel().getTableAliasModel(getGroupModel().getTableAlias()).getFieldNames(this).get(index));
	}

	public void setField(String fieldName)
	{
	  String save = getFieldModel().getName();

	  if(StringUtil.saveEquals(save,fieldName))
	    return;
	  
		// Falls der Name das Muster aliasnameFieldname01, aliasnameFieldname02, ,,,, hat, wird der Name
		// automatisch angepasst
		//
	  boolean fitName = getName().startsWith(getDefaultName());
		
	  
		getCastor().getCastorGuiElementChoice().getLabel().setTableField(fieldName);
	  
		firePropertyChange(ObjectModel.PROPERTY_FIELD_CHANGED, save, fieldName);
		
		try
    {
      if(fitName)
        setName(getDefaultName());
    }
    catch (Exception e)
    {
    	System.out.println(e);
      // falls ein package nicht umbenannt werden kann, kann der Name nicht angepasst werden
      //....ignore!
    }	    
  }
	
	public void renameFieldReference(FieldModel field, String fromName, String toName)
	{
		Label label = getCastor().getCastorGuiElementChoice().getLabel();
		
    // Wenn die Gruppe auf den selben alias zeigt UND das Feld ein
    // Datenbankfeld ist UND es sich um das gleiche Feld handelt
    // wird dieses ohnen auslösen von Events umbenannt.
    //
    // Anmerkung: obj.getFieldModel() kann man nicht mehr aufrufen, da das Feld bereits umbenannt ist
    //            und dieses jetzt nicht mehr gefunden wird. Ich gehe bei dem LocalInputField dann über die Gruppe um die
    //            Tabelle zu holen. Ein LocalInptField bezieht sich immer auf den selben Alias wie die Gruppe
    if (field.getTableModel() == getGroupModel().getTableAliasModel().getTableModel() && label.getTableField() != null && label.getTableField().equals(fromName))
    {
      label.setTableField(toName);
      // Das Label ist in diesem Fall von dem Datenbankfeld abhängig. 
      // Die Listener müssen sich aktualisieren
      //
      firePropertyChange(PROPERTY_LABEL_CHANGED,null,getLabel());
    }
	}

	public String getError()
  {
    if(getFieldModel() == getFieldModel().getTableModel().NULL_FIELD)
      return "Text input field ["+getName()+"] has not a valid reference to a column in the table ["+getFieldModel().getTableModel().getName()+"]";
    return null;
  }

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    if(model == getFieldModel())
      result.addReferences(this);
  }
}

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.ibm.wsdl.util.StringUtils;

import de.tif.jacob.core.definition.impl.jad.castor.CastorAction;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.ClearGroup;
import de.tif.jacob.core.definition.impl.jad.castor.DeleteRecord;
import de.tif.jacob.core.definition.impl.jad.castor.Generic;
import de.tif.jacob.core.definition.impl.jad.castor.NavigateToForm;
import de.tif.jacob.core.definition.impl.jad.castor.NewRecord;
import de.tif.jacob.core.definition.impl.jad.castor.RecordSelected;
import de.tif.jacob.core.definition.impl.jad.castor.Search;
import de.tif.jacob.core.definition.impl.jad.castor.SearchUpdateRecord;
import de.tif.jacob.core.definition.impl.jad.castor.UpdateRecord;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorFilldirection;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.ReadonlyTextPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public abstract class AbstractActionEmitterModel extends UIGroupElementModel
{
	protected static final String DEFAULT_LABEL="Button";
	private static Map i18nDefaultIds    = new IdentityHashMap();
	private static Map i18nDefaultLabels = new IdentityHashMap();
  public final static Set FAVOUR_ACTIONS;
	static
	{
    FAVOUR_ACTIONS = new HashSet();
    FAVOUR_ACTIONS.add(ObjectModel.ACTION_CLEARGROUP);
    FAVOUR_ACTIONS.add(ObjectModel.ACTION_DELETERECORD);
    FAVOUR_ACTIONS.add(ObjectModel.ACTION_LOCALSEARCH);
    FAVOUR_ACTIONS.add(ObjectModel.ACTION_NEWRECORD);
    FAVOUR_ACTIONS.add(ObjectModel.ACTION_SEARCH);
    FAVOUR_ACTIONS.add(ObjectModel.ACTION_SEARCH_UPDATE);
    FAVOUR_ACTIONS.add(ObjectModel.ACTION_UPDATERECORD);
	  
	  i18nDefaultIds.put(ACTION_CLEARGROUP,"%BUTTON_COMMON_CLEAR");
	  i18nDefaultIds.put(ACTION_DELETERECORD,"%BUTTON_COMMON_DELETE");
	  i18nDefaultIds.put(ACTION_GENERIC,"%BUTTON_COMMON_GENERIC");
	  i18nDefaultIds.put(ACTION_NEWRECORD,"%BUTTON_COMMON_NEW");
    i18nDefaultIds.put(ACTION_SEARCH,"%BUTTON_COMMON_SEARCH");
    i18nDefaultIds.put(ACTION_SEARCH_UPDATE,"%BUTTON_COMMON_SEARCH");
	  i18nDefaultIds.put(ACTION_LOCALSEARCH,"%BUTTON_COMMON_LOCALSEARCH");
	  i18nDefaultIds.put(ACTION_SELECTED,"%BUTTON_COMMON_GENERIC");
	  i18nDefaultIds.put(ACTION_UPDATERECORD,"%BUTTON_COMMON_CHANGE");

    i18nDefaultLabels.put(ACTION_SEARCH_UPDATE,"Search/Up.");
	  i18nDefaultLabels.put(ACTION_CLEARGROUP,"Clear");
	  i18nDefaultLabels.put(ACTION_DELETERECORD,"Delete");
	  i18nDefaultLabels.put(ACTION_NEWRECORD,"New");
	  i18nDefaultLabels.put(ACTION_SEARCH,"Search");
	  i18nDefaultLabels.put(ACTION_LOCALSEARCH,"Local Search");
	  i18nDefaultLabels.put(ACTION_UPDATERECORD,"Update");
  }
  

  public AbstractActionEmitterModel(JacobModel jacob, UIGroupContainer container, UIGroupModel group, CastorGuiElement gui)
  {
    super(jacob, container, group, gui);
  }
  

  public abstract CastorAction getCastorAction();
	public abstract void         setCastorLabel(String label);
	public abstract String       getCastorLabel();
	
  /**
   * 
   */
  public final String getTemplateFileName()
  {
    return "ActionType"+getAction()+".java";
  }
  

	public IPropertyDescriptor[] getPropertyDescriptors()
	{
      Map descriptors = new HashMap();
      
      IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
      // generate the descript for the GENERIC-Action
      IPropertyDescriptor[] desc;
      if(getAction()==ACTION_GENERIC || getAction()==ACTION_SELECTED)
      {
        desc = new IPropertyDescriptor[superDescriptors.length + 2];
        
        desc[superDescriptors.length  ] = new TextPropertyGroupingDescriptor(ID_PROPERTY_LABEL, "Label", PROPERTYGROUP_COMMON);
    		desc[superDescriptors.length+1] = new ReadonlyTextPropertyGroupingDescriptor(ID_PROPERTY_ACTION, "Type", PROPERTYGROUP_ACTION);
      }
      else
      {
        desc = new IPropertyDescriptor[superDescriptors.length +1];
        
    		desc[superDescriptors.length] = new ReadonlyTextPropertyGroupingDescriptor(ID_PROPERTY_ACTION, "Type", PROPERTYGROUP_ACTION);
      }
      for (int i = 0; i < superDescriptors.length; i++)
        desc[i] = superDescriptors[i];
      
      descriptors.put(ACTION_GENERIC, desc);
      descriptors.put(ACTION_NEWRECORD, desc);
      descriptors.put(ACTION_UPDATERECORD, desc);
      descriptors.put(ACTION_DELETERECORD, desc);
      descriptors.put(ACTION_CLEARGROUP, desc);
      descriptors.put(ACTION_SELECTED, desc);
      descriptors.put(ACTION_NAVIGATETOFORM, desc);
      
      // Search hat ein paar Attribute mehr
      //
      IPropertyDescriptor[] searchDesc = new IPropertyDescriptor[desc.length + 3];
      for (int i = 0; i < desc.length; i++)
        searchDesc[i] = desc[i];
      searchDesc[desc.length] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_RELATIONSET, "Relationset",(String[])getJacobModel().getRelationsetNames().toArray(new String[0]), PROPERTYGROUP_ACTION);
      searchDesc[desc.length + 1] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_SAFEMODE, "Force constraint", new String[] { "true", "false" }, PROPERTYGROUP_ACTION);
      searchDesc[desc.length + 2] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_FILLDIRECTION, "Filldirection", (String[]) FILLDIRECTIONS.toArray(new String[0]), PROPERTYGROUP_ACTION);
      
      descriptors.put(ACTION_SEARCH, searchDesc);
      descriptors.put(ACTION_LOCALSEARCH, searchDesc);
      descriptors.put(ACTION_SEARCH_UPDATE, searchDesc);

      
      // DeleteAction hat noch das zusätzlich UserConfimation Flag
      //
      IPropertyDescriptor[] deleteDesc = new IPropertyDescriptor[desc.length + 1];
      for (int i = 0; i < desc.length; i++)
        deleteDesc[i] = desc[i];
      deleteDesc[desc.length] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_USERCONFIRM, "User must Confirm", new String[] { "true", "false" }, PROPERTYGROUP_ACTION);
      descriptors.put(ACTION_DELETERECORD, deleteDesc);

      

      return (IPropertyDescriptor[])descriptors.get(getAction());
	}
	
	public void setPropertyValue(Object propName, Object val)
	{
	 
		try 
		{
			if (propName == ID_PROPERTY_LABEL)
				setLabel((String) val);
			else if (propName == ID_PROPERTY_ACTION)
				setAction((String)ACTIONS.get(((Integer)val).intValue()));
			else if (propName == ID_PROPERTY_RELATIONSET)
				setRelationset((String)(getJacobModel().getRelationsetNames().get(((Integer)val).intValue())));
			else if (propName == ID_PROPERTY_SAFEMODE)
				setSafeMode(new Boolean(((Integer)val).intValue()==0));
      else if (propName == ID_PROPERTY_FILLDIRECTION)
        setFilldirection((String)FILLDIRECTIONS.get(((Integer)val).intValue()));
      else if (propName == ID_PROPERTY_USERCONFIRM)
        setUserConfirmation(new Boolean(((Integer)val).intValue()==0));
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
		if (propName == ID_PROPERTY_ACTION)
			return getAction();
		if (propName == ID_PROPERTY_LABEL)
			return getLabel();
		if (propName == ID_PROPERTY_ACTION)
			return new Integer(ACTIONS.indexOf(getAction()));
		if (propName == ID_PROPERTY_RELATIONSET)
			return new Integer(getJacobModel().getRelationsetNames().indexOf(getRelationset()));
    if (propName == ID_PROPERTY_SAFEMODE)
      return new Integer(getSafeMode().booleanValue()?0:1);
    if (propName == ID_PROPERTY_USERCONFIRM)
      return new Integer(getUserConfimation().booleanValue()?0:1);
		if (propName == ID_PROPERTY_FILLDIRECTION)
			return new Integer(FILLDIRECTIONS.indexOf(getFilldirection()));

		return super.getPropertyValue(propName);
	}

	/**
	 * Only valid for the Search button
	 * @return
	 */
	public final RelationsetModel getRelationsetModel()
	{
	  return getJacobModel().getRelationsetModel(getRelationset());
	}
	
	public final String getRelationset()
	{
    if(getAction()==ACTION_SEARCH || getAction()==ACTION_LOCALSEARCH)
      return getCastorAction().getSearch().getRelationset();
    else if(getAction()==ACTION_SEARCH_UPDATE)
      return getCastorAction().getSearchUpdateRecord().getRelationset();
    else
      return null;
	}
	
	public final void setRelationset(String relation)
	{
	  String save      = getRelationset();
	  String saveLabel = getLabel();
	  if(StringUtil.saveEquals(save,relation))
	    return;
	  
    if(getAction()==ACTION_SEARCH || getAction()==ACTION_LOCALSEARCH)
      getCastorAction().getSearch().setRelationset(relation);
    else
      getCastorAction().getSearchUpdateRecord().setRelationset(relation);

    firePropertyChange(PROPERTY_RELATIONSET_CHANGED, save, relation);
		// Falls sich das Relationset verï¿½ndert, kann es passieren, dass sich auch das
		// Label verï¿½ndert. Event falls notwendig 'abfeuern'
		if(!getLabel().equals(saveLabel));
			firePropertyChange(PROPERTY_LABEL_CHANGED, saveLabel, getLabel());
	}
	
  

  @Override
  public void renameI18NKey(String fromName, String toName)
  {
    if (getCastorLabel() != null && getCastorLabel().equals(fromName))
      setLabel(toName); // firePropertyChangeEvent(!!!) to update the GUI-Editor
  }

  
  /**
   * @param key the key to check WITH the % at first character
   * @return
   * 
   */
  @Override
  public boolean isI18NKeyInUse(String key)
  {
    return StringUtil.saveEquals(key,getLabel());
  }

  public final Boolean getSafeMode()
	{
    if(getAction()==ACTION_SEARCH || getAction()==ACTION_LOCALSEARCH)
      return new Boolean(getCastorAction().getSearch().getSafeMode());
    else
      return new Boolean(getCastorAction().getSearchUpdateRecord().getSafeMode());
	}

	public final void setSafeMode(Boolean mode)
	{
	  Boolean save = getSafeMode();
	  if(save.equals(mode))
	    return;
	  
    if(getAction()==ACTION_SEARCH || getAction()==ACTION_LOCALSEARCH)
      getCastorAction().getSearch().setSafeMode(mode.booleanValue());
    else
      getCastorAction().getSearchUpdateRecord().setSafeMode(mode.booleanValue());
		firePropertyChange(PROPERTY_SAFEMODE_CHANGED, save, mode);
	}
	

  public final Boolean getUserConfimation()
  {
     return new Boolean(getCastorAction().getDeleteRecord().getNeedUserConfirmation());
  }

  public final void setUserConfirmation(Boolean mode)
  {
    Boolean save = getUserConfimation();
    if(save.equals(mode))
      return;
    
    getCastorAction().getDeleteRecord().setNeedUserConfirmation(mode.booleanValue());
    firePropertyChange(PROPERTY_CONFIRMATION_CHANGED, save, mode);
  }
  

  public final String getFilldirection()
	{
    String fill=null;
    if(getAction()==ACTION_SEARCH || getAction()==ACTION_LOCALSEARCH)
      fill =getCastorAction().getSearch().getFilldirection().toString();
    else
      fill =getCastorAction().getSearchUpdateRecord().getFilldirection().toString();
	  return (String)FILLDIRECTIONS.get(FILLDIRECTIONS.indexOf(fill));
	}

	public final void setFilldirection(String direction)
	{
		String save = getFilldirection();
	  if(StringUtil.saveEquals(save,direction))
	    return;

    if(getAction()==ACTION_SEARCH || getAction()==ACTION_LOCALSEARCH)
      getCastorAction().getSearch().setFilldirection(CastorFilldirection.valueOf(direction));
    else
      getCastorAction().getSearchUpdateRecord().setFilldirection(CastorFilldirection.valueOf(direction));
		firePropertyChange(PROPERTY_FILLDIRECTION_CHANGED, save, direction);
	}
	
	public final void setLabel(String l)
	{
		String save = getLabel();
	  if(StringUtil.saveEquals(save,l))
	    return;
	  
    if(getAction()==ACTION_GENERIC || getAction()==ACTION_SELECTED)
    {
			setCastorLabel(l);
			firePropertyChange(PROPERTY_LABEL_CHANGED, save, l);
    }
	}
	
	public final String getLabel()
	{
	  String action = getAction();
    if(action==ACTION_GENERIC || action==ACTION_SELECTED)
      return getCastorLabel();
    return (String)i18nDefaultLabels.get(action);
	}
	
	public final String getAction()
	{
	  if(getCastorAction().getClearGroup()!=null)
	    return ACTION_CLEARGROUP;
	  if(getCastorAction().getDeleteRecord()!=null)
	    return ACTION_DELETERECORD;
	  if(getCastorAction().getNavigateToForm()!=null)
	    return ACTION_NAVIGATETOFORM;
	  if(getCastorAction().getNewRecord()!=null)
	    return ACTION_NEWRECORD;
    if(getCastorAction().getRecordSelected()!=null)
      return ACTION_SELECTED;
    if(getCastorAction().getSearchUpdateRecord()!=null)
      return ACTION_SEARCH_UPDATE;
	  if(getCastorAction().getSearch()!=null)
	  {
	    String relationset = getCastorAction().getSearch().getRelationset();
	    if(RELATIONSET_LOCAL.equals(relationset))
	      return ACTION_LOCALSEARCH;
	    return ACTION_SEARCH;
	  }
	  if(getCastorAction().getUpdateRecord()!=null)
	    return ACTION_UPDATERECORD;
	  
	  return ACTION_GENERIC;
	}
	
	public final void setAction(String action)
	{
	  String save = getAction();

	  try
    {
	    clearAction();
		  if(action== ACTION_CLEARGROUP)
		  {
		    getCastorAction().setClearGroup(new ClearGroup());
		    setCastorLabel((String)i18nDefaultIds.get(action));
		  }
		  else if(action== ACTION_DELETERECORD)
		  {
		    getCastorAction().setDeleteRecord(new DeleteRecord());
		    setCastorLabel((String)i18nDefaultIds.get(action));
		  }
		  else if(action== ACTION_NAVIGATETOFORM)
		  {
		    getCastorAction().setNavigateToForm(new NavigateToForm());
		    setCastorLabel((String)i18nDefaultIds.get(action));
		  }
		  else if(action== ACTION_NEWRECORD)
		  {
		    getCastorAction().setNewRecord(new NewRecord());
		    setCastorLabel((String)i18nDefaultIds.get(action));
		  }
		  else if(action== ACTION_SELECTED)
		  {
		    getCastorAction().setRecordSelected(new RecordSelected());
		  }
		  else if(action== ACTION_SEARCH)
		  {
		    Search search=new Search();
		    search.setFilldirection(CastorFilldirection.BACKWARD);
		    search.setSafeMode(false);
		    search.setRelationset(RelationsetModel.RELATIONSET_DEFAULT);
		    getCastorAction().setSearch(search);
		    setCastorLabel((String)i18nDefaultIds.get(action));
		  }
      else if(action== ACTION_SEARCH_UPDATE)
      {
        SearchUpdateRecord search=new SearchUpdateRecord();
        search.setFilldirection(CastorFilldirection.BACKWARD);
        search.setSafeMode(false);
        search.setRelationset(RelationsetModel.RELATIONSET_DEFAULT);
        search.setChangeUpdate(false);
        getCastorAction().setSearchUpdateRecord(search);
        setCastorLabel((String)i18nDefaultIds.get(action));
      }
		  else if(action== ACTION_LOCALSEARCH)
		  {
		    Search search=new Search();
		    search.setFilldirection(CastorFilldirection.BACKWARD);
		    search.setSafeMode(false);
		    search.setRelationset(RelationsetModel.RELATIONSET_LOCAL);
		    getCastorAction().setSearch(search);
		    setCastorLabel((String)i18nDefaultIds.get(action));
		  }
		  else if(action== ACTION_UPDATERECORD)
		  {
		    getCastorAction().setUpdateRecord(new UpdateRecord());
		    getCastorAction().getUpdateRecord().setChangeUpdate(false);
		    setCastorLabel((String)i18nDefaultIds.get(action));
		  }
		  else 
		    getCastorAction().setGeneric(new Generic());

      firePropertyChange(PROPERTY_NAME_CHANGED, save, action);

      // Falls das Label nicht 'von Hand' geändert wurde, wird jetzt ein default Label gesetzt
		  //
		  if(getLabel()==DEFAULT_LABEL && getJacobModel()!=null)
		  {
        if(getJacobModel().useI18N())
        {
          String newLabel = (String)i18nDefaultIds.get(getAction());
          setLabel(newLabel);
        }
		  }
    }
    catch (Exception e)
    {
      JacobDesigner.showException(e);
    }
	}
	
	public final void renameRelationsetReference(String from, String to)
	{
	  if(getCastorAction()!=null && getCastorAction().getSearch()!=null && StringUtil.saveEquals(getCastorAction().getSearch().getRelationset(),from))
	    getCastorAction().getSearch().setRelationset(to);

    else if(getCastorAction()!=null && getCastorAction().getSearchUpdateRecord()!=null && StringUtil.saveEquals(getCastorAction().getSearchUpdateRecord().getRelationset(),from))
      getCastorAction().getSearchUpdateRecord().setRelationset(to);
	}
	
	private final void clearAction()
	{
	  getCastorAction().setGeneric(null);
	  getCastorAction().setClearGroup(null);
	  getCastorAction().setDeleteRecord(null);
	  getCastorAction().setNavigateToForm(null);
	  getCastorAction().setNewRecord(null);
	  getCastorAction().setRecordSelected(null);
    getCastorAction().setSearch(null);
    getCastorAction().setSearchUpdateRecord(null);
	  getCastorAction().setUpdateRecord(null);
	}
}

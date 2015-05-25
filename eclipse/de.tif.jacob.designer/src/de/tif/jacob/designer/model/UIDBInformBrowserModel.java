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
package de.tif.jacob.designer.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorCaption;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.core.definition.impl.jad.castor.InFormBrowser;
import de.tif.jacob.core.definition.impl.jad.castor.SelectionActionEventHandler;
import de.tif.jacob.core.definition.impl.jad.castor.types.CastorHorizontalAlignment;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.editor.jacobform.misc.ComboBoxPropertyGroupingDescriptor;
import de.tif.jacob.designer.preferences.I18NPreferences;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

public class UIDBInformBrowserModel extends UIGroupElementModel 
                                    implements UIIBrowserProviderModel, UIICaptionProviderModel, ISelectionActionProvider
{
	private UICaptionModel captionModel;
  private List<SelectionActionModel> actions = new ArrayList<SelectionActionModel>();
  
	public UIDBInformBrowserModel()
	{
	  super(null, null,null, new CastorGuiElement());

	  CastorGuiElementChoice choice  = new CastorGuiElementChoice();
	  InFormBrowser          browser = new InFormBrowser();
	  CastorCaption          caption = new CastorCaption();
	  caption.setHalign(CastorHorizontalAlignment.valueOf(JacobDesigner.getPluginProperty(JacobDesigner.LABEL_ALIGN, CastorHorizontalAlignment.LEFT.toString())));
	  
	  browser.setDimension(new CastorDimension());
	  CastorDimension dim = new CastorDimension();
	  dim.setWidth(200);
	  dim.setHeight(50);
	  dim.setX(0);
	  dim.setY(0);
	  caption.setDimension(dim);
	  browser.setCaption(caption);
    browser.setNewMode(false);
    browser.setUpdateMode(false);
    browser.setDeleteMode(false);
    
    caption.setLabel("Caption");
	  choice.setInFormBrowser(browser);
	  getCastor().setCastorGuiElementChoice(choice);
	  getCastor().setVisible(true);

	}

  /**
   * 
   */
  public UIDBInformBrowserModel(JacobModel jacob, UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, group.getGroupContainerModel(), group,guiElement);

    InFormBrowser castor = getCastorInformBrowser();
    for(int i=0;i<castor.getSelectionActionEventHandlerCount();i++)
    {
      actions.add( SelectionActionModel.createModelObject(this,castor.getSelectionActionEventHandler(i)));
    }
  }

	protected RelationModel getRelationToUse()
	{
    return getJacobModel().getRelationModel(getCastorInformBrowser().getRelationToUse());
	}
	
  public CastorCaption getCastorCaption()
  {
    return getCastorInformBrowser().getCaption();
  }
  
  public void setCastorCaption(CastorCaption caption)
  {
    getCastorInformBrowser().setCaption(caption);
  }
  
  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorInformBrowser().getDimension();
  }
  

  public String getDefaultNameSuffix()
  {
    return StringUtils.capitalise(getBrowserModel().getTableAliasModel().getName()+"Browser");
  }

  public String getDefaultCaption()
  {
    return StringUtils.capitalise(getBrowserModel().getTableAliasModel().getName());
  }
  
  /**
   * Per Default wird die Einstellung aus den Preferences genommen
   *
   */
  public String getDefaultCaptionAlign()
  {
    return JacobDesigner.getPluginProperty(JacobDesigner.LABEL_ALIGN, ALIGN_LEFT);
  }
  
  public int getDefaultWidth()
  {
    return ObjectModel.DEFAULT_ELEMENT_WIDTH;
  }
  
  public int getDefaultHeight()
  {
    // eine listbox geht per default über den Platz von 3 eingabeelemente
    //
    return DEFAULT_ELEMENT_HEIGHT*3+DEFAULT_ELEMENT_SPACING*2;
  }

  public String suggestI18NKey()
  {
    return "BROWSER"+getJacobModel().getSeparator()+getBrowserModel().getTableAliasModel().getName().toUpperCase();
  }

  
  /**
   * @param key the key to check WITH the % at first character
   * @return
   * 
   */
  @Override
  public boolean isI18NKeyInUse(String key)
  {
    return StringUtil.saveEquals(key,getCaption());
  }

  private boolean getNewMode()
  {
    return getCastorInformBrowser().getNewMode();
  }
  
	public void setNewMode(boolean mode)
	{
		boolean save = getNewMode();
		if(save ==mode)
		  return;

		getCastorInformBrowser().setNewMode(mode);
		
		firePropertyChange(PROPERTY_BROWSERMODE_CHANGED, new Boolean(save), new Boolean(mode));
	}

  private boolean getSearchMode()
  {
    return getCastorInformBrowser().getSearchMode();
  }
  
  public void setSearchMode(boolean mode)
  {
    boolean save = getSearchMode();
    if(save ==mode)
      return;

    getCastorInformBrowser().setSearchMode(mode);
    
    firePropertyChange(PROPERTY_BROWSERMODE_CHANGED, new Boolean(save), new Boolean(mode));
  }


  private boolean getUpdateMode()
  {
    return getCastorInformBrowser().getUpdateMode();
  }
  
	public void setUpdateMode(boolean mode)
	{
		boolean save = getUpdateMode();
		if(save ==mode)
		  return;

		getCastorInformBrowser().setUpdateMode(mode);
		
		firePropertyChange(PROPERTY_BROWSERMODE_CHANGED, new Boolean(save), new Boolean(mode));
	}

	private boolean getDeleteMode()
  {
    return getCastorInformBrowser().getDeleteMode();
  }
  
	public void setDeleteMode(boolean mode)
	{
		boolean save = getDeleteMode();
		if(save ==mode)
		  return;

		getCastorInformBrowser().setDeleteMode(mode);
		
		firePropertyChange(PROPERTY_BROWSERMODE_CHANGED, new Boolean(save), new Boolean(mode));
	}

	
	public void setSize(Dimension size)
	{
		Dimension save = getSize();
		if(save.equals(size))
		  return;
		
		getCastorInformBrowser().getDimension().setHeight(size.height);
		getCastorInformBrowser().getDimension().setWidth(size.width);

		firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
	}

	public Point getLocation()
	{
	  return new Point(getCastorInformBrowser().getDimension().getX(), getCastorInformBrowser().getDimension().getY());
	}
	
	public Dimension getSize()
	{
	  return new Dimension(getCastorInformBrowser().getDimension().getWidth(), getCastorInformBrowser().getDimension().getHeight());
	}

	/**
	 * true  => Die Überschrift wird als eigenstï¿½ndiges Element im Formlayout gezeichnet
	 * false => Das Model zeichnet die Caption selbst.
	 */
  public boolean   isCaptionExtern()
  {
    return false;
  }

	public UICaptionModel getCaptionModel()
	{
	  if(captionModel==null)
	  {
	    CastorCaption caption = getCastorInformBrowser().getCaption();
	    if(caption==null)
	    {
	      caption = new CastorCaption();
	  	  CastorDimension dim = new CastorDimension();
	  	  dim.setWidth(200);
	  	  dim.setHeight(50);
	  	  dim.setX(0);
	  	  dim.setY(0);
	  	  caption.setDimension(dim);
	  	  getCastorInformBrowser().setCaption(caption);
	    }
		  captionModel = new UICaptionModel(getJacobModel(), getGroupContainerModel(), this);
	  }
	  return captionModel;
	}

	
	public BrowserModel getBrowserModel()
	{
		return getJacobModel().getBrowserModel(getCastorInformBrowser().getBrowserToUse()); 
	}
	
	private String getBrowser()
	{
		return getCastorInformBrowser().getBrowserToUse(); 
	}

  public void setup(UIGroupModel group,BrowserModel b, Point location, Dimension size) throws Exception
	{
      setJacobModel(group.getJacobModel());
	    getCastorInformBrowser().setBrowserToUse(b.getName());
	    // Falls der Browser nicht direkt mit dem Alias, sondern durch eine M-N Relation verbunden ist, dann
	    // wird man nicht direkt eine Relation hierzu finden....
	    //
	    RelationModel relation = getJacobModel().getRelationModel(group.getTableAliasModel(), b.getTableAliasModel());
	    if(relation!=null)
	    {
	      getCastorInformBrowser().setRelationToUse(relation.getName());
	    }
	    else
	    {
	      getCastorInformBrowser().setRelationToUse("<leiche>");
	    }
	    getCastorInformBrowser().setNewMode(false);
	    getCastorInformBrowser().setUpdateMode(false);
      getCastorInformBrowser().setDeleteMode(false);
      getCastorInformBrowser().setSearchMode(true);
	    
	    setGroup(group);
			getCaptionModel().setCaption(getDefaultCaption());
    
	}

	private void setBrowser(String browser) throws Exception
	{
	  String save = getCastorInformBrowser().getBrowserToUse();
	  if(StringUtil.saveEquals(save, browser))
	    return;
	  
	  boolean fitCaption=false;
	  boolean fitName=false;
	  
		// Caption anpassen falls diese nicht geï¿½ndert worden ist - den
		// Defaultwert noch enthï¿½lt.
		//
		if(getCaption().equals(getDefaultCaption()))
		  fitCaption=true;

		if(getName().equals(getDefaultName()))
		  fitName=true;

		// browserToUse anpassen
	  //
		getCastorInformBrowser().setBrowserToUse(browser);
	  
		firePropertyChange(PROPERTY_BROWSER_CHANGED, save, browser);
		
		if(fitCaption)
		  setCaption(getDefaultCaption());
		
		try
    {
      if(fitName)
        setName(getDefaultName());
    }
    catch (Exception e)
    {
      // falls ein package nicht umbenannt werden kann kann der Name nicht angepasst werden
      //....ignore!
    }
	}

	public TableAliasModel getTableAliasModel()
	{
		return getBrowserModel().getTableAliasModel(); 
	}


	public void renameBrowserReference(String from, String to)
  {
    if(getCastorInformBrowser().getBrowserToUse().equals(from))
      getCastorInformBrowser().setBrowserToUse(to);
  }
	
  public void renameEventHandler(String fromClass, String toClass)
  {
    super.renameEventHandler(fromClass, toClass);
    
    Iterator iter = actions.iterator();
    while (iter.hasNext())
    {
      SelectionActionModel action = (SelectionActionModel) iter.next();
      if(action.getHookClassName().equals(fromClass))
      {
        if(toClass!=null)
        {
          action.setHookClassName(toClass);
        }
        else
        {
          removeElement(action);
        }
        break;
      }
    }
  }
  
  public void renameI18NKey(String fromName, String toName)
  {
    if (getCaption() != null && getCaption().equals(fromName))
      setCaption(toName);
  }
  
  public void createMissingI18NKey()
  {
    String  label = getCaption();
    if(label !=null && label.startsWith("%") && !getJacobModel().hasI18NKey(label.substring(1)))
      getJacobModel().addI18N(label.substring(1),"",false);
  }
  

  public IPropertyDescriptor[] getPropertyDescriptors()
	{
	  IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
	  IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 5];
	  
		for (int i = 0; i < superDescriptors.length; i++)
			descriptors[i] = superDescriptors[i];
		descriptors[superDescriptors.length]   = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_BROWSER, "Browser",(String[])getTableAliasModel().getPossibleBrowserNames().toArray(new String[0]), PROPERTYGROUP_DB);
		descriptors[superDescriptors.length+1] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_BROWSERMODE_NEW, "Create Records", new String[]{"yes","no"}, PROPERTYGROUP_FEATURES);
		descriptors[superDescriptors.length+2] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_BROWSERMODE_DEL, "Delete Records", new String[]{"yes","no"}, PROPERTYGROUP_FEATURES);
    descriptors[superDescriptors.length+3] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_BROWSERMODE_UPDATE, "Update Records",   new String[]{"yes","no"}, PROPERTYGROUP_FEATURES);
    descriptors[superDescriptors.length+4] = new ComboBoxPropertyGroupingDescriptor(ID_PROPERTY_BROWSERMODE_SEARCH, "Search Records",   new String[]{"yes","no"}, PROPERTYGROUP_FEATURES);
		return descriptors;
	}
	
	public void setPropertyValue(Object propName, Object val)
	{
	  try
	  {
			if (propName == ID_PROPERTY_BROWSER)
				setBrowser((String)getTableAliasModel().getPossibleBrowserNames().get(((Integer)val).intValue()));
			else if (propName == ID_PROPERTY_BROWSERMODE_DEL)
				setDeleteMode(((Integer)val).intValue()==0);
			else if (propName == ID_PROPERTY_BROWSERMODE_UPDATE)
				setUpdateMode(((Integer)val).intValue()==0);
      else if (propName == ID_PROPERTY_BROWSERMODE_NEW)
        setNewMode(((Integer)val).intValue()==0);
      else if (propName == ID_PROPERTY_BROWSERMODE_SEARCH)
        setSearchMode(((Integer)val).intValue()==0);
			else
				super.setPropertyValue(propName, val);
	  }
	  catch(Exception e)
	  {
	    JacobDesigner.showException(e);
	  }
	}
	
	public Object getPropertyValue(Object propName)
	{
		if (propName == ID_PROPERTY_BROWSER)
		{
		  List browsers = getTableAliasModel().getPossibleBrowserNames();
		  return new Integer(browsers.indexOf(getBrowser()));
		}
		if(propName == ID_PROPERTY_BROWSERMODE_DEL)
      return new Integer(getDeleteMode()?0:1);
		if(propName == ID_PROPERTY_BROWSERMODE_UPDATE)
      return new Integer(getUpdateMode()?0:1);
    if(propName == ID_PROPERTY_BROWSERMODE_NEW)
      return new Integer(getNewMode()?0:1);
    if(propName == ID_PROPERTY_BROWSERMODE_SEARCH)
      return new Integer(getSearchMode()?0:1);

		return super.getPropertyValue(propName);
	}
	
	private InFormBrowser getCastorInformBrowser()
	{
	  return getCastor().getCastorGuiElementChoice().getInFormBrowser();
	}
    
  /**
   * 
   */
  public String getTemplateFileName()
  {
    return "IInformBrowserEventHandler.java";
  }

  public List<SelectionActionModel> getActions()
  {
    return this.actions;
  }

  public void removeElement(SelectionActionModel element)
  {
    int index = actions.indexOf(element);
    if (index != -1)
    {
      actions.remove(element);
      getCastorInformBrowser().removeSelectionActionEventHandler(index);
      firePropertyChange(PROPERTY_ELEMENT_REMOVED, element, null);
      getGroupContainerModel().firePropertyChange(PROPERTY_ELEMENT_CHANGED, null, this);
    }
  }
  
  public void addElement(SelectionActionModel element)
  {
    actions.add(element);
    getCastorInformBrowser().addSelectionActionEventHandler(element.getCastor());
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null, element);
  }
  
  public void upElement(SelectionActionModel model)
  {
    int index = actions.indexOf(model);
    actions.remove(model);
    SelectionActionEventHandler castorField = getCastorInformBrowser().removeSelectionActionEventHandler(index);
    firePropertyChange(PROPERTY_ELEMENT_REMOVED, model, null);
    actions.add(index - 1, model);
    getCastorInformBrowser().addSelectionActionEventHandler(index - 1, castorField);
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null, model);
  }

  public void downElement(SelectionActionModel model)
  {
    int index = actions.indexOf(model);
    actions.remove(model);
    SelectionActionEventHandler castorField = getCastorInformBrowser().removeSelectionActionEventHandler(index);
    firePropertyChange(PROPERTY_ELEMENT_REMOVED, model, null);
    actions.add(index + 1, model);
    getCastorInformBrowser().addSelectionActionEventHandler(index + 1, castorField);
    firePropertyChange(PROPERTY_ELEMENT_ADDED, null, model);
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
    return null;
  }
  
  public boolean isInUse()
  {
    return true;
  }

  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
    if(model == getBrowserModel())
      result.addReferences(this);
  }
}

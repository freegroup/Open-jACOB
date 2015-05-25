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

import org.apache.commons.lang.StringUtils;
import de.tif.jacob.core.definition.impl.jad.castor.CastorHtmlGroup;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

public class UIHtmlGroupModel extends ObjectWithPropertyModel
{
  final CastorHtmlGroup castor;
  final UIHtmlFormModel form;
  
  public UIHtmlGroupModel(JacobModel jacob, UIHtmlFormModel form)
  {
    super(jacob);
    this.form = form;
    castor = new CastorHtmlGroup();
    castor.setName("anyGroup");
    castor.setLabel("<unused>");
  }

  public UIHtmlGroupModel(JacobModel jacob,UIHtmlFormModel form, CastorHtmlGroup group)
  {
    super(jacob);
    this.form = form;
    this.castor = group;
  }

  public String getGroupTableAlias()
  {
    return getTableAlias();
  }

  public BrowserModel getBrowserModel()
  {
    return getJacobModel().getBrowserModel(castor.getBrowser());
  }

  /**
   * 
   * @param browser
   */
  public void setBrowserModel(BrowserModel browser)
  {
    setBrowser(browser.getName());
  }

  public void setBrowser(String browser)
  {
    String save = castor.getBrowser();
    BrowserModel saveBrowser = getBrowserModel();
    
    // no changes
    //
    if (StringUtil.saveEquals(save, browser))
      return;
    
    BrowserModel newBrowser = getJacobModel().getBrowserModel(browser);
    if (newBrowser == null)
      throw new RuntimeException("Browser [" + browser + "] doesn't exists.");
    
    castor.setBrowser(browser);
    firePropertyChange(PROPERTY_BROWSER_CHANGED, save, browser);
    
    // Dem Browser und alle die diesen anzeigen mitteilen, das sich der (Error/Warning) Status 
    // m�glicherweise ge�ndert hat. Der Browser feuert ein Event und alle Listener aktualisieren sich dann
    //
    newBrowser.firePropertyChange(PROPERTY_BROWSER_CHANGED, null, newBrowser);
    if(saveBrowser!=null)
      saveBrowser.firePropertyChange(PROPERTY_BROWSER_CHANGED, null, saveBrowser);
  }

  @Override
  public final void resetHookClassName() throws Exception
  {
    if (getJacobModel().getApplicationModel().isEventHandlerLookupByReference())
    {
      String save = castor.getEventHandler();
      castor.setEventHandler(null);
      firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED, save, null);
    }
    else
      throw new Exception("Unable to set hook name if application is in 'find hooks by gui name' mode.");
  }

  @Override
  public void generateHookClassName() throws Exception
  {
    if (getJacobModel().getApplicationModel().isEventHandlerLookupByReference())
    {
      String save = castor.getEventHandler();
      castor.setEventHandler("jacob.event.ui." + getGroupTableAlias() + "." + StringUtils.capitalise(getName()));
      firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED, save, castor.getEventHandler());
    }
    else
      throw new Exception("Unable to set hook name if application is in 'find hooks by gui name' mode.");
  }

  @Override
  public String getName()
  {
    return castor.getName();
  }
  
  /**
   * @param name
   */
  public void setName(String name)
  {
    String save = getName();
    if (StringUtil.saveEquals(save, name))
      return;
    
    castor.setName(name);
    firePropertyChange(PROPERTY_NAME_CHANGED, save, name);
  }


  public void setTableAlias(String tableAlias)
  {
    String save = getTableAlias();
    if (save != null && save.equals(tableAlias))
      return;
    if (getJacobModel().getTableAliasModel(tableAlias) == null)
      return;
    
    castor.setAlias(tableAlias);
    firePropertyChange(PROPERTY_TABLEALIAS_CHANGED, save, tableAlias);
    // set a default browser for the new group
    //
    setBrowserModel((BrowserModel) getJacobModel().getBrowserModels(getJacobModel().getTableAliasModel(tableAlias)).get(0));
    if (getName() == null || getName().length() == 0)
      setName(tableAlias);
  }

  public String getTableAlias()
  {
    return castor.getAlias();
  }

  public TableAliasModel getTableAliasModel()
  {
    return getJacobModel().getTableAliasModel(castor.getAlias());
  }


  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorProperty(int)
   */
  @Override
  CastorProperty getCastorProperty(int index)
  {
    return this.castor.getProperty(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorPropertyCount()
   */
  @Override
  int getCastorPropertyCount()
  {
    return this.castor.getPropertyCount();
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  @Override
  void addCastorProperty(CastorProperty property)
  {
    this.castor.addProperty(property);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  @Override
  void removeCastorProperty(CastorProperty property)
  {
    for (int i = 0; i < castor.getPropertyCount(); i++)
    {
      if (castor.getProperty(i) == property)
      {
        castor.removeProperty(i);
        return;
      }
    }
    throw new ArrayIndexOutOfBoundsException("property [" + property.getName() + "] is not part of " + getClass().getName());
  }

  protected CastorHtmlGroup getCastor()
  {
    return castor;
  }


  protected void renameAliasReference(String from, String to)
  {
    // rename the anchor table of the group
    //
    if (castor.getAlias().equals(from))
      castor.setAlias(to);
  }

  public void renameEventHandler(String fromClass, String toClass)
  {
    // Nicht StringUtils.saveEquals(..) verwenden. Da sind null und "" gleich!!
    // Desweiteren darf dieser Teil nicht ausgeführt werden wenn zuvor kein
    // Eventhandler
    // definert war. Wenn zuvor kein Eventhandler definiert war, dann kann man
    // diesen auch nicht umbenennen.
    //
    if (castor.getEventHandler() != null && castor.getEventHandler().equals(fromClass))
    {
      castor.setEventHandler(toClass);
      firePropertyChange(PROPERTY_EVENTHANDLER_CHANGED, fromClass, toClass);
    }
  }

  public void renameBrowserReference(String from, String to)
  {
    // rename the browser of the group
    //
    if (castor.getBrowser().equals(from))
      castor.setBrowser(to);
  }


  protected boolean isI18NKeyInUse(String key)
  {
    return false;
  }


  @Override
  public String getHookClassName()
  {
    if (getJacobModel().getApplicationModel().isEventHandlerLookupByReference())
      return castor.getEventHandler();
    return "jacob.common.gui." + getTableAlias() + "." + StringUtils.capitalise(getName());
  }

  /**
   * 
   */
  @Override
  public String getTemplateFileName()
  {
    return "IHtmlGroupEventHandler.java";
  }

  @Override
  public String getError()
  {
    return null;
  }

  @Override
  public String getWarning()
  {
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

  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }
  
  @Override
  public ObjectModel getParent()
  {
    return form;
  }
}

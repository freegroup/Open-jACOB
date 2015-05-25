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
 * Created on Oct 26, 2004
 *
 */
package de.tif.jacob.designer.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import de.tif.jacob.core.definition.impl.jad.castor.CastorAction;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorProperty;
import de.tif.jacob.core.definition.impl.jad.castor.ContextMenuEntry;
import de.tif.jacob.core.definition.impl.jad.castor.Generic;


/**
 *
 */
public class ContextMenuEntryModel extends AbstractActionEmitterModel
{
  private final ContextMenuEntry menuCastor;

  public ContextMenuEntryModel(JacobModel jacob,  UIGroupModel group, String actionType)
  {
    super(jacob, group.getGroupContainerModel(),group, null);
    
    this.menuCastor   = new ContextMenuEntry();
    this.menuCastor.setAction(new CastorAction());
    this.menuCastor.getAction().setGeneric(new Generic());
    this.menuCastor.setLabel("label");
    this.menuCastor.setName(DEFAULT_NAME);
    this.menuCastor.setVisible(true);
    setAction(actionType);
    setGroup( group);
  }

	/**
   * @param relation
   * @param model
   * @param toTable
   * @param fromTable
   * 
   */
  public ContextMenuEntryModel(JacobModel jacob, UIGroupModel group, ContextMenuEntry entry)
  {
    super(jacob, group.getGroupContainerModel(),group, null);
    this.menuCastor     = entry;
    setGroup( group);
  }

  public CastorAction getCastorAction()
  {
    return menuCastor.getAction();
  }
  
  @Override
  public CastorDimension getCastorDimension()
  {
    return null;
  }
  
  public String getCastorLabel()
  {
    return menuCastor.getLabel();
  }
  
  public void setCastorLabel(String label)
  {
    menuCastor.setLabel(label);
  }
  
  public String getCastorName()
  {
    return menuCastor.getName();
  }
  
  public void setCastorName(String name)
  {
    menuCastor.setName(name);
  }
  
  protected String getCastorEventHandler()
  {
    return menuCastor.getEventHandler();
  }
  
  protected void setCastorEventHandler(String event)
  {
    menuCastor.setEventHandler(event);
  }
  
  public String getDefaultNameSuffix()
  {
    return "ContextMenu"+getAction();
  }
  
  public Point getLocation()
  {
    return null;
  }
  
  public Dimension getSize()
  {
    return null;
  }
  
  
  public void setSize(Dimension size)
  {
  }
	
  public boolean isInUse()
  {
    return true;
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
  
  protected ContextMenuEntry getMenuCastor()
  {
    return menuCastor;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorProperty(int)
   */
  CastorProperty getCastorProperty(int index)
  {
    return getCastor().getProperty(index);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#getCastorPropertyCount()
   */
  int getCastorPropertyCount()
  {
    if(getMenuCastor()==null)
      return 0;
    return getMenuCastor().getPropertyCount();
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void addCastorProperty(CastorProperty property)
  {
    getMenuCastor().addProperty(property);
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.designer.model.CastorPropertyContainerObjectModel#addCastorProperty(de.tif.jacob.core.definition.impl.jad.castor.CastorProperty)
   */
  void removeCastorProperty(CastorProperty property)
  {
    for(int i=0;i<getMenuCastor().getPropertyCount();i++)
    {
      if(getMenuCastor().getProperty(i)==property)
      {
        getMenuCastor().removeProperty(i);
        return;
      }
    }
    throw new ArrayIndexOutOfBoundsException("property ["+property.getName()+"] is not part of "+getClass().getName());
  }
  
}

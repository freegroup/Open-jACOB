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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.BarChart;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;
import de.tif.jacob.designer.editor.jacobform.misc.CheckboxPropertyDescriptor;

/**
 *
 */
public class UIBarChartModel extends UIGroupElementModel implements UIInputElementModel, AbstractChartModel
{
	public UIBarChartModel()
	{
	  super(null,null,null, new CastorGuiElement());
	  CastorGuiElementChoice choice = new CastorGuiElementChoice();
	  BarChart              chart  = new BarChart();
	  
	  chart.setDimension(new CastorDimension());
	  CastorDimension dim = new CastorDimension();
	  dim.setWidth(200);
	  dim.setHeight(50);
	  choice.setBarChart(chart);
	  getCastor().setCastorGuiElementChoice(choice);
	  getCastor().setVisible(true);
	}

	/**
   * 
   */
  public UIBarChartModel(JacobModel jacob,  UIGroupContainer container,  UIGroupModel group, CastorGuiElement guiElement)
  {
    super(jacob, container, group, guiElement);
  }

  
  @Override
  public void renameI18NKey(String fromName, String toName)
  {
    // nothing to do
  }

  
  /**
   * @param key the key to check WITH the % at first character
   * @return
   * 
   */
  @Override
  public boolean isI18NKeyInUse(String key)
  {
    return false;
  }
  
  public String getDefaultNameSuffix()
  {
    return "BarChart";
  }
    
  public String getTemplateFileName()
  {
    return "IBarChartEventHandler.java";
  }
  

	public void setSize(Dimension size)
	{
		Dimension save = getSize();
		getCastorBarChart().getDimension().setHeight(size.height);
		getCastorBarChart().getDimension().setWidth(size.width);
		firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
	}

  
  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
    
    IPropertyDescriptor[]  descriptors = new IPropertyDescriptor[superDescriptors.length + 5];
    for (int i = 0; i < superDescriptors.length; i++)
      descriptors[i] = superDescriptors[i];
    
    descriptors[superDescriptors.length  ] = new CheckboxPropertyDescriptor(ID_PROPERTY_HAS_BACKGROUND, "Background", PROPERTYGROUP_STYLE);
    descriptors[superDescriptors.length+1] = new CheckboxPropertyDescriptor(ID_PROPERTY_HAS_GRID, "Grid" , PROPERTYGROUP_STYLE);
    descriptors[superDescriptors.length+2] = new CheckboxPropertyDescriptor(ID_PROPERTY_HAS_LEGEND_X, "X Label",  PROPERTYGROUP_STYLE);
    descriptors[superDescriptors.length+3] = new CheckboxPropertyDescriptor(ID_PROPERTY_HAS_LEGEND_Y, "Y Label",  PROPERTYGROUP_STYLE);
    descriptors[superDescriptors.length+4] = new CheckboxPropertyDescriptor(ID_PROPERTY_HAS_TITLE,  "Title",  PROPERTYGROUP_STYLE);
    
    return descriptors;
  }
  
  
  public void setPropertyValue(Object propName, Object val)
  {
    if (propName == ID_PROPERTY_HAS_BACKGROUND)
      setHasBackground(((Boolean) val).booleanValue());
    else if (propName == ID_PROPERTY_HAS_GRID)
      setHasGrid(((Boolean) val).booleanValue());
    else if (propName == ID_PROPERTY_HAS_LEGEND_X)
      setHasLegendX(((Boolean) val).booleanValue());
    else if (propName == ID_PROPERTY_HAS_LEGEND_Y)
      setHasLegendY(((Boolean) val).booleanValue());
    else if (propName == ID_PROPERTY_HAS_TITLE)
      setHasTitle(((Boolean) val).booleanValue());
    else
      super.setPropertyValue(propName, val);
  }
  
  
  public Object getPropertyValue(Object propName)
  {
    if (propName == ID_PROPERTY_HAS_BACKGROUND)
      return new Boolean(getHasBackground());
    if (propName == ID_PROPERTY_HAS_GRID)
      return new Boolean(getHasGrid());
    if (propName == ID_PROPERTY_HAS_LEGEND_X)
      return new Boolean(getHasLegendX());
    if (propName == ID_PROPERTY_HAS_LEGEND_Y)
      return new Boolean(getHasLegendY());
    if (propName == ID_PROPERTY_HAS_TITLE)
      return new Boolean(getHasTitle());
    
    return super.getPropertyValue(propName);
  }
  
  
	public Point getLocation()
	{
	  return new Point(getCastorBarChart().getDimension().getX(), getCastorBarChart().getDimension().getY());
	}
	
  
  /* 
   * @see de.tif.jacob.designer.model.AbstractChartModel#setHasGrid(boolean)
   */
  public void setHasGrid(boolean flag)
  {
    boolean save = getHasGrid();
    if (flag==save)
      return;

    getCastorBarChart().setGrid(flag);
    firePropertyChange(PROPERTY_HAS_GRID_CHANGED, new Boolean(save), new Boolean(flag));
  }
  

  /* 
   * @see de.tif.jacob.designer.model.AbstractChartModel#getHasGrid()
   */
  public boolean getHasGrid()
  {
    return getCastorBarChart().getGrid();
  }
  
  /* 
   * @see de.tif.jacob.designer.model.AbstractChartModel#setHasBackground(boolean)
   */
  public void setHasBackground(boolean flag)
  {
    boolean save = getHasBackground();
    if (flag==save)
      return;

    getCastorBarChart().setBackground(flag);
    firePropertyChange(PROPERTY_HAS_BACKGROUND_CHANGED, new Boolean(save), new Boolean(flag));
  }

  /* 
   * @see de.tif.jacob.designer.model.AbstractChartModel#getHasBackground()
   */
  public boolean getHasBackground()
  {
    return getCastorBarChart().getBackground();
  }

  /* 
   * @see de.tif.jacob.designer.model.AbstractChartModel#setHasLegendX(boolean)
   */
  public void setHasLegendX(boolean flag)
  {
    boolean save = getHasLegendX();
    if (flag==save)
      return;

    getCastorBarChart().setLegendX(flag);
    firePropertyChange(PROPERTY_HAS_LEGEND_X_CHANGED, new Boolean(save), new Boolean(flag));
  }

  /* 
   * @see de.tif.jacob.designer.model.AbstractChartModel#getHasLegendX()
   */
  public boolean getHasLegendX()
  {
    return getCastorBarChart().getLegendX();
  }

  /* 
   * @see de.tif.jacob.designer.model.AbstractChartModel#setHasLegendY(boolean)
   */
  public void setHasLegendY(boolean flag)
  {
    boolean save = getHasLegendY();
    if (flag==save)
      return;

    getCastorBarChart().setLegendY(flag);
    firePropertyChange(PROPERTY_HAS_LEGEND_Y_CHANGED, new Boolean(save), new Boolean(flag));
  }

  /* 
   * @see de.tif.jacob.designer.model.AbstractChartModel#getHasLegendY()
   */
  public boolean getHasLegendY()
  {
    return getCastorBarChart().getLegendY();
  }

  /* 
   * @see de.tif.jacob.designer.model.AbstractChartModel#setHasTitle(boolean)
   */
  public void setHasTitle(boolean flag)
  {
    boolean save = getHasTitle();
    if (flag==save)
      return;

    getCastorBarChart().setTitle(flag);
    firePropertyChange(PROPERTY_HAS_TITLE_CHANGED, new Boolean(save), new Boolean(flag));
  }

  /* 
   * @see de.tif.jacob.designer.model.AbstractChartModel#getHasTitle()
   */
  public boolean getHasTitle()
  {
    return getCastorBarChart().getTitle();
  }

  public Dimension getSize()
	{
	  return new Dimension(getCastorBarChart().getDimension().getWidth(), getCastorBarChart().getDimension().getHeight());
	}

	private BarChart getCastorBarChart()
	{
	  return getCastor().getCastorGuiElementChoice().getBarChart();
	}
	
  @Override
  public CastorDimension getCastorDimension()
  {
    return getCastorBarChart().getDimension();
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
  
 
}


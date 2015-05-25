/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
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

package de.tif.jacob.core.definition.guielements;

import java.awt.Color;
import java.awt.Rectangle;

import de.tif.jacob.core.definition.IGUIElementDefinition;
import de.tif.jacob.core.definition.impl.AbstractDefinition;
import de.tif.jacob.core.definition.impl.AbstractElement;
import de.tif.jacob.core.definition.impl.AbstractGuiElement;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class GUIElementDefinition extends AbstractGuiElement implements IGUIElementDefinition
{
  static public final transient String RCS_ID = "$Id: GUIElementDefinition.java,v 1.2 2009/07/27 15:06:11 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";

  private final Caption caption;
  private final Dimension dimension;
  private final boolean visible;
  private final int tabIndex;
  private final int paneIndex;
  private final int borderWith;
  private Color borderColor;
  private Color backgroundColor;
  
	protected GUIElementDefinition(String name, String description, String eventHandler, Dimension dimension, boolean visible, int tabIndex, int paneIndex, Caption caption, int borderWith,  String borderColor, String backgroundColor)
  {
    super(name, description, eventHandler);
    this.caption = caption;
    this.visible = visible;
    this.dimension = dimension;
    this.tabIndex = tabIndex;
    this.paneIndex = paneIndex;
    this.borderWith = borderWith;
    try
    {
      this.backgroundColor = new javax.swing.text.html.StyleSheet().stringToColor(backgroundColor);
    }
    catch(Exception exc)
    {
      this.backgroundColor = null;
    }
    
    try
    {
      this.borderColor = new javax.swing.text.html.StyleSheet().stringToColor(borderColor);
    }
    catch(Exception exc)
    {
      this.borderColor = null;
    }
   }

	public Color getBackgroundColor()
  {
    return backgroundColor;
  }

  public Color getBorderColor()
  {
    return borderColor;
  }

  public int getBorderWith()
  {
    return borderWith;
  }

  /**
	 * @return Returns the caption.
	 */
	public final Caption getCaption()
	{
		return caption;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IGUIElementDefinition#isVisible()
	 */
	public final boolean isVisible()
	{
		return this.visible;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IGUIElementDefinition#getRectangle()
	 */
	public final Rectangle getRectangle()
	{
    if (dimension == null)
      return null;
    
    // rectangle is mutable -> therefore create it each time
		return new Rectangle(dimension.getLeft(), dimension.getTop(), dimension.getWidth(), dimension.getHeight());
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IGUIElementDefinition#getTabIndex()
	 */
	public final int getTabIndex()
	{
		return this.tabIndex;
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.IGUIElementDefinition#getPaneIndex()
   */
  public final int getPaneIndex()
  {
    return this.paneIndex;
  }
  
  public final CastorGuiElement toJacob(ConvertToJacobOptions options)
  {
    CastorGuiElement jacobGuiElement = new CastorGuiElement();
    jacobGuiElement.setCastorGuiElementChoice(new CastorGuiElementChoice());
    jacobGuiElement.setName(getName());
    jacobGuiElement.setDescription(getDescription());
    jacobGuiElement.setVisible(isVisible());
    jacobGuiElement.setTabIndex(getTabIndex());
    jacobGuiElement.setPaneIndex(getPaneIndex());
    jacobGuiElement.setProperty(getCastorProperties());
    toJacob(jacobGuiElement, options);
    return jacobGuiElement;
  }

  protected abstract void toJacob(CastorGuiElement jacobGuiElement, ConvertToJacobOptions options);
  
	/**
	 * @return Returns the dimension.
	 */
	protected Dimension getDimension()
	{
    if (null == dimension)
      throw new UnsupportedOperationException("Not supported for this gui element");
		return dimension;
	}

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
	 */
	public final void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
	{
    // not needed so far
	}

}

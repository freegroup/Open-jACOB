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

import de.tif.jacob.core.definition.ActionType;
import de.tif.jacob.core.definition.IGUIElementDefinition;
import de.tif.jacob.core.definition.impl.AbstractDefinition;
import de.tif.jacob.core.definition.impl.AbstractElement;
import de.tif.jacob.core.definition.impl.AbstractGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.CastorCaption;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Caption extends AbstractGuiElement implements IGUIElementDefinition
{
	static public final transient String RCS_ID = "$Id: Caption.java,v 1.6 2009/07/27 15:06:11 freegroup Exp $";
	static public final transient String RCS_REV = "$Revision: 1.6 $";

	private final Dimension dimension;
	private final boolean  ellipsis;
	private final String label;
	private Color color;
  private final ActionType actionType;
  private final Alignment.Vertical valign;
  private final Alignment.Horizontal halign;
  private final FontDefinition font;
  
  public Caption(Dimension dimension, String label,String eventHandler, ActionType actionType, Alignment.Horizontal halign, Alignment.Vertical valign, FontDefinition font,boolean ellipsis, String color)
  {
    super(null,null,eventHandler);
    if (halign == null)
      throw new NullPointerException("halign is null");
    if (valign == null)
      throw new NullPointerException("valign is null");
    this.dimension = dimension;
    this.label = label;
    this.actionType = actionType;
    this.valign = valign;
    this.halign = halign;
    this.font = font;
    this.ellipsis = ellipsis;
    try
    {
      this.color = new javax.swing.text.html.StyleSheet().stringToColor(color);
    }
    catch(Exception exc)
    {
      this.color = null;
    }
  }

  public Color getBackgroundColor()
  {
    return null;
  }

  public Color getBorderColor()
  {
    return null;
  }

  public int getBorderWith()
  {
    return -1;
  }

  public Caption(Dimension dimension, String label,String eventHandler, ActionType actionType, Alignment.Horizontal halign, Alignment.Vertical valign,boolean ellipsis, String color)
  {
    this(dimension, label,eventHandler, actionType, halign, valign, null, ellipsis, color);
  }

	public Caption(CastorCaption caption, FontDefinition font,String eventHandler, ActionType actionType,boolean ellipsis, String color)
	{
    super(null,null,eventHandler);
		this.dimension = caption.getDimension() == null ? null : new Dimension(caption.getDimension());
		this.label = caption.getLabel();
    this.actionType = actionType;
    this.halign = Alignment.fromJacob(caption.getHalign());
    this.valign = Alignment.fromJacob(caption.getValign());
    this.font = font;
    this.ellipsis = ellipsis;
    try
    {
      this.color = new javax.swing.text.html.StyleSheet().stringToColor(color);
    }
    catch(Exception exc)
    {
      this.color = null;
    }
	}

  
  public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
  {
  }

  public FontDefinition getFont()
  {
    return font;
  }
  
	/**
	 * @return Returns the label.
	 */
	public final String getLabel()
	{
		return label;
	}

	/**
	 * @return Returns the color of the caption or null to use the default.
	 */
	public final Color getColor()
	{
	  return color;
	}

  
	/**
	 * @return Returns the dimension.
	 */
	public final Rectangle getRectangle()
	{
		if (dimension != null)
			return new Rectangle(this.dimension.getLeft(), this.dimension.getTop(), this.dimension.getWidth(), this.dimension.getHeight());
		return new Rectangle();
	}

	/**
	 * @return Returns the caption.
	 */
	public final Caption getCaption()
	{
		return null;
	}


	public final boolean isVisible()
	{
		return true;
	}

	/**
	 * @return Returns the dimension.
	 */
	public final Dimension getDimension()
	{
		return dimension;
	}

	public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent)
	{
		return factory.createCaption(app, parent, this);
	}

  /**
   * Returns the caption action, if existing.
   * 
   * @return the associated action or <code>null</code>
   */
  public ActionType getActionType()
  {
    return this.actionType;
  }
  
  /**
   * @return Returns the halign.
   */
  public Alignment.Horizontal getHorizontalAlign()
  {
    return halign;
  }
  
  /**
   * @return Returns the valign.
   */
  public Alignment.Vertical getVerticalAlign()
  {
    return valign;
  }
  
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.tif.jacob.core.definition.IGUIElementDefinition#getTabIndex()
	 */
	public int getTabIndex()
	{
		return -1;
	}

	public boolean getEllipsis()
	{
	  return this.ellipsis;
	}
	
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.IGUIElementDefinition#getPaneIndex()
   */
  public int getPaneIndex()
  {
    return 0;
  }
  
	protected CastorCaption toJacob()
	{
		CastorCaption jacobCaption = new CastorCaption();
		if (dimension != null)
			jacobCaption.setDimension(this.dimension.toJacob());
		jacobCaption.setHalign(this.halign.toJacob());
		jacobCaption.setValign(this.valign.toJacob());
		jacobCaption.setLabel(getLabel());
    if (this.font != null)
      jacobCaption.setFont(this.font.toJacob());
    if (this.actionType != null)
      jacobCaption.setAction(this.actionType.toJacob());
		return jacobCaption;
	}

}

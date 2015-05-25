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

import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.Label;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class LabelDefinition extends GUIElementDefinition
{
  static public final transient String RCS_ID = "$Id: LabelDefinition.java,v 1.4 2010/08/19 09:33:32 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  private final Alignment.Vertical valign;
  private final Alignment.Horizontal halign;

  private final ITableField localTableField;
	private final ITableAlias localTableAlias;

	private final String nullDefaultValue;
	

  /**
	 * @param name
	 * @param eventHandler
	 * @param visible
	 * @param tabIndex
	 * @param caption
	 */
	public LabelDefinition(String name, String eventHandler, boolean visible, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField, String nullDefaultValue,  int borderWith,  String borderColor, String backgroundColor)
  {
    super(name, null, eventHandler, caption.getDimension(), visible, tabIndex, paneIndex, caption, borderWith, borderColor, backgroundColor);

    this.halign = caption.getHorizontalAlign();
    this.valign = caption.getVerticalAlign();
    this.nullDefaultValue = nullDefaultValue==null?"":nullDefaultValue;
		// plausibility check
		if((localTableAlias==null && localTableField!=null ) || (localTableAlias!=null && localTableField==null))
		{
			throw new RuntimeException("Table alias and field doesn't match [table="+localTableAlias+"][field="+localTableField+"]");
		}
		this.localTableField = localTableField;
		this.localTableAlias = localTableAlias;
  }

  

	/**
	 * Liefert bei einem Datenbank gebundenem Label den default Wert zurück der angezeigt werden soll, wenn in der Datenbank NULL steht.
	 * 
	 * 
	 * @return the default value. Returns never <b>null</b>
	 * @since 2.10
	 */
  public String getNullDefaultValue()
  {
    return nullDefaultValue;
  }

  public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent )
  {
    return factory.createLabel(app, parent,this); 
  }
  
  protected final void toJacob(CastorGuiElement jacobGuiElement, ConvertToJacobOptions options)
  {
    Label jacobLabel = new Label();
    jacobLabel.setCaption(getCaption().toJacob());
    if (this.localTableField != null)
      jacobLabel.setTableField(this.localTableField.getName());
    jacobGuiElement.getCastorGuiElementChoice().setLabel(jacobLabel);
  }

  /**
   * @return Returns the halign.
   */
  public Alignment.Horizontal getHorizontalAlign()
  {
    return halign;
  }
  
	/**
	 * @return the table field this input field is connected to or <code>null</code>
	 *         if this input field is not connected to a table field
	 */
	public ITableField getLocalTableField()
	{
		return localTableField;
	}

	/**
	 * @return the table alias of the table field this input field is connected
	 *         to or <code>null</code> if this input field is not connected to
	 *         a table field
	 */
	public final ITableAlias getLocalTableAlias()
	{
		return localTableAlias;
	}
 
	/**
   * @return Returns the valign.
   */
  public Alignment.Vertical getVerticalAlign()
  {
    return valign;
  }
}

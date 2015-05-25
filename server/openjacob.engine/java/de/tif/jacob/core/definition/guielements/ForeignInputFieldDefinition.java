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

import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IBrowserDefinition;
import de.tif.jacob.core.definition.IOneToManyRelation;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElement;
import de.tif.jacob.core.definition.impl.jad.castor.ForeignInputField;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ForeignInputFieldDefinition extends InputFieldDefinition
{
  static public final transient String RCS_ID = "$Id: ForeignInputFieldDefinition.java,v 1.4 2009/02/11 12:18:04 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  private final ITableField foreignTableField;
  private final IOneToManyRelation relationToUse;
  private final IBrowserDefinition browserToUse;
  private final IRelationSet relationSet;
  private final Filldirection fillDirection;
  private final boolean asComboBox;
  
	public ForeignInputFieldDefinition(String name, String description,String inputHint, String eventHandler, Dimension dimension, boolean visible, boolean readonly, boolean asComboBox,int tabIndex, int paneIndex, Caption caption, IBrowserDefinition browserToUse, IOneToManyRelation relationToUse, IRelationSet relationSet, Filldirection fillDirection, ITableField foreignTableField, FontDefinition font)
	{
		super(name, description, inputHint, eventHandler, dimension, visible, readonly, tabIndex, paneIndex, caption, font, null);
    
    if (null == foreignTableField)
      throw new NullPointerException("foreignTableField is null");
    this.foreignTableField = foreignTableField;
    
    if (null == relationToUse)
      throw new NullPointerException("relationToUse is null");
    this.relationToUse = relationToUse;
    
    if (null == browserToUse)
      throw new NullPointerException("browserToUse is null");
    this.browserToUse = browserToUse;
    
    if (null == relationSet)
      throw new NullPointerException("relationSet is null");
    this.relationSet = relationSet;
    
    if (null == fillDirection)
      throw new NullPointerException("fillDirection is null");
    this.fillDirection = fillDirection;
    this.asComboBox = asComboBox;
	}

  public boolean asComboBox()
  {
    return asComboBox;
  }
  
  /**
   * @return Returns the foreignTableAlias.
   */
  public ITableAlias getForeignTableAlias()
  {
    return this.relationToUse.getFromTableAlias();
  }


  /**
   * @return Returns the foreignTableField.
   */
  public ITableField getForeignTableField()
  {
    return foreignTableField;
  }


	/**
	 * @return Returns the browserToUse.
	 */
	public IBrowserDefinition getBrowserToUse()
	{
		return browserToUse;
	}

	/**
	 * @return Returns the fillDirection.
	 */
	public Filldirection getFillDirection()
	{
		return fillDirection;
	}

	/**
	 * @return Returns the relationSet.
	 */
	public IRelationSet getRelationSet()
	{
		return relationSet;
	}

	/**
	 * @return Returns the relationToUse.
	 */
	public IOneToManyRelation getRelationToUse()
	{
		return relationToUse;
	}
  
  public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent )
  {
    return factory.createForeignField(app, parent,this); 
  }
  
  protected void toJacob(CastorGuiElement jacobGuiElement, ConvertToJacobOptions options)
  {
    ForeignInputField jacobForeignInputField = new ForeignInputField();
    if (null != getCaption())
    	jacobForeignInputField.setCaption(getCaption().toJacob());
    jacobForeignInputField.setDimension(getDimension().toJacob());
    jacobForeignInputField.setForeignAlias(getForeignTableAlias().getName());
    jacobForeignInputField.setForeignTableField(getForeignTableField().getName());
    jacobForeignInputField.setRelationToUse(getRelationToUse().getName());
    jacobForeignInputField.setRelationset(getRelationSet().getName());
    jacobForeignInputField.setBrowserToUse(getBrowserToUse().getName());
    jacobForeignInputField.setFilldirection(this.fillDirection.toJad());
    jacobForeignInputField.setAsComboBox(this.asComboBox);
    jacobGuiElement.getCastorGuiElementChoice().setForeignInputField(jacobForeignInputField);
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.guielements.InputFieldDefinition#isReadOnly()
	 */
	public final boolean isReadOnly()
	{
    if (super.isReadOnly())
      return true;
    
      IOneToManyRelation oneToManyRelation =  this.relationToUse;
      return ((ITableField) oneToManyRelation.getToForeignKey().getTableFields().get(0)).isReadOnly();
	}

}

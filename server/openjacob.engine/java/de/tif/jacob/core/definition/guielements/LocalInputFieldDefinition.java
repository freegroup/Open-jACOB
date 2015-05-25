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
import de.tif.jacob.core.definition.impl.jad.castor.LocalInputField;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class LocalInputFieldDefinition extends InputFieldDefinition
{
  static public final transient String RCS_ID = "$Id: LocalInputFieldDefinition.java,v 1.3 2009/02/11 12:18:04 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";

  private final ITableField localTableField;
	private final ITableAlias localTableAlias;
 
	protected LocalInputFieldDefinition(String name, String description,String inputHint, String eventHandler, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField, FontDefinition font, Alignment.Horizontal halign)
	{
		super(name, description, inputHint, eventHandler, position, visible, readonly, tabIndex, paneIndex, caption, font, halign);
		
		// plausibility check
		if((localTableAlias==null && localTableField!=null ) || (localTableAlias!=null && localTableField==null))
		{
			throw new RuntimeException("Table alias and field doesn't match [table="+localTableAlias+"][field="+localTableField+"]");
		}
		this.localTableField = localTableField;
		this.localTableAlias = localTableAlias;
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
  
  protected final void toJacob(CastorGuiElement jacobGuiElement, ConvertToJacobOptions options)
  {
    LocalInputField jacobLocalInputField = new LocalInputField();
    if (null != this.localTableField)
    	jacobLocalInputField.setTableField(this.localTableField.getName());
    jacobGuiElement.getCastorGuiElementChoice().setLocalInputField(jacobLocalInputField);
    toJacob(jacobLocalInputField);
  }
  
  protected abstract void toJacob(LocalInputField jacobLocalInputField);
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.guielements.InputFieldDefinition#isReadOnly()
   */
  public final boolean isReadOnly()
  {
    // if readonly is desired, then this overrules readonly setting on table field
    if (super.isReadOnly())
      return true;
    
    // otherwise set to readonly, if the table field is read only
	  if(getLocalTableField()==null)
	    return false;
		return getLocalTableField().isReadOnly();
	}

}

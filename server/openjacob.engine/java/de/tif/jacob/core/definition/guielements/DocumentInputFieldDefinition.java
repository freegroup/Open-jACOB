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
import de.tif.jacob.core.definition.impl.jad.castor.DocumentInput;
import de.tif.jacob.core.definition.impl.jad.castor.LocalInputField;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DocumentInputFieldDefinition extends LocalInputFieldDefinition
{
  static public final transient String RCS_ID = "$Id: DocumentInputFieldDefinition.java,v 1.3 2009/02/11 12:18:04 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.3 $";
  
	/**
	 * @param name
	 * @param description
	 * @param eventHandler
	 * @param position
	 * @param visible
	 * @param readonly
	 * @param tabIndex
	 * @param caption
	 * @param localTableAlias
	 * @param localTableField
	 */
	public DocumentInputFieldDefinition(String name, String description,String inputHint, String eventHandler, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption,
      ITableAlias localTableAlias, ITableField localTableField, FontDefinition font)
  {
    super(name, description,inputHint, eventHandler, position, visible, readonly, tabIndex, paneIndex, caption, localTableAlias, localTableField, font, null);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.IGUIElementDefinition#createRepresentation(de.tif.jacob.screen.IApplicationFactory, de.tif.jacob.screen.IApplication, de.tif.jacob.screen.IGuiElement)
	 */
	public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent)
	{
    return factory.createDocument(app, parent, this); 
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.guielements.LocalInputFieldDefinition#toJacob(de.tif.jacob.core.jad.castor.LocalInputField)
	 */
	protected void toJacob(LocalInputField jacobLocalInputField)
	{
    DocumentInput jacobDocumentInput = new DocumentInput();
    jacobLocalInputField.setDocumentInput(jacobDocumentInput);
    jacobDocumentInput.setDimension(getDimension().toJacob());
    if (null != getCaption())
      jacobDocumentInput.setCaption(getCaption().toJacob());
  }

}

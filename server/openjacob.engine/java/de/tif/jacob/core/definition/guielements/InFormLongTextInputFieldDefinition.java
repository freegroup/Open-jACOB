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
import de.tif.jacob.core.definition.fieldtypes.LongTextEditMode;
import de.tif.jacob.core.definition.fieldtypes.LongTextFieldType;
import de.tif.jacob.core.definition.impl.jad.castor.LocalInputField;
import de.tif.jacob.core.definition.impl.jad.castor.LongTextInput;
import de.tif.jacob.core.definition.impl.jad.castor.types.LongTextInputContentTypeType;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IGuiElement;
import de.tif.jacob.screen.impl.IApplicationFactory;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InFormLongTextInputFieldDefinition extends LocalInputFieldDefinition
{
  static public final transient String RCS_ID = "$Id: InFormLongTextInputFieldDefinition.java,v 1.6 2010/08/04 13:13:38 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.6 $";
  
  private final LongTextInputMode mode;
  private final LongTextEditMode editMode;
  private final boolean wordwrap;
  private final String contentType;
  
	public InFormLongTextInputFieldDefinition(String name, String description,String inputHint, String eventHandler, Dimension position, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption, ITableAlias localTableAlias, ITableField localTableField, LongTextInputMode mode, boolean wordwrap, FontDefinition font, String contentType)
	{
		super(name, description,inputHint, eventHandler, position, visible, readonly, tabIndex, paneIndex, caption, localTableAlias, localTableField, font, null);
    this.mode = mode;
    this.wordwrap = wordwrap;
    this.editMode = getEditMode(mode, localTableField);
    this.contentType = contentType==null?LongTextInputContentTypeType.TEXT_PLAIN.toString():contentType;
	}

  private static LongTextEditMode getEditMode(LongTextInputMode mode, ITableField localTableField)
  {
    // Step 1: mode defined on long text definition?
    if (mode != null && mode.getEditMode() != null)
    {
      return mode.getEditMode();
    }
    // Step 2: No mode defined on long text definition -> try to get from underlying data field
    if (localTableField != null && localTableField.getType() instanceof LongTextFieldType)
    {
      LongTextFieldType field = (LongTextFieldType) localTableField.getType();
      if (field.getEditMode() != null)
        return field.getEditMode();
    }
    // Step 3: Return full edit as default
    return LongTextFieldType.FULLEDIT;
  }

  public IGuiElement createRepresentation(IApplicationFactory factory, IApplication app, IGuiElement parent )
  {
    return factory.createInFormLongText(app, parent,this); 
  }
  
	/**
	 * @return Returns the mode.
	 * @deprecated use {@link #getEditMode()}
	 */
	public LongTextInputMode getMode()
	{
		return mode;
	}

	public String getContentType()
	{
	  return contentType;
	}
	
  /**
   * Returns the edit mode of this long text definition.
   * 
   * @return the edit mode
   * @since 2.9
   */
  public LongTextEditMode getEditMode()
  {
    return this.editMode;
  }

  /**
   * @return Returns the wordwrap.
   */
  public boolean isWordwrap()
  {
    return wordwrap;
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.guielements.LocalInputFieldDefinition#toJacob(de.tif.jacob.core.jad.castor.LocalInputField)
	 */
	protected void toJacob(LocalInputField jacobLocalInputField)
	{
    LongTextInput jacobLongTextInput = new LongTextInput();
    jacobLocalInputField.setLongTextInput(jacobLongTextInput);
    jacobLongTextInput.setDimension(getDimension().toJacob());
    if (null != getCaption())
      jacobLongTextInput.setCaption(getCaption().toJacob());
    if (getMode() != null)
      jacobLongTextInput.setMode(getMode().toJacob());
    jacobLongTextInput.setInForm(true);
    jacobLongTextInput.setHtmlInput(false);
    jacobLongTextInput.setWordWrap(this.wordwrap);
  }

}

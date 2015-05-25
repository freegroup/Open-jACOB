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

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class InputFieldDefinition extends GUIElementDefinition
{
  private final FontDefinition font;
  private final Alignment.Horizontal halign;
	private final boolean readonly;
  private final String  inputHint;
  
	public InputFieldDefinition(String name, String description,String inputHint, String eventHandler, Dimension dimension, boolean visible, boolean readonly, int tabIndex, int paneIndex, Caption caption,  FontDefinition font,  Alignment.Horizontal halign)
	{
		super(name, description, eventHandler, dimension, visible, tabIndex, paneIndex, caption,-1,null,null);

    this.halign = halign;
		this.readonly = readonly;
    this.inputHint = inputHint;
    this.font = font;
	}

	/**
	 * Checks whether the field is read only in record update mode.
	 * 
	 * @return <code>true</code> if read only, otherwise <code>false</code>
	 */
	public boolean isReadOnly()
	{
		return this.readonly;
	}
  
  public String getInputHint()
  {
    return inputHint;
  }
  
  public FontDefinition getFont()
  {
    return this.font;
  }

  /**
   * @return Returns the halign.
   */
  public Alignment.Horizontal getHorizontalAlign()
  {
    return halign;
  }
}

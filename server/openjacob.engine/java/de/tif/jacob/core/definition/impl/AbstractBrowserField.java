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

package de.tif.jacob.core.definition.impl;

import de.tif.jacob.core.definition.IBrowserField;
import de.tif.jacob.core.definition.guielements.InputFieldDefinition;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserFieldChoice;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractBrowserField extends AbstractElement implements IBrowserField
{
	static public transient final String RCS_ID = "$Id: AbstractBrowserField.java,v 1.6 2010/11/12 20:53:54 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.6 $";

	private final String label;
	private final boolean visible;
  private final boolean configureable;
  private boolean readonly;
  private InputFieldDefinition inputFieldDefinition;
  private AbstractBrowserDefinition browserDefinition;
  public int fieldIndex;
  
	/**
	 *  
	 */
	public AbstractBrowserField(String name, String label, boolean visible, boolean readonly, boolean configureable)
	{
		super(name, null);
		this.label = label;
		this.visible = visible;
    this.readonly = readonly;
    this.configureable = configureable;
  }

  public final int hashCode()
  {
    return 31 * getName().hashCode() + this.browserDefinition.hashCode();
  }

  public final boolean equals(Object obj)
  {
    if (this == obj)
      return true;

    if (obj == null)
      return false;
    
    if (getClass() != obj.getClass())
      return false;
 
    AbstractBrowserField other = (AbstractBrowserField) obj;

    if(!getName().equals(other.getName()))
      return false;
    
    if (!this.browserDefinition.equals(other.browserDefinition))
      return false;

    return true;
  }

	/**
	 * @return Returns the label.
	 */
	public final String getLabel()
	{
		return label;
	}

	/**
	 * @return Returns the invisible.
	 */
	public final boolean isVisible()
	{
		return visible;
	}

  public final boolean isConfigureable()
  {
    return configureable;
  }

  protected final CastorBrowserField toJacob()
	{
		CastorBrowserField jacobBrowserField = new CastorBrowserField();
    jacobBrowserField.setCastorBrowserFieldChoice(new CastorBrowserFieldChoice());
		toJacob(jacobBrowserField);
		return jacobBrowserField;
	}

	protected void toJacob(CastorBrowserField jacobBrowserField)
	{
		jacobBrowserField.setName(getName());
		jacobBrowserField.setLabel(getLabel());
    jacobBrowserField.setVisible(isVisible());
    jacobBrowserField.setReadonly(isReadonly());
    jacobBrowserField.setConfigureable(isConfigureable());
    
    // handle properties
    jacobBrowserField.setProperty(getCastorProperties());
  }
  
	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractElement#postProcessing(de.tif.jacob.core.definition.impl.AbstractDefinition, de.tif.jacob.core.definition.impl.AbstractElement)
	 */
	public void postProcessing(AbstractDefinition definition, AbstractElement parent) throws Exception
	{
    this.browserDefinition = (AbstractBrowserDefinition) parent;
    
    // Browser definition could be used for searching & editing as well
    String inputFieldName = getName() + "Input";
    this.inputFieldDefinition = createInputFieldDefinition(inputFieldName);
	}
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.IBrowserTableField#getInputFieldDefinition()
   */
  public final InputFieldDefinition getInputFieldDefinition()
  {
    return this.inputFieldDefinition;
  }

  /**
   * @param inputFieldDefinition
   *          The inputFieldDefinition to set.
   */
  protected abstract InputFieldDefinition createInputFieldDefinition(String inputFieldName);

  /**
   * @return Returns the readonly.
   */
  public final boolean isReadonly()
  {
    return readonly;
  }
  
	/**
	 * @return Returns the browserDefinition.
	 */
	public final AbstractBrowserDefinition getBrowserDefinition()
	{
    if (null == this.browserDefinition)
      throw new IllegalStateException("browserDefinition has not been initialised so far");
    return browserDefinition;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public final String toString()
	{
	  if (this.browserDefinition == null)
	    return super.toString();
		return this.browserDefinition+"."+getName();
	}

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.IBrowserField#getFieldIndex()
   */
  public int getFieldIndex()
  {
    return this.fieldIndex;
  }

  /**
   * @param fieldIndex The fieldIndex to set.
   */
  protected final void setFieldIndex(int fieldIndex)
  {
    this.fieldIndex = fieldIndex;
  }
  
}

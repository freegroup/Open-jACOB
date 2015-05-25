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

package de.tif.qes.adl.element;

import de.tif.jacob.core.definition.guielements.InputFieldDefinition;
import de.tif.jacob.core.definition.guielements.TextInputFieldDefinition;
import de.tif.jacob.core.definition.impl.AbstractBrowserField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserField;
import de.tif.jacob.core.definition.impl.jad.castor.CastorBrowserRuntimeFieldType;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADLBrowserFormulaField extends AbstractBrowserField
{
  static public final transient String RCS_ID = "$Id: ADLBrowserFormulaField.java,v 1.4 2009-02-11 12:11:32 herz Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";
  
  private final String formula;
  
	/**
	 * @param name
	 * @param label
	 * @param invisible
	 */
  public ADLBrowserFormulaField(String name, String label, String formula, Boolean invisible)
  {
    // set readonly to false!
    super(name, label, !invisible.booleanValue(), false,false);
    this.formula = formula;
  }

  public ADLBrowserFormulaField(String name, String label, String formula, String colformula, Boolean invisible)
  {
    // IBIS: implementation of colformula missing
    
    // set readonly to false!
    super(name, label, !invisible.booleanValue(), false, false);
    this.formula = formula;
  }

  protected void toJacob(CastorBrowserField jacobBrowserField)
  {
    super.toJacob(jacobBrowserField);
    CastorBrowserRuntimeFieldType jacobBrowserRuntimeField = new CastorBrowserRuntimeFieldType(); 
    jacobBrowserField.getCastorBrowserFieldChoice().setRuntime(jacobBrowserRuntimeField);
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractBrowserField#createInputFieldDefinition(java.lang.String)
	 */
	protected InputFieldDefinition createInputFieldDefinition(String inputFieldName)
	{
    return new TextInputFieldDefinition(inputFieldName, null, null, null, null, isVisible(), isReadonly(), false, -1, 0, null, null, null, null, null);
	}

}

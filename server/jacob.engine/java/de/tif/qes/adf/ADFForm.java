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

package de.tif.qes.adf;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import de.tif.jacob.core.definition.impl.AbstractJacobFormDefinition;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm;
import de.tif.qes.IQeScriptContainer;
import de.tif.qes.QesLayoutAdjustment;
import de.tif.qes.adf.castor.Form;
import de.tif.qes.adf.castor.Group;
import de.tif.qes.adl.element.ADLDefinition;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class ADFForm extends AbstractJacobFormDefinition implements IQeScriptContainer
{
//  private final ADFDimension dimension;
  private final Set scripts;
  
	/**
	 * 
	 */
	protected ADFForm(Form form, ADLDefinition definition, QesLayoutAdjustment layout) throws Exception
	{
	  // use default event handler class name
    super(form.getName(), form.getCaption().getValue(),false, null, null, true);
//    this.dimension = new ADFDimension(0, 0, form.getWidth(), form.getHeight());
    
    for(int i=0; i<form.getGroups().getGroupCount();i++)
    {
      Group group = form.getGroups().getGroup(i);
      addGroup(new ADFGroup(group, definition, this, layout));
    }
    
    // fetch scripts
    this.scripts = ADFScript.fetchScripts(form.getScripts());
  }



  /**
   * @return Returns the scripts.
   */
  public Set getScripts()
  {
  	return scripts;
  }

	/* (non-Javadoc)
	 * @see de.tif.jacob.core.definition.impl.AbstractFormDefinition#toJacob(de.tif.jacob.core.definition.impl.jad.castor.CastorForm, de.tif.jacob.core.definition.impl.ConvertToJacobOptions)
	 */
	public void toJacob(CastorJacobForm jacobForm, ConvertToJacobOptions options)
	{
		super.toJacob(jacobForm, options);
    
    ADFScript.putScriptsToProperties(getScripts(), options, this);
    jacobForm.setProperty(getCastorProperties());
  }

}

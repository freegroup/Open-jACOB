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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.jacob.core.definition.impl.jad.castor.CastorJacobForm;
import de.tif.jacob.core.definition.impl.jad.castor.Jacob;
import de.tif.qes.QesLayoutAdjustment;
import de.tif.qes.adf.castor.Form;
import de.tif.qes.adf.castor.Forms;
import de.tif.qes.adl.element.ADLDefinition;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADFDefinition
{
  private final Map forms = new HashMap();
  
  protected ADFDefinition(Forms castorForms, ADLDefinition definition, QesLayoutAdjustment layout) throws Exception
  {
    for(int i=0; i<castorForms.getFormCount();i++)
    {
      Form form = castorForms.getForm(i);
      forms.put(form.getName(),new ADFForm(form, definition, layout));
    }
  }
  
  /**
   * @return Iterator{ADFForm}
   */
  public Iterator getForms()
  {
    return this.forms.values().iterator();
  }
  
  public ADFForm getForm(String name)
  {
    ADFForm result = (ADFForm) this.forms.get(name);
    if (null == result)
    {
      throw new RuntimeException("No form " + name + " found!");
    }
    return result;
  }
  
  public void toJacob(Jacob jacob, ConvertToJacobOptions options)
  {
    // fetch form data (attention: use TreeSet for sorted output!)
    de.tif.jacob.core.definition.impl.jad.castor.Forms jacobForms = new de.tif.jacob.core.definition.impl.jad.castor.Forms();
    jacob.setForms(jacobForms);
    Iterator iter = new TreeSet(this.forms.values()).iterator();
    while (iter.hasNext())
    {
      CastorJacobForm jacobForm = new CastorJacobForm();
      ADFForm adfForm = (ADFForm) iter.next();
      adfForm.toJacob(jacobForm, options);
      jacobForms.addForm(jacobForm);
    }
  }
}

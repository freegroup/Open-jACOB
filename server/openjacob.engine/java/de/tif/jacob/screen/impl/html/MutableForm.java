/*
 * Created on 20.06.2007
 *
 */
package de.tif.jacob.screen.impl.html;

import de.tif.jacob.core.definition.IMutableFormDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IMutableForm;

public class MutableForm extends Form implements IMutableForm
{
  static public final transient String RCS_ID = "$Id: MutableForm.java,v 1.4 2009/02/23 10:26:03 ibissw Exp $";
  static public final transient String RCS_REV = "$Revision: 1.4 $";

  public MutableForm(IApplication app, IMutableFormDefinition form)
  {
    super(app, form);
  }

  
}

/*
 * Created on 20.06.2007
 *
 */
package de.tif.jacob.screen.impl.html;

import de.tif.jacob.core.definition.IHtmlFormDefinition;
import de.tif.jacob.core.definition.IMutableFormDefinition;
import de.tif.jacob.core.definition.impl.AbstractMutableFormDefinition;
import de.tif.jacob.screen.IApplication;
import de.tif.jacob.screen.IHtmlForm;
import de.tif.jacob.screen.IMutableForm;

public class HtmlForm extends Form implements IHtmlForm
{

  public HtmlForm(IApplication app, IHtmlFormDefinition form)
  {
    super(app, form);
  }

}

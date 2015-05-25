/*
 * Created on 04.12.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import org.eclipse.ui.views.properties.IPropertySource;

public interface AbstractTargetModel  extends IPropertySource
{
  public void firePropertyChange(String propertyName, Object oldValue, Object newValue);
  public String getId();
  public RulesetModel getRulesetModel();
}

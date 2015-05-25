/*
 * Created on 07.05.2008
 *
 */
package de.tif.jacob.designer.model;

import java.beans.PropertyChangeListener;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public interface ISelectionActionProvider
{
  public void removeElement(SelectionActionModel action);
  public void addElement(SelectionActionModel action);
  public List<SelectionActionModel> getActions();
  public String getName();
  public JacobModel getJacobModel();
  public String getGroupTableAlias();
  
  public void upElement(SelectionActionModel action);
  public void downElement(SelectionActionModel action);

  public void addPropertyChangeListener(PropertyChangeListener listener);
  public void removePropertyChangeListener(PropertyChangeListener listener);
  }

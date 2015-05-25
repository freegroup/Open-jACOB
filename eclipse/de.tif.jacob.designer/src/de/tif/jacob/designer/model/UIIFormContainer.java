/*
 * Created on 31.05.2007
 *
 */
package de.tif.jacob.designer.model;

public interface UIIFormContainer
{
  public void addElement(UIFormModel newForm);
  public void removeElement(UIFormModel form);
  public JacobModel getJacobModel();
  public void addElement(UIFormModel targetForm, UIFormModel dragForm);
  
}

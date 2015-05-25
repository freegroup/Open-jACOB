/*
 * Created on 26.11.2005
 *
 */
package de.tif.jacob.rule.editor.rules.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

public abstract class ObjectModel  implements IPropertySource
{
	public static String START_NODE_ID ="start";

	public final static String PROPERTY_MODEL_CHANGED  = "model changed";
  public final static String PROPERTY_PARAMETERS_CHANGED ="parameters of BO changed";
  public final static String SOURCE_CONNECTIONS_PROP = "Shape.SourceConn";
  public final static String TARGET_CONNECTIONS_PROP = "Shape.TargetConn";

  public final static String PROPERTY_POSITION_CHANGED = "position changed";
  public final static String PROPERTY_ELEMENT_ADDED    = "element added";
  public final static String PROPERTY_ELEMENT_REMOVED  = "element removed";
  
  private PropertyChangeSupport listeners;
  private IPropertyDescriptor[] descriptors;
  
  public ObjectModel()
  {
    listeners = new PropertyChangeSupport(this);
  }
  
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    listeners.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    listeners.removePropertyChangeListener(listener);
  }

  public void firePropertyChange(String propertyName, Object oldValue, Object newValue)
  {
   listeners.firePropertyChange(propertyName, oldValue, newValue);
  }
  
  public IPropertyDescriptor[] getPropertyDescriptors()
  {
    if (descriptors == null)
      descriptors = new IPropertyDescriptor[0];
    return descriptors;
  }

  public Object getPropertyValue(Object propName)
  {
    return null;
  }

  /*
   * final Object getPropertyValue(String propName) { return null; }
   */
  public boolean isPropertySet(Object propName)
  {
    return true;
  }

  public Object getEditableValue()
  {
    return this;
  }
  
  /*
   * final boolean isPropertySet(String propName) { return true; }
   */
  public void resetPropertyValue(Object propName)
  {
  }

  public void setPropertyValue(Object propName, Object val)
  {
  }
}

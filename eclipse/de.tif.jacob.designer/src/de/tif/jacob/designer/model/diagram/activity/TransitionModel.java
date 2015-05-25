/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
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
package de.tif.jacob.designer.model.diagram.activity;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.Transition;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.model.ApplicationModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazz.ClassUtil;


public class TransitionModel extends ObjectModel
{

  private Transition castor;
  private ActivityDiagramModel parent;
  
  public TransitionModel(JacobModel jacob, DiagramElementModel source, DiagramElementModel target)
  {
    super(jacob);
    castor = new Transition();
   
    connect(source, target);
    this.parent = source.getActivityDiagramModel();
  }

  public TransitionModel(JacobModel jacob,ActivityDiagramModel parent, Transition model)
  {
    super(jacob);
    this.castor     = model;
    this.parent     = parent;
  }

  
  
  public final String getDescription()
  {
    return StringUtil.toSaveString(getCastor().getDescription());
  }
  
  /**
   * 
   * @param name
   */
  public void setDescription(String name)
  {
    String save = getDescription();
    if (StringUtil.saveEquals(name, save))
      return;
    
    getCastor().setDescription(name);

    firePropertyChange(PROPERTY_DESCRIPTION_CHANGED, save, name);
  }

    
  /**
   * Returns the source endpoint of this connection.
   * 
   * @return a non-null Shape instance
   */
  public DiagramElementModel getSource()
  {
    return parent.getDiagramElementModel(castor.getFromNode());
  }

  /**
   * Returns the target endpoint of this connection.
   * 
   * @return a non-null Shape instance
   */
  public DiagramElementModel getTarget()
  {
    return parent.getDiagramElementModel(castor.getToNode());
  }

  public IPropertyDescriptor[] getPropertyDescriptors()
  {
		IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 1];
		for (int i = 0; i < superDescriptors.length; i++)
			descriptors[i] = superDescriptors[i];
		
		descriptors[superDescriptors.length ] = new TextPropertyGroupingDescriptor(ID_PROPERTY_DESCRIPTION,  "Description", PROPERTYGROUP_COMMON);
		
		return descriptors;
  }

  
  public Object getPropertyValue(Object propertyId)
  {
    if (ID_PROPERTY_DESCRIPTION.equals(propertyId))
      return getDescription();

    return super.getPropertyValue(propertyId);
  }

  /**
   * Set the property value for the given property id. If no matching id is
   * found, the call is forwarded to the superclass.
   * <p>
   * The property view uses the IDs from the IPropertyDescriptors array to set
   * the values of the corresponding properties.
   * </p>
   * 
   * @see #descriptors
   * @see #getPropertyDescriptors()
   */
  public void setPropertyValue(Object propertyId, Object value)
  {
    if (ID_PROPERTY_DESCRIPTION.equals(propertyId))
      setDescription((String) value);
  }
  
    /**
   * Reconnect to a different source and/or target shape. The connection will
   * disconnect from its current attachments and reconnect to the new source and
   * target.
   * 
   * @param newSource
   *          a new source endpoint for this connection (non null)
   * @param newTarget
   *          a new target endpoint for this connection (non null)
   * @throws IllegalArgumentException
   *           if any of the paramers are null or newSource == newTarget
   */
  public void connect(DiagramElementModel newSource, DiagramElementModel newTarget)
  {
    if (newSource == null || newTarget == null || newSource == newTarget)
    {
      throw new IllegalArgumentException();
    }
    castor.setFromNode(newSource.getName());
    castor.setToNode(newTarget.getName());
    newTarget.addConnection(this);
    newSource.addConnection(this);
  }

  public final void setActivityDiagramModel(ActivityDiagramModel parent)
  {
    this.parent = parent;
  }

  public final ActivityDiagramModel getActivityDiagramModel()
  {
    return parent;
  }

  public String getError()
  {
    return null;
  }

  public String getInfo()
  {
    return null;
  }

  public String getWarning()
  {
    return null;
  }

  public boolean isInUse()
  {
    return false;
  }
  public Transition getCastor()
  {
    return castor;
  }
  
  @Override
  public String getName()
  {
    return "<unused>";
  }
  
  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }

  @Override
  public ObjectModel getParent()
  {
    return getActivityDiagramModel();
  }
}

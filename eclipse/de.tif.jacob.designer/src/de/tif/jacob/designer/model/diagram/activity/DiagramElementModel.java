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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import de.tif.jacob.core.definition.impl.jad.castor.CastorDimension;
import de.tif.jacob.core.definition.impl.jad.castor.DiagramNode;
import de.tif.jacob.core.definition.impl.jad.castor.DiagramNodeChoice;
import de.tif.jacob.designer.editor.jacobform.misc.IntegerPropertyGroupingDescriptor;
import de.tif.jacob.designer.editor.jacobform.misc.TextPropertyGroupingDescriptor;
import de.tif.jacob.designer.model.ApplicationModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazz.ClassUtil;

/**
 * Abstract prototype of a shape. Has a size (width and height), a location (x
 * and y position) and a list of incoming and outgoing connections. Use
 * subclasses to instantiate a specific shape.
 * 
 * @see de.tif.jacob.designer.model.diagram.activity.ActivityModel
 * @see de.tif.jacob.designer.model.diagram.activity.EllipticalShapeModel
 * @author Elias Volanakis
 */
public abstract class DiagramElementModel extends ObjectModel
{
  public static final String SOURCE_CONNECTIONS_PROP = "Shape.SourceConn";
  public static final String TARGET_CONNECTIONS_PROP = "Shape.TargetConn";

  private ActivityDiagramModel parent;
  private final   DiagramNode model;
  
  
  public DiagramElementModel()
  {
    model = new DiagramNode();
    model.setName("activity"+System.currentTimeMillis());
    model.setDiagramNodeChoice(new DiagramNodeChoice());
		model.setDimension(new CastorDimension());
  }
  
  public DiagramElementModel(JacobModel jacob,ActivityDiagramModel parent, DiagramNode model)
  {
    super(jacob);
    this.parent     = parent;
    this.model      = model;
  }

  public final DiagramNode getCastor()
  {
    return model;
  }
  
  
  public final String getName()
  {
    return getCastor().getName();
  }
  
  /**
   * 
   * @param name
   */
  public void setName(String name)
  {
    String save = getName();
    if (StringUtil.saveEquals(name, save))
      return;
    
    getCastor().setName(name);

    firePropertyChange(PROPERTY_NAME_CHANGED, save, name);
  }

  public final void setActivityDiagramModel(ActivityDiagramModel parent)
  {
    this.parent = parent;
  }

  public final ActivityDiagramModel getActivityDiagramModel()
  {
    return parent;
  }

  /**
   * Returns an array of IPropertyDescriptors for this shape.
   * <p>
   * The returned array is used to fill the property view, when the edit-part
   * corresponding to this model element is selected.
   * </p>
   * 
   * @see #descriptors
   * @see #getPropertyValue(Object)
   * @see #setPropertyValue(Object, Object)
   */
  public IPropertyDescriptor[] getPropertyDescriptors()
  {
		IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[superDescriptors.length + 5];
		for (int i = 0; i < superDescriptors.length; i++)
			descriptors[i] = superDescriptors[i];
		
		descriptors[superDescriptors.length]     = new IntegerPropertyGroupingDescriptor(ID_PROPERTY_LOCATION_X, "X", PROPERTYGROUP_LOCATION);
		descriptors[superDescriptors.length + 1] = new IntegerPropertyGroupingDescriptor(ID_PROPERTY_LOCATION_Y, "Y", PROPERTYGROUP_LOCATION);
		descriptors[superDescriptors.length + 2] = new IntegerPropertyGroupingDescriptor(ID_PROPERTY_HEIGHT    , "Height", PROPERTYGROUP_DIMENSION);
		descriptors[superDescriptors.length + 3] = new IntegerPropertyGroupingDescriptor(ID_PROPERTY_WIDTH     , "Width" , PROPERTYGROUP_DIMENSION);
		descriptors[superDescriptors.length + 4] = new TextPropertyGroupingDescriptor(ID_PROPERTY_NAME,  "Name", PROPERTYGROUP_COMMON);
		
		return descriptors;
  }

  /**
   * Return the property value for the given propertyId, or null.
   * <p>
   * The property view uses the IDs from the IPropertyDescriptors array to
   * obtain the value of the corresponding properties.
   * </p>
   * 
   * @see #descriptors
   * @see #getPropertyDescriptors()
   */
  public Object getPropertyValue(Object propertyId)
  {
    if (ID_PROPERTY_LOCATION_X.equals(propertyId))
      return Integer.toString(getLocation().x);
    if (ID_PROPERTY_LOCATION_Y.equals(propertyId))
      return Integer.toString(getLocation().y);
    if (ID_PROPERTY_HEIGHT.equals(propertyId))
      return Integer.toString(getSize().height);
    if (ID_PROPERTY_WIDTH.equals(propertyId))
      return Integer.toString(getSize().width);
    if (ID_PROPERTY_NAME.equals(propertyId))
      return getName();

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
    if (ID_PROPERTY_LOCATION_X.equals(propertyId))
    {
      int x = Integer.parseInt((String) value);
      setLocation(new Point(x, getLocation().y));
    }
    else if (ID_PROPERTY_LOCATION_Y.equals(propertyId))
    {
      int y = Integer.parseInt((String) value);
      setLocation(new Point(getLocation().x, y));
    }
    else if (ID_PROPERTY_HEIGHT.equals(propertyId))
    {
      int height = Integer.parseInt((String) value);
      setSize(new Dimension(getSize().width, height));
    }
    else if (ID_PROPERTY_WIDTH.equals(propertyId))
    {
      int width = Integer.parseInt((String) value);
      setSize(new Dimension(width, getSize().height));
    }
    else if (ID_PROPERTY_NAME.equals(propertyId))
    {
      setName((String)value);
    }
    else
    {
      super.setPropertyValue(propertyId, value);
    }
  }

  /**
   * Return a List of outgoing Connections.
   */
  public final List getSourceConnections()
  {
    List result = new ArrayList();
    Iterator iter = getActivityDiagramModel().getTransitionModels().iterator();
    while (iter.hasNext())
    {
      TransitionModel trans = (TransitionModel) iter.next();
      if(trans.getSource()==this)
        result.add(trans);
    }
    return result;
  }

  /**
   * Return a List of incoming Connections.
   */
  public final List getTargetConnections()
  {
    List result = new ArrayList();
    Iterator iter = getActivityDiagramModel().getTransitionModels().iterator();
    while (iter.hasNext())
    {
      TransitionModel trans = (TransitionModel) iter.next();
      if(trans.getTarget()==this)
        result.add(trans);
    }
    return result;
  }

  /**
   * Add an incoming or outgoing connection to this shape.
   * 
   * @param conn
   *          a non-null connection instance
   * @throws IllegalArgumentException
   *           if the connection is null or has not distinct endpoints
   */
  final void addConnection(TransitionModel conn)
  {
      firePropertyChange(SOURCE_CONNECTIONS_PROP, null, conn);
      firePropertyChange(TARGET_CONNECTIONS_PROP, null, conn);
  }


  /**
   * Remove an incoming or outgoing connection from this shape.
   * 
   * @param conn
   *          a non-null connection instance
   * @throws IllegalArgumentException
   *           if the parameter is null
   */
  final void removeConnection(TransitionModel conn)
  {
      firePropertyChange(SOURCE_CONNECTIONS_PROP, null, conn);
      firePropertyChange(TARGET_CONNECTIONS_PROP, null, conn);
  }

  /**
   * Set the Location of this shape.
   * 
   * @param newLocation
   *          a non-null Point instance
   * @throws IllegalArgumentException
   *           if the parameter is null
   */
  public final void setLocation(Point location)
  {
		Point save = getLocation();
	  getCastor().getDimension().setX(location.x);
	  getCastor().getDimension().setY(location.y);
    firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, null, location);
  }


  /**
   * Return the Location of this shape.
   * 
   * @return a non-null location instance
   */
  public final Point getLocation()
  {
	  return new Point(getCastor().getDimension().getX(), getCastor().getDimension().getY());
  }
 

  /**
   * Set the Size of this shape. Will not modify the size if newSize is null.
   * 
   * @param newSize
   *          a non-null Dimension instance or null
   */
	public final void setSize(Dimension size)
	{
		Dimension save = getSize();
	  getCastor().getDimension().setHeight(size.height);
	  getCastor().getDimension().setWidth(size.width);
		firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, size);
	}
  
  /**
   * Return the Size of this shape.
   * 
   * @return a non-null Dimension instance
   */
  	public final Dimension getSize()
  	{
  	  return new Dimension(getCastor().getDimension().getWidth(), getCastor().getDimension().getHeight());
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
      return true;
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
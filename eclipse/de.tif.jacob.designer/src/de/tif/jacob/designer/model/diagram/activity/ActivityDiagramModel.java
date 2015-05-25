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
import de.tif.jacob.core.definition.impl.jad.castor.CastorActivityDiagram;
import de.tif.jacob.designer.model.ApplicationModel;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;
import de.tif.jacob.util.clazz.ClassUtil;

/**
 * A container for multiple shapes.
 * This is the "root" of the model data structure.
 * @author Elias Volanakis
 */
public class ActivityDiagramModel extends ObjectModel
{
  private List shapes;
  private List transitions;
  
  final private CastorActivityDiagram castor;
  
	public ActivityDiagramModel(JacobModel jacobModel, String name)
	{
	  this(jacobModel, new CastorActivityDiagram());
	  
	  castor.setName(name);
	}
	
  public ActivityDiagramModel(JacobModel jacob, CastorActivityDiagram model)
  {
    super(jacob);
    this.castor     = model;
  }
  
	/** 
	 * Add a shape to this diagram.
	 * @param node a non-null shape instance
	 * @return true, if the shape was added, false otherwise
	 */
	public boolean addElement(DiagramElementModel node) 
	{
		if (shapes.add(node)) 
		{
			castor.addDiagramNode(node.getCastor());
			firePropertyChange(PROPERTY_ELEMENT_ADDED, null, node);
			return true;
		}
		return false;
	}
	
	/** 
	 * Add a shape to this diagram.
	 * @param transition a non-null shape instance
	 * @return true, if the shape was added, false otherwise
	 */
	public boolean addElement(TransitionModel transition) 
	{
		if (transitions.add(transition)) 
		{
			castor.addTransition(transition.getCastor());
			firePropertyChange(PROPERTY_ELEMENT_ADDED, null, transition);
			return true;
		}
		return false;
	}
	
	public List getTransitionModels()
	{
	  if(transitions==null)
	    getChildren();
	  
	  return transitions;
	}

	/** Return a List of Shapes in this diagram. */
	public List getChildren() 
	{
	  List result = new ArrayList();
	  
	  if(transitions ==null)
	  {
	    transitions= new ArrayList();
	    for(int i=0;i< castor.getTransitionCount();i++)
	    {
	      transitions.add(new TransitionModel(getJacobModel(),this, castor.getTransition(i)));
	    }
	  }
	  
	  if(shapes==null)
	  {
	    shapes= new ArrayList();
	    for(int i=0;i< castor.getDiagramNodeCount();i++)
	    {
	      if(castor.getDiagramNode(i).getDiagramNodeChoice().getActivity()!=null)
	        shapes.add(new ActivityModel(getJacobModel(),this, castor.getDiagramNode(i)));
	      else if(castor.getDiagramNode(i).getDiagramNodeChoice().getBranchMerge()!=null)
	        shapes.add(new BranchMergeModel(getJacobModel(),this, castor.getDiagramNode(i)));
	      else if(castor.getDiagramNode(i).getDiagramNodeChoice().getForkJoin()!=null)
	        shapes.add(new ForkJoinModel(getJacobModel(),this, castor.getDiagramNode(i)));
	      else if(castor.getDiagramNode(i).getDiagramNodeChoice().getStart()!=null)
	        shapes.add(new StartModel(getJacobModel(),this, castor.getDiagramNode(i)));
	      else if(castor.getDiagramNode(i).getDiagramNodeChoice().getEnd()!=null)
	        shapes.add(new EndModel(getJacobModel(),this, castor.getDiagramNode(i)));
	    }
	  }
	  result.addAll(shapes);

	  return result;
	}

	public DiagramElementModel getDiagramElementModel(String name)
	{
	  if(shapes==null)
	    getChildren();
	  
	  Iterator iter = shapes.iterator();
	  while(iter.hasNext())
	  {
	    DiagramElementModel model = (DiagramElementModel)iter.next();
	    if(model.getName().equals(name))
	      return model;
	  }
	  return null;
	}
	
	/**
	 * Remove a shape from this diagram.
	 * @param s a non-null shape instance;
	 * @return true, if the shape was removed, false otherwise
	 */
	public boolean removeElement(DiagramElementModel s) 
	{
	  int i=shapes.indexOf(s);
		if (i != -1) 
		{
		  shapes.remove(s);
		  castor.removeDiagramNode(i);
			firePropertyChange(PROPERTY_ELEMENT_REMOVED, s,null);
			return true;
		}
		return false;
	}
	
	/**
	 * Remove a shape from this diagram.
	 * @param s a non-null shape instance;
	 * @return true, if the shape was removed, false otherwise
	 */
	public boolean removeElement(TransitionModel t) 
	{
	  int i=transitions.indexOf(t);
		if (i != -1) 
		{
		  transitions.remove(t);
		  t.getSource().removeConnection(t);
		  t.getTarget().removeConnection(t);
		  castor.removeTransition(i);
			firePropertyChange(PROPERTY_ELEMENT_REMOVED, t,null);
			return true;
		}
		return false;
	}
	
	public String getName()
	{
	  return castor.getName();
	}
	
  public void setName(String name) throws Exception
  {
    String save = getName();
    if (StringUtil.saveEquals(name, save))
      return;
    
    castor.setName(name);

    firePropertyChange(PROPERTY_NAME_CHANGED, save, name);
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
  
  public CastorActivityDiagram getCastor()
  {
    return castor;
  }

  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }

  @Override
  public ObjectModel getParent()
  {
    return getJacobModel();
  }
}

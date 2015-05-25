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
/*
 * Created on 14.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.tif.jacob.designer.views.applicationoutline;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.views.properties.IPropertySource;
import de.tif.jacob.designer.JacobDesigner;
import de.tif.jacob.designer.model.JacobModel;
import de.tif.jacob.designer.model.ObjectModel;
import de.tif.jacob.designer.model.RelationModel;
import de.tif.jacob.designer.util.ModelTransfer;
import de.tif.jacob.designer.util.OverlayImageDescriptor;

/**
 * @author andreas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class TreeModelObject extends TreeObject
{
  public TreeModelObject(TreeViewer viewer,TreeParent parent, JacobModel jacob, ObjectModel model, String name) 
  {
    super(viewer, parent, jacob,model,  name);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.designer.views.applicationoutline.TreeObject#getImage()
   */
  public final Image getImage()
  {
    if(error!=null)
      return JacobDesigner.getImage(model.getImageName(),JacobDesigner.DECORATION_ERROR);
    if(warning!=null)
      return JacobDesigner.getImage(model.getImageName(),JacobDesigner.DECORATION_WARNING);
    if(info!=null)
      return JacobDesigner.getImage(model.getImageName(),JacobDesigner.DECORATION_INFO);
    if(hook)
      return JacobDesigner.getImage(model.getImageName(),JacobDesigner.DECORATION_HOOK);
      
    return JacobDesigner.getImage(model.getImageName(),JacobDesigner.DECORATION_NONE);
  }
}


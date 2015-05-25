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
package de.tif.jacob.designer.editor.jacobform.editpolicies;

import java.util.Iterator;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.rulers.RulerProvider;
import de.tif.jacob.designer.editor.jacobform.commands.AddGroupElementCommand;
import de.tif.jacob.designer.editor.jacobform.commands.ChangeGuideCommand;
import de.tif.jacob.designer.editor.jacobform.commands.CreateDBForeignFieldCommand;
import de.tif.jacob.designer.editor.jacobform.commands.CreateDBInformBrowserCommand;
import de.tif.jacob.designer.editor.jacobform.commands.CreateDBTableListBoxCommand;
import de.tif.jacob.designer.editor.jacobform.commands.CreateGroupDBElementCommand;
import de.tif.jacob.designer.editor.jacobform.commands.CreateGroupElementCommand;
import de.tif.jacob.designer.editor.jacobform.commands.CreateStackContainerCommand;
import de.tif.jacob.designer.editor.jacobform.commands.CreateStaticImageCommand;
import de.tif.jacob.designer.editor.jacobform.commands.CreateTabContainerCommand;
import de.tif.jacob.designer.editor.jacobform.commands.DeleteGroupElementCommand;
import de.tif.jacob.designer.editor.jacobform.commands.MoveGroupElementCommand;
import de.tif.jacob.designer.editor.jacobform.commands.RemoveGroupElementCommand;
import de.tif.jacob.designer.editor.jacobform.editpart.GroupEditPart;
import de.tif.jacob.designer.editor.jacobform.editpart.GroupElementEditPart;
import de.tif.jacob.designer.model.UIDBForeignFieldModel;
import de.tif.jacob.designer.model.UIDBInformBrowserModel;
import de.tif.jacob.designer.model.UIDBLocalInputFieldModel;
import de.tif.jacob.designer.model.UIDBTableListBoxModel;
import de.tif.jacob.designer.model.UIFormGuideModel;
import de.tif.jacob.designer.model.UIGroupElementModel;
import de.tif.jacob.designer.model.UIGroupModel;
import de.tif.jacob.designer.model.UIStackContainerModel;
import de.tif.jacob.designer.model.UIStaticImageModel;
import de.tif.jacob.designer.model.UITabContainerModel;

public class XYLayoutEditPolicyGroupImpl extends XYLayoutEditPolicy 
{
	protected Command createAddCommand(EditPart child, Object constraint)
	{
    Object obj = child.getModel();
    GroupEditPart host = (GroupEditPart)getHost();
    if(obj instanceof UIGroupElementModel && constraint instanceof Rectangle)
    {
      UIGroupElementModel model = (UIGroupElementModel)obj;
      // kann nur eingefügt werden, wenn dass element vom selben Alias kommt
      //
      if(host.getGroupModel().getTableAliasModel()!= model.getGroupModel().getTableAliasModel())
        return null;
      
      return new AddGroupElementCommand(host.getGroupModel(), model,(Rectangle)constraint );
    }
    return null;
	}

	/**
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChangeConstraintCommand(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) 
	{
		return null;
	}
	
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint)
  {
  	Rectangle rect = (Rectangle)constraint;
  	UIGroupElementModel model = (UIGroupElementModel)child.getModel();
		MoveGroupElementCommand cmd = new MoveGroupElementCommand(model, (Rectangle)constraint);
    CompoundCommand cc = new CompoundCommand();
    cc.add(cmd);
    
    // Element wird nach oben vergrößert
  	if ((request.getResizeDirection() & PositionConstants.NORTH) != 0) 
 			chainGuideVerticalRulerCommand(request, model, cc,-1);
  	
    // Element wird nach unten vergrößert
    if ((request.getResizeDirection() & PositionConstants.SOUTH) != 0) 
      chainGuideVerticalRulerCommand(request, model, cc,1);

    // Element wird nach rechts vergrößert
  	if ((request.getResizeDirection() & PositionConstants.EAST) != 0) 
      chainGuideHorizontalRulerCommand(request, model, cc,1);
  	
    // Element wird nach links vergrößert
    if ((request.getResizeDirection() & PositionConstants.WEST) != 0) 
      chainGuideHorizontalRulerCommand(request, model, cc,-1);

    if (request.getType().equals(REQ_MOVE_CHILDREN)	|| request.getType().equals(REQ_ALIGN_CHILDREN)) 
  	{
      chainGuideHorizontalRulerCommand(request, model, cc,-1);
      chainGuideHorizontalRulerCommand(request, model, cc,1);
      chainGuideVerticalRulerCommand(request, model, cc,-1);
      chainGuideVerticalRulerCommand(request, model, cc,1);
  	}

  	return cc;
	}

	protected Command getCreateCommand(CreateRequest request)
	{
	  Object newObj = request.getNewObject();
		UIGroupModel group   = (UIGroupModel)getHost().getModel();
		if(newObj instanceof UIDBInformBrowserModel)
		{
			UIDBInformBrowserModel  model = (UIDBInformBrowserModel)newObj;
			Rectangle rect = (Rectangle)getConstraintFor(request);
			if(group.getTableAliasModel().getToLinkedTableAliases().size()==0)
				return null;
			return new CreateDBInformBrowserCommand(group, model, rect.getLocation(), rect.getSize());
		}
		if(newObj instanceof UIDBForeignFieldModel)
		{
		  UIDBForeignFieldModel  model = (UIDBForeignFieldModel)newObj;
			Rectangle rect = (Rectangle)getConstraintFor(request);
			// Es darf nur ein GUI Element angelegt werden, wenn ein entsprechendes Datenfeld auch vorhanden ist
			//
			group.getTableAliasModel().getFromLinkedTableAliases();
			if(group.getTableAliasModel().getFromLinkedTableAliases().size()==0)
				return null;
			return new CreateDBForeignFieldCommand(group, model, rect.getLocation(), rect.getSize());
		}
    if(newObj instanceof UIDBTableListBoxModel)
    {
      UIDBTableListBoxModel  model = (UIDBTableListBoxModel)newObj;
      Rectangle rect = (Rectangle)getConstraintFor(request);
      return new CreateDBTableListBoxCommand(group, model, rect.getLocation(), rect.getSize());
    }
		if(newObj instanceof UIDBLocalInputFieldModel)
		{
		  UIDBLocalInputFieldModel  model = (UIDBLocalInputFieldModel)newObj;
			Rectangle rect = (Rectangle)getConstraintFor(request);
			return new CreateGroupDBElementCommand(group, model, rect.getLocation(), rect.getSize());
		}
    if(newObj instanceof UITabContainerModel)
    {
      UITabContainerModel  model = (UITabContainerModel)newObj;
      Rectangle rect = (Rectangle)getConstraintFor(request);
      return new CreateTabContainerCommand( group, model, rect.getLocation(), rect.getSize());
    }
    if(newObj instanceof UIStaticImageModel)
    {
      UIStaticImageModel  model = (UIStaticImageModel)newObj;
      Rectangle rect = (Rectangle)getConstraintFor(request);
      return new CreateStaticImageCommand(group, model, rect.getLocation());
    }
		if(newObj instanceof UIGroupElementModel)
		{
		  UIGroupElementModel  model = (UIGroupElementModel)newObj;
			Rectangle rect = (Rectangle)getConstraintFor(request);
			return new CreateGroupElementCommand(group, model, rect.getLocation(), rect.getSize());
		}
    if(newObj instanceof UIGroupModel)
    {
      UIGroupModel  model = (UIGroupModel)newObj;
      Rectangle rect = (Rectangle)getConstraintFor(request);
      return new CreateStackContainerCommand(group, model, rect.getLocation(), rect.getSize());
    }
		return null;
	}

	protected Command getDeleteDependantCommand(Request request)
	{
		CompoundCommand cc = new CompoundCommand();
		Iterator it = ((GroupRequest) request).getEditParts().iterator();
		while(it.hasNext())
		{
		  Object object = it.next();
		  if(object instanceof GroupElementEditPart)
		  {
		    GroupElementEditPart elementPart = (GroupElementEditPart)object;
	  	  
			  GroupEditPart  groupPart  = (GroupEditPart)elementPart.getParent();
				cc.add(new DeleteGroupElementCommand((UIGroupModel)groupPart.getModel(),(UIGroupElementModel)elementPart.getModel() ));
		  }
		}
		if(cc.isEmpty())
		  return null;
		return cc;
	}
	
  
	protected Command getOrphanChildrenCommand(Request request)
  {
    if(request instanceof GroupRequest)
    {
      GroupRequest r = (GroupRequest)request;
      CompoundCommand cmd = new CompoundCommand();
      
      Iterator iter = r.getEditParts().iterator();
      while(iter.hasNext())
      {
        GroupElementEditPart part =(GroupElementEditPart)iter.next();
        UIGroupElementModel model = part.getGroupElementModel();
        cmd.add(new RemoveGroupElementCommand(model.getGroupModel(), model));
      }
      if(cmd.size()>0)
        return cmd;
    }
    return super.getOrphanChildrenCommand(request);
  }

  protected void chainGuideVerticalRulerCommand(Request request, UIGroupElementModel part, CompoundCommand cmd, int alignment) 
  {
    UIFormGuideModel newGuide = null;
    // ACHTUNG: VERTICAL_RULER liefert einen HORIZONTAL_GUIDE
    // Grund  : Ruler ist horizontal ABER die Guide Linie ist dann ja vertikal! *uff*
    Integer guidePos = (Integer)request.getExtendedData().get(SnapToGuides.KEY_HORIZONTAL_GUIDE);
    if (guidePos != null)
    {
      newGuide = findGuideAt(guidePos.intValue(),false);
      alignment = ((Integer)request.getExtendedData().get(SnapToGuides.KEY_HORIZONTAL_ANCHOR)).intValue();
    }

    cmd.add(new ChangeGuideCommand(part,newGuide,alignment , false));
  }

	protected void chainGuideHorizontalRulerCommand(Request request, UIGroupElementModel part,	CompoundCommand cmd, int alignment) 
	{
    UIFormGuideModel newGuide = null;
    // ACHTUNG: HORIZONTAL_RULER liefert einen VERTICAL_GUIDE
    // Grund  : Ruler ist horizontal ABER die Guide Linie ist dann ja vertikal! *uff*
		Integer guidePos = (Integer)request.getExtendedData().get(SnapToGuides.KEY_VERTICAL_GUIDE);
		if (guidePos != null)
    {
		  newGuide = findGuideAt(guidePos.intValue(),true);
      alignment = ((Integer)request.getExtendedData().get(SnapToGuides.KEY_VERTICAL_ANCHOR)).intValue();
    }

    cmd.add(new ChangeGuideCommand(part,newGuide,alignment , true));
	}
	
  
	protected UIFormGuideModel findGuideAt(int pos, boolean horizontal) 
	{
		RulerProvider provider = ((RulerProvider)getHost().getViewer().getProperty(horizontal ? RulerProvider.PROPERTY_HORIZONTAL_RULER : RulerProvider.PROPERTY_VERTICAL_RULER));
		return (UIFormGuideModel)provider.getGuideAt(pos);
	}
}

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
 * Created on 10.02.2005
 *
 */
package de.tif.jacob.designer.model;

import java.rmi.server.UID;
import org.eclipse.draw2d.geometry.Rectangle;
import de.tif.jacob.designer.views.search.ReferenceSearchResult;
import de.tif.jacob.util.StringUtil;

/**
 *
 */
public class RelationsetStickyNoteModel extends ObjectModel
{
  protected final static String STICKY_NOTE_PREFIX = "stickyNote_";
  
  final String guid;
  RelationsetModel relationset;
  
  public RelationsetStickyNoteModel()
  {
    this.guid = STICKY_NOTE_PREFIX+new UID().toString();
  }
  
  public RelationsetStickyNoteModel(RelationsetModel relationset, String guid)
  {
    this.relationset = relationset;
    this.guid        = guid;
  }
  
  public void setRelationsetModel(RelationsetModel relationset)
  {
    this.relationset = relationset;
  }
  
	public void setBounds(Rectangle bounds)
	{
	  Rectangle save = getBounds();
	  
	  relationset.setCastorProperty(guid+".x",      Integer.toString(bounds.x));
	  relationset.setCastorProperty(guid+".y",      Integer.toString(bounds.y));
	  relationset.setCastorProperty(guid+".width",  Integer.toString(bounds.width));
	  relationset.setCastorProperty(guid+".height", Integer.toString(bounds.height));
	  
		firePropertyChange(PROPERTY_CONSTRAINT_CHANGED, save, bounds);
	}

	public Rectangle getBounds()
	{
	  Rectangle bounds = new Rectangle();

	  bounds.x      = relationset.getCastorIntProperty(guid+".x", 0);
	  bounds.y      = relationset.getCastorIntProperty(guid+".y", 0);
	  bounds.width  = relationset.getCastorIntProperty(guid+".width", -1);
	  bounds.height = relationset.getCastorIntProperty(guid+".height", -1);
	  
	  return bounds;
	}
	
	/* 
   * @see de.tif.jacob.designer.model.ObjectModel#getError()
   */
  public String getError()
  {
    return null;
  }

  /* 
   * @see de.tif.jacob.designer.model.ObjectModel#getWarning()
   */
  public String getWarning()
  {
    return null;
  }

  /* 
   * @see de.tif.jacob.designer.model.ObjectModel#getInfo()
   */
  public String getInfo()
  {
    return null;
  }

  /* 
   * @see de.tif.jacob.designer.model.ObjectModel#isInUse()
   */
  public boolean isInUse()
  {
    return true;
  }

  public String getText()
  {
	  return StringUtil.toSaveString(relationset.getCastorStringProperty(guid+".text"));
  }
  
  public void setText(String text)
  {
    String save = getText();
    if(StringUtil.saveEquals(save,text))
      return;
    
	  relationset.setCastorProperty(guid+".text",text);
		firePropertyChange(PROPERTY_LABEL_CHANGED, save, text);
  }
  
  protected String getGuid()
  {
    return guid;
  }

  @Override
  public String getName()
  {
    return guid;
  }
  
  @Override
  public void addReferrerObject(ReferenceSearchResult result, ObjectModel model)
  {
  }
 
  @Override
  public ObjectModel getParent()
  {
    return relationset;
  }
}

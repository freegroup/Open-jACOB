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
 * Created on 20.12.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.tif.jacob.designer.util;

import org.eclipse.gef.dnd.SimpleObjectTransfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * @author Andreas
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
public class ModelTransfer
{
  private static Object[] objects;
  

  public static Object[] getObjects()
  {
    return objects;
  }

  public static void setObjects(Object[] objects)
  {
    ModelTransfer.objects = objects;
  }
  
  public static boolean areObjectsSameType()
  {
    if(objects==null)
      return false;
    
    if(objects.length==0)
      return false;

    if(objects.length==1)
      return true;
      
    for (int i = 1; i < objects.length; i++)
    {
      if(objects[i-1].getClass() != objects[i].getClass())
        return false;
    }
    return true;
  }
}

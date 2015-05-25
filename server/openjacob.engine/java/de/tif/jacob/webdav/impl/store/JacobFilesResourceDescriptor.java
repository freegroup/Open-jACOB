/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
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
package de.tif.jacob.webdav.impl.store;

import java.util.List;
import java.util.Vector;

import org.apache.slide.common.ServiceAccessException;
import org.apache.slide.common.Uri;
import org.apache.slide.structure.ObjectNode;
import org.apache.slide.structure.ObjectNotFoundException;
import org.apache.slide.structure.SubjectNode;

import de.tif.jacob.webdav.impl.WebDAV;
import de.tif.jacob.webdav.impl.WebdavDocumentManager;

public class JacobFilesResourceDescriptor extends JacobResourceDescriptor
{
  static public final transient String RCS_ID = "$Id: JacobFilesResourceDescriptor.java,v 1.1 2007/01/19 09:50:41 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public JacobFilesResourceDescriptor(Uri uri) throws ServiceAccessException
  {
    super(uri);
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.webdav.store.JacobResourceDescriptor#retrieveObject()
   */
  public ObjectNode retrieveObject() throws ServiceAccessException, ObjectNotFoundException
  {
    ObjectNode node = super.retrieveObject();
    if (node == null)
      return null;

    List ids = WebdavDocumentManager.getDocumentIds();
    for (int i = 0; i < ids.size(); i++)
    {
      // Note: the document id will become an own directory
      String id = (String) ids.get(i);

      Vector bindings = new Vector();
      Vector parentBindings = new Vector();

      parentBindings.add(new ObjectNode.ParentBinding(id, WebDAV.FILES_URI));

      ObjectNode child = new SubjectNode(WebDAV.FILES_URI + "/" + id, bindings, parentBindings, null);
      child.setUri(child.getUuri());
      node.addChild(child);
    }

    return node;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.webdav.store.JacobResourceDescriptor#removeObject(org.apache.slide.structure.ObjectNode)
   */
  public void removeObject(ObjectNode aObject) throws ServiceAccessException, ObjectNotFoundException
  {
    throwUnsupportedError();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.webdav.store.JacobResourceDescriptor#storeObject(org.apache.slide.structure.ObjectNode)
   */
  public void storeObject(ObjectNode aObject) throws ServiceAccessException, ObjectNotFoundException
  {
    super.storeObject(aObject);
//    throwUnsupportedError();
  }

}

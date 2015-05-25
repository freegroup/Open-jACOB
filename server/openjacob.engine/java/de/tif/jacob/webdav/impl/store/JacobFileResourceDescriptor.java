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

import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.slide.common.ServiceAccessException;
import org.apache.slide.common.Uri;
import org.apache.slide.content.NodeProperty;
import org.apache.slide.content.NodeRevisionDescriptor;
import org.apache.slide.content.NodeRevisionDescriptors;
import org.apache.slide.content.NodeRevisionNumber;
import org.apache.slide.content.RevisionDescriptorNotFoundException;
import org.apache.slide.structure.ObjectNode;
import org.apache.slide.structure.ObjectNotFoundException;
import org.apache.slide.structure.SubjectNode;

import de.tif.jacob.messaging.Message;
import de.tif.jacob.webdav.impl.WebDAV;
import de.tif.jacob.webdav.impl.WebdavDocument;
import de.tif.jacob.webdav.impl.WebdavDocumentManager;

public class JacobFileResourceDescriptor extends JacobResourceDescriptor
{
  static public final transient String RCS_ID = "$Id: JacobFileResourceDescriptor.java,v 1.1 2007/01/19 09:50:41 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private static final SimpleDateFormat creationDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  
  static
  {
    creationDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
  }
  
  private final String name;
  private final String parent;
  private final String filename;
  private final boolean isDirectory;
  
  private static WebdavDocument getWebdavDocument(String parent, String name)
  {
    String id;
    if (WebDAV.FILES_URI.equals(parent))
      id = name;
    else
    {
      int idx = parent.lastIndexOf('/');
      if (idx < 0)
      {
        return null;
      }

      id = parent.substring(idx + 1);
    }

    return WebdavDocumentManager.get(id);
  }
  
  public JacobFileResourceDescriptor(Uri uri) throws ServiceAccessException, ObjectNotFoundException
  {
    super(uri);
    
    // Note: the uri is either /files/docid or /files/docid/docname. 
    //
    String uriString = uri.toString();
    int idx = uriString.lastIndexOf('/');
    this.parent = uriString.substring(0, idx);
    this.name = uriString.substring(idx+1);
    this.isDirectory = WebDAV.FILES_URI.equals(this.parent);
    
    WebdavDocument webdavDocument = getWebdavDocument(this.parent, this.name);
    if (webdavDocument == null)
      throw new ObjectNotFoundException(uri);
    
    this.filename = webdavDocument.getName();

    NodeRevisionNumber aInitialRevision = new NodeRevisionNumber(1, 0);
    boolean aUseVersionning = false;

    Hashtable aLastestRevisions = new Hashtable();
    aLastestRevisions.put(NodeRevisionDescriptors.MAIN_BRANCH, aInitialRevision);

    Hashtable aBranches = new Hashtable();
    aBranches.put(aInitialRevision, new Vector());

    revisionDescriptors = new NodeRevisionDescriptors(uriString, aInitialRevision, new Hashtable(), aLastestRevisions, aBranches, aUseVersionning);

    descriptor = new Hashtable();
    NodeRevisionNumber aRevisionNumber = aInitialRevision;
    Vector aLabels = new Vector();
    Hashtable aProperties = new Hashtable();
    boolean aProtected = true;
    String aNamespace = NodeProperty.DEFAULT_NAMESPACE;
    String aType = null;

    String aName = NodeRevisionDescriptor.ETAG;
    aProperties.put(aNamespace + aName, new NodeProperty(aName, webdavDocument.getId(), aNamespace, aType, aProtected));

    aName = NodeRevisionDescriptor.SOURCE;
    aProperties.put(aNamespace + aName, new NodeProperty(aName, null, aNamespace, aType, aProtected));

    aName = NodeRevisionDescriptor.OWNER;
    aProperties.put(aNamespace + aName, new NodeProperty(aName, webdavDocument.getOwner(), aNamespace, aType, aProtected));

    aName = NodeRevisionDescriptor.NAME;
    aProperties.put(aNamespace + aName, new NodeProperty(aName, this.name, aNamespace, aType, aProtected));

    aName = NodeRevisionDescriptor.LAST_MODIFIED;
    aProperties.put(aNamespace + aName, new NodeProperty(aName, webdavDocument.getModificationDate(), aNamespace, aType, aProtected));
    
    aName = NodeRevisionDescriptor.MODIFICATION_DATE;
    aProperties.put(aNamespace + aName, new NodeProperty(aName, webdavDocument.getModificationDate(), aNamespace, aType, aProtected));

    aName = NodeRevisionDescriptor.CREATION_DATE;
    aProperties.put(aNamespace + aName, new NodeProperty(aName, webdavDocument.getCreationDate(), aNamespace, aType, aProtected));

    aName = NodeRevisionDescriptor.MODIFICATION_USER;
    aProperties.put(aNamespace + aName, new NodeProperty(aName, webdavDocument.getOwner(), aNamespace, aType, aProtected));

    aName = NodeRevisionDescriptor.CREATION_USER;
    aProperties.put(aNamespace + aName, new NodeProperty(aName, webdavDocument.getOwner(), aNamespace, aType, aProtected));

    if (this.isDirectory)
    {
      aName = NodeRevisionDescriptor.RESOURCE_TYPE;
      aProperties.put(aNamespace + aName, new NodeProperty(aName, NodeRevisionDescriptor.COLLECTION_TYPE, aNamespace, aType, aProtected));
    }
    else
    {
      aName = NodeRevisionDescriptor.RESOURCE_TYPE;
      aProperties.put(aNamespace + aName, new NodeProperty(aName, null, aNamespace, aType, aProtected));

      aName = NodeRevisionDescriptor.CONTENT_LANGUAGE;
      aProperties.put(aNamespace + aName, new NodeProperty(aName, "en", aNamespace, aType, aProtected));

      aName = NodeRevisionDescriptor.CONTENT_LENGTH;
      aProperties.put(aNamespace + aName, new NodeProperty(aName, Integer.toString(webdavDocument.getContentLength()), aNamespace, aType, aProtected));

      aName = NodeRevisionDescriptor.CONTENT_TYPE;
      aProperties.put(aNamespace + aName, new NodeProperty(aName, Message.getMimeType(this.name), aNamespace, aType, aProtected));

      // for Windows!?
      aName = "isreadonly";
      aProperties.put(aNamespace + aName, new NodeProperty(aName, "false", aNamespace, aType, aProtected));

      // aName = "href";
      // aProperties.put(aNamespace+aName, new NodeProperty(aName,
      // "http://djoser:8080/jacob/webdav/files/"+URLUtil.URLEncode(this.name,
      // "ISO-8859-1"), aNamespace, aType, aProtected));
    }
    
    NodeRevisionDescriptor aNode = new NodeRevisionDescriptor(aRevisionNumber, NodeRevisionDescriptors.MAIN_BRANCH, aLabels, aProperties);
    descriptor.put(aRevisionNumber.toString(), aNode);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.tif.jacob.webdav.store.JacobResourceDescriptor#retrieveObject()
   */
  public ObjectNode retrieveObject() throws ServiceAccessException, ObjectNotFoundException
  {
    Vector bindings = new Vector();
    Vector parentBindings = new Vector();
    parentBindings.add(new ObjectNode.ParentBinding(this.name, this.parent));
    ObjectNode node = new SubjectNode(uri.toString(), bindings, parentBindings, null);
    node.setUri(node.getUuri());

    if (this.isDirectory)
    {
      Vector childBindings = new Vector();
      Vector childParentBindings = new Vector();

      childParentBindings.add(new ObjectNode.ParentBinding(this.filename, WebDAV.FILES_URI + "/" + this.name));

      ObjectNode child = new SubjectNode(WebDAV.FILES_URI + "/" + this.name + "/" + this.filename, childBindings, childParentBindings, null);
      child.setUri(child.getUuri());
      node.addChild(child);
    }
    
    return node;
  }

  /* (non-Javadoc)
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
    throwUnsupportedError();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.webdav.store.JacobResourceDescriptor#createRevisionDescriptors(org.apache.slide.content.NodeRevisionDescriptors)
   */
  public void createRevisionDescriptors(NodeRevisionDescriptors aRevisionDescriptors) throws ObjectNotFoundException, ServiceAccessException
  {
    if (this.isDirectory)
      super.createRevisionDescriptors(aRevisionDescriptors);
    else
      throwUnsupportedError();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.webdav.store.JacobResourceDescriptor#removeRevisionDescriptors()
   */
  public void removeRevisionDescriptors() throws ObjectNotFoundException, ServiceAccessException
  {
    if (this.isDirectory)
      super.removeRevisionDescriptors();
    else
      throwUnsupportedError();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.webdav.store.JacobResourceDescriptor#storeRevisionDescriptors(org.apache.slide.content.NodeRevisionDescriptors)
   */
  public void storeRevisionDescriptors(NodeRevisionDescriptors aRevisionDescriptors) throws RevisionDescriptorNotFoundException, ObjectNotFoundException, ServiceAccessException
  {
    if (this.isDirectory)
      super.storeRevisionDescriptors(aRevisionDescriptors);
    else
      throwUnsupportedError();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.webdav.store.JacobResourceDescriptor#createRevisionDescriptor(org.apache.slide.content.NodeRevisionDescriptor)
   */
  public void createRevisionDescriptor(NodeRevisionDescriptor aRevisionDescriptor) throws ObjectNotFoundException, ServiceAccessException
  {
    throwUnsupportedError();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.webdav.store.JacobResourceDescriptor#removeRevisionDescriptor(org.apache.slide.content.NodeRevisionNumber)
   */
  public void removeRevisionDescriptor(NodeRevisionNumber number) throws ObjectNotFoundException, ServiceAccessException
  {
    throwUnsupportedError();
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.webdav.store.JacobResourceDescriptor#storeRevisionDescriptor(org.apache.slide.content.NodeRevisionDescriptor)
   */
  public void storeRevisionDescriptor(NodeRevisionDescriptor aRevisionDescriptor) throws RevisionDescriptorNotFoundException, ObjectNotFoundException, ServiceAccessException
  {
    // IBIS: was machen?
    // throw new ServiceAccessException(null, "Unsupported operation");
  }
}

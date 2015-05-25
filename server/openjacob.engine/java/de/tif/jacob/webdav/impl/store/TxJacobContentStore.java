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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.transaction.util.FileHelper;
import org.apache.slide.common.AbstractServiceBase;
import org.apache.slide.common.ServiceAccessException;
import org.apache.slide.common.ServiceConnectionFailedException;
import org.apache.slide.common.ServiceDisconnectionFailedException;
import org.apache.slide.common.ServiceParameterErrorException;
import org.apache.slide.common.ServiceParameterMissingException;
import org.apache.slide.common.ServiceResetFailedException;
import org.apache.slide.common.Uri;
import org.apache.slide.content.NodeRevisionContent;
import org.apache.slide.content.NodeRevisionDescriptor;
import org.apache.slide.content.NodeRevisionDescriptors;
import org.apache.slide.content.NodeRevisionNumber;
import org.apache.slide.content.RevisionAlreadyExistException;
import org.apache.slide.content.RevisionDescriptorNotFoundException;
import org.apache.slide.content.RevisionNotFoundException;
import org.apache.slide.lock.LockTokenNotFoundException;
import org.apache.slide.lock.NodeLock;
import org.apache.slide.security.NodePermission;
import org.apache.slide.store.ContentStore;
import org.apache.slide.store.LockStore;
import org.apache.slide.store.NodeStore;
import org.apache.slide.store.RevisionDescriptorStore;
import org.apache.slide.store.RevisionDescriptorsStore;
import org.apache.slide.store.SecurityStore;
import org.apache.slide.structure.ObjectAlreadyExistsException;
import org.apache.slide.structure.ObjectNode;
import org.apache.slide.structure.ObjectNotFoundException;

import de.tif.jacob.webdav.impl.WebDAV;
import de.tif.jacob.webdav.impl.WebdavDocument;
import de.tif.jacob.webdav.impl.WebdavDocumentManager;

/**
 * jACOB content and revision file store.
 * 
 * @author Andreas Sonntag
 */
public class TxJacobContentStore extends AbstractServiceBase implements ContentStore, NodeStore, LockStore, SecurityStore, RevisionDescriptorsStore,
    RevisionDescriptorStore
{
  static public final transient String RCS_ID = "$Id: TxJacobContentStore.java,v 1.1 2007/01/19 09:50:41 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private static final Log logger = LogFactory.getLog(TxJacobContentStore.class);

  private static final Map uriToDescriptorMap = new HashMap();

  private void throwInternalError(Throwable cause) throws ServiceAccessException
  {
    ServiceAccessException ex = new ServiceAccessException(this, cause);

    if (logger.isWarnEnabled())
      logger.warn("Internal webdav error", ex);
    throw ex;
  }
  
  /**
   * Either returns a cached file descriptor or loads it from DB
   */
  private AbstractJacobResourceDescriptor getFileDescriptor(Uri uri) throws ServiceAccessException, ObjectNotFoundException
  {
    String uriStr = uri.toString(); 
    if (uriStr.startsWith(WebDAV.FILES_URI))
    {
      if (!uriStr.equals(WebDAV.FILES_URI))
      {
        return new JacobFileResourceDescriptor(uri);
      }
    }
    
    AbstractJacobResourceDescriptor descriptor = (AbstractJacobResourceDescriptor) uriToDescriptorMap.get(uri);
    if (descriptor == null)
    {
      throw new ObjectNotFoundException(uri);
    }
    return descriptor;
  }

  private AbstractJacobResourceDescriptor createFileDescriptor(Uri uri) throws ServiceAccessException, ObjectNotFoundException
  {
    AbstractJacobResourceDescriptor descriptor;
    String uriStr = uri.toString(); 
    if (uriStr.startsWith(WebDAV.FILES_URI))
    {
      if (uriStr.equals(WebDAV.FILES_URI))
      {
        descriptor = new JacobFilesResourceDescriptor(uri);
      }
      else
      {
        // we do not allow to create a new document
        // this might happen, if someone has loaded a document in word, but
        // the transaction has already been committed. Hence the document
        // has a new GUID.
        throw new ObjectNotFoundException(uri);
//        throw new ServiceAccessException(this, "Creating descriptor for "+uriStr+" not allowed");
      }
    }
    else
    {
      descriptor = new JacobResourceDescriptor(uri);
    }
    
    if (uriToDescriptorMap.put(uri, descriptor) != null)
    {
      if (logger.isWarnEnabled())
        logger.warn("#### Descriptor already exists: " + descriptor);
    }
    return descriptor;
  }

  protected AbstractJacobResourceDescriptor removeFileDescriptor(Uri uri) throws ServiceAccessException, ObjectNotFoundException
  {
    AbstractJacobResourceDescriptor descriptor = (AbstractJacobResourceDescriptor) uriToDescriptorMap.remove(uri);
    if (descriptor == null)
    {
      ObjectNotFoundException ex = new ObjectNotFoundException(uri);
      if (logger.isWarnEnabled())
        logger.warn(ex.toString());
      throw ex;
    }
    return descriptor;
  }

  /**
   * Dies Methode beruht darauf, dass uri = /files/docid/docname ist!
   * 
   * @param uri
   * @return
   */
  private static WebdavDocument getWebdavDocument(Uri uri)
  {
    String uriString = uri.toString();
    int idEndIdx = uriString.lastIndexOf('/');
    if (idEndIdx > 0)
    {
      int idStartIdx = uriString.lastIndexOf('/', idEndIdx - 1);
      if (idStartIdx > 0)
      {
        String id = uriString.substring(idStartIdx + 1, idEndIdx);
        return WebdavDocumentManager.get(id);
      }
    }
    return null;
  }
  
  public NodeRevisionContent retrieveRevisionContent(Uri uri, NodeRevisionDescriptor revisionDescriptor) throws ServiceAccessException,
      RevisionNotFoundException
  {
    if (logger.isDebugEnabled())
      logger.debug("Retrieving revision " + revisionDescriptor + " of " + uri);

    WebdavDocument webdavDocument = getWebdavDocument(uri);
    if (webdavDocument != null)
    {
      byte[] content = webdavDocument.getContent();
      if (content != null)
      {
        NodeRevisionContent result = new NodeRevisionContent();
        result.setContent(content);
        return result;
      }
    }
    throw new RevisionNotFoundException(uri.toString(), revisionDescriptor.getRevisionNumber());
  }
  
  public void createRevisionContent(Uri uri, NodeRevisionDescriptor revisionDescriptor, NodeRevisionContent revisionContent) throws ServiceAccessException,
      RevisionAlreadyExistException
  {
    if (logger.isDebugEnabled())
      logger.debug("Creating revision content " + revisionDescriptor + " of " + uri);

    // do nothing here
  }

  public void storeRevisionContent(Uri uri, NodeRevisionDescriptor revisionDescriptor, NodeRevisionContent revisionContent) throws ServiceAccessException,
      RevisionNotFoundException
  {
    if (logger.isDebugEnabled())
      logger.debug("Store revision content " + revisionDescriptor + " of " + uri);

    WebdavDocument webdavDocument = getWebdavDocument(uri);
    if (webdavDocument != null)
    {
      byte[] content;
      try
      {
        ByteArrayOutputStream os = new ByteArrayOutputStream(10000);
        InputStream is = revisionContent.streamContent();
        try
        {
          long contentBytes = FileHelper.copy(is, os);

          if (logger.isDebugEnabled())
            logger.debug("Stored " + contentBytes + " bytes of revision content " + revisionDescriptor + " of " + uri);

          content = os.toByteArray();
        }
        finally
        {
          is.close();
        }
        if (webdavDocument.setContent(content))
          return;
      }
      catch (IOException e)
      {
        throwInternalError(e);
      }

    }
    throw new RevisionNotFoundException(uri.toString(), revisionDescriptor.getRevisionNumber());
  }

  public void removeRevisionContent(Uri uri, NodeRevisionDescriptor revisionDescriptor) throws ServiceAccessException
  {
    if (logger.isDebugEnabled())
      logger.debug("Store revision content " + revisionDescriptor + " of " + uri);

    // do nothing here
  }

  private boolean started = false;

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.common.AbstractServiceBase#connect()
   */
  public void connect() throws ServiceConnectionFailedException
  {
    started = true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.common.AbstractServiceBase#disconnect()
   */
  public void disconnect() throws ServiceDisconnectionFailedException
  {
    started = false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.common.AbstractServiceBase#isConnected()
   */
  public boolean isConnected() throws ServiceAccessException
  {
    return started;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.common.AbstractServiceBase#reset()
   */
  public void reset() throws ServiceResetFailedException
  {
    // do nothing here
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.common.AbstractServiceBase#setParameters(java.util.Hashtable)
   */
  public void setParameters(Hashtable arg0) throws ServiceParameterErrorException, ServiceParameterMissingException
  {
    if (logger.isDebugEnabled())
    {
      Enumeration enumeration = arg0.keys();
      while (enumeration.hasMoreElements())
      {
        Object key = enumeration.nextElement();
        logger.debug("Setting parameter " + key + "=" + arg0.get(key));
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.transaction.xa.XAResource#commit(javax.transaction.xa.Xid,
   *      boolean)
   */
  public void commit(Xid arg0, boolean arg1) throws XAException
  {
    if (logger.isDebugEnabled())
      logger.debug("commit: " + arg0 + ", " + arg1);

    // do nothing here
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.transaction.xa.XAResource#end(javax.transaction.xa.Xid, int)
   */
  public void end(Xid arg0, int arg1) throws XAException
  {
    if (logger.isDebugEnabled())
      logger.debug("end: " + arg0 + ", " + arg1);

    // do nothing here
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.transaction.xa.XAResource#forget(javax.transaction.xa.Xid)
   */
  public void forget(Xid arg0) throws XAException
  {
    if (logger.isDebugEnabled())
      logger.debug("forget: " + arg0);

    // do nothing here
  }

  private int timeoutInSecs = 3600;

  /*
   * (non-Javadoc)
   * 
   * @see javax.transaction.xa.XAResource#getTransactionTimeout()
   */
  public int getTransactionTimeout() throws XAException
  {
    if (logger.isDebugEnabled())
      logger.debug("getTransactionTimeout: ");

    return timeoutInSecs;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.transaction.xa.XAResource#isSameRM(javax.transaction.xa.XAResource)
   */
  public boolean isSameRM(XAResource arg0) throws XAException
  {
    if (logger.isDebugEnabled())
      logger.debug("isSameRM: " + arg0);

    return arg0 instanceof TxJacobContentStore;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.transaction.xa.XAResource#prepare(javax.transaction.xa.Xid)
   */
  public int prepare(Xid arg0) throws XAException
  {
    if (logger.isDebugEnabled())
      logger.debug("prepare: " + arg0);

    return XA_OK;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.transaction.xa.XAResource#recover(int)
   */
  public Xid[] recover(int arg0) throws XAException
  {
    // do not care as this never is called by Slide
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.transaction.xa.XAResource#rollback(javax.transaction.xa.Xid)
   */
  public void rollback(Xid arg0) throws XAException
  {
    if (logger.isDebugEnabled())
      logger.debug("rollback: " + arg0);

    // do nothing here
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.transaction.xa.XAResource#setTransactionTimeout(int)
   */
  public boolean setTransactionTimeout(int arg0) throws XAException
  {
    if (logger.isDebugEnabled())
      logger.debug("setTransactionTimeout: " + arg0);

    timeoutInSecs = arg0;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.transaction.xa.XAResource#start(javax.transaction.xa.Xid, int)
   */
  public void start(Xid arg0, int arg1) throws XAException
  {
    if (logger.isDebugEnabled())
      logger.debug("start: " + arg0 + ", " + arg1);

    // do nothing here
  }

  /* ============================================================================ */

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.NodeStore#createObject(org.apache.slide.common.Uri,
   *      org.apache.slide.structure.ObjectNode)
   */
  public void createObject(Uri uri, ObjectNode object) throws ServiceAccessException, ObjectAlreadyExistsException
  {
    if (logger.isDebugEnabled())
      logger.debug("createObject: " + uri + ", " + object);

    try
    {
      AbstractJacobResourceDescriptor xfd = createFileDescriptor(uri);
      xfd.storeObject(object);
    }
    catch (ObjectNotFoundException e)
    {
      // HACK: we want to avoid to log stacktrace
      throw new ObjectAlreadyExistsException(e.getObjectUri());
      
      // should not happen, if it does, it is an error inside this store:
//      throwInternalError("Newly created file vanished");
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.NodeStore#removeObject(org.apache.slide.common.Uri,
   *      org.apache.slide.structure.ObjectNode)
   */
  public void removeObject(Uri uri, ObjectNode object) throws ServiceAccessException, ObjectNotFoundException
  {
    if (logger.isDebugEnabled())
      logger.debug("removeObject: " + uri + ", " + object);

    removeFileDescriptor(uri);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.NodeStore#retrieveObject(org.apache.slide.common.Uri)
   */
  public ObjectNode retrieveObject(Uri uri) throws ServiceAccessException, ObjectNotFoundException
  {
    if (logger.isDebugEnabled())
      logger.debug("retrieveObject: " + uri);

    AbstractJacobResourceDescriptor rd = getFileDescriptor(uri);
    ObjectNode object = rd.retrieveObject();
    return object;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.NodeStore#storeObject(org.apache.slide.common.Uri,
   *      org.apache.slide.structure.ObjectNode)
   */
  public void storeObject(Uri uri, ObjectNode object) throws ServiceAccessException, ObjectNotFoundException
  {
    if (logger.isDebugEnabled())
      logger.debug("storeObject: " + uri + ", " + object);

    AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
    xfd.storeObject(object);
  }

  /* ============================================================================ */

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.LockStore#enumerateLocks(org.apache.slide.common.Uri)
   */
  public Enumeration enumerateLocks(Uri uri) throws ServiceAccessException
  {
    if (logger.isDebugEnabled())
      logger.debug("enumerateLocks: " + uri);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      return xfd.enumerateLocks();
    }
    catch (ObjectNotFoundException e)
    {
      throwInternalError(e);
      return null; // XXX fake (is never called)
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.LockStore#killLock(org.apache.slide.common.Uri,
   *      org.apache.slide.lock.NodeLock)
   */
  public void killLock(Uri uri, NodeLock lock) throws ServiceAccessException, LockTokenNotFoundException
  {
    if (logger.isDebugEnabled())
      logger.debug("killLock: " + uri + ", " + lock);

    removeLock(uri, lock);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.LockStore#putLock(org.apache.slide.common.Uri,
   *      org.apache.slide.lock.NodeLock)
   */
  public void putLock(Uri uri, NodeLock lock) throws ServiceAccessException
  {
    if (logger.isDebugEnabled())
      logger.debug("putLock: " + uri + ", " + lock);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      xfd.putLock(lock);
    }
    catch (ObjectNotFoundException e)
    {
      throwInternalError(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.LockStore#removeLock(org.apache.slide.common.Uri,
   *      org.apache.slide.lock.NodeLock)
   */
  public void removeLock(Uri uri, NodeLock lock) throws ServiceAccessException, LockTokenNotFoundException
  {
    if (logger.isDebugEnabled())
      logger.debug("removeLock: " + uri + ", " + lock);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      xfd.removeLock(lock);
    }
    catch (ObjectNotFoundException e)
    {
      throw new LockTokenNotFoundException(lock);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.LockStore#renewLock(org.apache.slide.common.Uri,
   *      org.apache.slide.lock.NodeLock)
   */
  public void renewLock(Uri uri, NodeLock lock) throws ServiceAccessException, LockTokenNotFoundException
  {
    if (logger.isDebugEnabled())
      logger.debug("renewLock: " + uri + ", " + lock);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      xfd.renewLock(lock);
    }
    catch (ObjectNotFoundException e)
    {
      throwInternalError(e);
    }
  }

  /* ============================================================================ */

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.SecurityStore#enumeratePermissions(org.apache.slide.common.Uri)
   */
  public Enumeration enumeratePermissions(Uri uri) throws ServiceAccessException
  {
    if (logger.isDebugEnabled())
      logger.debug("enumeratePermissions: " + uri);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      return xfd.enumeratePermissions();
    }
    catch (ObjectNotFoundException e)
    {
      throwInternalError(e);
      return null; // XXX fake (is never called)
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.SecurityStore#grantPermission(org.apache.slide.common.Uri,
   *      org.apache.slide.security.NodePermission)
   */
  public void grantPermission(Uri uri, NodePermission permission) throws ServiceAccessException
  {
    if (logger.isDebugEnabled())
      logger.debug("grantPermission: " + uri + ", " + permission);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      xfd.grantPermission(permission);
    }
    catch (ObjectNotFoundException e)
    {
      throwInternalError(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.SecurityStore#revokePermission(org.apache.slide.common.Uri,
   *      org.apache.slide.security.NodePermission)
   */
  public void revokePermission(Uri uri, NodePermission permission) throws ServiceAccessException
  {
    if (logger.isDebugEnabled())
      logger.debug("revokePermission: " + uri + ", " + permission);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      xfd.revokePermission(permission);
    }
    catch (ObjectNotFoundException e)
    {
      throwInternalError(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.SecurityStore#revokePermissions(org.apache.slide.common.Uri)
   */
  public void revokePermissions(Uri uri) throws ServiceAccessException
  {
    if (logger.isDebugEnabled())
      logger.debug("revokePermissions: " + uri);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      xfd.revokePermissions();
    }
    catch (ObjectNotFoundException e)
    {
      throwInternalError(e);
    }
  }

  /* ============================================================================ */

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.RevisionDescriptorsStore#createRevisionDescriptors(org.apache.slide.common.Uri,
   *      org.apache.slide.content.NodeRevisionDescriptors)
   */
  public void createRevisionDescriptors(Uri uri, NodeRevisionDescriptors revisionDescriptors) throws ServiceAccessException
  {
    if (logger.isDebugEnabled())
      logger.debug("createRevisionDescriptors: " + uri + ", " + revisionDescriptors);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      xfd.createRevisionDescriptors(revisionDescriptors);
    }
    catch (ObjectNotFoundException e)
    {
      throwInternalError(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.RevisionDescriptorsStore#removeRevisionDescriptors(org.apache.slide.common.Uri)
   */
  public void removeRevisionDescriptors(Uri uri) throws ServiceAccessException
  {
    if (logger.isDebugEnabled())
      logger.debug("removeRevisionDescriptors: " + uri);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      xfd.removeRevisionDescriptors();
    }
    catch (ObjectNotFoundException e)
    {
      throwInternalError(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.RevisionDescriptorsStore#retrieveRevisionDescriptors(org.apache.slide.common.Uri)
   */
  public NodeRevisionDescriptors retrieveRevisionDescriptors(Uri uri) throws ServiceAccessException, RevisionDescriptorNotFoundException
  {
    if (logger.isDebugEnabled())
      logger.debug("retrieveRevisionDescriptors: " + uri);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      return xfd.retrieveRevisionDescriptors();
    }
    catch (ObjectNotFoundException e)
    {
      throw new RevisionDescriptorNotFoundException(uri.toString());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.RevisionDescriptorsStore#storeRevisionDescriptors(org.apache.slide.common.Uri,
   *      org.apache.slide.content.NodeRevisionDescriptors)
   */
  public void storeRevisionDescriptors(Uri uri, NodeRevisionDescriptors revisionDescriptors) throws ServiceAccessException, RevisionDescriptorNotFoundException
  {
    if (logger.isDebugEnabled())
      logger.debug("storeRevisionDescriptors: " + uri + ", " + revisionDescriptors);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      xfd.storeRevisionDescriptors(revisionDescriptors);
    }
    catch (ObjectNotFoundException e)
    {
      throw new RevisionDescriptorNotFoundException(uri.toString());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.RevisionDescriptorStore#createRevisionDescriptor(org.apache.slide.common.Uri,
   *      org.apache.slide.content.NodeRevisionDescriptor)
   */
  public void createRevisionDescriptor(Uri uri, NodeRevisionDescriptor revisionDescriptor) throws ServiceAccessException
  {
    if (logger.isDebugEnabled())
      logger.debug("createRevisionDescriptor: " + uri + ", " + revisionDescriptor);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      xfd.createRevisionDescriptor(revisionDescriptor);
    }
    catch (ObjectNotFoundException e)
    {
      throwInternalError(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.RevisionDescriptorStore#removeRevisionDescriptor(org.apache.slide.common.Uri,
   *      org.apache.slide.content.NodeRevisionNumber)
   */
  public void removeRevisionDescriptor(Uri uri, NodeRevisionNumber number) throws ServiceAccessException
  {
    if (logger.isDebugEnabled())
      logger.debug("removeRevisionDescriptor: " + uri + ", " + number);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      xfd.removeRevisionDescriptor(number);
    }
    catch (ObjectNotFoundException e)
    {
      throwInternalError(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.slide.store.RevisionDescriptorStore#retrieveRevisionDescriptor(org.apache.slide.common.Uri,
   *      org.apache.slide.content.NodeRevisionNumber)
   */
  public NodeRevisionDescriptor retrieveRevisionDescriptor(Uri uri, NodeRevisionNumber revisionNumber) throws ServiceAccessException,
      RevisionDescriptorNotFoundException
  {
    if (logger.isDebugEnabled())
      logger.debug("retrieveRevisionDescriptor: " + uri + ", " + revisionNumber);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      return xfd.retrieveRevisionDescriptor(revisionNumber);
    }
    catch (ObjectNotFoundException e)
    {
      throw new RevisionDescriptorNotFoundException(uri.toString());
    }
  }

  /* (non-Javadoc)
   * @see org.apache.slide.store.RevisionDescriptorStore#storeRevisionDescriptor(org.apache.slide.common.Uri, org.apache.slide.content.NodeRevisionDescriptor)
   */
  public void storeRevisionDescriptor(Uri uri, NodeRevisionDescriptor revisionDescriptor) throws ServiceAccessException, RevisionDescriptorNotFoundException
  {
    if (logger.isDebugEnabled())
      logger.debug("storeRevisionDescriptor: " + uri + ", " + revisionDescriptor);

    try
    {
      AbstractJacobResourceDescriptor xfd = getFileDescriptor(uri);
      xfd.storeRevisionDescriptor(revisionDescriptor);
    }
    catch (ObjectNotFoundException e)
    {
      throw new RevisionDescriptorNotFoundException(uri.toString());
    }
  }

}

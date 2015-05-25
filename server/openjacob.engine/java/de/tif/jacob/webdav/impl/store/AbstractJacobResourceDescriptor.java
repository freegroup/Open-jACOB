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

import java.util.Enumeration;

import org.apache.slide.common.ServiceAccessException;
import org.apache.slide.common.Uri;
import org.apache.slide.content.NodeRevisionDescriptor;
import org.apache.slide.content.NodeRevisionDescriptors;
import org.apache.slide.content.NodeRevisionNumber;
import org.apache.slide.content.RevisionDescriptorNotFoundException;
import org.apache.slide.lock.LockTokenNotFoundException;
import org.apache.slide.lock.NodeLock;
import org.apache.slide.security.NodePermission;
import org.apache.slide.structure.ObjectNode;
import org.apache.slide.structure.ObjectNotFoundException;

public abstract class AbstractJacobResourceDescriptor
{
  static public final transient String RCS_ID = "$Id: AbstractJacobResourceDescriptor.java,v 1.1 2007/01/19 09:50:41 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  protected final Uri uri;
  protected final Object txId;

  public AbstractJacobResourceDescriptor(Uri uri) throws ServiceAccessException
  {
    this.txId = "NONE";

    if (uri == null)
    {
      throw new ServiceAccessException(null, "Trying to initialize ResourceDescriptor with null URI");
    }
    this.uri = uri;
  }

  /**
   * Gets the URI of this descriptor.
   * 
   * @return the URI
   */
  public final String getUri()
  {
    return uri.toString();
  }

  /**
   * Gets the transaction this descriptor lives in.
   * 
   * @return the transaction identifier
   */
  public final Object getTxId()
  {
    return txId;
  }

  /**
   * Checks if the specified object is a descriptor with the same URI in the
   * same transaction.
   * 
   * 
   * @param o
   *          object to compare this descriptor to
   * @return <code>true</code> if object is equal as described above
   */
  public final boolean equals(Object o)
  {
    return (this == o || (o != null && o instanceof AbstractJacobResourceDescriptor && ((AbstractJacobResourceDescriptor) o).uri.equals(uri) && ((AbstractJacobResourceDescriptor) o).txId
        .equals(txId)));
  }

  public final String toString()
  {
    return txId + ": " + uri;
  }

  /**
   * Store an object in the Descriptors Store.
   * 
   * @param object
   *          Object to update
   * @exception ServiceAccessException
   *              Error accessing the Descriptors Store
   * @exception ObjectNotFoundException
   *              The object to update was not found
   */
  public abstract void storeObject(ObjectNode aObject) throws ServiceAccessException, ObjectNotFoundException;

  /**
   * Retrieve an object from the Descriptors Store.
   * 
   * @exception ServiceAccessException
   *              Error accessing the Descriptors Store
   * @exception ObjectNotFoundException
   *              The object to retrieve was not found
   */
  public abstract ObjectNode retrieveObject() throws ServiceAccessException, ObjectNotFoundException;

  /**
   * Returns the list of locks put on a subject.
   * 
   * @param subject
   *          Subject
   * @return Enumeration List of locks which have been put on the subject
   * @exception ServiceAccessException
   *              Service access error
   */
  public abstract Enumeration enumerateLocks() throws ServiceAccessException;

  /**
   * Puts a lock on a subject.
   * 
   * @param lock
   *          Lock token
   * @exception ServiceAccessException
   *              Service access error
   */
  public abstract void putLock(NodeLock lock) throws ObjectNotFoundException, ServiceAccessException;

  /**
   * Renews a lock.
   * 
   * @param lock
   *          Token to renew
   * @exception ServiceAccessException
   *              Service access error
   * @exception LockTokenNotFoundException
   *              Lock token was not found
   */
  public abstract void renewLock(NodeLock lock) throws LockTokenNotFoundException, ObjectNotFoundException, ServiceAccessException;

  /**
   * Removes (cancels) a lock.
   * 
   * @param lock
   *          Token to remove
   * @exception ServiceAccessException
   *              Service access error
   * @exception LockTokenNotFoundException
   *              Lock token was not found
   */
  public abstract void removeLock(NodeLock lock) throws LockTokenNotFoundException, ObjectNotFoundException, ServiceAccessException;

  /**
   * Store an object permissions in the Descriptors Store.
   * 
   * @param permission
   *          Permission we want to create
   * @exception ServiceAccessException
   *              Error accessing the Descriptors Store
   */
  public abstract Enumeration enumeratePermissions() throws ServiceAccessException;

  /**
   * Store an object permissions in the Descriptors Store.
   * 
   * @param permission
   *          Permission we want to create
   * @exception ServiceAccessException
   *              Error accessing the Descriptors Store
   */
  public abstract void grantPermission(NodePermission permission) throws ObjectNotFoundException, ServiceAccessException;

  /**
   * Store an object permissions in the Descriptors Store.
   * 
   * @param permission
   *          Permission we want to create
   * @exception ServiceAccessException
   *              Error accessing the Descriptors Store
   */
  public abstract void revokePermission(NodePermission permission) throws ObjectNotFoundException, ServiceAccessException;

  /**
   * Revoke all the permissions on the object .
   * 
   * @param permission
   *          Permission we want to create
   * @exception ServiceAccessException
   *              Error accessing the Descriptors Store
   */
  public abstract void revokePermissions() throws ObjectNotFoundException, ServiceAccessException;

  /**
   * Create new revision descriptors.
   * 
   * @param revisionDescriptors
   *          Node revision descriptors
   * @exception ServiceAccessException
   *              Service access error
   */
  public abstract void createRevisionDescriptors(NodeRevisionDescriptors aRevisionDescriptors) throws ObjectNotFoundException, ServiceAccessException;

  /**
   * Remove revision descriptors.
   * 
   * @exception ServiceAccessException
   *              Service access error
   */
  public abstract void removeRevisionDescriptors() throws ObjectNotFoundException, ServiceAccessException;

  /**
   * Retrieve a revision descriptors.
   * 
   * @exception ServiceAccessException
   *              Service access error
   * @exception RevisionDescriptorNotFoundException
   *              Revision descriptor was not found
   */
  public abstract NodeRevisionDescriptors retrieveRevisionDescriptors() throws ServiceAccessException, RevisionDescriptorNotFoundException;

  /**
   * Update revision descriptors.
   * 
   * @param revisionDescriptors
   *          Node revision descriptors
   * @exception ServiceAccessException
   *              Service access error
   * @exception RevisionDescriptorNotFoundException
   *              Revision descriptor was not found
   */
  public abstract void storeRevisionDescriptors(NodeRevisionDescriptors aRevisionDescriptors) throws RevisionDescriptorNotFoundException,
      ObjectNotFoundException, ServiceAccessException;

  /**
   * Create new revision descriptor.
   * 
   * @param revisionDescriptor
   *          Node revision descriptor
   * @exception ServiceAccessException
   *              Service access error
   */
  public abstract void createRevisionDescriptor(NodeRevisionDescriptor aRevisionDescriptor) throws ObjectNotFoundException, ServiceAccessException;

  /**
   * Remove revision descriptor.
   * 
   * @param revisionNumber
   *          Revision number
   * @exception ServiceAccessException
   *              Service access error
   */
  public abstract void removeRevisionDescriptor(NodeRevisionNumber number) throws ObjectNotFoundException, ServiceAccessException;

  /**
   * Retrieve revision descriptor.
   * 
   * @param revisionNumber
   *          Node revision number
   */
  public abstract NodeRevisionDescriptor retrieveRevisionDescriptor(NodeRevisionNumber revisionNumber) throws ServiceAccessException,
      RevisionDescriptorNotFoundException;

  /**
   * Update revision descriptor.
   * 
   * @param revisionDescriptors
   *          Node revision descriptor
   * @exception ServiceAccessException
   *              Service access error
   * @exception RevisionDescriptorNotFoundException
   *              Revision descriptor was not found
   */
  public abstract void storeRevisionDescriptor(NodeRevisionDescriptor aRevisionDescriptor) throws RevisionDescriptorNotFoundException, ObjectNotFoundException,
      ServiceAccessException;
}

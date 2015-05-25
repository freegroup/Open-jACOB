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
import java.util.Hashtable;
import java.util.Vector;

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

public class JacobResourceDescriptor extends AbstractJacobResourceDescriptor
{
  static public final transient String RCS_ID = "$Id: JacobResourceDescriptor.java,v 1.1 2007/01/19 09:50:41 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  protected boolean registeredForSaving = false;

  /** Stored object.*/
  protected ObjectNode object;

  /** Permissions vector. */
  protected Vector permissions;

  /** Locks vector.*/
  protected Vector locks;

  /** Revision descriptors.*/
  protected NodeRevisionDescriptors revisionDescriptors;

  /** Revision descriptor hashtable.*/
  protected Hashtable descriptor;

  //    protected static Object createObject(String aNomClasse, Class aTypes[], Object aArgs[])
  //        throws UnknownObjectClassException {
  //        Class aClasse = null;
  //        try {
  //            // First, load the object's class
  //            aClasse = Class.forName(aNomClasse);
  //            Constructor aConstructor = aClasse.getConstructor(aTypes);
  //            if (aConstructor == null)
  //                aConstructor = aClasse.getSuperclass().getConstructor(aTypes);
  //            return aConstructor.newInstance(aArgs);
  //
  //        } catch (Exception e) {
  //            throw new UnknownObjectClassException(aNomClasse);
  //        }
  //    }

  /**
   * Creates an XML descriptor resource.
   * 
   * @param uri uri of the resource
   * @param txId identifier for the transaction in which the descriptor is to be managed
   * @param characterEncoding charcter enconding used to store this descriptor in XML
   * @throws ServiceAccessException if anything goes wrong at system level
   */
  public JacobResourceDescriptor(Uri uri) throws ServiceAccessException
  {

    super(uri);
  }

  // -------------- PART TAKE OVER FROM AbstractUriProperties START
  // --------------

  protected static void throwUnsupportedError() throws ServiceAccessException
  {
    throw new ServiceAccessException(null, "Unsupported operation");
  }
  
  /**
   * Retrive an object from the Descriptors Store.
   *
   * @exception ServiceAccessException Error accessing the Descriptors Store
   * @exception ObjectNotFoundException The object to retrieve was not found
   */
  public ObjectNode retrieveObject() throws ServiceAccessException, ObjectNotFoundException
  {
    if (object == null)
    {
      throw new ObjectNotFoundException(uri);
    }
    return object.cloneObject();
  }

  /**
   * Store an object in the Descriptors Store.
   *
   * @param object Object to update
   * @exception ServiceAccessException Error accessing the Descriptors Store
   * @exception ObjectNotFoundException The object to update was not found
   */
  public void storeObject(ObjectNode aObject) throws ServiceAccessException, ObjectNotFoundException
  {
    object = aObject.cloneObject();
  }

  /**
   * Remove an object from the Descriptors Store.
   *
   * @param object Object to remove
   * @exception ServiceAccessException Error accessing the Descriptors Store
   * @exception ObjectNotFoundException The object to remove was not found
   */
  public void removeObject(ObjectNode aObject) throws ServiceAccessException, ObjectNotFoundException
  {
    object = null;
  }

  /**
   * Store an object permissions in the Descriptors Store.
   *
   * @param permission Permission we want to create
   * @exception ServiceAccessException Error accessing the Descriptors Store
   */
  public void grantPermission(NodePermission permission) throws ObjectNotFoundException, ServiceAccessException
  {
    if (permissions == null)
      permissions = new Vector();
    permissions.addElement(permission.cloneObject());
  }

  /**
   * Store an object permissions in the Descriptors Store.
   *
   * @param permission Permission we want to create
   * @exception ServiceAccessException Error accessing the Descriptors Store
   */
  public void revokePermission(NodePermission permission) throws ObjectNotFoundException, ServiceAccessException
  {
    if (permissions != null)
      permissions.removeElement(permission);
  }

  /**
   * Revoke all the permissions on the object .
   *
   * @param permission Permission we want to create
   * @exception ServiceAccessException Error accessing the Descriptors Store
   */
  public void revokePermissions() throws ObjectNotFoundException, ServiceAccessException
  {
    if (permissions != null)
      permissions.removeAllElements();
  }

  /**
   * Store an object permissions in the Descriptors Store.
   *
   * @param permission Permission we want to create
   * @exception ServiceAccessException Error accessing the Descriptors Store
   */
  public Enumeration enumeratePermissions() throws ServiceAccessException
  {
    if (permissions == null)
      permissions = new Vector();
    return permissions.elements();
  }

  /**
   * Puts a lock on a subject.
   *
   * @param lock Lock token
   * @exception ServiceAccessException Service access error
   */
  public void putLock(NodeLock lock) throws ObjectNotFoundException, ServiceAccessException
  {
    if (locks == null)
      locks = new Vector();
    locks.addElement(lock.cloneObject());
  }

  /**
   * Renews a lock.
   *
   * @param lock Token to renew
   * @exception ServiceAccessException Service access error
   * @exception LockTokenNotFoundException Lock token was not found
   */
  public void renewLock(NodeLock lock) throws LockTokenNotFoundException, ObjectNotFoundException, ServiceAccessException
  {
    if (locks == null)
      locks = new Vector();
    boolean wasPresent = locks.removeElement(lock);
    if (!wasPresent)
    {
      throw new LockTokenNotFoundException(lock);
    }
    locks.addElement(lock.cloneObject());
  }

  /**
   * Removes (cancels) a lock.
   *
   * @param lock Token to remove
   * @exception ServiceAccessException Service access error
   * @exception LockTokenNotFoundException Lock token was not found
   */
  public void removeLock(NodeLock lock) throws LockTokenNotFoundException, ObjectNotFoundException, ServiceAccessException
  {

    if (locks == null)
    {
      throw new LockTokenNotFoundException(lock);
    }
    boolean wasPresent = locks.removeElement(lock);
    if (!wasPresent)
    {
      throw new LockTokenNotFoundException(lock);
    }
  }

  /**
   * Returns the list of locks put on a subject.
   *
   * @param subject Subject
   * @return Enumeration List of locks which have been put on the subject
   * @exception ServiceAccessException Service access error
   */
  public Enumeration enumerateLocks() throws ServiceAccessException
  {
    if (locks == null)
      locks = new Vector();
    return locks.elements();
  }

  /**
   * Retrieve a revision descriptors.
   *
   * @exception ServiceAccessException Service access error
   * @exception RevisionDescriptorNotFoundException Revision descriptor
   * was not found
   */
  public NodeRevisionDescriptors retrieveRevisionDescriptors() throws ServiceAccessException, RevisionDescriptorNotFoundException
  {
    if (revisionDescriptors == null)
    {
      throw new RevisionDescriptorNotFoundException(uri.toString());
    }
    return revisionDescriptors.cloneObject();
  }

  /**
   * Create new revision descriptors.
   *
   * @param revisionDescriptors Node revision descriptors
   * @exception ServiceAccessException Service access error
   */
  public void createRevisionDescriptors(NodeRevisionDescriptors aRevisionDescriptors) throws ObjectNotFoundException, ServiceAccessException
  {
    revisionDescriptors = aRevisionDescriptors.cloneObject();
  }

  /**
   * Update revision descriptors.
   *
   * @param revisionDescriptors Node revision descriptors
   * @exception ServiceAccessException Service access error
   * @exception RevisionDescriptorNotFoundException Revision descriptor
   * was not found
   */
  public void storeRevisionDescriptors(NodeRevisionDescriptors aRevisionDescriptors) throws RevisionDescriptorNotFoundException, ObjectNotFoundException,
      ServiceAccessException
  {
    if (!revisionDescriptors.getUri().equals(uri.toString()))
    {
      throw new RevisionDescriptorNotFoundException(uri.toString());
    }
    revisionDescriptors = aRevisionDescriptors.cloneObject();
  }

  /**
   * Remove revision descriptors.
   *
   * @exception ServiceAccessException Service access error
   */
  public void removeRevisionDescriptors() throws ObjectNotFoundException, ServiceAccessException
  {
    revisionDescriptors = null;
  }

  /**
   * Retrieve revision descriptor.
   *
   * @param revisionNumber Node revision number
   */
  public NodeRevisionDescriptor retrieveRevisionDescriptor(NodeRevisionNumber revisionNumber) throws ServiceAccessException,
      RevisionDescriptorNotFoundException
  {
    Object result = null;

    if (descriptor != null && revisionNumber != null)
      result = descriptor.get(revisionNumber.toString());

    if (result == null)
    {
      throw new RevisionDescriptorNotFoundException(uri.toString());
    }
    return ((NodeRevisionDescriptor) result).cloneObject();
  }

  /**
   * Create new revision descriptor.
   *
   * @param revisionDescriptor Node revision descriptor
   * @exception ServiceAccessException Service access error
   */
  public void createRevisionDescriptor(NodeRevisionDescriptor aRevisionDescriptor) throws ObjectNotFoundException, ServiceAccessException
  {
    if (descriptor == null)
      descriptor = new Hashtable();

    descriptor.put(aRevisionDescriptor.getRevisionNumber().toString(), aRevisionDescriptor.cloneObject());
  }

  /**
   * Update revision descriptor.
   *
   * @param revisionDescriptors Node revision descriptor
   * @exception ServiceAccessException Service access error
   * @exception RevisionDescriptorNotFoundException Revision descriptor
   * was not found
   */
  public void storeRevisionDescriptor(NodeRevisionDescriptor aRevisionDescriptor) throws RevisionDescriptorNotFoundException, ObjectNotFoundException,
      ServiceAccessException
  {
    String key = aRevisionDescriptor.getRevisionNumber().toString();
    if (descriptor == null || !descriptor.containsKey(key))
    {
      throw new RevisionDescriptorNotFoundException(uri.toString());
    }
    descriptor.put(key, aRevisionDescriptor.cloneObject());
  }

  /**
   * Remove revision descriptor.
   *
   * @param revisionNumber Revision number
   * @exception ServiceAccessException Service access error
   */
  public void removeRevisionDescriptor(NodeRevisionNumber number) throws ObjectNotFoundException, ServiceAccessException
  {
    if (descriptor == null)
      return;

    descriptor.remove(number.toString());
  }

  // -------------- PART TAKE OVER FROM AbstractUriProperties END -------------- 

  public void registerForSaving()
  {
    registeredForSaving = true;
  }

  public boolean isRegisteredForSaving()
  {
    return registeredForSaving;
  }
}

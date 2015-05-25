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

package de.tif.jacob.core.data.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.tif.jacob.util.Base64;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class DataReference
{
  static public transient final String        RCS_ID = "$Id: DataReference.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
  static public transient final String        RCS_REV = "$Revision: 1.1 $";
  
  protected static DataReference createInstanceByReference(String referenceString)
  {
  	if (null == referenceString)
  		throw new NullPointerException("referenceString is null");
  	
    try
    {
      ByteArrayInputStream stream = new ByteArrayInputStream(Base64.decode(referenceString));
      ObjectInputStream os = new ObjectInputStream(stream);
      DataReference result = (DataReference) os.readObject();
      os.close();
      return result;
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex.toString());
    }
    catch (ClassNotFoundException ex)
    {
      throw new RuntimeException(ex.toString());
    }
  }

  protected final String toReferenceInternal()
  {
    try
    {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(stream);
      os.writeObject(this);
      os.close();
      return Base64.encode(stream.toByteArray());
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex.toString());
    }
  }

  
}

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

package de.tif.qes.adl.element;

import java.util.Iterator;
import java.util.List;

import de.tif.jacob.core.definition.impl.AbstractApplicationInfo;

/**
 * @author Andreas
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ADLApplication extends AbstractApplicationInfo implements Comparable
{
  static public final transient String RCS_ID = "$Id: ADLApplication.java,v 1.1 2006-12-21 11:32:20 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";
  
  private final List moduleNames;
  
	public ADLApplication(String name, String title, List moduleNames)
  {
    super(name, title, null);
    this.moduleNames = moduleNames;
  }

  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.impl.AbstractApplicationInfo#getEventHandler()
   */
  public String getEventHandler()
  {
    // use default event handler mechanism
    return null;
  }
  
  /* (non-Javadoc)
   * @see de.tif.jacob.core.definition.impl.AbstractApplicationInfo#lookupEventHandlerByReference()
   */
  public boolean lookupEventHandlerByReference()
  {
    // lookup event handlers by name for this application
    return false;
  }

	/**
	 * @return Returns the moduleNames.
	 */
	public List getModuleNames()
	{
		return moduleNames;
	}

  public Iterator getDomainNames()
  {
    return new FocusIterator(this.moduleNames.iterator());
  }
  
  private static class FocusIterator implements Iterator
  {
    private final Iterator moduleIter;
    private Object next;

    FocusIterator(Iterator moduleIter)
    {
      this.moduleIter = moduleIter;
      iterate();
    }

    private void iterate()
    {
      while (this.moduleIter.hasNext())
      {
        this.next = this.moduleIter.next();
        if (ADLModule.isFocusName((String) this.next))
          return;
      }
      this.next = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext()
    {
      return this.next != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#next()
     */
    public Object next()
    {
      if (this.next == null)
        return null;

      Object next = this.next;
      iterate();
      return next;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }

}

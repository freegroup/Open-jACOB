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

package de.tif.qes.adf;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.tif.jacob.core.definition.impl.AbstractElement;
import de.tif.jacob.core.definition.impl.ConvertToJacobOptions;
import de.tif.qes.IQeScript;
import de.tif.qes.IQeScriptContainer;
import de.tif.qes.adf.castor.Event;
import de.tif.qes.adf.castor.Events;
import de.tif.qes.adf.castor.Script;
import de.tif.qes.adf.castor.Scripts;

/**
 * @author Andreas Sonntag
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class ADFScript implements IQeScript
{
  private final String file;
  private final String script;
  private final String type;
  
  protected static Set fetchScripts(Events events)
  {
    Set scripts = new HashSet();
    if (null != events)
    {
      for (int i = 0; i < events.getEventCount(); i++)
      {
        Event event = events.getEvent(i);
        scripts.add(new ADFScript(event.getFile(), event.getName(), event.getType()));
      }
    }
    return Collections.unmodifiableSet(scripts);
  }
  
  protected static Set fetchScripts(Scripts cscripts)
  {
    Set scripts = new HashSet();
    for (int i = 0; i < cscripts.getScriptCount(); i++)
    {
      Script script = cscripts.getScript(i);
      scripts.add(new ADFScript(script.getFile(), script.getName(), script.getEvent()));
    }
    return Collections.unmodifiableSet(scripts);
  }
  
  protected static void putScriptsToProperties(Set scripts, ConvertToJacobOptions options, AbstractElement container)
  {
    if (scripts.size() > 0 && options.isFetchScripts())
    {
      StringBuffer buffer = new StringBuffer();
      Iterator iter = scripts.iterator();
      boolean first = true;
      while (iter.hasNext())
      {
        IQeScript script = (IQeScript) iter.next();
        if (!first)
        {  
          buffer.append(";");
        }
        else
        {
          // just for the first script!
          container.setProperty(IQeScriptContainer.SCRIPT_FILE_PROPERTY, script.getFile());
          container.setProperty(IQeScriptContainer.SCRIPT_NAME_PROPERTY, script.getScript());
          container.setProperty(IQeScriptContainer.SCRIPT_TYPE_PROPERTY, script.getType());
          
          first = false;
        }
        buffer.append(script);
      }
      
      container.setProperty(IQeScriptContainer.SCRIPTS_PROPERTY, buffer.toString());
    }
  }
  
  /**
   * 
   */
  protected ADFScript(String file, String script, String type)
  {
    this.file = file;
    this.script = script;
    this.type = type;
  }
  
	/**
	 * @return Returns the file.
	 */
	public String getFile()
	{
		return file;
	}

	/**
	 * @return Returns the script.
	 */
	public String getScript()
	{
		return script;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType()
	{
		return type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object anObject)
	{
		if (this == anObject)
		{
			return true;
		}
		if (anObject instanceof ADFScript)
		{
			ADFScript another = (ADFScript) anObject;
			
			return this.file.equals(another.file) && this.script.equals(another.script) && this.type.equals(another.type);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return this.script.hashCode() + 31 * this.file.hashCode() + 127 * this.type.hashCode();
	}
	
	
	/**
	 * toString methode: creates a String representation of the object
	 * @return the String representation
	 * @author info.vancauwenberge.tostring plugin
	
	 */
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("ADFScript[");
		buffer.append("file = ").append(file);
		buffer.append(", script = ").append(script);
		buffer.append(", type = ").append(type);
		buffer.append("]");
		return buffer.toString();
	}
}

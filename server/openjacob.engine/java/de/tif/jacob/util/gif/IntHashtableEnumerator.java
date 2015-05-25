package de.tif.jacob.util.gif;
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

import java.util.Enumeration;
import java.util.NoSuchElementException;

class IntHashtableEnumerator implements Enumeration
{
    boolean keys;
    int index;
    IntHashtableEntry table[];
    IntHashtableEntry entry;

    IntHashtableEnumerator( IntHashtableEntry table[], boolean keys )
	{
	this.table = table;
	this.keys = keys;
	this.index = table.length;
	}

    public boolean hasMoreElements()
	{
	if ( entry != null )
	    return true;
	while ( index-- > 0 )
	    if ( ( entry = table[index] ) != null )
		return true;
	return false;
	}

    public Object nextElement()
	{
	if ( entry == null )
	    while ( ( index-- > 0 ) && ( ( entry = table[index] ) == null ) )
		;
	if ( entry != null )
	    {
	    IntHashtableEntry e = entry;
	    entry = e.next;
	    return keys ? new Integer( e.key ) : e.value;
	    }
	throw new NoSuchElementException( "IntHashtableEnumerator" );
	}
    }

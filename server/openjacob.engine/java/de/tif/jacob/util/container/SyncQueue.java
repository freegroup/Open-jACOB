package de.tif.jacob.util.container;

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

import java.util.NoSuchElementException;


/**
 *  Synchronized Queue implementation
 *
 */
public class SyncQueue extends Queue
{
    /**
     *  Add element to synchronized queue
     */
    public synchronized void add( Object obj )
    {
        super.add(obj);
    }

    /**
     *  Remove element from synchronized queue
     */
    public synchronized Object remove( ) throws NoSuchElementException
    {
        return (super.remove());
    }

    /**
     * Peek element from synchronized queue
     */
    public synchronized Object peek( ) throws NoSuchElementException
    {
        return (super.peek());
    }

    /**
     *  Query size of synchronized queue
     */
    public synchronized int size()
    {
        return (super.size());
    }
}

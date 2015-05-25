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


import java.util.ArrayList;
import java.util.NoSuchElementException;


/**
 * Queue implementation; this is a really bad implementation since it's
 * based on a ArrayList structure, but for now it has to suffice (low on time)
 *
 */
public class Queue
{
    // Storage ArrayList for queue elements
    ArrayList storage = new ArrayList ( );

    /**
     *  Add element to queue
     */
    public void add( Object obj )
    {
        // Add element to end of storage ArrayList
        storage.add( obj );
    }

    /**
     *  Add priority element to queue on top of all other elements
     */
    public void addPriority( Object obj )
    {
        // Add priority element to begin of storage ArrayList
        storage.add (0, obj );
    }

    /**
     *  Remove element from queue
     */
    public Object remove( ) throws NoSuchElementException
    {
        Object obj;

        // Get and return first element from begin of storage ArrayList and remove this element
        obj = storage.get(0 );
        storage.remove( obj );
        return obj;
    }

    /**
     * Peek element from queue
     */
    public Object peek( ) throws NoSuchElementException
    {
        // Get and return first element from begin of storage ArrayList
        return storage.get(0 );
    }

    /**
     * Query size of queue
     */
    public int size ( )
    {
        // Return size of storage ArrayList
        return storage.size();
    }
}

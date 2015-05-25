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
 *  Blocking Synchronized Queue implementation,
 *  i.e. removeElement waits until at least one element can be retrieved
 *
 */
public class BlockingQueue extends Queue
{
    // Semaphore holding access rights to remove element from synchronized queue
    private int  m_max;

    public BlockingQueue( int max)
    {
        m_max = max;
    }

    /**
     *  Add element to blocking synchronized queue
     */
    public synchronized void add( Object obj )
    {
        while(isFull())
        {
            try
            {
                wait();
            } catch(InterruptedException exc){};
        }
        // Add element to queue and release one access right
        super.add( obj );
        notify();
    }

    /**
     * Peek element from queue
     */
    public synchronized Object peek( ) throws NoSuchElementException
    {
        while(isEmpty())
        {
            try
            {
                wait();
            } catch (InterruptedException exc){};
        }
        return super.peek();
    }


    /**
     *  Remove element from blocking synchronized queue
     */
    public synchronized Object remove( ) throws NoSuchElementException
    {
        while(isEmpty())
        {
            try
            {
                wait();
            } catch (InterruptedException exc){};
        }
        // Request one access right and remove element from queue
        notify();
        return super.remove();
    }

    public boolean isFull()
    {
        return super.size() >= m_max;
    }

    public boolean isEmpty()
    {
        return super.size()==0;
    }
}

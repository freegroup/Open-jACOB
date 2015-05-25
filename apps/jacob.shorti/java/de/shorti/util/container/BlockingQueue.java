package de.shorti.util.container;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */

import java.util.NoSuchElementException;
import de.shorti.util.basic.Semaphore;


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

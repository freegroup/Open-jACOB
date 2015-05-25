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
import de.shorti.util.*;


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

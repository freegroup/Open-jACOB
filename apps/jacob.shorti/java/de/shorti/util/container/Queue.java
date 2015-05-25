package de.shorti.util.container;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */


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

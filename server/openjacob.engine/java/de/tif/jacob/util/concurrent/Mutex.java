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

package de.tif.jacob.util.concurrent;

public class Mutex
{
  public static final long ONE_SECOND = 1000;
  public static final long ONE_MINUTE = 60 * ONE_SECOND;
  public static final long ONE_HOUR = 60 * ONE_MINUTE;
  public static final long ONE_DAY = 24 * ONE_HOUR;
  public static final long ONE_WEEK = 7 * ONE_DAY;
  public static final long ONE_YEAR = (long)(365.2425 * ONE_DAY);
  public static final long ONE_CENTURY = 100 * ONE_YEAR;

  /** The lock status **/
  protected boolean inuse_ = false;

  /**
   * Acquire the lock for this mutex and wait infinite.
   * 
   * @throws InterruptedException
   */
  public void acquire() throws InterruptedException 
  {
    if (Thread.interrupted()) throw new InterruptedException();
    synchronized(this) 
    {
      try 
      {
        while (inuse_) wait();
        inuse_ = true;
      }
      catch (InterruptedException ex) 
      {
        notify();
        throw ex;
      }
    }
  }

  /**
   * Release the current lock
   *
   */
  public synchronized void release()  
  {
    inuse_ = false;
    notify(); 
  }


  /**
   * Try to lock the mutex.
   * 
   * @param msecs wait time before the caller retrieves an Interrupt Exception
   * @return
   * @throws InterruptedException
   */
  public boolean attempt(long msecs) throws InterruptedException 
  {
    if (Thread.interrupted()) 
      throw new InterruptedException();
    synchronized(this) 
    {
      if (!inuse_) 
      {
        inuse_ = true;
        return true;
      }
      else if (msecs <= 0)
      {
        return false;
      }
      else 
      {
        long waitTime = msecs;
        long start = System.currentTimeMillis();
        try {
          for (;;) 
          {
            wait(waitTime);
            if (!inuse_) 
            {
              inuse_ = true;
              return true;
            }
            else 
            {
              waitTime = msecs - (System.currentTimeMillis() - start);
              if (waitTime <= 0) 
                return false;
            }
          }
        }
        catch (InterruptedException ex) 
        {
          notify();
          throw ex;
        }
      }
    }  
  }
  
  
}

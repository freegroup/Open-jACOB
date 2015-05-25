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
package de.tif.jacob.cluster.impl;

import java.io.PrintWriter;
import java.io.Serializable;


public class GroupTest implements Runnable, IGroupMessageListener
{
  private static final String MESSAGE_TYPE = "myMessage";
  
  private final Group grp;

  public GroupTest() throws Exception
  {
    this.grp = new Group("test", ClusterUdpGroupProvider.getJGroupProperties("228.8.8.7", "35555"));
    this.grp.register(MESSAGE_TYPE, this, true);
    this.grp.start();
  }

  public void receive(Serializable msg) throws Exception
  {
    // do nothing here
  }
  
  public static void main(String[] args) throws Exception
  {
    Thread t1 = new Thread(new GroupTest(), "TEST 1");
    Thread t2 = new Thread(new GroupTest(), "TEST 2");
    t1.start();
    t2.start();

    int secs = 10;
    System.out.println("sleeping for "+secs+"secs");
    Thread.sleep(secs * 1000);

    System.out.println("joining threads");
    t1.join();
    t2.join();
    System.out.println("finished");
  }

  public void run()
  {
    try
    {
      int secs = 5;
      System.out.println(Thread.currentThread().toString() + ": waiting for " + secs + "secs");
      Thread.sleep(secs * 1000);
      System.out.println(Thread.currentThread().toString() + ": start sending: " + this.grp);
      for (int j = 0; j < 8000; j++)
      {
        {
          final int COUNT = 5; //5000;
          long time = System.currentTimeMillis();
          for (int i = 0; i < COUNT; i++)
          {
            this.grp.setValue("Counter", new Integer(i));
          }
          long diff = System.currentTimeMillis() - time;
//          System.out.println(Thread.currentThread().toString() + ": iteration " + j + ": setValue: " + diff + "ms; " + ((double) diff / COUNT) + "ms/req");
        }
        {
          final int COUNT = 5; //5000;
          long time = System.currentTimeMillis();
          for (int i = 0; i < COUNT; i++)
          {
            this.grp.sendMessage("myMessage", new Integer(i));
          }
          long diff = System.currentTimeMillis() - time;
//          System.out.println(Thread.currentThread().toString() + ": iteration " + j + ": sendMessage: " + diff + "ms; " + ((double) diff / COUNT) + "ms/req");
        }
        {
          long time = System.currentTimeMillis();
//          System.gc();
          long diff = System.currentTimeMillis() - time;
//          System.out.println(Thread.currentThread().toString() + ": iteration " + j + ": gc: " + diff + "ms");
        }
        Thread.sleep(100);
      }
      
      PrintWriter pw = new PrintWriter(System.out);
      this.grp.printInfo(pw);
      pw.flush();
      this.grp.close();
      System.out.println(Thread.currentThread().toString() + ": finished sending: " + this.grp);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}

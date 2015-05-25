/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2010 Andreas Herz | FreeGroup
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
/*
 * Created on 17.02.2005
 *
 */
package de.tif.jacob.designer.util;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * @author andreas sonntag
 *
 */
public class Verify
{
  public static final VerifyListener INTEGER = new VerifyListener()
  {
    public void verifyText(VerifyEvent e)
    {
      e.doit = e.text.length() ==0 || e.text.matches("-?[0-9]*");
    }
  };
  
  public static final VerifyListener FIELDNAME = new VerifyListener()
  {
    public void verifyText(VerifyEvent e)
    {
      // Das widget wird das erste mal initialisiert
      // data="" und im e.text steht der in itiale text
      //
      
      String data = ((Text)e.widget).getText();
      if(e.text.matches("^[a-zA-Z][a-zA-Z_0-9]*") && data.length()==0)
      {
        // e.doit = true;
      }
      else if(data.length()>0 && data.matches("^[a-zA-Z_0-9]*") && e.text.matches("[a-zA-Z_0-9]*"))
      {
        // e.doit = true;
      }
      else
      {
        e.doit=false;
      }
    }
  };
  
  
  public static final VerifyListener DECIMAL = new VerifyListener()
  {
    public void verifyText(VerifyEvent e)
    {
      e.doit = e.text.length() ==0 || e.text.matches("-?[0-9]*[.]?[0-9]*");
    }
  };
  
  public static final VerifyListener UNSIGNED_INTEGER = new VerifyListener()
  {
    public void verifyText(VerifyEvent e)
    {
      System.out.println("VerifyEvent:"+e.toString());
      
      // Here, we could use a RegExp such as the following
      // if using JRE1.4 such as e.doit = e.text.matches("[\\-0-9]*");
      e.doit = e.text.length() ==0 || e.text.matches("[0-9]*");
//      e.doit = "0123456789".indexOf(e.text) >= 0;
    }
  };

  // TODO: Exponentialangabe ist hier mit nicht möglich
  public static final VerifyListener FLOAT = DECIMAL;

  public static final VerifyListener DOUBLE = DECIMAL;
}

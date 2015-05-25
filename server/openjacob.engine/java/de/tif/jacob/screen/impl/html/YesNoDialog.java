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

package de.tif.jacob.screen.impl.html;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
class YesNoDialog
{
  static public final transient String RCS_ID = "$Id: YesNoDialog.java,v 1.1 2007/01/19 09:50:31 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  private static ThreadLocal yesNoDialog = null;
   
  private String message = null;
  
  private YesNoDialog(String title, String message)
  {
   this.message = message;
  }

  public String getMessage()
  {
   return message; 
  }
  
  public static YesNoDialog get() 
  {
    // get from the current thread the yesNoDialog if the application programmes has been
    // requested one
    //
    if(yesNoDialog==null)
      return null;
    
  	YesNoDialog dialog =  (YesNoDialog)yesNoDialog.get();
    yesNoDialog = null;
    
    return dialog;
  }
  
  /**
   * Shows an HTML-Dialog on the client. The hands over event handler will be called
   * if the user pressed OK or CANCEL. If the user doesn't presse any button (e.g. ALT+F4)
   * no event will be fired.
   * 
   * @param title    The title of the dialog
   * @param question The question of the dialog.
   */
  public static void show(String title, String question)
  {
    final String t = title;
    final String q = question;
  	yesNoDialog = new ThreadLocal() {
             protected synchronized Object initialValue() {
                   return new YesNoDialog(t, q);
               }
         };
  }
}

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

package de.tif.jacob.screen.dialogs;

/**
 * Base interface for message dialog implementations.
 * <p>
 * A message ask dialog can be created by means of: <br>
 * <li>{@link de.tif.jacob.screen.IClientContext#createMessageDialog(String)}
 * <li>{@link de.tif.jacob.screen.IClientContext#createMessageDialog(Message)}
 * 
 * @author Andreas Herz
 */
public interface IMessageDialog extends IDialog
{
  /**
   * The internal revision control system id.
   */
  static public final String RCS_ID = "$Id: IMessageDialog.java,v 1.1 2007/01/19 09:50:44 freegroup Exp $";

  /**
   * The internal revision control system id in short form.
   */
  static public final String RCS_REV = "$Revision: 1.1 $";

  /**
   * Appends a new message line to the message text.
   * <p>
   * Example:
   * 
   * <pre>
   * IMessageDialog dialog = context.createMessageDialog(&quot;The following items are out of stock:&quot;);
   * dialog.println(&quot;- Item A&quot;);
   * dialog.println(&quot;- Item C&quot;);
   * dialog.println(&quot;- Item G&quot;);
   * dialog.show();
   * </pre>
   * 
   * @param messageLine
   *          the new message line to append
   */
  public abstract void println(String messageLine);

  /**
   * Returns the dialog message. The returned message will be localized, if the
   * message dialog has been invoked by means of
   * {@link de.tif.jacob.screen.IClientContext#createMessageDialog(Message)}.
   * 
   * @return the dialog message
   */
  public abstract String getMessage();
}

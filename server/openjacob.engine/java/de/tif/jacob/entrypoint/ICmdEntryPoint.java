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

package de.tif.jacob.entrypoint;

import java.util.Properties;

/**
 *
 */
public interface ICmdEntryPoint extends IEntryPoint
{
 
  /**
   * The mime type of the returned document. A entry point can return different types
   * of documents. If you implement a document provider (download of documents from a DB)
   * you can set the mimetype for each request depending on the handsover properties or the
   * context information.
   * <br>
   * 
   * <br>
   * Note: since 2.10 call CmdEntryPointContext.setContentType in your <code>enter</code> method instead.
   * 
   * @param context The context of the user client
   * @param properties The hands over properties to the entry point
   * @return a valid mime type definition. (text/plain or text/html......)
   * @deprecated use {@link CmdEntryPointContext#setContentType(String)}
   * @see CmdEntryPointContext#setContentType(String)
   * @see CmdEntryPointContext#setContentName(String)
   */
  public String getMimeType(CmdEntryPointContext context, Properties properties);
  

  /**
   * The worker function of the entry point. <br>
   * You must return the content of the entry point in
   * this method.<br><br>
   * Example of a simple HTML data table:
   * <br>
   <pre>
   public void enter(CmdEntryPointContext context, Properties properties) throws IOException
   {
      context.setContentType("text/html");
    
      context.getStream().write("&lt;table&gt;\n".getBytes());
      context.getStream().write("&lt;tr&gt;&lt;td&gt;a&lt;/td&gt;&lt;td&gt;b&lt;/td&gt;&lt;td&gt;c&lt;/td&gt;&lt;td&gt;e&lt;/td&gt;&lt;td&gt;f&lt;/td&gt;&lt;td&gt;1&lt;/td&gt;&lt;/tr&gt;\n".getBytes());
      context.getStream().write("&lt;tr&gt;&lt;td&gt;a&lt;/td&gt;&lt;td&gt;b&lt;/td&gt;&lt;td&gt;c&lt;/td&gt;&lt;td&gt;e&lt;/td&gt;&lt;td&gt;f&lt;/td&gt;&lt;td&gt;1&lt;/td&gt;&lt;/tr&gt;\n".getBytes());
      context.getStream().write("&lt;/table&gt;\n".getBytes());
    }
   </pre>;
   * 
   * @param context The context of the user client
   * @param properties The hands over properties / parameters of the entry point call
   * @throws Exception
   */
  public void enter(CmdEntryPointContext context, Properties properties) throws Exception;
}

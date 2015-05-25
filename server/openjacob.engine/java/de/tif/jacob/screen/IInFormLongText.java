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

package de.tif.jacob.screen;

import de.tif.jacob.screen.event.ILinkEventListener;


/**
 * @author Andreas Herz
 *
 */
public interface IInFormLongText extends ILongText 
{
  static public final String RCS_ID = "$Id: IInFormLongText.java,v 1.9 2010/09/27 15:34:16 freegroup Exp $";
  static public final String RCS_REV = "$Revision: 1.9 $";

  /**
   * Returns the position of the text insertion caret for the text component.<br>
   * 
   * A <b>newline</b> is only one caret position. You must handle the \r\n issue by yourself to find
   * the correct position in your text string.
   * <pre>
   * IInFormLongText textarea =(IInFormLongText) context.getGroup().findByName("storage_emailText_body");
   * String value = textarea.getValue();
   * // NOTE: A linebreak is only ONE caret position. You must handle the \r\n issue by your self.
   * value = StringUtil.replace(value, "\r\n","\n");
   * if(textarea.getSelectionStart()!= textarea.getSelectionEnd())
   * {
   *   String start  = value.substring(0,textarea.getSelectionStart());
   *   String center = value.substring(textarea.getSelectionStart(), textarea.getSelectionEnd());
   *   String end    = value.substring(textarea.getSelectionEnd(), value.length());
   *   textarea.setValue(start+"[b]"+center+"[/b]"+end);
   * }
   * else
   * {
   *   String start  = value.substring(0,textarea.getCaretPosition());
   *   String end    = value.substring(textarea.getCaretPosition(), value.length());
   *   textarea.setValue(start+"[X]"+end);
   * }
   * </pre> 
   * @since 2.8.5
   * @return the position of the text insertion caret for the text component >= 0
   */
  public int getCaretPosition();
  
  /**
   * This method request the focus for the text element and tries to set the caret position
   * at the hands over location.<br>
   * <br>
   * It is important to know, that a linebreak (e.g. <i>\r\n</i> or <i>\n</i> or <i>\r</i>) will be counted as a single caret postion.
   * 
   * @see IForm#setFocus(IInFormLongText, int)
   * @param caretPosition the new caret position
   * @since 2.8.5
   */
  public void setCaretPosition(int caretPosition);
  
  /**
   * Returns the selected text's start position. Return 0 for an empty document, 
   * or the value of getCaretPosition if no selection.<br>
   * <br>
   * A <b>newline</b> is only one caret position. You must handle the \r\n issue by yourself to find
   * the correct position in your text string.
   * <pre>
   * IInFormLongText textarea =(IInFormLongText) context.getGroup().findByName("storage_emailText_body");
   * String value = textarea.getValue();
   * // NOTE: A linebreak is only ONE caret position. You must handle the \r\n issue by your self.
   * value = StringUtil.replace(value, "\r\n","\n");
   * if(textarea.getSelectionStart()!= textarea.getSelectionEnd())
   * {
   *   String start  = value.substring(0,textarea.getSelectionStart());
   *   String center = value.substring(textarea.getSelectionStart(), textarea.getSelectionEnd());
   *   String end    = value.substring(textarea.getSelectionEnd(), value.length());
   *   textarea.setValue(start+"[b]"+center+"[/b]"+end);
   * }
   * else
   * {
   *   String start  = value.substring(0,textarea.getCaretPosition());
   *   String end    = value.substring(textarea.getCaretPosition(), value.length());
   *   textarea.setValue(start+"[X]"+end);
   * }
   * </pre> 
   * @since 2.8.5
   * @return the start position >= 0 && <=getSelectionEnd
   */
  public int getSelectionStart();
  
  /**
   * Returns the selected text's end position. Return 0 for an empty document, 
   * or the value of getCaretPosition if no selection.<br>
   * <br>
   * A <b>newline</b> is only one caret position. You must handle the \r\n issue by yourself to find
   * the correct position in your text string.
   * <pre>
   * IInFormLongText textarea =(IInFormLongText) context.getGroup().findByName("storage_emailText_body");
   * String value = textarea.getValue();
   * // NOTE: A linebreak is only ONE caret position. You must handle the \r\n issue by your self.
   * value = StringUtil.replace(value, "\r\n","\n");
   * if(textarea.getSelectionStart()!= textarea.getSelectionEnd())
   * {
   *   String start  = value.substring(0,textarea.getSelectionStart());
   *   String center = value.substring(textarea.getSelectionStart(), textarea.getSelectionEnd());
   *   String end    = value.substring(textarea.getSelectionEnd(), value.length());
   *   textarea.setValue(start+"[b]"+center+"[/b]"+end);
   * }
   * else
   * {
   *   String start  = value.substring(0,textarea.getCaretPosition());
   *   String end    = value.substring(textarea.getCaretPosition(), value.length());
   *   textarea.setValue(start+"[X]"+end);
   * }
   * </pre> 
   * 
   * @since 2.8.5
   * @return the end position >= 0 && >= getSelectionStart
   */
  public int getSelectionEnd();
  
  /**
   * Sets the parser to use when parsing links. We need to parse links to transform a link 
   * reference passed as a raw string by the long text element into a clickable link object.
   * <br>
   * Use {@link RegExprLinkParser} as regualr expression link parser.
   * 
   * @since 2.10
   * @param parser the link parser to use
   * @param listener the listener to use
   * @throws Exception 
   * @see RegExprLinkParser
   */
  public void setLinkHandling(ILinkParser parser, ILinkEventListener listener) throws Exception;
}

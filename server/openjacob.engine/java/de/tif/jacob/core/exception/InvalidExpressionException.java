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

package de.tif.jacob.core.exception;

import de.tif.jacob.i18n.CoreMessage;
import de.tif.jacob.i18n.Message;

/**
 * This exception indicates that an invalid expression has been entered.
 * 
 * @author Andreas Sonntag
 */
public class InvalidExpressionException extends UserException
{
  /**
   * The internal revision control system id.
   */
  public static transient final String RCS_ID = "$Id: InvalidExpressionException.java,v 1.2 2009/03/23 00:09:21 ibissw Exp $";

  /**
   * The internal revision control system id in short form.
   */
  public static transient final String RCS_REV = "$Revision: 1.2 $";

  /**
   * Internal long text type.
   */
  public static final int LONGTEXT_TYPE = 1;
  
  /**
   * Internal binary type.
   */
  public static final int BINARY_TYPE = 2;

  private final String expression;

  /**
   * Constructs the <code>InvalidExpressionException</code>.
   * 
   * @param expression
   *          the invalid expression
   * @param cause
   *          a further cause
   */
  public InvalidExpressionException(String expression, Exception cause)
  {
    super(new CoreMessage(CoreMessage.INVALID_EXPRESSION, expression, ""), cause.getLocalizedMessage(), cause);
    this.expression = expression;
  }

  /**
   * Constructs the <code>InvalidExpressionException</code>.
   * 
   * @param expression
   *          the invalid expression
   * @param message
   *          an additional message
   * @param cause
   *          a further cause
   */
  public InvalidExpressionException(String expression, String message, Exception cause)
  {
    super(new CoreMessage(CoreMessage.INVALID_EXPRESSION, expression, " [" + message + "]"), cause);
    this.expression = expression;
  }

  /**
   * Constructs the <code>InvalidExpressionException</code>.
   * 
   * @param expression
   *          the invalid expression
   * @param message
   *          an additional message
   */
  public InvalidExpressionException(String expression, String message)
  {
    super(new CoreMessage(CoreMessage.INVALID_EXPRESSION, expression, " [" + message + "]"));
    this.expression = expression;
  }

  /**
   * Constructs the <code>InvalidExpressionException</code>.
   * 
   * @param expression
   *          the invalid expression
   */
  public InvalidExpressionException(String expression)
  {
    super(new CoreMessage(CoreMessage.INVALID_EXPRESSION, expression, ""));
    this.expression = expression;
  }

  /**
   * Constructs the <code>InvalidExpressionException</code>.
   * 
   * @param expression
   *          the invalid expression
   * @param type
   *          an additional type
   * 
   * @see #BINARY_TYPE
   * @see #LONGTEXT_TYPE
   */
  public InvalidExpressionException(String expression, int type)
  {
    super(getTypeMessage(expression, type));
    this.expression = expression;
  }

  protected InvalidExpressionException(Message message, String expression)
  {
    super(message);
    this.expression = expression;
  }

  protected InvalidExpressionException(Message message, String expression, Exception cause)
  {
    super(message, cause);
    this.expression = expression;
  }

  private static CoreMessage getTypeMessage(String expression, int type)
  {
    switch (type)
    {
      case LONGTEXT_TYPE:
        return new CoreMessage(CoreMessage.INVALID_LONGTEXT_EXPRESSION, expression);
      case BINARY_TYPE:
        return new CoreMessage(CoreMessage.INVALID_BINARY_EXPRESSION, expression);
      default:
        return new CoreMessage(CoreMessage.INVALID_EXPRESSION, expression, "");
    }
  }

  /**
   * Returns the invalid expression.
   * 
   * @return the invalid expression
   */
  public String getExpression()
  {
    return expression;
  }
}

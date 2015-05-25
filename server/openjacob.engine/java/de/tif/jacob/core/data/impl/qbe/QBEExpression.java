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

package de.tif.jacob.core.data.impl.qbe;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tif.jacob.core.exception.InvalidExpressionException;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class QBEExpression
{
	static public transient final String RCS_ID = "$Id: QBEExpression.java,v 1.2 2010/01/29 00:47:09 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.2 $";

	static private final transient Log logger = LogFactory.getLog(QBEExpression.class);
  
  private Integer orGroupNumber = null;

	protected QBEExpression()
	{
	}
  
  /**
   * Checks whether this QBE expression should be used as a constraint in an OR
   * group and returns the number of the OR group, if existing.
   * 
   * @return the OR group number or <code>null</code>, if it is used as a
   *         AND-constraint.
   */
  public Integer getOrGroupNumber()
  {
    return orGroupNumber;
  }

  /**
   * Checks whether the given expression is a so-called null-expression, i.e.
   * the expression is "NULL" or "!NULL". White space will be ignored.
   * 
   * @return <code>true</code> if expression is a null-expression otherwise
   *         <code>false</code>
   * @since 2.9         
   */
  public boolean isNullExpression()
  {
    return false;
  }
  
  protected final void incrementOrGroupNumber()
  {
    if (this.orGroupNumber == null)
      this.orGroupNumber = new Integer(1);
    else
      this.orGroupNumber = new Integer(this.orGroupNumber.intValue() + 1);
  }

	public abstract void print(PrintWriter writer);

  public abstract void makeConstraint(QBEConstraintBuilder builder, boolean doNot) throws InvalidExpressionException;
 
	public static QBEExpression parseText(String expression) throws InvalidExpressionException
	{
		return parse(QBEScanner.TEXT, expression);
	}

	public static QBEExpression parseEnum(String expression) throws InvalidExpressionException
	{
		return parse(QBEScanner.ENUM, expression);
	}

  public static QBEExpression parseInteger(String expression) throws InvalidExpressionException
  {
    return parse(QBEScanner.INTEGER, expression);
  }

  public static QBEExpression parseBoolean(String expression) throws InvalidExpressionException
  {
    return parse(QBEScanner.BOOLEAN, expression);
  }

	/**
   * Parse a decimal expression.
   * 
   * @param expression
   *          the expression
   * @param locale
   *          the locale to use or <code>null</code> for default
   * @return the QBE expression
   * @throws InvalidExpressionException
   *           if the given expression is invalid
   */
	public static QBEExpression parseDecimal(String expression, Locale locale) throws InvalidExpressionException
	{
	  // use dot notation as default 
	  if (locale == null || "en".equals(locale.getLanguage()))
	    return parse(QBEScanner.DOT_DECIMAL, expression);
	  
    return parse(QBEScanner.COMMA_DECIMAL, expression);
	}

	public static QBEExpression parseInterval(String expression) throws InvalidExpressionException
	{
		return parse(QBEScanner.INTERVAL, expression);
	}

	public static QBEExpression parseDate(String expression) throws InvalidExpressionException
	{
		return parse(QBEScanner.DATE, expression);
	}

	public static QBEExpression parseTime(String expression) throws InvalidExpressionException
	{
		return parse(QBEScanner.TIME, expression);
	}

	public static QBEExpression parseTimestamp(String expression) throws InvalidExpressionException
	{
		return parse(QBEScanner.TIMESTAMP, expression);
	}

	public static QBEExpression parse(int scannerInitialState, String expression) throws InvalidExpressionException
	{
		try
		{
			QBEScanner scanner = new QBEScanner(new StringReader(expression));
			scanner.yybegin(scannerInitialState);
			QBEParser parser = new QBEParser(scanner);
			return (QBEExpression) parser.parse().value;
		}
		catch (Exception ex)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Can not parse QBE expression \"" + expression + "\": " + ex.getMessage());
			}
			throw new InvalidExpressionException(expression, ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public final String toString()
	{
		StringWriter string = new StringWriter();
    PrintWriter pw = new PrintWriter(string);
    if (this.orGroupNumber != null)
    {
      for (int i = 0; i < this.orGroupNumber.intValue(); i++)
        pw.print("|");
    }
		print(pw);
		return string.toString();
	}

}

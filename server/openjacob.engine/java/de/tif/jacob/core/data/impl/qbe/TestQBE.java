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

import java.util.Collections;
import java.util.Map;

import de.tif.jacob.core.data.impl.sql.SQLDataSource;
import de.tif.jacob.core.data.impl.sql.SQLStatementBuilder;
import de.tif.jacob.core.definition.FieldType;
import de.tif.jacob.core.definition.IKey;
import de.tif.jacob.core.definition.ITableAlias;
import de.tif.jacob.core.definition.ITableAliasCondition;
import de.tif.jacob.core.definition.ITableDefinition;
import de.tif.jacob.core.definition.ITableField;
import de.tif.jacob.core.definition.fieldtypes.TextFieldType;
import de.tif.jacob.core.definition.fieldtypes.TimestampFieldType;
import de.tif.jacob.core.definition.fieldtypes.TimestampResolution;
import de.tif.jacob.util.DatetimeUtil;

/**
 * @author Andreas Sonntag
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestQBE
{
	static public transient final String RCS_ID = "$Id: TestQBE.java,v 1.8 2010/10/26 23:30:42 ibissw Exp $";
	static public transient final String RCS_REV = "$Revision: 1.8 $";

	private static final String[] interval_expressions = { //
		"", //
		" ", //
		"NULL", //
		"!NULL", //
		"2", "2:30", "0:01:00", "2:30: 5", ">3:15", "< 6:30:10", //
		"!24:10", "!>5:45", "!< 4:15", //
		"5:30|6|6:30", "5:30 .. 5:45:15", "1:10|!2..2:10" };

  private static final String[] integer_expressions = { //
    "", //
    " ", //
    "NULL", //
    "!NULL", //
    "24", ">37", "< 67", //
    "!24", "!>37", "!< 67", //
    "!24&!33", "!24&!33&<50", //
    "5|7|9", "-1 .. 9", "5|!7..10" };

  private static final String[] boolean_expressions = { //
    "", //
    " ", //
    "NULL", //
    "!NULL", //
    "true", "false", "TRUE", "FALSE", "trUE", "FALse", //
    "!True", "!falSE", //
    "true|false", "true|null" };

	private static final String[] decimal_expressions = { //
		"", //
		" ", //
		"NULL", //
		"!NULL", //
		"24.3", ">37.5", "< -67.0", //
		"!24.3", "!>37.5", "!< 67.0", //
		"5|7.345|9", "-1 .. 9.56", "5|7.1..10" };

	private static final String[] datevalue_expressions = { //
		"", //
		" ", //
		"12.02.2005", "12.02.05", //
		"11 Mrz 2004", "11. März 2004", //
		"11Mrz 2004", "11.März2004", //
		"4 Jan 96", //
		"2005-02-12", "5-02- 12", "0005-02-12", "105-02-12", //
		"1/4/96", //
		"January 4 1996" };

	private static final String[] date_expressions = { //
		"", //
		" ", //
		"NULL", "!NULL", //
		"12.02.2005", "12.02.05", //
		"11 Mrz 2004", "11. März 2004", //
		"11Mrz 2004", "11.März2004", //
		"4 Jan 96", //
		"1/4/96", //
		"2005-02-12", //
		"January 4 1996", //
		"YTD", //
		"YTD+1y", //
		"!YTD", //
		"!YTD+1y", //
		"QTD", //
		"QTD-3m", //
		"MTD", //
		"MTD-1y", //
		"WTD", //
		"WTD-7d", //
		"THISY", //
		"THISQ", //
		"THISM", //
		"THISW", //
		"WEEK3", //
		"WEEK3-1y", //
		"WEEK3..week5", //
		"WEEK7|WEEK8", //
		"WEEK53", //
		"WEEK0", //
		"Feb 4 1996..11.März 96", //
		"Feb 4 1996..11.März 96|1/4/96", //
		"!Feb 4 1996..11.März 96", //
		">04Jan96", //
		"<010496", "!<010496", //
		"TODAY", "TODAY +2d", //
		"<TODAY- 1m", ">TodAY+3m", //
		"today-1y", "TODAY +4y", "today-1y..today" };

	private static final String[] time_expressions = { //
		"", //
		" ", //
		"NULL", "!NULL", //
		"11 :00", "10:00:00", //
		"!> 12:00:41", //
		"01:00..9:40 PM|10:10 PM", //
		"9:40:58 AM", "9:40:58 PM" };

	private static final String[] timevalue_expressions = { //
		"", //
		" ", //
		"11 :00", "10:00:00", "13:55", //
		"9:40:58 AM", "9:40:58 PM" };

	private static final String[] timestamp_expressions = { //
		"", //
		" ", //
		"NULL", "!NULL", //
		"12.02.2005 9:40:58 AM", "12.02.05 11 :00", //
		"11 Mrz 2004", "11. März 2004 10:10 PM", //
		"1/4/96 10:10 AM", //
		"January 4 1996", //
		"Feb 4 1996 11:00:05..11.März 96|1/4/96|now", //
		"YTD", //
		"YTD+1y", //
		"!YTD", //
		"!YTD+1y", //
		"QTD", //
		"QTD-3m", //
		"MTD", //
		"MTD-1y", //
		"WTD", //
		"WTD-7d", //
		"THISY", //
		"THISQ", //
		"THISM", //
		"THISW", //
		"NOW", "NOW-2h", "TODAY +2d", //
    "<NOW- 1min", ">noW+3sec", //
    "today 6:00", "today 16:00", "today 16:00:31", //
		"now-1y", "now +4d", "today 0:00.. today 12:00", "now-1y..today-3d", "today..today-3d" };

	private static final String[] timestampvalue_expressions = { //
		"", //
		" ", //
		"12.02.2005 9:40:58 AM", "12.02.05 11 :00", "30.4.2002 13:00:00", //
		"12.02.2005 9:40:58.12", "12.02.05 11:00:00.0001", //
		"2005-02-12 9:40:58.12", "2005-02-12 9:40:58 AM", //
		"11 Mrz 2004", "11. März 2004 10:10 PM", //
		"1/4/96 10:10 AM", //
    "today 6:00", "today 16:00", "today 16:00:31", //
		"January 4 1996", "10:00", //
    "now+ 1d", "now+5min", "now - 15min", "today - 300d"};

	private static final String[] text_expressions = { //
		"", //
		" ", //
		"NULL", "!NULL", //
		"NULLABLE", //
		"\"NULL\"", //
		"p??b*", "*", //
		"*abc", //
		"?abC", //
		">berta", //
		"!>bcd", //
		"be>RTa", //
		"!abc", //
		"ab!c", //
    "abc!", //
    "=exact", //
		"= exact", //
    "left*right", //
    "^left", //
    "^??left", //
    "^left*right", //
    "^??left*right", //
    "right$", //
    "right??$", //
    "more?left*right$", //
    "more?left*right?$", //
    "^more?left*right?$", //
		"10$ and more", //
		"=\"<*@\"\"jacob>\"", //
		"\"<\"*\"@jacob>\"", //
		"Mike|Andi&Achim", //
		"Mike|=Andi|!Achim", //
		"!=Mike|!=Andi",
    "!^. & !^SMC", 
    "\"\"", 
    "\"\"text\"\"*", 
    "\"alpha\"|beta", 
    "!^.&!^SMC"//, 
    /* "!^.&!^SMC*" */};

	private static void testExpression(SQLDataSource datasource, ITableAlias alias, ITableField field, QBEExpression expr, String expression) throws Exception
	{
		String sql;
		if (null == expr)
		{
			sql = "NO EXPRESSION!";
		}
		else
		{
			SQLStatementBuilder builder = new SQLStatementBuilder(datasource, true);
      builder.setTableField(alias, field);
			expr.makeConstraint(builder, false);
			sql = builder.toString();
		}
		//    System.out.println("'" + expression + "' -> " + expr + " -> SQL: " +
		// sql);
		System.out.println("'" + expression + "' -> SQL: " + sql);
	}

	private static void testTextExpressions(SQLDataSource datasource, ITableAlias alias, boolean isCaseSensitive, boolean isLeftAnchored) throws Exception
	{
		ITableField field = new TestTextTableField(isCaseSensitive, isLeftAnchored);
		System.out.println();
		System.out.println("####### TEXT Expressions for isCaseSensitive=" + isCaseSensitive + "/isLeftAnchored=" + isLeftAnchored);
		String[] expressions = text_expressions;
		for (int i = 0; i < expressions.length; i++)
		{
			testExpression(datasource, alias, field, QBEExpression.parseText(expressions[i]), expressions[i]);
		}
	}

	private static void testIntervalExpressions(SQLDataSource datasource, ITableAlias alias) throws Exception
	{
		ITableField field = new TestTextTableField(true, true);
		System.out.println();
		System.out.println("####### INTERVAL Expressions");
		String[] expressions = interval_expressions;
		for (int i = 0; i < expressions.length; i++)
		{
			testExpression(datasource, alias, field, QBEExpression.parseInterval(expressions[i]), expressions[i]);
		}
	}

  private static void testIntegerExpressions(SQLDataSource datasource, ITableAlias alias) throws Exception
  {
    ITableField field = new TestTextTableField(true, true);
    System.out.println();
    System.out.println("####### INTEGER Expressions");
    String[] expressions = integer_expressions;
    for (int i = 0; i < expressions.length; i++)
    {
      testExpression(datasource, alias, field, QBEExpression.parseInteger(expressions[i]), expressions[i]);
    }
  }

  private static void testBooleanExpressions(SQLDataSource datasource, ITableAlias alias) throws Exception
  {
    ITableField field = new TestTextTableField(true, true);
    System.out.println();
    System.out.println("####### BOOLEAN Expressions");
    String[] expressions = boolean_expressions;
    for (int i = 0; i < expressions.length; i++)
    {
      testExpression(datasource, alias, field, QBEExpression.parseBoolean(expressions[i]), expressions[i]);
    }
  }

	private static void testDecimalExpressions(SQLDataSource datasource, ITableAlias alias) throws Exception
	{
		ITableField field = new TestTextTableField(true, true);
		System.out.println();
		System.out.println("####### DECIMAL Expressions");
		String[] expressions = decimal_expressions;
		for (int i = 0; i < expressions.length; i++)
		{
			testExpression(datasource, alias, field, QBEExpression.parseDecimal(expressions[i], null), expressions[i]);
		}
	}

	private static void testDateExpressions(SQLDataSource datasource, ITableAlias alias) throws Exception
	{
		ITableField field = new TestTextTableField(true, true);
		System.out.println();
		System.out.println("####### DATE Expressions");
		String[] expressions = date_expressions;
		for (int i = 0; i < expressions.length; i++)
		{
			testExpression(datasource, alias, field, QBEExpression.parseDate(expressions[i]), expressions[i]);
		}
		
		System.out.println();
    System.out.println("####### WEEK calculations");
    int week = 3;
    for (int i = -10; i <= 10; i++)
    {
      QBEExpression expr = new QBEWeekRangeExpression(week, i);
      SQLStatementBuilder builder = new SQLStatementBuilder(datasource, true);
      builder.setTableField(alias, field);
      expr.makeConstraint(builder, false);
      String sql = builder.toString();
      System.out.println("'WEEK(" + week + "," + i + ")' -> SQL: " + sql);
    }
	}

	private static void testDateValueExpressions() throws Exception
	{
		System.out.println();
		System.out.println("####### DATEVALUE Expressions");
		String[] expressions = datevalue_expressions;
		for (int i = 0; i < expressions.length; i++)
		{
			System.out.println("'" + expressions[i] + "' -> VALUE: " + DatetimeUtil.convertToDate(expressions[i]));
		}
	}

	private static void testTimeValueExpressions() throws Exception
	{
		System.out.println();
		System.out.println("####### TIMEVALUE Expressions");
		String[] expressions = timevalue_expressions;
		for (int i = 0; i < expressions.length; i++)
		{
			System.out.println("'" + expressions[i] + "' -> VALUE: " + DatetimeUtil.convertToTime(expressions[i]));
		}
	}

	private static void testTimestampValueExpressions() throws Exception
	{
		System.out.println();
		System.out.println("####### TIMESTAMPVALUE Expressions");
		String[] expressions = timestampvalue_expressions;
		for (int i = 0; i < expressions.length; i++)
		{
			System.out.println("'" + expressions[i] + "' -> VALUE: " + DatetimeUtil.convertToTimestamp(expressions[i]));
		}
	}

	private static void testTimeExpressions(SQLDataSource datasource, ITableAlias alias) throws Exception
	{
		ITableField field = new TestTextTableField(true, true);
		System.out.println();
		System.out.println("####### TIME Expressions");
		String[] expressions = time_expressions;
		for (int i = 0; i < expressions.length; i++)
		{
			testExpression(datasource, alias, field, QBEExpression.parseTime(expressions[i]), expressions[i]);
		}
	}

	private static void testTimestampExpressions(SQLDataSource datasource, ITableAlias alias) throws Exception
	{
		ITableField field = new TestTimestampTableField();
		System.out.println();
		System.out.println("####### TIMESTAMP Expressions");
		String[] expressions = timestamp_expressions;
		for (int i = 0; i < expressions.length; i++)
		{
			testExpression(datasource, alias, field, QBEExpression.parseTimestamp(expressions[i]), expressions[i]);
		}
	}

	public static void main(String[] args)
	{
		try
		{
			SQLDataSource datasource = SQLDataSource.TEST_INSTANCE;
			ITableAlias alias = new TestAlias();
			testTextExpressions(datasource, alias, false, false);
			testTextExpressions(datasource, alias, false, true);
			testTextExpressions(datasource, alias, true, false);
			testTextExpressions(datasource, alias, true, true);
			testIntervalExpressions(datasource, alias);
      testIntegerExpressions(datasource, alias);
      testBooleanExpressions(datasource, alias);
			testDecimalExpressions(datasource, alias);
			testDateExpressions(datasource, alias);
			testTimestampExpressions(datasource, alias);
			testTimeExpressions(datasource, alias);
			testDateValueExpressions();
			testTimeValueExpressions();
			testTimestampValueExpressions();
			System.out.println("SUCCESS!");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

  private static final class TestTimestampTableField implements ITableField
  {
    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.definition.ITableField#getDBName()
     */
    public String getDBName()
    {
      return getName();
    }

    public Map getProperties()
    {
      return Collections.EMPTY_MAP;
    }

    /* (non-Javadoc)
     * @see de.tif.jacob.core.definition.INamedObjectDefinition#getDescription()
     */
    public String getDescription()
    {
      return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.definition.ITableField#getFieldIndex()
     */
    public int getFieldIndex()
    {
      return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.definition.ITableField#getName()
     */
    public String getName()
    {
      return "field";
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.definition.ITableField#getType()
     */
    public FieldType getType()
    {
      return new TimestampFieldType(null, TimestampResolution.SEC_BASE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.tif.jacob.core.definition.ITableField#isRequired()
     */
    public boolean isRequired()
    {
      return false;
    }
    
		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.ITableField#isReadOnly()
		 */
		public boolean isReadOnly()
		{
			return false;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.ITableField#isEnabledForHistory()
		 */
		public boolean isEnabledForHistory()
		{
			return false;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.ITableField#getLabel()
		 */
		public String getLabel()
		{
			return getName();
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.ITableField#matchingForeignKey()
		 */
		public IKey getMatchingForeignKey()
		{
			return null;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.ITableField#isPrimary()
		 */
		public boolean isPrimary()
		{
			return false;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.IDefinition#getProperty(java.lang.String)
		 */
		public String getProperty(String name)
		{
			return null;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.ITableField#getTableDefinition()
		 */
		public ITableDefinition getTableDefinition()
		{
			return null;
		}

  }
  
	private static final class TestTextTableField implements ITableField
	{
		private final boolean isCaseSensitive;
		private final boolean isLeftAnchored;

		TestTextTableField(boolean isCaseSensitive, boolean isLeftAnchored)
		{
			this.isCaseSensitive = isCaseSensitive;
			this.isLeftAnchored = isLeftAnchored;
		}

    public Map getProperties()
    {
      return Collections.EMPTY_MAP;
    }
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.ITableField#getDBName()
		 */
		public String getDBName()
		{
			return getName();
		}

    /* (non-Javadoc)
     * @see de.tif.jacob.core.definition.INamedObjectDefinition#getDescription()
     */
    public String getDescription()
    {
      return null;
    }
    
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.ITableField#getFieldIndex()
		 */
		public int getFieldIndex()
		{
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.ITableField#getName()
		 */
		public String getName()
		{
			return "field";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.ITableField#getType()
		 */
		public FieldType getType()
		{
			return new TextFieldType(0, null, this.isCaseSensitive, this.isLeftAnchored ? TextFieldType.LEFT_BOUND : TextFieldType.UNBOUND, false);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.ITableField#isRequired()
		 */
		public boolean isRequired()
		{
			return false;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.ITableField#isReadOnly()
		 */
		public boolean isReadOnly()
		{
			return false;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.ITableField#isEnabledForHistory()
		 */
		public boolean isEnabledForHistory()
		{
			return false;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.ITableField#getLabel()
		 */
		public String getLabel()
		{
			return getName();
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.ITableField#matchingForeignKey()
		 */
		public IKey getMatchingForeignKey()
		{
			return null;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.ITableField#isPrimary()
		 */
		public boolean isPrimary()
		{
			return false;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.IDefinition#getProperty(java.lang.String)
		 */
		public String getProperty(String name)
		{
			return null;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.ITableField#getTableDefinition()
		 */
		public ITableDefinition getTableDefinition()
		{
			return null;
		}

	}

	private static final class TestAlias implements ITableAlias
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.ITableAlias#getCondition()
		 */
		public ITableAliasCondition getCondition()
		{
			return null;
		}
    public Map getProperties()
    {
      return Collections.EMPTY_MAP;
    }
		/* (non-Javadoc)
     * @see de.tif.jacob.core.definition.ITableAlias#getCondition(de.tif.jacob.core.definition.ITableAlias.IAdjuster)
     */
    public ITableAliasCondition getCondition(ITableAliasConditionAdjuster adjuster)
    {
      return null;
    }

    /*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.ITableAlias#getName()
		 */
		public String getName()
		{
			return "alias";
		}

    /* (non-Javadoc)
     * @see de.tif.jacob.core.definition.INamedObjectDefinition#getDescription()
     */
    public String getDescription()
    {
      return null;
    }
    
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.tif.jacob.core.definition.ITableAlias#getTableDefinition()
		 */
		public ITableDefinition getTableDefinition()
		{
			return null;
		}

		/* (non-Javadoc)
		 * @see de.tif.jacob.core.definition.IDefinition#getProperty(java.lang.String)
		 */
		public String getProperty(String name)
		{
			return null;
		}

	}
}

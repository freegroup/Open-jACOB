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

import java.io.PrintStream;
import java.io.PrintWriter;

import de.tif.jacob.core.data.impl.sql.SQLDataSource;

/**
 * @author Andreas Herz
 *  
 */
public class SQLException extends java.sql.SQLException
{
	private final Exception causedBy;
	private final String statement;
	private final SQLDataSource dataSource;

	public SQLException(SQLDataSource dataSource, Exception causedBy, String statement)
	{
		// check whether only the embedded exception is an SQLException
		if (!(causedBy instanceof java.sql.SQLException) && causedBy.getCause() instanceof java.sql.SQLException)
			this.causedBy = (Exception) causedBy.getCause();
		else
			this.causedBy = causedBy;
		
		// check for client abort
		if (this.causedBy instanceof java.sql.SQLException)
		{  
		  if (dataSource.isClientAbortErrorCode(((java.sql.SQLException)this.causedBy).getErrorCode()))
		    throw new RequestCanceledException();
		}

		this.dataSource = dataSource;
		this.statement = statement;
	}

	public SQLException(SQLDataSource dataSource, String reason, String statement)
	{
		super(reason);
		this.dataSource = dataSource;
		this.statement = statement;
		this.causedBy = null;
	}

	/**
	 * @return Returns the statement.
	 */
	protected String getStatement()
	{
		return statement;
	}

	/*
	 * @see java.sql.SQLException#getErrorCode() @author Andreas Herz
	 */
	public int getErrorCode()
	{
		if (this.causedBy instanceof java.sql.SQLException)
			return ((java.sql.SQLException) causedBy).getErrorCode();
		return super.getErrorCode();
	}

	/*
	 * @see java.sql.SQLException#getNextException() @author Andreas Herz
	 */
	public java.sql.SQLException getNextException()
	{
		if (this.causedBy instanceof java.sql.SQLException)
			return ((java.sql.SQLException) causedBy).getNextException();
		return super.getNextException();
	}

	/*
	 * @see java.sql.SQLException#getSQLState() @author Andreas Herz
	 */
	public String getSQLState()
	{
		if (this.causedBy instanceof java.sql.SQLException)
			return ((java.sql.SQLException) causedBy).getSQLState();
		return super.getSQLState();
	}

	/*
	 * @see java.sql.SQLException#setNextException(java.sql.SQLException) @author
	 *      Andreas Herz
	 */
	public void setNextException(java.sql.SQLException ex)
	{
		if (this.causedBy instanceof java.sql.SQLException)
			 ((java.sql.SQLException) causedBy).setNextException(ex);
		else
			super.setNextException(ex);
	}

	/*
	 * @see java.lang.Throwable#getCause() @author Andreas Herz
	 */
	public Throwable getCause()
	{
		return causedBy == null ? null : causedBy.getCause();
	}

	/*
	 * @see java.lang.Throwable#getLocalizedMessage() @author Andreas Herz
	 */
	public String getLocalizedMessage()
	{
	  StringBuffer buffer = new StringBuffer();
	  buffer.append("Datasource '").append(this.dataSource);
	  buffer.append("': ").append(causedBy == null ? super.getLocalizedMessage() : causedBy.getLocalizedMessage());
		buffer.append("[SQLState:").append(getSQLState()).append("][ErrorCode:").append(getErrorCode()).append("][Statement:");
		buffer.append(this.statement).append("]");
		return buffer.toString();
	}

	/*
	 * @see java.lang.Throwable#getMessage() @author Andreas Herz
	 */
	public String getMessage()
	{
	  StringBuffer buffer = new StringBuffer();
	  buffer.append("Datasource '").append(this.dataSource);
	  buffer.append("': ").append(causedBy == null ? super.getMessage() : causedBy.getMessage());
		buffer.append("[SQLState:").append(getSQLState()).append("][ErrorCode:").append(getErrorCode()).append("][Statement:");
		buffer.append(this.statement).append("]");
		return buffer.toString();
	}

	/*
	 * @see java.lang.Throwable#getStackTrace() @author Andreas Herz
	 */
	public StackTraceElement[] getStackTrace()
	{
		return causedBy == null ? super.getStackTrace() : causedBy.getStackTrace();
	}

	/*
	 * @see java.lang.Throwable#initCause(java.lang.Throwable) @author Andreas
	 *      Herz
	 */
	public Throwable initCause(Throwable cause)
	{
		return causedBy == null ? super.initCause(cause) : causedBy.initCause(cause);
	}

	/*
	 * @see java.lang.Throwable#printStackTrace() @author Andreas Herz
	 */
	public void printStackTrace()
	{
		if (causedBy == null)
			super.printStackTrace();
		else
			causedBy.printStackTrace();
	}

	/*
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream) @author
	 *      Andreas Herz
	 */
	public void printStackTrace(PrintStream s)
	{
		if (causedBy == null)
			super.printStackTrace(s);
		else
			causedBy.printStackTrace(s);
	}

	/*
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter) @author
	 *      Andreas Herz
	 */
	public void printStackTrace(PrintWriter s)
	{
		if (causedBy == null)
			super.printStackTrace(s);
		else
			causedBy.printStackTrace(s);
	}

	/*
	 * @see java.lang.Throwable#setStackTrace(java.lang.StackTraceElement[])
	 *      @author Andreas Herz
	 */
	public void setStackTrace(StackTraceElement[] stackTrace)
	{
		if (causedBy == null)
			super.setStackTrace(stackTrace);
		else
			causedBy.setStackTrace(stackTrace);
	}

	/*
	 * @see java.lang.Object#toString() @author Andreas Herz
	 */
	public String toString()
	{
		if (causedBy == null)
			return super.toString();
		return causedBy.toString();
	}
}

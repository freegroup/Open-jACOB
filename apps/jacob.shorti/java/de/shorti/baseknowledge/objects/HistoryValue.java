package de.shorti.baseknowledge.objects;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import de.shorti.db.*;
import de.shorti.util.basic.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;

public class HistoryValue extends _dbHistoryValue
{
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

	/**
	 *  Method:      findByKey(ShortiUser user, ContextRegistry context, String _name )
	 *  Description:
	 *  Returns:     HistoryValue
	 */
	public static HistoryValue findByKey(ShortiUser user, ContextRegistry context, String _name )
	{
		HistoryValue result = null;
		SaveStatement  stmt;

		try
		{
			stmt  = ConnectionManager.getValid().createStatement("_dbHistoryValue.findByName");
			try
			{
				ResultSet s = stmt.executeQuery( "SELECT  value, id FROM HistoryValue WHERE name='"+_name+"' AND shortiUser_id='"+user.getId()+"' AND contextRegistry_id='"+context.getId()+"' order by generatedId desc");
				if(s.next())
				{
					result = new HistoryValue();
					result.name=_name;
					result.shortiUser_id=user.getId();
					result.value=s.getString(1);
					result.contextRegistry_id=context.getId();
					result.id=s.getString(2);
				}
			}
			catch(Exception exc)
			{
				trace.error(exc);
			}
			stmt.close();

		}
		catch (Exception ex)
		{
			trace.error(ex);
		}
		return result;
	}
}